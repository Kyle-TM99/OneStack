document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ portfolioModal.js 로드됨");

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

                // ✅ 제목, 이미지, 설명 추가
                $("#portfolioTitle").text(data.portfolioTitle);
                $("#thumbnailImage").attr("src", data.thumbnailImage);
                $("#portfolioContent").text(data.portfolioContent);

                $("#memberName").text(data.memberName || "알 수 없음");
                $("#categoryTitle").text(data.categoryTitle || "없음");
                $("#selfIntroduction").text(data.selfIntroduction || "없음");
                $("#contactableTime").text(data.contactableTime || "없음");

                let careerList = data.career.length > 0 ? data.career.map(c => `<li>${c}</li>`).join("") : "<li>경력 없음</li>";
                $("#careerList").html(careerList);

                let awardList = data.awardCareer.length > 0 ? data.awardCareer.map(a => `<li>${a}</li>`).join("") : "<li>수상 경력 없음</li>";
                $("#awardList").html(awardList);

                // ✅ 포트폴리오 이미지 추가
                let imagesHtml = "";
                data.portfolioFiles.forEach((file, index) => {
                    if (file) {
                        imagesHtml += `
                            <div class="carousel-item ${index === 0 ? 'active' : ''}">
                                <img src="${file}" class="d-block w-100 rounded shadow-lg" alt="포트폴리오 이미지">
                            </div>
                        `;
                    }
                });

                $("#portfolioImages").html(imagesHtml || "<p>이미지가 없습니다.</p>");

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
