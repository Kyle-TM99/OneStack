document.addEventListener('DOMContentLoaded', function () {
    if (window.editPortfolioScriptLoaded) return;
    window.editPortfolioScriptLoaded = true;

    const categorySelect = document.getElementById("categoryNo");
    const itemSelect = document.getElementById("itemNo");
    const surveyContainer = document.getElementById("surveyContainer");
    const updateBtn = document.getElementById("updatePortfolioBtn");
    const portfolioForm = document.getElementById("portfolioForm");

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

    // ✅ 카테고리 선택 시 해당하는 전문분야 표시
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

    // ✅ 초기 전문분야 목록 불러오기
    if (categorySelect && itemSelect) {
        const selectedCategory = categorySelect.value;
        const selectedItem = document.getElementById("selectedItemNo").value;
        updateItemOptions(selectedCategory, selectedItem);

        categorySelect.addEventListener("change", function () {
            updateItemOptions(this.value);
        });
    }

    // ✅ 포트폴리오 파일 추가 기능
    document.getElementById('addFileButtonBtn').addEventListener('click', function () {
        const portfolioFileContainer = document.getElementById('portfolioFileContainer');
        const fileCount = portfolioFileContainer.querySelectorAll('input[type="file"]').length;

        if (fileCount >= 10) {
            alert("최대 10개의 파일만 추가할 수 있습니다.");
            return;
        }

        const newFileDiv = document.createElement('div');
        newFileDiv.className = 'mb-3';
        newFileDiv.innerHTML = `
            <input type="file" class="form-control portfolioFiles" name="portfolioFiles" accept="image/*">
            <button type="button" class="btn btn-danger btn-sm remove-btn mt-2">삭제</button>
        `;
        portfolioFileContainer.appendChild(newFileDiv);
    });

    // ✅ 파일 삭제 기능 (이벤트 위임)
    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('remove-btn')) {
            e.target.parentElement.remove();
        }
    });

    // ✅ 썸네일 이미지 삭제 기능
    document.querySelector('.remove-existing-thumbnail').addEventListener('click', function () {
        if (confirm("썸네일 이미지를 삭제하시겠습니까?")) {
            document.getElementById("currentThumbnail").textContent = "";
        }
    });

    // ✅ 기존 포트폴리오 파일 삭제 기능
    document.querySelectorAll('.remove-existing-file').forEach(button => {
        button.addEventListener('click', function () {
            if (confirm("이 파일을 삭제하시겠습니까?")) {
                this.parentElement.remove();
            }
        });
    });

    // ✅ 경력 추가 기능
    document.getElementById('addCareer').addEventListener('click', function () {
        const careerContainer = document.getElementById('careerContainer');
        const newCareerInput = document.createElement('div');
        newCareerInput.className = 'd-flex mb-2';
        newCareerInput.innerHTML = `
            <input type="text" class="form-control" name="career">
            <button type="button" class="btn btn-danger btn-sm remove-career ms-2">삭제</button>
        `;
        careerContainer.appendChild(newCareerInput);
    });

    // ✅ 수상 경력 추가 기능
    document.getElementById('addAward').addEventListener('click', function () {
        const awardContainer = document.getElementById('awardContainer');
        const newAwardInput = document.createElement('div');
        newAwardInput.className = 'd-flex mb-2';
        newAwardInput.innerHTML = `
            <input type="text" class="form-control" name="awardCareer">
            <button type="button" class="btn btn-danger btn-sm remove-award ms-2">삭제</button>
        `;
        awardContainer.appendChild(newAwardInput);
    });

    // ✅ 경력 및 수상 경력 삭제 기능 (이벤트 위임)
    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('remove-career')) {
            e.target.parentElement.remove();
        }
        if (e.target.classList.contains('remove-award')) {
            e.target.parentElement.remove();
        }
    });

    // ✅ 수정 완료 버튼 클릭 시 데이터 전송
    updateBtn.addEventListener('click', async function () {
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

    // ✅ 취소 버튼 클릭 시 목록으로 이동
    document.getElementById('cancelBtn').addEventListener('click', function () {
        if (confirm("수정을 취소하시겠습니까?")) {
            window.location.href = "/portfolioList";
        }
    });
});
