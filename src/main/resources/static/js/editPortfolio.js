document.addEventListener('DOMContentLoaded', function () {
    if (window.portfolioEditScriptLoaded) return;
    window.portfolioEditScriptLoaded = true;

    const categorySelect = document.getElementById("categoryNo");
    const itemSelect = document.getElementById("itemNo");
    const surveyContainer = document.getElementById("surveyContainer");
    const updateBtn = document.getElementById("updatePortfolioBtn");
    const portfolioForm = document.getElementById("portfolioForm");
    const portfolioNoInput = document.getElementById("portfolioNo");

    if (!categorySelect || !itemSelect || !surveyContainer || !updateBtn || !portfolioForm || !portfolioNoInput) {
        console.error("필수 요소를 찾을 수 없습니다.");
        return;
    }

    // ✅ 기존 데이터 유지
    const savedCategory = categorySelect.getAttribute("data-db-value");
    const savedItem = itemSelect.getAttribute("data-db-value");

    // ✅ 카테고리별 전문분야 목록
    const categoryOptions = {
        "1": [
            { value: "11", text: "기획" },
            { value: "12", text: "웹" },
            { value: "13", text: "소프트웨어" },
            { value: "14", text: "안드로이드" },
            { value: "15", text: "iOS" },
            { value: "16", text: "게임" },
            { value: "17", text: "AI" },
            { value: "18", text: "QA 및 테스트" }
        ],
        "2": [
            { value: "21", text: "가공 및 라벨링" },
            { value: "22", text: "데이터 복구" },
            { value: "23", text: "크롤링" },
            { value: "24", text: "DB 구축" },
            { value: "25", text: "통계 분석" }
        ]
    };

    // ✅ 기존 데이터 로드
    if (savedCategory) {
        categorySelect.value = savedCategory;
        updateItemOptions(savedCategory, savedItem);
    }

    function updateItemOptions(selectedCategory, selectedItem = null) {
        itemSelect.innerHTML = '<option value="">전문분야 선택</option>';
        if (selectedCategory && categoryOptions[selectedCategory]) {
            categoryOptions[selectedCategory].forEach(option => {
                const opt = document.createElement("option");
                opt.value = option.value;
                opt.textContent = option.text;
                if (selectedItem && option.value === selectedItem) {
                    opt.selected = true;
                }
                itemSelect.appendChild(opt);
            });
        }
    }

    // ✅ 카테고리 선택 시 해당하는 전문분야 표시
    categorySelect.addEventListener("change", function () {
        updateItemOptions(categorySelect.value);
    });

    // ✅ 전문분야 변경 시 자동 리로드
    itemSelect.addEventListener("change", function () {
        location.href = `/editPortfolio?portfolioNo=${portfolioNoInput.value}&itemNo=${this.value}`;
    });

    // ✅ 기존 파일 리스트 로드
    const portfolioFileContainer = document.getElementById("portfolioFileContainer");
    const existingFiles = JSON.parse(portfolioFileContainer.getAttribute("data-files-json") || "[]");
    existingFiles.forEach(file => {
        const fileElement = document.createElement("p");
        fileElement.innerHTML = `📂 현재 파일: ${file} <button type="button" class="btn btn-danger btn-sm remove-btn" data-file="${file}">삭제</button>`;
        portfolioFileContainer.appendChild(fileElement);
    });

    // ✅ 파일 추가 기능
    document.querySelector(".add-file-btn")?.addEventListener("click", function () {
        if (portfolioFileContainer.querySelectorAll('input[type="file"]').length >= 10) {
            alert("최대 10개의 파일만 추가할 수 있습니다.");
            return;
        }

        const newFileDiv = document.createElement("div");
        newFileDiv.className = "mb-3";
        newFileDiv.innerHTML = `
            <input type="file" class="form-control portfolioFiles" name="portfolioFiles" accept="image/*">
            <button type="button" class="btn btn-danger btn-sm remove-btn mt-2">삭제</button>
        `;
        portfolioFileContainer.appendChild(newFileDiv);
    });

    // ✅ 기존 경력 데이터 로드
    const careerContainer = document.getElementById("careerContainer");
    careerContainer.querySelectorAll("input").forEach(input => {
        input.value = input.getAttribute("data-db-value") || "";
    });

    // ✅ 기존 수상 경력 데이터 로드
    const awardsContainer = document.getElementById("awardsContainer");
    awardsContainer.querySelectorAll("input").forEach(input => {
        input.value = input.getAttribute("data-db-value") || "";
    });

    // ✅ 삭제 버튼 이벤트
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("remove-btn")) {
            event.target.parentElement.remove();
        }
    });

    // ✅ 포트폴리오 수정 완료 버튼
    updateBtn.addEventListener("click", async function () {
        const formData = new FormData(portfolioForm);

        try {
            const response = await fetch("/editPortfolio/submit", {
                method: "POST",
                body: formData
            });

            if (!response.ok) throw new Error("서버 응답 오류");
            alert("포트폴리오가 수정되었습니다.");
            window.location.href = "/portfolioList";
        } catch (error) {
            console.error("오류 발생:", error);
            alert("수정 중 오류가 발생했습니다.");
        }
    });

    // ✅ 취소 버튼
    document.getElementById("cancelBtn")?.addEventListener("click", function () {
        if (confirm("수정을 취소하시겠습니까?")) {
            window.location.href = "/portfolioList";
        }
    });

    // ✅ 썸네일 삭제 기능
    document.querySelector(".remove-thumbnail")?.addEventListener("click", function () {
        if (confirm("썸네일을 삭제하시겠습니까?")) {
            document.getElementById("currentThumbnail").textContent = "삭제됨";
            document.getElementById("thumbnailImage").value = "";
        }
    });
});
