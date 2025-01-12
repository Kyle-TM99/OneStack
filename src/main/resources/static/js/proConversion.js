document.addEventListener('DOMContentLoaded', function () {
    // 경력 추가 버튼 동작
    const addCarrerBtn = document.getElementById('addCarrerBtn');
    const carrerContainer = document.getElementById('carrerContainer');
    if (addCarrerBtn) {
        addCarrerBtn.addEventListener('click', function () {
            const carrerCount = carrerContainer.children.length + 1; // 현재 입력 필드 수
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
            const awardsCount = awardsContainer.children.length + 1; // 현재 입력 필드 수
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
                targetElement.remove(); // 해당 요소 삭제
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
            // 레이아웃 변경: applicationForm을 왼쪽으로 이동, surveyContainer를 표시
            surveyContainer.style.display = 'block';
            applicationFormContainer.classList.add('justify-content-between');
            applicationForm.classList.replace('col-md-6', 'col-md-4');
            surveyContainer.classList.replace('col-md-6', 'col-md-8');

            // 설문조사 로드
            const itemNoElement = document.getElementById('itemNo');
            const selectedItemValue = itemNoElement.value;

            if (!selectedItemValue) {
                console.error('전문분야 선택 값이 비어있습니다.');
                alert('전문분야를 선택해주세요.');
                return;
            }
            console.log('Selected itemNo:', selectedItemValue);

            fetch('/survey?itemNo=' + selectedItemValue)
                .then(response => {
                    if (!response.ok) throw new Error('Failed to load survey');
                    return response.text();
                })
                .then(html => {
                    surveyContainer.innerHTML = html; // 설문조사 내용 삽입
                })
                .catch(error => {
                    console.error(error);
                    surveyContainer.innerHTML = '<p class="text-danger">설문조사를 불러오지 못했습니다. 다시 시도해주세요.</p>';
                });
        });
    }

    // 포트폴리오 제출 버튼 동작
    const submitPortfolioBtn = document.getElementById('submitPortfolioBtn'); // 포트폴리오 제출 버튼
    if (submitPortfolioBtn) {
        submitPortfolioBtn.addEventListener('click', function () {
            // 입력된 데이터를 가져오기
            const portfolioTitle = document.getElementById('portfolioTitle').value;
            const portfolioContent = document.getElementById('portfolioContent').value;
            const thumbnailImage = document.getElementById('thumbnailImage').files[0];
            const portfolioFile1 = document.getElementById('portfolioFile1').files[0];

            // 데이터 확인
            console.log('포트폴리오 제목:', portfolioTitle);
            console.log('포트폴리오 내용:', portfolioContent);
            console.log('썸네일 이미지:', thumbnailImage);
            console.log('파일 1:', portfolioFile1);

            // 데이터를 FormData로 준비
            const formData = new FormData();
            formData.append('portfolioTitle', portfolioTitle);
            formData.append('portfolioContent', portfolioContent);
            formData.append('thumbnailImage', thumbnailImage);
            formData.append('portfolioFile1', portfolioFile1);

            // proConversion.html로 데이터 전송
            fetch('/proConversion/receivePortfolio', {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // proConversion.html의 폼에 데이터 추가
                        const form = document.getElementById('applicationForm');
                        form.insertAdjacentHTML('beforeend', `
                            <input type="hidden" name="portfolioTitle" value="${portfolioTitle}">
                            <input type="hidden" name="portfolioContent" value="${portfolioContent}">
                            <input type="hidden" name="thumbnailImage" value="${data.thumbnailPath}">
                            <input type="hidden" name="portfolioFile1" value="${data.filePath}">
                        `);

                        alert('포트폴리오 데이터가 성공적으로 추가되었습니다!');
                    } else {
                        alert('포트폴리오 데이터를 추가하는 데 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('오류가 발생했습니다.');
                });
        });
    }
});
