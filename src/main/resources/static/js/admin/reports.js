document.addEventListener("DOMContentLoaded", () => {
    // 상세 보기 버튼 클릭 이벤트 설정
    const reportEditButtons = document.querySelectorAll('.report-edit-btn');
    reportEditButtons.forEach(button => {
        button.addEventListener('click', () => {
            console.log("버튼 클릭됨");

            // 버튼이 포함된 tr 요소 가져오기
            const row = button.closest('tr');

            // tr 요소에서 데이터를 추출 (children 배열로 접근)
            const reportData = {
                reportsNo: row.children[1].innerText.trim(),
                memberNo: row.children[2].innerText.trim(),
                reportedMemberNo: row.children[3].innerText.trim(),
                reportsType: row.children[4].innerText.trim(),
                reportsReason: row.children[5].innerText.trim(),
                reportsDate: row.children[7].innerText.trim(),
                reportsStatus: row.children[8].innerText.trim()
            };

            console.log('추출된 데이터:', reportData); // 데이터 확인용

            // 모달의 입력 필드에 데이터 삽입
            document.getElementById('reportsNo').value = reportData.reportsNo;
            document.getElementById('memberNo').value = reportData.memberNo;
            document.getElementById('reportedMemberNo').value = reportData.reportedMemberNo;
            document.getElementById('reportsType').value = reportData.reportsType;
            document.getElementById('reportsReason').value = reportData.reportsReason;
            document.getElementById('reportsDate').value = reportData.reportsDate;
            document.getElementById('reportsStatus').value = reportData.reportsStatus;
        });
    });
});
