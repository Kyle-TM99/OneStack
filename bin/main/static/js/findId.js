$(function() {
    // findId 페이지에서만 실행되도록 체크
    if ($("#findIdForm").length > 0) {
        // 전화번호 형식 자동 변환
        $("#phone").on("input", function() {
            let number = $(this).val().replace(/[^0-9]/g, "").substr(0, 11);
            if (number.length > 3) {
                number = number.length > 7 
                    ? `${number.substr(0,3)}-${number.substr(3,4)}-${number.substr(7)}`
                    : `${number.substr(0,3)}-${number.substr(3)}`;
            }
            $(this).val(number);
            
            // 전화번호 형식 검사만 수행하고 중복 검사는 하지 않음
            if (!/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/.test(number)) {
            } else {
                $("#phoneMessage").text(""); // 메시지 제거
            }
        });

        // 아이디 찾기 버튼 클릭 시
        $("#findIdBtn").on("click", function(e) {
            e.preventDefault();
            
            // 입력값 가져오기
            const name = $("#name").val().trim();
            const phone = $("#phone").val().trim();
            

            // 서버로 데이터 전송
            $.ajax({
                url: "/findId",
                type: "POST",
                data: {
                    name: name,
                    phone: phone
                },
                success: function(response) {
                    if (response.status === "success") {
                        $("#maskedId").text(maskMemberId(response.memberId));
                        new bootstrap.Modal(document.getElementById('resultModal')).show();
                    } else {
                        alert(response.message || "일치하는 회원정보가 없습니다.");
                    }
                },
                error: function() {
                    alert("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                }
            });
        });

        // 아이디 마스킹 처리 함수
        function maskMemberId(memberId) {
            if (!memberId) return "";
            const length = memberId.length;
            let visibleLength = Math.min(4, Math.ceil(length / 2));
            return memberId.substring(0, visibleLength) + "*".repeat(length - visibleLength);
        }
    }
}); 