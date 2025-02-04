document.addEventListener("DOMContentLoaded", function () {
    console.log("포트폴리오 리스트 로드됨");
});

/* 포트폴리오 상세 보기 */
function viewPortfolioDetail(element) {
    const portfolioId = element.getAttribute("data-portfolio-id");
    window.location.href = `/portfolio/portfolioDetail?portfolioNo=${portfolioId}`;
}

/* 포트폴리오 수정 */
function editPortfolio(event, button) {
   event.stopPropagation(); // 부모 요소 클릭 방지

   const portfolioId = button.closest(".portfolio-card").getAttribute("data-portfolio-id");
   console.log(portfolioId);
   if (!portfolioId) {
       alert("포트폴리오 정보를 찾을 수 없습니다.");
       return;
   }

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

    // 모달창 동작 및 데이터 불러오는 함수
    function openPortfolioModal(element) {
        console.log("🔍 [DEBUG] openPortfolioModal 실행됨");

        const portfolioModalElement = document.getElementById('portfolioModal');
        if (!portfolioModalElement) {
            console.error("🚨 [ERROR] 'portfolioModal' 요소를 찾을 수 없습니다. HTML을 확인하세요.");
            return;
        }

        console.log("📢 [INFO] 'portfolioModal'이 존재함. 모달을 실행합니다.");

        try {
            var portfolioModal = new bootstrap.Modal(portfolioModalElement);
            portfolioModal.show();
        } catch (error) {
            console.error("🚨 [ERROR] 모달 실행 중 오류 발생: ", error);
        }

        // 선택한 포트폴리오의 데이터 가져오기
        const title = element.getAttribute('data-title');
        const memberName = element.getAttribute('data-member-name');
        const category = element.getAttribute('data-category');
        const introduction = element.getAttribute('data-introduction');
        const contactTime = element.getAttribute('data-contact-time');
        const content = element.getAttribute('data-content');
        const thumbnail = element.getAttribute('data-thumbnail');
        const images = element.getAttribute('data-images').split(',');

        console.log("🎯 [INFO] 선택된 포트폴리오 데이터: ", {
            title, memberName, category, introduction, contactTime, content, thumbnail, images
        });

        // 모달의 내용을 업데이트
        document.getElementById('portfolioTitle').textContent = title;
        document.getElementById('portfolioMember').textContent = memberName;
        document.getElementById('portfolioCategory').textContent = category;
        document.getElementById('portfolioIntroduction').textContent = introduction;
        document.getElementById('portfolioContactTime').textContent = contactTime;
        document.getElementById('portfolioContent').textContent = content;

        // 썸네일 이미지 설정
        if (thumbnail && thumbnail !== "null") {
            document.getElementById('thumbnailImage').src = thumbnail;
        } else {
            console.warn("⚠️ [WARNING] 썸네일 이미지 없음. 기본 이미지 사용");
            document.getElementById('thumbnailImage').src = "/images/default-thumbnail.png";
        }

        // 이미지 갤러리 업데이트
        let carouselInner = document.querySelector('.carousel-inner');
        carouselInner.innerHTML = `<div class="carousel-item active">
                                  <img src="${thumbnail}" class="d-block w-100" alt="썸네일 이미지">
                               </div>`;

        images.forEach((img, index) => {
            if (img.trim() !== '' && img !== 'null') {
                console.log(`📸 [INFO] 추가된 포트폴리오 이미지: ${img}`);
                carouselInner.innerHTML += `<div class="carousel-item">
                                          <img src="${img}" class="d-block w-100" alt="포트폴리오 이미지 ${index + 1}">
                                       </div>`;
            } else {
                console.warn(`⚠️ [WARNING] 이미지 ${index + 1}가 비어있음`);
            }
        });

        // 부트스트랩 모달 열기
        try {
            console.log("📢 [INFO] 모달 실행");
            var portfolioModal = new bootstrap.Modal(portfolioModalElement);
            portfolioModal.show();
        } catch (error) {
            console.error("🚨 [ERROR] 모달 실행 중 오류 발생: ", error);
        }
    }

}