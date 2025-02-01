document.addEventListener('DOMContentLoaded', function () {
    const categorySelect = document.getElementById("categoryNo");
    const itemSelect = document.getElementById("itemNo");
    const surveyContainer = document.getElementById("surveyContainer");
    const submitBtn = document.getElementById("submitPortfolioBtn");
    const portfolioForm = document.getElementById("portfolioForm");
    const selectedItemNoInput = document.getElementById("selectedItemNo");

    if (!categorySelect || !itemSelect || !surveyContainer || !submitBtn || !portfolioForm) {
        console.error("필수 요소를 찾을 수 없습니다.");
        return;
    }

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
            { value: "18", text: "QA 및 테스트" },
        ],
        "2": [
            { value: "21", text: "가공 및 라벨링" },
            { value: "22", text: "데이터 복구" },
            { value: "23", text: "크롤링" },
            { value: "24", text: "DB 구축" },
            { value: "25", text: "통계 분석" },
        ]
    };

    // ✅ 카테고리 선택 시 해당하는 전문분야 표시
    categorySelect.addEventListener("change", function () {
        const selectedCategory = categorySelect.value;
        itemSelect.innerHTML = '<option value="" hidden>전문분야를 선택해주세요.</option>';

        if (selectedCategory && categoryOptions[selectedCategory]) {
            categoryOptions[selectedCategory].forEach(option => {
                const opt = document.createElement("option");
                opt.value = option.value;
                opt.textContent = option.text;
                itemSelect.appendChild(opt);
            });
        }
    });

    // ✅ 전문분야 선택 시 해당하는 설문조사 표시
    itemSelect.addEventListener("change", function () {
        selectedItemNoInput.value = itemSelect.value;

        const surveyElements = document.querySelectorAll("#surveyContainer > div");
        surveyElements.forEach(el => {
            if (el.getAttribute("data-itemNo") === selectedItemNoInput.value) {
                el.style.display = "block";
            } else {
                el.style.display = "none";
            }
        });
    });

    // ✅ 버튼 클릭 이벤트 (이벤트 위임)
    document.body.addEventListener('click', function (event) {
        // 🚀 경력 추가 버튼
        if (event.target.id === 'addCareerBtn') {
            const careerContainer = document.getElementById('careerContainer');
            const careerCount = careerContainer.children.length + 1;
            const newCareerInput = document.createElement('div');
            newCareerInput.className = 'mb-3';
            newCareerInput.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <label class="form-label">경력 ${careerCount}</label>
                    <button type="button" class="btn btn-danger btn-sm remove-btn">삭제</button>
                </div>
                <input type="text" class="form-control" name="career" required>
            `;
            careerContainer.appendChild(newCareerInput);
        }

        // 🚀 수상 경력 추가 버튼
        if (event.target.id === 'addAwardCareerBtn') {
            const awardsContainer = document.getElementById('awardsContainer');
            const awardsCount = awardsContainer.children.length + 1;
            const newAwardInput = document.createElement('div');
            newAwardInput.className = 'mb-3';
            newAwardInput.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <label class="form-label">수상경력 ${awardsCount}</label>
                    <button type="button" class="btn btn-danger btn-sm remove-btn">삭제</button>
                </div>
                <input type="text" class="form-control" name="awardCareer" required>
            `;
            awardsContainer.appendChild(newAwardInput);
        }

        // 🚀 포트폴리오 파일 추가 버튼
        if (event.target.id === 'addFileButtonBtn') {
            const portfolioFileContainer = document.getElementById('portfolioFileContainer');
            let fileCount = portfolioFileContainer.children.length;

            if (fileCount >= 10) {
                alert('최대 10개의 파일만 추가할 수 있습니다.');
                return;
            }

            const newFileInput = document.createElement('div');
            newFileInput.className = 'mb-3';
            newFileInput.innerHTML = `
                <label class="form-label">포트폴리오 이미지 ${fileCount + 1}</label>
                <input type="file" class="form-control" name="portfolioFile${fileCount + 1}" accept="image/*" />
                <button type="button" class="btn btn-danger btn-sm remove-btn">삭제</button>
            `;
            portfolioFileContainer.appendChild(newFileInput);
        }

        // 🚀 삭제 버튼 동작
        if (event.target.classList.contains('remove-btn')) {
            event.target.parentElement.parentElement.remove();
        }
    });

    // ✅ 포트폴리오 제출 이벤트
    submitBtn.addEventListener("click", function () {
        const formData = new FormData(portfolioForm);

        // ✅ 선택된 설문조사 데이터 수집
        const surveyAnswers = [];
        surveyContainer.querySelectorAll('.form-check-input:checked').forEach(input => {
            surveyAnswers.push(input.value);
        });
        formData.append("surveyAnswers", JSON.stringify(surveyAnswers));

        fetch('/proConversion/save', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            window.location.href = '/portfolioList';
        })
        .catch(error => {
            console.error('포트폴리오 저장 실패:', error);
            alert('포트폴리오 저장 중 오류 발생');
        });
    });
});
