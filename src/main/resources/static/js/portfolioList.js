document.addEventListener("DOMContentLoaded", function () {
    console.log("포트폴리오 리스트 로드됨");
});

///* 포트폴리오 상세 보기 */
//function viewPortfolioDetail(element) {
//    const portfolioId = element.getAttribute("data-portfolio-id");
//    window.location.href = `/portfolio/portfolioDetail?portfolioNo=${portfolioId}`;
//}

/* 포트폴리오 수정 */
function editPortfolio(event, button) {
   event.stopPropagation(); // 부모 요소 클릭 방지 (부모 요소의 클릭 이벤트 실행 방지)

   const portfolioCard = button.closest(".portfolio-card"); // 해당 버튼이 속한 카드 찾기
   const portfolioId = portfolioCard.getAttribute("data-portfolio-id");

   console.log("🟢 수정 버튼 클릭됨! 포트폴리오 ID:", portfolioId);

   if (!portfolioId) {
       alert("❌ 포트폴리오 정보를 찾을 수 없습니다.");
       return;
   }

   // ✅ 수정 페이지로 이동
   window.location.href = `/editPortfolio?portfolioNo=${portfolioId}`;
}


// ✅ 포트폴리오 삭제 함수
async function deletePortfolio(event, button) {
    event.stopPropagation(); // 부모 요소 클릭 방지

    const portfolioCard = button.closest(".portfolio-card");
    const portfolioNo = portfolioCard.getAttribute("data-portfolio-id");

    if (!portfolioNo) {
        alert("포트폴리오 정보를 찾을 수 없습니다.");
        return;
    }

    if (!confirm("정말 삭제하시겠습니까?")) return;

    try {
        const response = await fetch(`/portfolio/delete?portfolioNo=${portfolioNo}`, {
            method: "DELETE"
        });

        if (!response.ok) throw new Error("삭제 실패");

        alert("포트폴리오가 삭제되었습니다.");
        portfolioCard.remove(); // UI에서 삭제
    } catch (error) {
        console.error("삭제 실패:", error);
        alert("삭제 중 오류 발생.");
    }
}