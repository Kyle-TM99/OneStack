/* 결제 버튼 상수화 */
const payKakaoBtn = document.getElementById("payKakaoBtn");
const payKgBtn = document.getElementById("payKgBtn");

/* 결제 버튼 클릭시 결제 요청 함수 실행 */
payKakaoBtn.addEventListener("click", () => requestPayment(1));
payKgBtn.addEventListener("click", () => requestPayment(2));

/* 카카오페이 */
function requestPayment(channelNum) {

    const IMP = window.IMP; // IMP 초기화 및 연동
    IMP.init("imp80060870"); // 고객사 식별 코드(PortOne 콘솔 - 식별코드 - 고객사 식별코드)

    const projectNumber = document.getElementById("projectName").textContent.replace(/[^0-9]/g, '');
    const projectPrice = parseInt(document.getElementById("projectPrice").textContent.replace(/[₩,]/g, ''), 10);
    const memberName = document.getElementById("memberName").textContent;
    const memberPhone = document.getElementById("memberPhone").textContent;
    const memberEmail = document.getElementById("memberEmail").textContent;
    const memberNo = document.getElementById("memberNo").textContent;
    const quotationContent = document.getElementById("quotationContent").textContent;
    let channelKey;

    if (channelNum == 1) {
        channelKey = "channel-key-ecc4b522-d290-47ed-92c9-73ed6d22ab77";
    } else if(channelNum == 2) {
        channelKey = "channel-key-ad888640-2d53-49e2-924f-c653f7929d94";
    }

    console.log(projectNumber);
    console.log(projectPrice);
    console.log(memberName);
    console.log(memberPhone);
    console.log(memberEmail);
    console.log(memberNo);
    console.log(quotationContent);
    console.log(channelKey);

    IMP.request_pay({
        channelKey: channelKey, // 채널 키 설정
        pay_method: "card",
        merchant_uid: `onestackPayment-${crypto.randomUUID()}`, // 상점에서 생성한 고유 주문번호
        name: `원스택-${projectNumber}`,
        amount: projectPrice,
        buyer_name: memberName,
        buyer_tel: memberPhone,
        buyer_email: memberEmail
    }, function (rsp) {
        if (rsp.success) {
            alert("결제 성공: " + rsp.imp_uid);
            // 서버에 결제 조회 요청
            fetch('/api/payment/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    impUid: rsp.imp_uid,
                    merchantUid: rsp.merchant_uid,
                    payMethod: rsp.pay_method,
                    paidAmount: rsp.paid_amount,
                    buyerName: rsp.buyer_name,
                    buyerPhone: rsp.buyer_tel,
                    payTime: rsp.paid_at,
                    quotationNo: projectNumber,
                    memberNo: memberNo,
                    payContent: quotationContent
                })
            }).then(response => {
                if (!response.ok) {
                    throw new Error('결제 검증 실패');
                }
                return response.text(); // 서버에서 quotationNo를 텍스트로 반환
            }).then(quotationNo => {
                alert('결제가 성공적으로 완료되었습니다.');
                // 주문 완료 페이지로 이동하며 주문 번호 전달
                location.href = `/payDoneForm?quotationNo=${quotationNo}`;
            }).catch(error => {
                    alert('결제 처리 중 오류가 발생했습니다: ' + error.message);
                });
        } else {
            alert("결제 실패: " + rsp.error_msg);
        }
    });

};
