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
		document.getElementById('submitPortfolioBtn').addEventListener('click', function(){
			//설문조사 답변 수집
			const surveyAnswers = Array.from(document.querySelectorAll('[name^="answer_"]:checked')).map(input =>({
				surveyNo: input.name.replace('answer_',''),// 설문 번호 추출
				answer: input.value, //선택된 답변
			}));
			
			// 포트폴리오 데이터 수집
			const portfolioTitle = document.querySeletor('[name="portfolioTitle"]').value.trim();
			const portfolioContent = document.querySeletor('[name="portfolioContent"]').value.trim();
			
			// 파일 경로 데이터 수집
		});
});