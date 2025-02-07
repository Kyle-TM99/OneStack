document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ portfolioModal.js 로드됨");
const modalContent = document.querySelector(".modal-content");
    const computedStyles = window.getComputedStyle(modalContent);
    console.log("🎨 현재 modal-content display:", computedStyles.display);
        // portfolioModal.css가 이미 로드되어 있는지 확인
        function isCSSLoaded(href) {
            return [...document.styleSheets].some(sheet => sheet.href && sheet.href.includes(href));
        }

        // portfolioModal.css가 로드되지 않았다면 추가
        if (!isCSSLoaded("portfolioModal.css")) {
            const cssLink = document.createElement("link");
            cssLink.rel = "stylesheet";
            cssLink.href = "/css/portfolioModal.css?v=" + new Date().getTime(); // 캐시 문제 방지
            cssLink.onload = () => console.log("✅ portfolioModal.css 강제 로드 성공");
            cssLink.onerror = () => console.error("❌ portfolioModal.css 로드 실패!");
            document.head.appendChild(cssLink);
        }

        console.log("✅ viewPortfolioDetail 함수가 정상적으로 로드되었습니다.");

    window.viewPortfolioDetail = function (portfolioId) {
        console.log("🟢 포트폴리오 클릭됨! ID:", portfolioId);

        $.ajax({
            url: `/portfolioDetail/${portfolioId}`,
            type: "GET",
            success: function (data) {
                console.log("🟢 서버 응답:", data);

                if (!data || Object.keys(data).length === 0) {
                    console.error("❌ 데이터 없음. 모달을 열 수 없음.");
                    alert("포트폴리오 데이터를 불러오는 중 오류가 발생했습니다.");
                    return;
                }

                // ✅ 제목, 기본 정보 업데이트
                $("#portfolioTitle").text(data.portfolioTitle);
                $("#portfolioContent").text(data.portfolioContent);
                $("#memberName").text(data.memberName || "알 수 없음");
                $("#categoryTitle").text(data.categoryTitle || "없음");
                $("#selfIntroduction").text(data.selfIntroduction || "없음");
                $("#contactableTime").text(data.contactableTime || "없음");

                // ✅ undefined 또는 null 방지 및 줄바꿈 처리
                // ✅ data.career와 data.awardCareer가 배열인지 확인 후 첫 번째 요소만 사용
                let careerData = Array.isArray(data.career) && data.career.length > 0 ? data.career[0].split("\n").map(c => c.trim()) : [];
                let awardData = Array.isArray(data.awardCareer) && data.awardCareer.length > 0 ? data.awardCareer[0].split("\n").map(a => a.trim()) : [];

                // ✅ <li> 태그로 변환하여 HTML에 적용
                let careerList = careerData.length > 0
                    ? careerData.map(c => `<li>${c}</li>`).join("")
                    : "<li>경력 없음</li>";
                $("#careerList").html(careerList);

                let awardList = awardData.length > 0
                    ? awardData.map(a => `<li>${a}</li>`).join("")
                    : "<li>수상 경력 없음</li>";
                $("#awardList").html(awardList);



                // ✅ 이미지 슬라이드 추가 (썸네일 + 포트폴리오 이미지들)
                let imagesHtml = "";

                // 1️⃣ 썸네일 이미지 (항상 첫 번째)
                imagesHtml += `
                    <div class="carousel-item active">
                        <img th:src="${data.thumbnailImage}" class="d-block w-100 rounded shadow-lg" alt="썸네일 이미지">
                    </div>
                `;

                // 2️⃣ 포트폴리오 이미지 1 (필수)
                if (data.portfolioFiles[0]) {
                    imagesHtml += `
                        <div class="carousel-item">
                            <img src="${data.portfolioFiles[0]}" class="d-block w-100 rounded shadow-lg" alt="포트폴리오 이미지 1">
                        </div>
                    `;
                }

                // 3️⃣ 포트폴리오 이미지 2~10 (null 허용)
                data.portfolioFiles.slice(1).forEach((file, index) => {
                    if (file) {
                        imagesHtml += `
                            <div class="carousel-item">
                                <img src="${file}" class="d-block w-100 rounded shadow-lg" alt="포트폴리오 이미지 ${index + 2}">
                            </div>
                        `;
                    }
                });

                // ✅ 기존 슬라이드 초기화 후 새 이미지 추가
                $("#portfolioImages").html(imagesHtml);

                console.log("✅ 모달 표시 준비 완료");
                $('#portfolioDetailModal').modal('show');
            },
            error: function (xhr, status, error) {
                console.error("❌ AJAX 요청 오류:", xhr.status, xhr.responseText);
            }
        });
    };

    console.log("✅ viewPortfolioDetail 함수가 정상적으로 로드되었습니다.");
});
