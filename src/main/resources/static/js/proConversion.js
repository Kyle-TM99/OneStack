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
                <input type="text" class="form-control" id="carrer${carrerCount}" name="carrer${carrerCount}" required>
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
                <input type="text" class="form-control" id="awardCarrer${awardsCount}" name="awardCarrer${awardsCount}">
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

    // 포트폴리오 추가 및 설문조사 로드
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

    // 설문조사 및 포트폴리오 제출
    document.addEventListener('click', function (event) {
        if (event.target && event.target.id === 'submitPortfolioBtn') {
            const portfolioTitle = document.getElementById('portfolioTitle')?.value.trim();
            const portfolioContent = document.getElementById('portfolioContent')?.value.trim();
            const thumbnailImageInput = document.getElementById('thumbnailImage');
            const portfolioFileInputs = Array.from(document.querySelectorAll('[id^="portfolioFile"]'));

            if (!portfolioTitle) {
                alert('포트폴리오 제목을 입력해주세요.');
                return;
            }

            if (!portfolioContent) {
                alert('포트폴리오 내용을 입력해주세요.');
                return;
            }

            if (!thumbnailImageInput?.files.length) {
                alert('썸네일 이미지를 추가해주세요.');
                return;
            }

            const thumbnailImage = thumbnailImageInput.files[0].name;
            const portfolioFiles = portfolioFileInputs
                .filter(fileInput => fileInput.files.length > 0)
                .map(fileInput => fileInput.files[0].name);

            if (!portfolioFiles.length) {
                alert('포트폴리오 파일을 추가해주세요.');
                return;
            }

            const surveyAnswers = Array.from(document.querySelectorAll('[name^="answer_"]:checked')).map(input => ({
                surveyNo: input.name.replace('answer_', ''),
                answer: input.value,
            }));

            const dataToSend = {
                portfolioTitle,
                portfolioContent,
                thumbnailImage,
                portfolioFiles,
                surveyAnswers,
            };

            console.log('전송 데이터:', dataToSend);
            window.parent.postMessage(dataToSend, '*');
        }
    });
	document.getElementById('addFileButton').addEventListener('click', function () {
	    const portfolioFilesContainer = document.getElementById('portfolioFiles');
	    const fileCount = portfolioFilesContainer.querySelectorAll('input[type="file"]').length;

	    if (fileCount >= 10) {
	        alert('최대 10개의 파일만 추가할 수 있습니다.');
	        return;
	    }

	    const newFileInput = document.createElement('input');
	    newFileInput.type = 'file';
	    newFileInput.className = 'form-control mt-2';
	    newFileInput.name = `portfolioFile${fileCount + 1}`;
	    newFileInput.accept = '*';

	    portfolioFilesContainer.appendChild(newFileInput);
	});


    // proConversion에서 데이터 수신 및 반영
    window.addEventListener('message', function (event) {
        console.log('받은 데이터:', event.data);

        const { portfolioTitle, portfolioContent, surveyAnswers, thumbnailImage, portfolioFiles } = event.data;

        surveyAnswers.forEach((answer, index) => {
            const answerField = document.querySelector(`[name="proAnswer${index + 1}"]`);
            if (answerField) {
                answerField.value = answer.answer;
            }
        });

        document.querySelector('[name="portfolioTitle"]').value = portfolioTitle;
        document.querySelector('[name="portfolioContent"]').value = portfolioContent;

        document.getElementById('thumbnailImage').value = thumbnailImage;

        const portfolioFilesDisplay = document.getElementById('portfolioFilesDisplay');
        if (portfolioFilesDisplay) {
            portfolioFilesDisplay.innerHTML = portfolioFiles.map(file => `<li>${file}</li>`).join('');
        }

        surveyContainer.style.display = 'none';
        applicationFormContainer.classList.remove('justify-content-between');
        applicationForm.classList.replace('col-md-4', 'col-md-6');
    });
});
