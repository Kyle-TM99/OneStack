$(function() {
    $("#detailDelete").on("click", function() {
        // 삭제 확인 모달 표시
        const deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'), {
            backdrop: false
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
                // alert 대신 모달 표시
                const answerModal = new bootstrap.Modal(document.getElementById('answerCompleteModal'), {
                    backdrop: false
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
        let formattedDate = date.toLocaleString('ko-KR', options).replace(',', ''); // 한국어 형식으로 변환 후 쉼표 제거
        formattedDate = formattedDate.replace(/(\d{4})\.\s?(\d{2})\.\s?(\d{2})\./, '$1.$2.$3').replace(/\s+/g, ' ') // 연속된 공백 제거.trim();
        return formattedDate;
    }

    // 만족/불만족 상태 업데이트 함수
    function updateSatisfaction(inquiryNo, isSatisfied) {
        const modal = new bootstrap.Modal(document.getElementById('satisfactionModal'), {
            backdrop: false
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
                    // 첫 번째 모달이 완전히 닫힌 후 두 번째 모달 표시
                    modal.hide();
                    $('#satisfactionModal').on('hidden.bs.modal', function () {
                        const secondModal = new bootstrap.Modal(document.getElementById('satisfactionModal'), {
                            backdrop: false
                        });

                        if (isSatisfied) {
                            $("#satisfactionIcon").removeClass().addClass('bi bi-emoji-smile fs-1 text-success mb-3');
                            $("#satisfactionModalLabel").text("만족도 평가 완료");
                            $("#satisfactionModalBody").text("감사합니다. 행복한 하루 되세요.");
                        } else {
                            $("#satisfactionIcon").removeClass().addClass('bi bi-emoji-frown fs-1 text-warning mb-3');
                            $("#satisfactionModalLabel").text("만족도 평가 완료");
                            $("#satisfactionModalBody").text("죄송합니다. 다음엔 조금 더 나은 답변을 드릴게요.");
                        }

                        // 확인 버튼 이벤트 변경
                        $("#confirmSatisfaction").one('click', function() {
                            secondModal.hide();
                            location.reload();
                        });

                        secondModal.show();
                        $(this).off('hidden.bs.modal');
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
        const inquiryAnswerNo = $(this).data("answer-no"); // 답변 번호 가져오기
        const textarea = $(this).siblings(".review-textarea"); // 해당 텍스트 영역 가져오기

        // 텍스트 영역을 토글하여 보여주기
        textarea.toggle();

        // 텍스트 영역이 보일 때, 기존 답변 내용을 채워넣기
        if (textarea.is(":visible")) {
            textarea.val($(this).parent().find("p").text()); // 기존 답변 내용으로 채우기
        } else {
            // 텍스트 영역이 숨겨질 때, AJAX 요청을 통해 수정
            const inquiryNo = $("input[name='inquiryNo']").val(); // 문의글 번호 가져오기
            const answerContent = textarea.val(); // 수정할 답변 내용 가져오기

            // 콘솔 로그 추가
            console.log("inquiryAnswerNo:", inquiryAnswerNo);
            console.log("inquiryNo:", inquiryNo);
            console.log("inquiryAnswerContent:", answerContent);

            // AJAX 요청을 통해 답변 수정
            $.ajax({
                url: '/memberInquiry/updateInquiryAnswer',
                type: 'POST', // POST 방식
                contentType: 'application/json',
                data: JSON.stringify({
                    inquiryAnswerNo: inquiryAnswerNo,
                    inquiryNo: inquiryNo,
                    inquiryAnswerContent: answerContent
                }),
                success: function(response) {
                    console.log("수정 성공:", response); // 성공 시 로그
                    alert(response);
                    location.reload(); // 페이지 새로 고침
                },
                error: function(xhr, status, error) {
                    console.error("수정 실패:", xhr.responseText); // 실패 시 로그
                    alert("답변 수정에 실패했습니다.");
                }
            });
        }
    });

    // isAdmin 값 확인
    console.log("isAdmin value:", $("#isAdminValue").val());
    $("img[data-is-admin]").each(function() {
        console.log("data-is-admin:", $(this).data("isAdmin"));
    });
});