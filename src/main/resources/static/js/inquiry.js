$(function() {
    // 정렬 버튼 클릭 시
    $("#sortButton").on("click", function() {
        const sortOption = $("#sortOptions").val();
        const currentUrl = window.location.href.split('?')[0];

        // 정렬 기준에 따라 URL에 쿼리 파라미터 추가
        window.location.href = `${currentUrl}?sort=${sortOption}`;
    });

    $("#detailDelete").on("click", function() {
        // 삭제 확인 모달 표시
        const deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'), {
            backdrop: 'static'
        });

        // 삭제 확인 버튼 클릭 이벤트
        $("#confirmDelete").one('click', function() {
            $("#checkForm").attr("action", "delete");
            $("#checkForm").attr("method", "post");
            $("#checkForm").submit();
        });

        deleteModal.show();
    });

    $("#detailUpdate").on("click", function() {
        $("#updateForm").attr("action", "updateForm");
        $("#updateForm").attr("method", "post");
        $("#updateForm").submit();
    });

    // 프로필 이미지 결정 및 출력 함수
    function ProfileImage(isAdmin, memberImage) {
        const defaultImage = isAdmin ? '/images/admin.png' : '/images/default-profile.png';
        const imageUrl = memberImage || defaultImage;
        return `<img src="${imageUrl}" alt="프로필" class="rounded-circle" style="width: 40px; height: 40px;">`;
    }

    $("#submitAnswer").on("click", function() {
        const inquiryNo = $("input[name='inquiryNo']").val();
        const answerContent = $("#reviewText").val();
        const memberNo = $("input[name='memberNo']").val();
        const nickname = $("input[name='nickname']").val();
        const isAdmin = $("input[name='isAdmin']").val() === 'true';
        const memberImage = $("input[name='memberImage']").val();

        if (!answerContent.trim()) {
            alert("답변창이 비어있습니다.\n답변을 입력해주세요.");
            return;
        }

        // AJAX 요청을 통해 답변 등록
        $.ajax({
            url: '/memberInquiry/addInquiryAnswer',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                inquiryNo: inquiryNo,
                memberNo: parseInt(memberNo),
                inquiryAnswerContent: answerContent
            }),
            success: function(response) {
                const answerModal = new bootstrap.Modal(document.getElementById('answerCompleteModal'), {
                    backdrop: 'static'
                });

                // 모달이 닫힐 때 페이지 새로고침
                $('#answerCompleteModal').on('hidden.bs.modal', function () {
                    location.reload();
                });

                answerModal.show();
            },
            error: function(xhr, status, error) {
                alert("답변 등록에 실패했습니다. 다시 시도해주세요.");
            }
        });
    });

    // 날짜 형식 변환 함수
    function formatDate(date) {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true};
        let formattedDate = date.toLocaleString('ko-KR', options).replace(',', '');
        formattedDate = formattedDate.replace(/(\d{4})\.\s?(\d{2})\.\s?(\d{2})\./, '$1.$2.$3').replace(/\s+/g, ' ')
        return formattedDate;
    }

    // 만족/불만족 상태 업데이트 함수
    function updateSatisfaction(inquiryNo, isSatisfied) {
        const modal = new bootstrap.Modal(document.getElementById('satisfactionModal'), {
            backdrop: 'static'
        });

        // 첫 번째 모달 내용 설정
        const message = isSatisfied ? "만족" : "불만족";
        $("#satisfactionIcon").removeClass().addClass(`bi ${isSatisfied ? 'bi-hand-thumbs-up text-primary' : 'bi-hand-thumbs-down text-warning'} fs-1 mb-3`);
        $("#satisfactionModalLabel").text("만족도 평가");
        $("#satisfactionModalBody").text(`답변이 ${message}스러우셨나요?`);

        // 확인 버튼 클릭 이벤트
        $("#confirmSatisfaction").one('click', function() {
            $.ajax({
                url: '/memberInquiry/inquiry/satisfaction',
                type: 'POST',
                data: {
                    inquiryNo: inquiryNo,
                    isSatisfied: isSatisfied
                },
                success: function(response) {
                    // 모달 내용 직접 변경
                    if (isSatisfied) {
                        $("#satisfactionIcon").removeClass().addClass('bi bi-emoji-smile fs-1 text-success mb-3');
                        $("#satisfactionModalLabel").text("만족도 평가 완료");
                        $("#satisfactionModalBody").text("감사합니다. 행복한 하루 되세요.");
                    } else {
                        $("#satisfactionIcon").removeClass().addClass('bi bi-emoji-smile fs-1 text-success mb-3');
                        $("#satisfactionModalLabel").text("만족도 평가 완료");
                        $("#satisfactionModalBody").text("감사합니다. 행복한 하루 되세요.");
                    }

                    // 확인 버튼 이벤트 변경
                    $("#confirmSatisfaction").one('click', function() {
                        modal.hide();
                        location.reload();
                    });
                },
                error: function(xhr, status, error) {
                    modal.hide();
                    alert("만족도 업데이트에 실패했습니다.");
                    console.error("Error:", error);
                }
            });
        });

        modal.show();
    }

    // 만족 버튼 클릭 시
    $("#satisfactionButton").on("click", function() {
        const inquiryNo = $("input[name='inquiryNo']").val();
        updateSatisfaction(inquiryNo, true);
    });

    // 불만족 버튼 클릭 시
    $("#dissatisfactionButton").on("click", function() {
        const inquiryNo = $("input[name='inquiryNo']").val();
        updateSatisfaction(inquiryNo, false);
    });

    // 답변 수정 버튼 클릭 시
    $(".editAnswerButton").on("click", function() {
        const inquiryAnswerNo = $(this).data("answer-no");
        const textarea = $(this).siblings(".review-textarea");

        // 텍스트 영역을 토글하여 보여주기
        textarea.toggle();

        // 텍스트 영역이 보일 때, 기존 답변 내용을 채워넣기
        if (textarea.is(":visible")) {
            textarea.val($(this).parent().find("p").text());
        } else {
            // 텍스트 영역이 숨겨질 때, AJAX 요청을 통해 수정
            const inquiryNo = $("input[name='inquiryNo']").val();
            const answerContent = textarea.val();

            // AJAX 요청을 통해 답변 수정
            $.ajax({
                url: '/memberInquiry/updateInquiryAnswer',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    inquiryAnswerNo: inquiryAnswerNo,
                    inquiryNo: inquiryNo,
                    inquiryAnswerContent: answerContent
                }),
                success: function(response) {
                    alert(response);
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("답변 수정에 실패했습니다.");
                }
            });
        }
    });


    // 제목 글자 수 제한 기능
    const titleInput = document.getElementById('inquiryTitle');
    const MAX_LENGTH = 40;

    if (titleInput) {  // titleInput이 존재할 때만 이벤트 리스너 추가
        titleInput.addEventListener('input', function() {
            if (this.value.length > MAX_LENGTH) {
                this.value = this.value.slice(0, MAX_LENGTH);
                alert('제목은 공백 포함 40자를 초과할 수 없습니다.');
            }
        });
    }

    // 기존 submitForm 함수 수정
    function submitForm() {
        var form = document.getElementById('updateForm') || document.getElementById('writeForm');
        var title = document.getElementById('inquiryTitle').value;
        var content = document.getElementById('inquiryContent');

        if (title.trim() === '') {
            alert('제목을 입력해주세요.');
            return false;
        }

        if (title.length > MAX_LENGTH) {
            alert('제목은 공백 포함 40자를 초과할 수 없습니다.');
            return false;
        }

        if (quill.getText().trim() === '') {
            alert('내용을 입력해주세요.');
            return false;
        }

        content.value = quill.root.innerHTML;
        form.submit();
    }

});