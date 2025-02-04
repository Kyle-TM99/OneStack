document.addEventListener('DOMContentLoaded', function () {
    if (window.portfolioScriptLoaded) return;
    window.portfolioScriptLoaded = true;

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

// ✅ 기본 카테고리 설정
    function setDefaultCategoryOption() {
        categorySelect.innerHTML = ""; // 기존 옵션 제거

        const defaultOption = document.createElement("option");
        defaultOption.value = "";
        defaultOption.textContent = "카테고리를 선택해주세요.";
        defaultOption.selected = true;
        defaultOption.hidden = true;
        categorySelect.appendChild(defaultOption);

        // 기존 카테고리 목록 추가
        Object.keys(categoryOptions).forEach(categoryValue => {
            const opt = document.createElement("option");
            opt.value = categoryValue;
            opt.textContent = categoryValue === "1" ? "개발" : "데이터";
            categorySelect.appendChild(opt);
        });

        categorySelect.value = ""; // 강제 초기화
    }

    // ✅ 기본 전문분야 설정
    function setDefaultItemOption() {
        itemSelect.innerHTML = ""; // 기존 옵션 초기화

        const defaultOption = document.createElement("option");
        defaultOption.value = "";
        defaultOption.textContent = "전문분야를 선택해주세요.";
        defaultOption.selected = true;
        defaultOption.hidden = true;

        itemSelect.appendChild(defaultOption);
        itemSelect.value = ""; // 강제 초기화
    }

    // ✅ 카테고리 선택 시 해당하는 전문분야 표시
    function updateItemOptions(selectedCategory, selectedItem = null) {
        setDefaultItemOption(); // 기본 옵션 추가

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

    // ✅ 리로드 시 sessionStorage 값 복원
    const savedCategory = sessionStorage.getItem("selectedCategory");
    const savedItem = sessionStorage.getItem("selectedItem");

    if (savedCategory) {
        categorySelect.value = savedCategory;
        updateItemOptions(savedCategory, savedItem);
    } else {
        setDefaultCategoryOption();
        setDefaultItemOption();
    }

    // ✅ 카테고리 변경 시 sessionStorage 업데이트
    categorySelect.addEventListener("change", function () {
        sessionStorage.setItem("selectedCategory", categorySelect.value);
        sessionStorage.removeItem("selectedItem"); // 새 카테고리를 선택하면 기존 전문분야 초기화
        updateItemOptions(categorySelect.value);
    });

    // ✅ 전문분야 선택 시 sessionStorage 업데이트
    itemSelect.addEventListener("change", function () {
        sessionStorage.setItem("selectedItem", itemSelect.value);
    });

    // ✅ 페이지를 완전히 벗어날 때 sessionStorage 초기화
    window.addEventListener("pageshow", function (event) {
        if (event.persisted) return; // 뒤로가기 방지

        sessionStorage.removeItem("selectedCategory");
        sessionStorage.removeItem("selectedItem");
    });

    // ✅ 설문조사 데이터 로드 (선택한 전문분야가 있을 경우)
    if (savedItem) {
        fetchSurveyData(savedItem);
    }

    itemSelect.addEventListener("change", function () {
        fetchSurveyData(itemSelect.value);
    });

    function fetchSurveyData(itemNo) {
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
    }

    function updateSurveyUI(surveyList) {
        surveyContainer.innerHTML = ""; // 기존 설문조사 초기화

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

//            function setDefaultCategoryOption() {
//                categorySelect.innerHTML = ""; // 기존 옵션 제거
//
//                const defaultOption = document.createElement("option");
//                defaultOption.value = "";
//                defaultOption.textContent = "카테고리를 선택해주세요.";
//                defaultOption.selected = true;
//                defaultOption.hidden = true;
//
//                categorySelect.appendChild(defaultOption);
//
//                // 기존 카테고리 목록 추가
//                Object.keys(categoryOptions).forEach(categoryValue => {
//                    const opt = document.createElement("option");
//                    opt.value = categoryValue;
//                    opt.textContent = categoryValue === "1" ? "개발" : "데이터";
//                    categorySelect.appendChild(opt);
//                });
//
//                categorySelect.value = ""; // 강제 초기화
//            }
//
//            // ✅ 전문분야 기본 옵션 설정
//            function setDefaultItemOption() {
//                itemSelect.innerHTML = ""; // 기존 옵션 초기화
//
//                const defaultOption = document.createElement("option");
//                defaultOption.value = "";
//                defaultOption.textContent = "전문분야를 선택해주세요.";
//                defaultOption.selected = true;
//                defaultOption.hidden = true;
//
//                itemSelect.appendChild(defaultOption);
//                itemSelect.value = ""; // 강제 초기화
//            }
//
//            // ✅ 카테고리 선택 시 해당하는 전문분야 표시
//            function updateItemOptions(selectedCategory, selectedItem = null) {
//                setDefaultItemOption(); // 기본 옵션 추가
//
//                if (selectedCategory && categoryOptions[selectedCategory]) {
//                    categoryOptions[selectedCategory].forEach(option => {
//                        const opt = document.createElement("option");
//                        opt.value = option.value;
//                        opt.textContent = option.text;
//                        if (selectedItem && option.value === selectedItem) {
//                            opt.selected = true;  // 저장된 값이 있으면 자동 선택
//                        }
//                        itemSelect.appendChild(opt);
//                    });
//                }
//            }
//
//            // ✅ 리로드 시 저장된 카테고리 및 전문분야 복원
//            const savedCategory = sessionStorage.getItem("selectedCategory");
//            const savedItem = sessionStorage.getItem("selectedItem");
//
//            if (savedCategory) {
//                categorySelect.value = savedCategory;
//                updateItemOptions(savedCategory, savedItem);
//            } else {
//                setDefaultCategoryOption();
//                setDefaultItemOption();
//            }
//
//            // ✅ 카테고리 변경 시 저장 & 전문분야 초기화
//            categorySelect.addEventListener("change", function () {
//                sessionStorage.setItem("selectedCategory", categorySelect.value);
//                sessionStorage.removeItem("selectedItem"); // 새 카테고리를 선택하면 기존 전문분야 초기화
//                updateItemOptions(categorySelect.value);
//            });
//
//            // ✅ 전문분야 선택 시 값 저장
//            itemSelect.addEventListener("change", function () {
//                sessionStorage.setItem("selectedItem", itemSelect.value);
//            });
//
////            // ✅ 강제적으로 "카테고리를 선택해주세요."가 기본 선택되도록 설정
////            if (!savedCategory) {
////                categorySelect.value = "";
////            }
////            if (!savedItem) {
////                itemSelect.value = "";
////            }
////
////           window.addEventListener("beforeunload", function () {
////                sessionStorage.removeItem("selectedCategory");
////                sessionStorage.removeItem("selectedItem");
////            });

    // ✅ 경력 추가 버튼 이벤트
    const addCareerBtn = document.getElementById('addCareerBtn');
    const careerContainer = document.getElementById('careerContainer');
    if (addCareerBtn) {
        addCareerBtn.addEventListener('click', function () {
            const careerCount = careerContainer.children.length + 1;
            const newCareerInput = document.createElement('div');
            newCareerInput.className = 'mb-3';
            newCareerInput.setAttribute('id', `careerDiv${careerCount}`);
            newCareerInput.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <label class="form-label">경력 ${careerCount}</label>
                    <button type="button" class="btn btn-danger btn-sm remove-btn" data-target="careerDiv${careerCount}">삭제</button>
                </div>
                <input type="text" class="form-control" name="career" required>
            `;
            careerContainer.appendChild(newCareerInput);
        });
    }

    // ✅ 수상 경력 추가 버튼 이벤트
    const addAwardCareerBtn = document.getElementById('addAwardCareerBtn');
    const awardsContainer = document.getElementById('awardsContainer');
    if (addAwardCareerBtn) {
        addAwardCareerBtn.addEventListener('click', function () {
            const awardsCount = awardsContainer.children.length + 1;
            const newAwardInput = document.createElement('div');
            newAwardInput.className = 'mb-3';
            newAwardInput.setAttribute('id', `awardDiv${awardsCount}`);
            newAwardInput.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <label class="form-label">수상경력 ${awardsCount}</label>
                    <button type="button" class="btn btn-danger btn-sm remove-btn" data-target="awardDiv${awardsCount}">삭제</button>
                </div>
                <input type="text" class="form-control" name="awardCareer" required>
            `;
            awardsContainer.appendChild(newAwardInput);
        });
    }

    // ✅ 포트폴리오 파일 추가 버튼 이벤트 위임
    document.addEventListener('click', function (event) {
           const portfolioFileContainer = document.getElementById('portfolioFileContainer');
           if (!portfolioFileContainer) return;

           if (event.target && event.target.id === 'addFileButtonBtn') {
               let fileInputs = portfolioFileContainer.querySelectorAll('input[type="file"]');
               let fileCount = fileInputs.length + 1;

               if (fileCount > 10) {
                   alert('최대 10개의 파일만 추가할 수 있습니다.');
                   return;
               }

               const newFileInput = document.createElement('div');
               newFileInput.className = 'mb-3 file-input';
               newFileInput.setAttribute('id', `portfolioFile${fileCount}Div`);
               newFileInput.innerHTML = `
                   <label for="portfolioFile${fileCount}" class="form-label">포트폴리오 이미지 ${fileCount}</label>
                   <input type="file" class="form-control portfolioFiles" id="portfolioFile${fileCount}" name="portfolioFiles" accept="image/*" />
                   <button type="button" class="btn btn-danger btn-sm mt-2 remove-btn" data-target="portfolioFile${fileCount}Div">삭제</button>
               `;
               portfolioFileContainer.appendChild(newFileInput);
           }
       });

       // ✅ 파일 삭제 버튼 이벤트 (이벤트 위임)
       document.addEventListener('click', function (event) {
           if (event.target.classList.contains('remove-btn')) {
               const targetId = event.target.getAttribute('data-target');
               const targetElement = document.getElementById(targetId);
               if (targetElement) {
                   targetElement.remove();
                   console.log(`🗑️ 파일 삭제됨: ${targetId}`);
               }
           }
       });

   if (submitBtn) {
          console.log("✅ submitPortfolioBtn 이벤트 리스너 등록됨");

          submitBtn.addEventListener('click', async function () {
              console.log("✅ '추가 완료' 버튼 클릭됨");

              try {
                  // ✅ 필수 값 체크
                  const surveyAnswers = Array.from(document.querySelectorAll('[name^="answer_"]:checked'))
                      .map(input => input.value);
                  if (surveyAnswers.length === 0) {
                      alert('모든 설문 질문에 답변해주세요.');
                      return;
                  }

                  const portfolioTitle = document.querySelector('[name="portfolioTitle"]').value.trim();
                  const portfolioContent = document.querySelector('[name="portfolioContent"]').value.trim();
                  if (!portfolioTitle || !portfolioContent) {
                      alert('포트폴리오 제목과 내용을 입력해주세요.');
                      return;
                  }

                  const memberNo = document.querySelector('[name="memberNo"]').value;
                  const categoryNo = categorySelect ? categorySelect.value.trim() : "";
                  const itemNo = itemSelect ? itemSelect.value.trim() : "";

                  const selfIntroduction = document.getElementById('selfIntroduction').value.trim();
                  const contactableTimeStart = document.getElementById('contactableTimeStart').value.trim();
                  const contactableTimeEnd = document.getElementById('contactableTimeEnd').value.trim();
                  const career = Array.from(document.querySelectorAll('[name="career"]')).map(input => input.value.trim());
                  const awardCareer = Array.from(document.querySelectorAll('[name="awardCareer"]')).map(input => input.value.trim());

                  if (!memberNo || !categoryNo || !itemNo || !selfIntroduction) {
                      alert('모든 필수 정보를 입력해주세요.');
                      return;
                  }

                  // ✅ 파일 업로드 준비 (썸네일 + 포트폴리오 파일들)
                  const thumbnailImage = document.querySelector('[name="thumbnailImage"]').files[0];
                  const portfolioFiles = Array.from(document.querySelectorAll('[name="portfolioFiles"]'))
                      .flatMap(input => Array.from(input.files)); // ✅ 여러 input 태그에 있는 파일들 합치기

                  if (!thumbnailImage) {
                      alert('썸네일 이미지를 업로드해주세요.');
                      return;
                  }

                  console.log("✅ 업로드할 파일 개수:", portfolioFiles.length);

                  // ✅ 파일 업로드 실행
                  const uploadedFiles = await uploadFiles(thumbnailImage, portfolioFiles);

                  if (!uploadedFiles || !uploadedFiles.thumbnailImage || uploadedFiles.portfolioFiles.length === 0) {
                      alert('파일 업로드 실패');
                      return;
                  }

                  // ✅ 최종 데이터 JSON 생성
                  const requestData = {
                      memberNo,
                      categoryNo,
                      itemNo,
                      selfIntroduction,
                      contactableTimeStart,
                      contactableTimeEnd,
                      career,
                      awardCareer,
                      surveyAnswers,
                      portfolioTitle,
                      portfolioContent,
                      thumbnailImage: uploadedFiles.thumbnailImage,
                      portfolioFilePaths: uploadedFiles.portfolioFiles
                  };

                  console.log("✅ 최종 전송 데이터:", requestData);

                  // ✅ 서버에 데이터 전송
                  const response = await fetch(`/proConversion/save`, {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(requestData),
                  });

                 const result = await response.json();

                                 // ✅ 중복된 itemNo가 있는 경우
                                 if (response.status === 409) {
                                     alert(result.message); // "이미 같은 전문 분야를 선택한 전문가가 존재합니다."
                                     return;
                                 }

                                 if (!response.ok) {
                                     throw new Error(result.message || 'DB 저장 실패');
                                 }

                                 alert('포트폴리오 추가 완료!');
                                 window.location.href = "/portfolioList";
                             } catch (error) {
                                 console.error('오류 발생:', error);
                                 alert('저장 중 오류가 발생했습니다.');
                             }
                         });
                     } else {
                         console.error("❌ submitPortfolioBtn 요소를 찾을 수 없습니다.");
                     }

      // ✅ 파일 업로드 함수 (썸네일 + 포트폴리오 파일)
      async function uploadFiles(thumbnail, portfolioFiles) {
          const formData = new FormData();
          formData.append("thumbnailImage", thumbnail);

          portfolioFiles.forEach((file, index) => {
              if (file) {
                  formData.append(`portfolioFiles`, file);
              }
          });

          try {
              const response = await fetch("/portfolio/upload", {
                  method: "POST",
                  body: formData
              });

              const result = await response.json();
              if (response.ok) {
                  console.log("✅ 업로드 결과:", result);
                  return {
                      thumbnailImage: result.thumbnailImage,
                      portfolioFiles: result.portfolioFiles || []
                  };
              } else {
                  throw new Error(result.error || "파일 업로드 실패");
              }
          } catch (error) {
              console.error("파일 업로드 중 오류 발생:", error);
              return null;
          }
      }
  });