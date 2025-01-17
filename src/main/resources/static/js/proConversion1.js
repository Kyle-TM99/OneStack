document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM 로드 완료');

    // 경력 추가 버튼 동작
	const addCarrerBtn = document.getElementById('addCarrerBtn');
	const carrerContainer = document.getElementById('carrerContainer');
	if (addCarrerBtn) {
	    addCarrerBtn.addEventListener('click', function () {
	        const carrerCount = carrerContainer.children.length + 1;
	        const newCarrerInput = document.createElement('div');
	        newCarrerInput.className = 'mb-3';
	        newCarrerInput.setAttribute('id', `carrerDiv${carrerCount}`);
	        newCarrerInput.innerHTML = `
	            <div class="d-flex justify-content-between align-items-center">
	                <label for="carrer${carrerCount}" class="form-label">경력 ${carrerCount}</label>
	                <button type="button" class="btn btn-danger btn-sm remove-btn" data-target="carrerDiv${carrerCount}">삭제</button>
	            </div>
	            <input type="text" class="form-control" id="carrer${carrerCount}" name="carrer" required>
	        `;
	        carrerContainer.appendChild(newCarrerInput);
	    });
	}

    // 수상 경력 추가 버튼 동작
	const addAwardCarrerBtn = document.getElementById('addAwardCarrerBtn');
	const awardsContainer = document.getElementById('awardsContainer');
	if (addAwardCarrerBtn) {
	    addAwardCarrerBtn.addEventListener('click', function () {
	        const awardsCount = awardsContainer.children.length + 1;
	        const newAwardInput = document.createElement('div');
	        newAwardInput.className = 'mb-3';
	        newAwardInput.setAttribute('id', `awardDiv${awardsCount}`);
	        newAwardInput.innerHTML = `
	            <div class="d-flex justify-content-between align-items-center">
	                <label for="awardCarrer${awardsCount}" class="form-label">수상경력 ${awardsCount}</label>
	                <button type="button" class="btn btn-danger btn-sm remove-btn" data-target="awardDiv${awardsCount}">삭제</button>
	            </div>
	            <input type="text" class="form-control" id="awardCarrer${awardsCount}" name="awardCarrer" required>
	        `;
	        awardsContainer.appendChild(newAwardInput);
	    });
	}

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
            const selectedItemValue = itemNoElement.value;

            if (!selectedItemValue) {
                alert('전문분야를 선택해주세요.');
                return;
            }

            fetch(`/survey?itemNo=${selectedItemValue}`)
                .then(response => {
                    if (!response.ok) throw new Error('설문조사 로드 실패');
                    return response.text();
                })
                .then(html => {
                    surveyContainer.innerHTML = html;
                })
                .catch(error => {
                    console.error(error);
                    surveyContainer.innerHTML = '<p class="text-danger">설문조사를 불러오지 못했습니다. 다시 시도해주세요.</p>';
                });
        });
    }

	document.addEventListener('click', function (event) {
	    if (event.target && event.target.id === 'submitPortfolioBtn') {
	        // 포트폴리오 제목과 내용 가져오기
	        const portfolioTitle = document.getElementById('portfolioTitle')?.value.trim();
	        const portfolioContent = document.getElementById('portfolioContent')?.value.trim();
	        const thumbnailImageInput = document.getElementById('thumbnailImage');
	        const portfolioFileInputs = document.querySelectorAll('input[type="file"][id^="portfolioFile"]');
			
			// 경력 필드에서 값 가져오기
			const carrerInputs = Array.from(document.querySelectorAll('[name="carrer"]'));
            const carrerData = carrerInputs.map(input => input.value.trim()).filter(value => value !== "");
            const carrerString = carrerData.join(",");  // 경력을 쉼표로 구분된 문자열로 결합

			// 수상 경력 가져오기
			 const awardCarrerInputs = Array.from(document.querySelectorAll('[name="awardCarrer"]'));
			 const awardCarrerData = awardCarrerInputs.map(input => input.value.trim()).filter(value => value !== "");
			 const awardCarrerString = awardCarrerData.join(",");  // 수상 경력을 쉼표로 구분된 문자열로 결합

			// 설문조사 답변 가져오기
			 const surveyAnswers = Array.from(document.querySelectorAll('[name^="answer_"]:checked')).map(input => ({
			     surveyNo: input.name.replace('answer_', ''),
			     answer: input.value,
			 }));

			 // 설문조사 질문 수 계산
			 const totalSurveyQuestions = new Set(
			     Array.from(document.querySelectorAll('[name^="answer_"]')).map(input => input.name)
			 ).size;

	        // 설문조사 답변 검증
			if (surveyAnswers.length < totalSurveyQuestions) {
			    alert('모든 설문조사 답변을 선택해주세요.');
			    return;
			}

	        // 필수 입력값 검증
	        if (!portfolioTitle) {
	            alert('포트폴리오 제목을 입력해주세요.');
	            return;
	        }

	        if (!portfolioContent) {
	            alert('포트폴리오 내용을 입력해주세요.');
	            return;
	        }

			if (!thumbnailImageInput || !thumbnailImageInput.files || !thumbnailImageInput.files.length) {
			    alert('썸네일 이미지를 선택해주세요.');
			    return;
			}


	        if (portfolioFileInputs.length === 0 || !portfolioFileInputs[0]?.files?.length) {
	            alert('필수 포트폴리오 이미지 1개를 추가해주세요.');
	            return;
	        }

	        // FormData 생성
			const formData = new FormData();
			formData.append('portfolioTitle', portfolioTitle);
			formData.append('portfolioContent', portfolioContent);

			
			if (thumbnailImageInput.files.length > 0) {
			    formData.append('thumbnailImage', thumbnailImageInput.files[0]);
			} else {
			    alert('썸네일 이미지를 반드시 추가해주세요.');
			    return;
			}
			
			portfolioFileInputs.forEach((input, index) => {
			    Array.from(input.files).forEach(file => {
			        formData.append(`portfolioFiles`, file); // 여러 파일을 같은 이름으로 전송
			    });
			});
			
			formData.append('carrer', carrerString);
			formData.append('awardCarrer', awardCarrerString);
	        formData.append('surveyAnswers', JSON.stringify(surveyAnswers));

	        console.log('전송 데이터(FormData):', formData);
			formData.forEach((value, key) => {
			    console.log(`${key}:`, value);
			});

	        // 서버로 데이터 전송
	        fetch('/proConversion/update', {
	            method: 'POST',
	            body: formData,
	        })
	            .then(response => {
	                if (!response.ok) {
	                    throw new Error('서버 응답 오류');
	                }
	                return response.json();
	            })
	            .then(data => {
	                console.log('서버 응답 데이터:', data);
	                if (data.success) {
	                    window.parent.postMessage(data, '*');
	                } else {
	                    alert('서버 저장 중 문제가 발생했습니다.');
	                }
	            })
	            .catch(error => {
	                console.error('데이터 전송 오류:', error);
	                alert('데이터 전송 중 문제가 발생했습니다. 다시 시도해주세요.');
	            });
	    }
	});


	    // 부모 창에서 데이터 수신 및 반영
		window.addEventListener('message', function (event) {
		    console.log('받은 데이터:', event.data);

		    // 데이터 추출
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
		        portfolioFilesDisplay.innerHTML = portfolioFilePaths?.length
		            ? portfolioFilePaths
		                  .map((file) => `<li><a href="${file}" target="_blank">${file.split('/').pop()}</a></li>`)
		                  .join('')
		            : '<p>포트폴리오 파일이 없습니다.</p>';
		    }

		    // 설문조사 답변 반영
		    if (surveyAnswers) {
		        surveyAnswers.forEach((answer, index) => {
		            const answerField = document.querySelector(`[name="proAnswer${index + 1}"]`);
		            if (answerField) {
		                answerField.value = answer.answer || '';
		            }
		        });
		    }

		    console.log('데이터가 성공적으로 반영되었습니다.');

        const surveyContainer = document.getElementById('surveyContainer');
        const applicationFormContainer = document.getElementById('applicationFormContainer');
        const applicationForm = document.getElementById('applicationForm');

        if (surveyContainer) surveyContainer.style.display = 'none';
        if (applicationFormContainer) applicationFormContainer.classList.remove('justify-content-between');
        if (applicationForm) applicationForm.classList.replace('col-md-4', 'col-md-6');
    });
});
