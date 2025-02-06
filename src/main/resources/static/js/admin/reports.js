document.addEventListener("click", function (e) {
    // 신고 상세 보기 버튼 클릭 시
    if (e.target.classList.contains("report-edit-btn")) {
        // 버튼에서 신고 번호 가져오기
        const reportId = e.target.getAttribute("data-report-id");
        console.log(`신고 상세 보기 클릭: reportId=${reportId}`);

        // 신고 데이터를 포함한 테이블 행 찾기
        const reportRow = document.querySelector(`tr[data-report-no="${reportId}"]`);
        if (reportRow) {
            // 테이블 데이터 가져와 모달에 채우기
            document.getElementById("reportsNo").value = reportRow.querySelector("td:nth-child(2)").textContent.trim();
            document.getElementById("reporterNickname").value = reportRow.querySelector("td:nth-child(3)").textContent.trim();
            document.getElementById("reportedNickname").value = reportRow.querySelector("td:nth-child(4)").textContent.trim();
            document.getElementById("reportsType").value = reportRow.querySelector("td:nth-child(5)").textContent.trim();
            document.getElementById("reportsDate").value = reportRow.querySelector("td:nth-child(8)").textContent.trim();

            // 신고사유는 버튼의 data-attribute에서 가져옴
            const reportsReason = e.target.getAttribute("data-reports-reason");
            document.getElementById("reportsReason").value = reportsReason ? reportsReason.trim() : "내용 없음";

            // 활성화 여부 업데이트
            const statusText = reportRow.querySelector("td:nth-child(9)").textContent.trim();
            document.getElementById("reportsStatus").value = statusText === "처리 완료" ? "비활성화" : statusText;

            console.log("모달 데이터 업데이트 완료");
        } else {
            console.error(`신고 번호 ${reportId}에 해당하는 데이터를 찾을 수 없습니다.`);
        }
    }

     // 비활성화 버튼 클릭 시
     if (e.target.classList.contains("btn-disable")) {
         const reportsNo = document.getElementById("reportsNo").value;
         const targetId = document.querySelector(`tr[data-report-no="${reportsNo}"]`).getAttribute("data-target-id");
         const reportsType = document.getElementById("reportsType").value.toLowerCase();

         // 한글 타입 변환
         const typeMap = {
             "커뮤니티": "community",
             "회원": "member",
             "댓글": "reply",
             "리뷰": "review"
         };
         const typeParam = typeMap[reportsType] || reportsType;


            console.log(`비활성화 요청: reportsNo=${reportsNo}, targetId=${targetId}, reportsType=${reportsType}`);

            // 비활성화 요청 서버 전송
             fetch(`/disable`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: `type=${typeParam}&targetId=${targetId}&reportsNo=${reportsNo}`
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("비활성화 요청 실패");
                        }
                        return response.text();
                    })
                    .then(data => {
                        alert(data);
                        location.reload(); // 페이지 새로고침하여 변경 내용 반영
                    })
                    .catch(error => console.error("비활성화 요청 중 오류 발생:", error));
                }
      });

// 모달 닫기 이벤트 (모달 초기화)
document.getElementById("reportModal").addEventListener("hidden.bs.modal", function () {
    document.getElementById("reportForm").reset();
    console.log("모달 초기화 완료");
});
