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
        const memberNo = $("input[name='isAdmin']").val(); // 히든 필드에서 memberNo 가져오기

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

                // 새로운 답변을 DOM에 추가 (가장 위에 추가)
                const newAnswerHtml = `
            <div class="row g-0 mt-2">
                <p id="answerContent" style="margin: 0;">${answerContent}</p>
                <span class="float-right ml-2 border-left pl-2 text-secondary small">
                    ${new Date().toLocaleString()} <!-- 현재 시간 표시 -->
                </span>
            </div>
        `;
                $(".review-form").before(newAnswerHtml); // 새로운 답변을 추가
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
});