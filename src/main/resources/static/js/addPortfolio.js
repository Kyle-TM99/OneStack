    document.addEventListener('DOMContentLoaded', function () {
        const categorySelect = document.getElementById('categoryNo');
        const itemSelect = document.getElementById('itemNo');

        const options = {
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

        categorySelect.addEventListener('change', function () {
            const selectedCategory = this.value;
            // 기존 옵션 초기화
            itemSelect.innerHTML = '<option value="" hidden>전문분야를 선택해주세요.</option>';

            if (options[selectedCategory]) {
                // 해당 카테고리의 옵션 추가
                options[selectedCategory].forEach(option => {
                    const opt = document.createElement('option');
                    opt.value = option.value;
                    opt.textContent = option.text;
                    itemSelect.appendChild(opt);
                });
            }
        });
    });

    // 경력 추가 버튼 동작
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
                    <label for="career${careerCount}" class="form-label">경력 ${careerCount}</label>
                    <button type="button" class="btn btn-danger btn-sm remove-btn" data-target="careerDiv${careerCount}">삭제</button>
                </div>
                <input type="text" class="form-control" id="career${careerCount}" name="career" required>
            `;
            careerContainer.appendChild(newCareerInput);
        });
    }

    // 수상 경력 추가 버튼 동작
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
                    <label for="awardCareer${awardsCount}" class="form-label">수상경력 ${awardsCount}</label>
                    <button type="button" class="btn btn-danger btn-sm remove-btn" data-target="awardDiv${awardsCount}">삭제</button>
                </div>
                <input type="text" class="form-control" id="awardCareer${awardsCount}" name="awardCareer" required>
            `;
            awardsContainer.appendChild(newAwardInput);
        });
    }

	document.addEventListener('click', function (event) {
	    // 파일 추가 버튼 처리
	    if (event.target && event.target.id === 'addFileButtonBtn') {
	        const portfolioFileContainer = document.getElementById('portfolioFiles');
	        let fileCount = portfolioFileContainer.children.length + 0;

			console.log("버튼 클릭");
	        if (fileCount >= 11) {
	            alert('최대 10개의 파일만 추가할 수 있습니다.');
	            return;
	        }

	        const newFileInput = document.createElement('div');
	        newFileInput.className = 'mb-3';
	        newFileInput.setAttribute('id', `portfolioFile${fileCount}Div`);
	        newFileInput.innerHTML = `
	            <label for="portfolioFile${fileCount}" class="form-label">포트폴리오 이미지 ${fileCount}</label>
	            <input type="file" class="form-control" id="portfolioFile${fileCount}" name="portfolioFile${fileCount}" accept="*" />
	            <button type="button" class="btn btn-danger btn-sm mt-2 remove-btn" data-target="portfolioFile${fileCount}Div">삭제</button>
	        `;
	        portfolioFileContainer.appendChild(newFileInput);
	    }
});

    // 동적으로 생성된 삭제 버튼 이벤트 처리
    document.addEventListener('click', function (event) {
        if (event.target.classList.contains('remove-btn')) {
            const targetId = event.target.getAttribute('data-target');
            const targetElement = document.getElementById(targetId);
            if (targetElement) {
                targetElement.remove();
            }
        }
    });

    // 설문조사 및 포트폴리오 추가하기
    const addPortfolioBtn = document.getElementById('addPortfolioBtn');
    const surveyContainer = document.getElementById('surveyContainer');
    const applicationFormContainer = document.getElementById('applicationFormContainer');
    const applicationForm = document.getElementById('applicationForm');

    if (addPortfolioBtn) {
        addPortfolioBtn.addEventListener('click', function () {
            surveyContainer.style.display = 'block';
            applicationFormContainer.classList.add('justify-content-between');
            applicationForm.classList.replace('col-md-6', 'col-md-4');
            surveyContainer.classList.replace('col-md-6', 'col-md-8');

			const itemNoElement = document.getElementById('itemNo');
			if (!itemNoElement) {
			     alert('전문 분야 선택 요소를 찾을 수 없습니다.');
			     return;
			 }
			 const selectedItemValue = itemNoElement.value;
			  if (!selectedItemValue) {
			      alert('전문 분야를 선택해주세요.');
			      return;
			  }

			 // 설문조사 데이터를 서버에서 가져오기
			    fetch(`/survey?itemNo=${selectedItemValue}`)
			        .then(response => {
			            if (!response.ok) {
			                throw new Error('설문조사 로드 실패');
			            }
			            return response.text(); // HTML 반환
			        })
			        .then(html => {
			            // surveyContainer에 설문조사 HTML 삽입
			            const surveyContainer = document.getElementById('surveyContainer');
			            if (surveyContainer) {
			                surveyContainer.innerHTML = html;
			            }

			            // 설문조사 제출 버튼에 이벤트 리스너 추가
			            addSubmitPortfolioBtnListener();
			        })
			        .catch(error => {
			            console.error('설문조사 데이터 로드 실패:', error);

			            // 오류 메시지 표시
			            const surveyContainer = document.getElementById('surveyContainer');
			            if (surveyContainer) {
			                surveyContainer.innerHTML = '<p class="text-danger">설문조사를 불러오지 못했습니다. 다시 시도해주세요.</p>';
			            }
			        });
			});
    }

	// 포트폴리오 제출 버튼 동작 추가 함수
	function addSubmitPortfolioBtnListener() {
	    const submitPortfolioBtn = document.getElementById('submitPortfolioBtn');

	    if (submitPortfolioBtn) {
	        submitPortfolioBtn.addEventListener('click', async function () {
	            try {
	                // 설문조사 답변 수집
	                const surveyAnswers = Array.from(document.querySelectorAll('[name^="answer_"]:checked')).map(input => input.value);

	                if (surveyAnswers.length === 0) {
	                    alert('모든 설문 질문에 답변해주세요.');
	                    return;
	                }

	                // 포트폴리오 데이터 수집
	                const portfolioTitle = document.getElementById('portfolioTitle')?.value.trim();
	                const portfolioContent = document.getElementById('portfolioContent')?.value.trim();

	                if (!portfolioTitle || !portfolioContent) {
	                    alert('포트폴리오 제목과 내용을 입력해주세요.');
	                    return;
	                }

	                // 파일 업로드 준비
	                const thumbnailImage = document.querySelector('[name="thumbnailImage"]').files[0];
	                const portfolioFiles = Array.from(document.querySelectorAll('[name^="portfolioFile"]')).map(fileInput => fileInput.files[0]);
					const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
	                const formData = new FormData();

					// 썸네일 이미지 크기 확인
	               if (thumbnailImage) {
	                   if (thumbnailImage.size > MAX_FILE_SIZE) {
	                       alert(`${thumbnailImage.name} 파일 크기가 너무 큽니다. (최대 10MB)`);
	                       return; // 중단
	                   }
	                   formData.append('thumbnailImage', thumbnailImage);
	               } else {
	                   alert('썸네일 이미지를 업로드해주세요.');
	                   return; // 중단
	               }

	               // 포트폴리오 파일 크기 확인
	               for (const file of portfolioFiles) {
	                   if (file) {
	                       if (file.size > MAX_FILE_SIZE) {
	                           alert(`${file.name} 파일 크기가 너무 큽니다. (최대 10MB)`);
	                           return; // 중단
	                       }
	                       formData.append('portfolioFiles', file);
	                   }
	               }

				   // 업로드 요청
                   const uploadResponse = await fetch('/portfolio/upload', { method: 'POST', body: formData });

                   if (!uploadResponse.ok) {
                       throw new Error('파일 업로드 실패');
                   }

                   const filePaths = await uploadResponse.json();
                   console.log('서버 응답:', filePaths);

                   // 최종 데이터 준비
                   const data = {
                       surveyAnswers,
                       portfolioTitle,
                       portfolioContent,
                       thumbnailImagePath: filePaths.thumbnailImage,
                       portfolioFilePaths: filePaths.portfolioFiles || [],
                   };

                   // 데이터 전달
                   window.parent.postMessage(data, '*');

                   // UI 상태 갱신
                   surveyContainer.style.display = 'none';
                   applicationFormContainer.classList.remove('justify-content-between');
                   applicationForm.classList.replace('col-md-4', 'col-md-6');

               } catch (error) {
                   console.error('저장 실패', error);
                   alert('저장 중 오류가 발생했습니다. 다시 시도해주세요.');
               }
           });
       } else {
           console.error('submitPortfolioBtn 요소가 존재하지 않습니다.');
       }
   }


    // 데이터 수신 처리
    window.addEventListener('message', function (event) {
		console.log('받은 데이터:', event.data);
        const { portfolioTitle, portfolioContent, surveyAnswers, thumbnailImagePath, portfolioFilePaths } = event.data;

		// 포트폴리오 제목 및 내용 반영
		const portfolioTitleField = document.querySelector('[name="portfolioTitle"]');
		const portfolioContentField = document.querySelector('[name="portfolioContent"]');
		if (portfolioTitleField) {
		    portfolioTitleField.value = portfolioTitle || '';
		}
		if (portfolioContentField) {
		    portfolioContentField.value = portfolioContent || '';
		}

        // 썸네일 이미지 표시
        const thumbnailImageDisplay = document.getElementById('thumbnailImageDisplay');
        if (thumbnailImageDisplay) {
            thumbnailImageDisplay.innerHTML = thumbnailImagePath
                ? `<li><a href="${thumbnailImagePath}" target="_blank">${thumbnailImagePath.split('/').pop()}</a></li>`
                : '<p>썸네일 이미지 없음</p>';
        }
		// 포트폴리오 파일 반영
		   const portfolioFilesDisplay = document.getElementById('portfolioFilesDisplay');
		   if (portfolioFilesDisplay) {
		       if (portfolioFilePaths && portfolioFilePaths.length > 0) {
		           portfolioFilesDisplay.innerHTML = portfolioFilePaths
		               .map((filePath) => `<li><a href="/uploads/${filePath}" target="_blank">${filePath.split('/').pop()}</a></li>`)
		               .join('');
		       } else {
		           portfolioFilesDisplay.innerHTML = '<p>포트폴리오 파일이 없습니다.</p>';
		       }
		   }
			// 설문조사 답변
			if (surveyAnswers) {
			    surveyAnswers.forEach((answer, index) => {
			        const answerField = document.querySelector(`[name="proAnswer${index + 1}"]`);
			        if (answerField) answerField.value = answer;
			    });
			}

		});
		document.getElementById('submitProConversionBtn').addEventListener('click', async function () {
		    try {
		        const memberNo = document.querySelector('[name="memberNo"]').value;
		        const categoryNo = document.getElementById('categoryNo').value.trim();
		        const itemNo = document.getElementById('itemNo').value.trim();
		        const selfIntroduction = document.getElementById('selfIntroduction').value.trim();
		        const contactableTimeStart = document.getElementById('contactableTimeStart').value.trim();
		        const contactableTimeEnd = document.getElementById('contactableTimeEnd').value.trim();
		        const career = Array.from(document.querySelectorAll('[name="career"]')).map(input => input.value.trim());
		        const awardCareer = Array.from(document.querySelectorAll('[name="awardCareer"]')).map(input => input.value.trim());
		        const proAnswers = Array.from(document.querySelectorAll('[name^="proAnswer"]')).map(input => input.value.trim()).filter(answer => answer);
		        const portfolioTitle = document.querySelector('[name="portfolioTitle"]').value.trim();
		        const portfolioContent = document.querySelector('[name="portfolioContent"]').value.trim();
		        const thumbnailImagePath = document.getElementById('thumbnailImageDisplay').innerText.trim();
		        const portfolioFilePaths = Array.from(document.querySelectorAll('#portfolioFilesDisplay li a')).map(link => link.href);

		        if (!memberNo || !categoryNo || !itemNo || !selfIntroduction || !portfolioTitle || !portfolioContent) {
		            alert('모든 필수 정보를 입력해주세요.');
		            return;
		        }

		        const requestData = {
		            memberNo,
		            categoryNo,
		            itemNo,
		            selfIntroduction,
		            contactableTimeStart,
		            contactableTimeEnd,
		            career,
		            awardCareer,
		            surveyAnswers: proAnswers,
		            portfolioTitle,
		            portfolioContent,
		            thumbnailImage: thumbnailImagePath,
		            portfolioFilePaths,
		        };

		        const response = await fetch('/proConversion/save', {
		            method: 'POST',
		            headers: {
		                'Content-Type': 'application/json',
		            },
		            body: JSON.stringify(requestData),
		        });

		        if (!response.ok) {
		            const errorData = await response.json();
		            console.error('서버 오류:', errorData);
		            throw new Error('DB 저장 실패');
		        }

		        alert('심사 요청이 완료되었습니다.');
				window.location.href = "/main";
		    } catch (error) {
		        console.error('오류 발생:', error);
		        alert('심사 요청 중 오류가 발생했습니다.');
		    }
		});
