// portfolioList.js

document.addEventListener("DOMContentLoaded", function () {
    const portfolioCards = document.querySelectorAll(".portfolio-card");

    portfolioCards.forEach((card) => {
        card.addEventListener("click", function () {
            const portfolioId = this.getAttribute("data-portfolio-id");
            window.location.href = `/portfolio/detail?portfolioNo=${portfolioId}`;
        });
    });
});

// 수정 버튼 클릭 시
function editPortfolio(event, element) {
    event.stopPropagation(); // 부모 클릭 방지
    const portfolioId = element.closest(".portfolio-card").getAttribute("data-portfolio-id");
    window.location.href = `/portfolio/edit?portfolioNo=${portfolioId}`;
}

// 삭제 버튼 클릭 시
function deletePortfolio(event, element) {
    event.stopPropagation();
    const portfolioId = element.closest(".portfolio-card").getAttribute("data-portfolio-id");

    if (confirm("정말로 삭제하시겠습니까?")) {
        fetch(`/portfolio/delete?portfolioNo=${portfolioId}`, { method: "DELETE" })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    alert("삭제 완료");
                    location.reload();
                } else {
                    alert(data.message);
                }
            })
            .catch((error) => console.error("삭제 실패:", error));
    }
}
