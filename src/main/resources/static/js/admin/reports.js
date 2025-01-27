// 이벤트 위임 방식으로 수정
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("report-edit-btn")) {

        // 버튼에서 신고 번호 가져오기
        const reportId = e.target.getAttribute("data-report-id");

        // 신고 데이터를 포함한 테이블 행 찾기
        const reportRow = document.querySelector(`tr[data-report-no="${reportId}"]`);

        if (reportRow) {
            document.getElementById("reportsNo").value = reportRow.querySelector("td:nth-child(2)").textContent.trim();
            document.getElementById("reporterNickname").value = reportRow.querySelector("td:nth-child(3)").textContent.trim();
            document.getElementById("reportedNickname").value = reportRow.querySelector("td:nth-child(4)").textContent.trim();
            document.getElementById("reportsType").value = reportRow.querySelector("td:nth-child(5)").textContent.trim();
            document.getElementById("reportsReason").value = reportRow.querySelector("td:nth-child(6)").textContent.trim();
            document.getElementById("reportsDate").value = reportRow.querySelector("td:nth-child(8)").textContent.trim();
            document.getElementById("reportsDate").value = reportRow.querySelector("td:nth-child(8)").textContent.trim();

            // 처리 상태 업데이트
            const statusText = reportRow.querySelector("td:nth-child(9)").textContent.trim();
            const statusInput = document.getElementById("reportsStatus");
            if (statusText === "처리 완료") {
                statusInput.value = "비활성화"; // 처리 완료를 비활성화로 표시
            } else {
                statusInput.value = statusText; // 미처리 등 기존 상태 유지
            }

            console.log("모달 데이터 업데이트 완료");
        } else {
            console.error(`신고 번호 ${reportId}에 해당하는 데이터를 찾을 수 없습니다.`);
        }
    }
});
