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
                        <div class="text-start border-bottom">
                            <p class="m-0">${answerContent}</p>
                            <span class="ml-2 border-left pl-2 text-secondary small">
                                ${currentDate.toLocaleString()}
                            </span>
                        </div>
                    </div>
                `;

                // 새로운 답변을 <div class="fw-bold mb-3 fs-3"> 뒤에 추가
                $('.fw-bold.mb-3.fs-3').after(newAnswerHtml);

                $("#reviewText").val(''); // 텍스트 영역 초기화
            },
            error: function(xhr, status, error) {
                alert("답변창이 비어있습니다.\n답변을 입력해주세요.");
            }
        });
    });

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