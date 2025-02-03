$(function() {
    $("#detailDelete").on("click", function() {
        $("#checkForm").attr("action", "delete");
        $("#checkForm").attr("method", "post");
        $("#checkForm").submit();
    });

    $("#detailUpdate").on("click", function() {
        $("#updateForm").attr("action", "updateForm");
        $("#updateForm").attr("method", "post");
        $("#updateForm").submit();
    });

    $("#submitAnswer").on("click", function() {
        const inquiryNo = $("input[name='inquiryNo']").val(); // 히든 필드에서 문의글 번호 가져오기
        const answerContent = $("#reviewText").val(); // 답변 내용 가져오기
        const memberNo = $("input[name='memberNo']").val(); // 히든 필드에서 memberNo 가져오기

        // AJAX 요청을 통해 답변 등록
        $.ajax({
            url: '/memberInquiry/addInquiryAnswer',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                inquiryNo: inquiryNo,
                memberNo: parseInt(memberNo), // memberNo를 정수로 변환
                inquiryAnswerContent: answerContent
            }),
            success: function(response) {
                alert("답변이 등록되었습니다.");

                const currentDate = new Date();
                // 새로운 답변을 DOM에 추가
                const newAnswerHtml = `
        <div class="row g-0 mb-2">
            <th:block>
                <div class="text-start">
                    <p class="m-0">${answerContent}</p>
                    <span class="ml-2 border-left pl-2 text-secondary small">
                        ${currentDate.toLocaleString()}
                    </span>
                </div>
            </th:block>
        </div>
    `;

                // 모든 답변의 시간을 비교하여 가장 최근 답변 찾기
                let latestAnswer = null;
                let latestTime = new Date(0); // 1970년 1월 1일

                $('.col-md-8 .row.g-0.mb-2').each(function() {
                    const timeStr = $(this).find('.text-secondary').text().trim();
                    const answerTime = new Date(timeStr);
                    if (answerTime > latestTime) {
                        latestTime = answerTime;
                        latestAnswer = $(this);
                    }
                });

                // 가장 최근 답변 뒤에 새로운 답변 추가
                if (latestAnswer) {
                    latestAnswer.after(newAnswerHtml);
                } else {
                    // 답변이 없는 경우 h3 태그 다음에 추가
                    $('.col-md-8 h3.fw-bold.mb-3').after(newAnswerHtml);
                }

                $("#reviewText").val(''); // 텍스트 영역 초기화
            },
            error: function(xhr, status, error) {
                alert("답변 등록에 실패했습니다.");
            }
        });
    });

    function updateSatisfaction(inquiryNo, isSatisfied, memberNo) {
        console.log("inquiryNo:", inquiryNo); // inquiryNo 값 확인
        console.log("isSatisfied:", isSatisfied); // isSatisfied 값 확인
        console.log("memberNo:", memberNo); // memberNo 값 확인

        $.ajax({
            url: '/memberInquiry/inquiry/satisfaction',
            type: 'POST',
            data: {
                inquiryNo: inquiryNo,
                isSatisfied: isSatisfied,
                memberNo: memberNo
            },
            success: function(response) {
                alert(response);
                location.reload(); // 페이지 새로 고침
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText); // 오류 메시지 출력
                alert("업데이트에 실패했습니다.");
            }
        });
    }

// 만족 버튼 클릭 시
    $("#satisfactionButton").on("click", function() {
        const inquiryNo = $("input[name='inquiryNo']").val(); // 히든 필드에서 inquiryNo 가져오기
        const memberNo = $("input[name='memberNo']").val(); // 히든 필드에서 memberNo 가져오기
        updateSatisfaction(inquiryNo, true, memberNo); // 만족으로 설정
    });

// 불만족 버튼 클릭 시
    $("#dissatisfactionButton").on("click", function() {
        const inquiryNo = $("input[name='inquiryNo']").val(); // 히든 필드에서 inquiryNo 가져오기
        const memberNo = $("input[name='memberNo']").val(); // 히든 필드에서 memberNo 가져오기
        updateSatisfaction(inquiryNo, false, memberNo); // 불만족으로 설정
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
});