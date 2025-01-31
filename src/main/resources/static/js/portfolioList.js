document.addEventListener("DOMContentLoaded", function () {
    console.log("포트폴리오 리스트 로드됨");
});

/* 포트폴리오 상세 보기 */
function viewPortfolioDetail(element) {
    const portfolioId = element.getAttribute("data-portfolio-id");
    window.location.href = `/portfolio/detail?portfolioNo=${portfolioId}`;
}

/* 포트폴리오 수정 */
function editPortfolio(event, element) {
    event.stopPropagation();
    const portfolioId = element.closest(".portfolio-card").getAttribute("data-portfolio-id");
    window.location.href = `/portfolio/edit?portfolioNo=${portfolioId}`;
}

/* 포트폴리오 삭제 */
function deletePortfolio(event, element) {
    event.stopPropagation();
    const portfolioId = element.closest(".portfolio-card").getAttribute("data-portfolio-id");

    if (confirm("정말 삭제하시겠습니까?")) {
        fetch(`/portfolio/delete?portfolioNo=${portfolioId}`, { method: "DELETE" })
            .then(response => {
                if (response.ok) {
                    alert("포트폴리오가 삭제되었습니다.");
                    window.location.reload();
                } else {
                    alert("삭제 실패. 다시 시도해주세요.");
                }
            })
            .catch(error => console.error("삭제 오류:", error));
    }
}
