function submitReport() {
    // HTML 요소에서 값 가져오기
    const memberNo = document.querySelector('input[name="memberNo"]').value; // 숨겨진 input
    const reportedMemberNo = document.querySelector('input[name="reportedMemberNo"]').value; // 숨겨진 input
    const reportsType = document.querySelector('#reportsType').value; // 신고 유형 select
    const reportsTarget = document.querySelector('#reportsTarget').value; // 신고 항목 select
    const reportsReason = document.querySelector('#reportsReason').value; // 신고 사유 textarea
    const reportsTargetLink = window.location.pathname; // 현재 페이지 URL 사용

    // report 객체 생성
    const report = {
    memberNo: parseInt(memberNo), // 숫자로 변환
    reportedMemberNo: parseInt(reportedMemberNo), // 숫자로 변환
    reportsType: reportsType,
    reportsTarget: reportsTarget,
    reportsReason: reportsReason,
    reportsStatus: 0, // 초기값
    reportsTargetLink: reportsTargetLink // 현재 페이지 경로
};

    // 데이터 전송
    fetch('/reports', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(report)
})
    .then(response => {
    if (response.ok) {
    return response.text();
} else {
    throw new Error("신고 처리 중 오류 발생");
}
})

    .then(data => {
    alert("신고가 완료되었습니다."); // 성공 메시지 알림
    const modal = bootstrap.Modal.getInstance(document.getElementById('reportModal'));
    modal.hide(); // 모달 창 닫기
})
    .catch(error => {
    console.error(error);
    alert('신고 처리 중 오류가 발생했습니다.');
});
}

