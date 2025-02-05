document.addEventListener("DOMContentLoaded", function () {
    if (window.portfolioEditScriptLoaded) return;
    window.portfolioEditScriptLoaded = true;

    const categorySelect = document.getElementById("categoryNo");
    const itemSelect = document.getElementById("itemNo");
    const surveyContainer = document.getElementById("surveyContainer");
    const updateBtn = document.getElementById("updatePortfolioBtn");
    const portfolioForm = document.getElementById("portfolioForm");
    const portfolioNoInput = document.getElementById("portfolioNo");
    const thumbnailInput = document.getElementById("thumbnailImage");
    const currentThumbnail = document.getElementById("currentThumbnail").textContent.trim();
    const removeThumbnailBtn = document.querySelector(".remove-thumbnail");
    const portfolioFileContainer = document.getElementById("portfolioFileContainer");

    console.log("✅ editPortfolio.js 로드 완료");

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

    // ✅ 기존 데이터 유지
    const savedCategory = categorySelect.value;
    const savedItem = itemSelect.value;

    // ✅ 선택된 카테고리에 맞춰 전문분야 옵션 필터링 (초기 로드 시 실행)
    updateItemOptions(savedCategory, savedItem);

    // ✅ 카테고리 선택 시 해당하는 전문분야 표시
    categorySelect.addEventListener("change", function () {
        updateItemOptions(categorySelect.value);
    });

    // ✅ 전문분야 옵션 필터링 함수
    function updateItemOptions(selectedCategory, selectedItem = null) {
        itemSelect.innerHTML = '<option value="" hidden>전문분야 선택</option>'; // 기존 옵션 초기화

        if (selectedCategory && categoryOptions[selectedCategory]) {
            categoryOptions[selectedCategory].forEach(option => {
                const opt = document.createElement("option");
                opt.value = option.value;
                opt.textContent = option.text;
                if (selectedItem && option.value === selectedItem) {
                    opt.selected = true;  // 저장된 값이 있으면 자동 선택
                }
                itemSelect.appendChild(opt);
            });
        }
    }

    // ✅ 카테고리 선택 시 해당하는 전문분야 표시
    categorySelect.addEventListener("change", function () {
        updateItemOptions(categorySelect.value);
    });

    // ✅ 전문분야 변경 시 설문조사 데이터를 AJAX로 가져오기
    itemSelect.addEventListener("change", function () {
        const itemNo = this.value;
        if (!itemNo) return;

        fetch(`/api/getSurvey?itemNo=${itemNo}`)
            .then(response => {
                if (!response.ok) throw new Error("설문조사 데이터를 불러오지 못했습니다.");
                return response.json();
            })
            .then(data => {
                updateSurveyUI(data);
            })
            .catch(error => console.error("🚨 설문조사 로드 오류:", error));
    });

    // ✅ 설문조사 데이터를 동적으로 갱신하는 함수
    function updateSurveyUI(surveyList) {
        surveyContainer.innerHTML = ""; // 기존 설문조사 데이터 초기화

        if (!surveyList || surveyList.length === 0) {
            surveyContainer.innerHTML = "<p class='text-muted'>해당 전문분야에 대한 설문조사가 없습니다.</p>";
            return;
        }

        surveyList.forEach(survey => {
            const questionBlock = document.createElement("div");
            questionBlock.classList.add("fw-bold", "my-2");
            questionBlock.innerHTML = `Q${survey.survey.surveyNo}: ${survey.survey.surveyQuestion}`;

            const optionsBlock = document.createElement("div");

            const options = survey.survey.surveyOption.split(",");
            options.forEach(option => {
                const optionDiv = document.createElement("div");
                optionDiv.classList.add("form-check");

                const input = document.createElement("input");
                input.type = "radio";
                input.classList.add("form-check-input");
                input.name = `answer_${survey.survey.surveyNo}`;
                input.value = option.trim();
                input.required = true;

                const label = document.createElement("label");
                label.classList.add("form-check-label", "ms-2");
                label.textContent = option.trim();

                optionDiv.appendChild(input);
                optionDiv.appendChild(label);
                optionsBlock.appendChild(optionDiv);
            });

            surveyContainer.appendChild(questionBlock);
            surveyContainer.appendChild(optionsBlock);
        });

        console.log("✅ 설문조사 데이터 로드 완료:", surveyList);
    }

    // ✅ 기존 파일 리스트 로드
    const existingFiles = JSON.parse(portfolioFileContainer.getAttribute("data-files-json") || "[]");
    existingFiles.forEach(file => {
        const fileElement = document.createElement("p");
        fileElement.innerHTML = `📂 기존 파일: ${file} <button type="button" class="btn btn-danger btn-sm remove-btn" data-file="${file}">X</button>`;
        portfolioFileContainer.appendChild(fileElement);
    });



    // ✅ 썸네일이 존재하면 파일 입력 필드를 비활성화
    if (currentThumbnail) {
        thumbnailInput.disabled = true;
    }

    // ✅ 썸네일 이미지 추가 시 제한
    thumbnailInput.addEventListener("change", function () {
        if (currentThumbnail) {
            alert("기존 썸네일이 이미 존재합니다. 삭제 후 새 파일을 추가하세요.");
            thumbnailInput.value = ""; // 파일 선택 초기화
        }
    });

    // ✅ 썸네일 삭제 버튼 이벤트
    removeThumbnailBtn.addEventListener("click", function () {
        if (confirm("썸네일을 삭제하시겠습니까?")) {
            document.getElementById("currentThumbnail").textContent = "삭제됨";
            thumbnailInput.value = "";
            thumbnailInput.disabled = false; // 다시 선택 가능하도록 활성화
        }
    });

        // ✅ 수상 경력 추가 버튼 이벤트
        document.getElementById("addAwardCareerBtn").addEventListener("click", function () {
            const awardsContainer = document.getElementById("awardsContainer");

            const newAwardInput = document.createElement("div");
            newAwardInput.className = "d-flex align-items-center justify-content-between mb-2 award-entry";
            newAwardInput.innerHTML = `
                <input type="text" class="form-control me-2" name="awardCareer" placeholder="수상 경력을 입력하세요">
                <button type="button" class="btn btn-danger btn-sm remove-award">X</button>
            `;

            awardsContainer.appendChild(newAwardInput);
        });

        // ✅ 수상 경력 삭제 버튼 이벤트 (이벤트 위임)
        document.addEventListener("click", function (event) {
            if (event.target.classList.contains("remove-award")) {
                event.target.parentElement.remove();
            }
        });

        // ✅ 경력 추가 버튼 이벤트
           document.getElementById("addCareerBtn").addEventListener("click", function () {
               const careerContainer = document.getElementById("careerContainer");

               const newCareerInput = document.createElement("div");
               newCareerInput.className = "d-flex align-items-center justify-content-between mb-2 career-entry";
               newCareerInput.innerHTML = `
                   <input type="text" class="form-control me-2" name="career" placeholder="경력을 입력하세요">
                   <button type="button" class="btn btn-danger btn-sm remove-career">X</button>
               `;

               careerContainer.appendChild(newCareerInput);
         });

        // ✅ 경력 삭제 버튼 이벤트 (이벤트 위임)
        document.addEventListener("click", function (event) {
            if (event.target.classList.contains("remove-career")) {
                event.target.parentElement.remove();
            }
        });



    // ✅ 포트폴리오 파일 추가 버튼 이벤트 (이벤트 위임 방식)
    document.addEventListener('click', function (event) {
        if (event.target.id === 'addFileButtonBtn') {
            const portfolioFileContainer = document.getElementById('portfolioFileContainer');
            let fileInputs = portfolioFileContainer.querySelectorAll('input[type="file"]');
            let fileCount = fileInputs.length;

            if (fileCount >= 10) {
                alert('최대 10개의 파일만 추가할 수 있습니다.');
                return;
            }

            const newFileInput = document.createElement('div');
            newFileInput.className = 'd-flex align-items-center justify-content-between mb-2 file-input';
            newFileInput.innerHTML = `
                <input type="file" class="form-control portfolioFiles me-2" name="portfolioFiles" accept="image/*">
                <button type="button" class="btn btn-danger btn-sm remove-file">X</button>
            `;

            portfolioFileContainer.appendChild(newFileInput);
        }
    });

    // ✅ 추가한 포트폴리오 파일 삭제 기능 수정
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("remove-file")) {
            event.target.closest(".file-input").remove();
            console.log("🗑️ 추가된 파일 삭제됨");
        }
    });
       // ✅ 파일 삭제 버튼 이벤트 (이벤트 위임)

     document.addEventListener("click", function (event) {
         if (event.target.classList.contains("remove-existing-file")) {
             const targetElement = event.target.closest(".existing-file");
             if (targetElement) {
                 if (confirm("해당 파일을 삭제하시겠습니까?")) {
                     targetElement.remove();
                     console.log("🗑️ 기존 파일 삭제됨");
                 }
             }
         }
     });


    // ✅ 포트폴리오 수정 완료 버튼
    function validatePortfolioForm() {
         let isValid = true;
         let errorMessage = "";

         const portfolioTitle = document.getElementById("portfolioTitle").value.trim();
         const portfolioContent = document.getElementById("portfolioContent").value.trim();
         const categoryNo = document.getElementById("categoryNo").value;
         const itemNo = document.getElementById("itemNo").value;
         const selfIntroduction = document.getElementById("selfIntroduction").value.trim();
         const contactableTimeStart = document.getElementById("contactableTimeStart").value;
         const contactableTimeEnd = document.getElementById("contactableTimeEnd").value;
         const careerInputs = document.querySelectorAll("input[name='career']");
         const surveyAnswers = document.querySelectorAll("input[type='radio']:checked");

         // ✅ 필수 항목 확인
         if (!portfolioTitle) {
             errorMessage += "포트폴리오 제목을 입력해주세요.\n";
             isValid = false;
         }
         if (!portfolioContent) {
             errorMessage += "포트폴리오 내용을 입력해주세요.\n";
             isValid = false;
         }
         if (!categoryNo) {
             errorMessage += "카테고리를 선택해주세요.\n";
             isValid = false;
         }
         if (!itemNo) {
             errorMessage += "전문분야를 선택해주세요.\n";
             isValid = false;
         }
         if (!selfIntroduction) {
             errorMessage += "자기소개를 입력해주세요.\n";
             isValid = false;
         }
         if (!contactableTimeStart || !contactableTimeEnd) {
             errorMessage += "연락 가능 시간을 선택해주세요.\n";
             isValid = false;
         }

         // ✅ 최소 한 개 이상의 경력 입력 체크
         let hasCareer = false;
         careerInputs.forEach(input => {
             if (input.value.trim() !== "") hasCareer = true;
         });
         if (!hasCareer) {
             errorMessage += "경력을 최소 1개 이상 입력해주세요.\n";
             isValid = false;
         }

         // ✅ 설문조사 답변 확인
         if (surveyAnswers.length === 0) {
             errorMessage += "설문조사 문항에 최소 1개 이상 답변해주세요.\n";
             isValid = false;
         }

         // ✅ 유효성 검사 실패 시 알림창 표시
         if (!isValid) {
             alert(errorMessage);
         }

         return isValid;
     }

     // ✅ 포트폴리오 수정 완료 버튼
     updateBtn.addEventListener("click", async function (event) {
         event.preventDefault(); // 기본 동작 방지

         if (!validatePortfolioForm()) {
             return; // 유효성 검사 실패 시 실행 중단
         }

         const formData = new FormData(portfolioForm);

         // ✅ JSON 변환을 위해 객체 생성
         const requestData = {
             portfolioNo: document.getElementById("portfolioNo").value,
             proNo: document.getElementById("proNo").value,
             proAdvancedNo: document.getElementById("proAdvancedNo").value,
             portfolioTitle: document.getElementById("portfolioTitle").value,
             portfolioContent: document.getElementById("portfolioContent").value,
             thumbnailImage: document.getElementById("currentThumbnail").textContent.trim(),
             portfolioFilePaths: Array.from(document.querySelectorAll(".currentPortfolioFile")).map(el => el.textContent.trim()),
             categoryNo: document.getElementById("categoryNo").value,
             itemNo: document.getElementById("itemNo").value,
             selfIntroduction: document.getElementById("selfIntroduction").value,
             contactableTimeStart: document.getElementById("contactableTimeStart").value,
             contactableTimeEnd: document.getElementById("contactableTimeEnd").value,
             career: Array.from(document.querySelectorAll("input[name='career']")).map(input => input.value),
             awardCareer: Array.from(document.querySelectorAll("input[name='awardCareer']")).map(input => input.value), // null 허용
             surveyAnswers: Array.from(document.querySelectorAll("input[type='radio']:checked")).map(input => input.value),
             proAnswer1: document.getElementById("proAnswer1")?.value.trim(), // 필수
             proAnswer2: document.getElementById("proAnswer2")?.value.trim() || null, // null 허용
             proAnswer3: document.getElementById("proAnswer3")?.value.trim() || null, // null 허용
             proAnswer4: document.getElementById("proAnswer4")?.value.trim() || null, // null 허용
             proAnswer5: document.getElementById("proAnswer5")?.value.trim() || null // null 허용
         };

         console.log("📤 전송할 데이터:", requestData);

         try {
             const response = await fetch("/portfolio/update", {
                 method: "POST",
                 headers: {
                     "Content-Type": "application/json"
                 },
                 body: JSON.stringify(requestData)
             });

             if (response.status === 409) {
                 const result = await response.json();
                 alert(result.message);
                 return;
             }

             alert("포트폴리오가 성공적으로 수정되었습니다.");
             window.location.href = "/portfolioList";
         } catch (error) {
             console.error("🚨 오류 발생:", error);
             alert("수정 중 오류가 발생했습니다.");
         }
     });


    // ✅ 취소 버튼
    document.getElementById("cancelBtn")?.addEventListener("click", function () {
        if (confirm("수정을 취소하시겠습니까?")) {
            window.location.href = "/portfolioList";
        }
    });

});
