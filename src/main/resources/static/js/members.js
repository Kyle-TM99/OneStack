// 이헌복 - 회원리스트 및 관리 관련
// 체크박스 초기화 함수. 전체 선택/개별 선택 상태를 동기화하고 삭제 버튼 표시 여부를 갱신.
function initializeCheckboxes() {
	// querySelector 메서드를 사용하여 deleteButton, headerCheckbox, rowCheckboxes를 찾음
    const deleteButton = document.querySelector('#deleteButton');
    const headerCheckbox = document.querySelector('thead input[type="checkbox"]');
    const rowCheckboxes = document.querySelectorAll('tbody input[type="checkbox"]');

    if (!deleteButton || !headerCheckbox || rowCheckboxes.length === 0) {
        console.log('Required elements not found');
        return;
    }
	// addEventListener 메서드를 사용하여 headerCheckbox의 change 이벤트에 대한 리스너를 추가
	// change 이벤트가 발생하면 rowCheckboxes의 checked 속성을 headerCheckbox의 checked 속성으로 설정
    headerCheckbox.addEventListener('change', function() {
        rowCheckboxes.forEach(checkbox => {
            checkbox.checked = headerCheckbox.checked;
        });
        updateDeleteButton();
    });
	//array.from 메서드를 사용하여 rowCheckboxes의 배열을 생성하고, 배열의 모든 요소가 checked 속성을 가지고 있는지 확인
    rowCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const allChecked = Array.from(rowCheckboxes).every(cb => cb.checked);
            headerCheckbox.checked = allChecked;
            updateDeleteButton();
        });
    });
	// updateDeleteButton 함수를 호출하여 deleteButton의 표시 여부를 갱신
	//isAnyChecked 함수를 사용하여 rowCheckboxes 배열 중 하나라도 checked 속성을 가지고 있는지 확인
    function updateDeleteButton() {
        const isAnyChecked = Array.from(rowCheckboxes).some(checkbox => checkbox.checked);
        deleteButton.style.display = isAnyChecked ? 'block' : 'none';
    }

    updateDeleteButton();
}
//domcontentloaded 이벤트가 발생하면 initializeCheck
//initializeCheckboxes 함수를 호출하여 체크박스를 초기화
document.addEventListener('DOMContentLoaded', function() {
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.addedNodes.length) {
                initializeCheckboxes();
            }
        });
    });

    const dashboardContent = document.getElementById('dashboard-content');
    if (dashboardContent) {
        observer.observe(dashboardContent, { childList: true, subtree: true });
    }
}); 

// 회원 관리 초기화 함수. 회원 수정 버튼에 클릭 이벤트를 추가하고 모달을 통해 회원 정보를 수정 가능하게 설정.
function initializeMemberManagement() {
	//querySelectorAll 메서드를 사용하여 member-edit-btn 클래스를 가진 모든 요소를 찾음
    document.querySelectorAll('.member-edit-btn').forEach(button => {
        button.addEventListener('click', function() {
            const row = this.closest('tr');
            const memberData = {
                name: row.cells[1].textContent,
                id: row.cells[2].textContent,
                type: row.cells[3].textContent,
                email: row.cells[4].textContent,
                status: row.cells[5].textContent,
                joinDate: row.cells[6].textContent
            };
            openMemberModal(memberData);
        });
    });

    document.getElementById('saveChanges')?.addEventListener('click', function() {
        const updatedData = {
            id: document.getElementById('memberId').value,
            type: document.getElementById('memberType').value,
            status: document.getElementById('memberStatus').value,
            note: document.getElementById('memberNote').value
        };
        console.log('Updated member data:', updatedData);

        const modal = bootstrap.Modal.getInstance(document.getElementById('memberModal'));
        modal.hide();
    });
}

// 회원 정보를 표시하는 모달 열기 함수. 선택한 회원 데이터를 모달에 표시.
// getElementById 메서드를 사용하여 모달의 각 요소를 찾고, memberData 객체의 속성을 사용하여 각 요소의 value 속성을 설정
function openMemberModal(memberData) {
    console.log('전체 데이터:', memberData);

    document.getElementById('memberName').value = memberData.name;
    document.getElementById('memberId').value = memberData.id;
    document.getElementById('memberEmail').value = memberData.email;
	//parseInt 함수를 사용하여 memberData.type 속성을 정수로 변환하고, 변환된 값에 따라 memberTypeValue 변수에 'pro' 또는 'beginner'를 설정
    const memberTypeValue = parseInt(memberData.type) === 1 ? 'pro' : 'beginner';
    document.getElementById('memberType').value = memberTypeValue;
    document.getElementById('memberStatus').value = memberData.status === '활성화' ? 'active' : 'inactive';
    document.getElementById('joinDate').value = memberData.joinDate;

    const modal = new bootstrap.Modal(document.getElementById('memberModal'));
    modal.show();
}

document.addEventListener('DOMContentLoaded', function() {
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.addedNodes.length) {
                initializeCheckboxes();
                initializeMemberManagement();
            }
        });
    });

    const dashboardContent = document.getElementById('dashboard-content');
    if (dashboardContent) {
        observer.observe(dashboardContent, { childList: true, subtree: true });
    }
});

let screeningModal = null;

// 심사 관리 초기화 함수. 심사 버튼과 저장 버튼 이벤트를 설정하고 모달을 초기화.
function initializeScreeningManagement() {
    const modalElement = document.getElementById('screeningModal');
    if (modalElement) {
        screeningModal = new bootstrap.Modal(modalElement);
    }
	//screeningButtons 배열을 생성하고, 모든 screening-btn 클래스를 가진 요소를 찾아 배열에 추가
    const screeningButtons = document.querySelectorAll('.screening-btn');
    console.log('Found screening buttons:', screeningButtons.length);
	//btn.addEventListener 메서드를 사용하여 각 버튼에 대한 클릭 이벤트 리스너를 추가
    screeningButtons.forEach(btn => {
        btn.addEventListener('click', handleScreeningClick);
    });

    const saveButton = document.getElementById('saveScreening');
    if (saveButton) {
        saveButton.addEventListener('click', handleSaveScreening);
    }
}

// 심사 버튼 클릭 핸들러. 버튼 데이터를 읽어와 모달에 표시.
// handleScreeningClick 함수를 사용하여 버튼의 data-* 속성을 읽고, 모달의 각 요소에 값을 설정
function handleScreeningClick(e) {
    e.preventDefault();
    console.log('Screening button clicked');

    const proNo = this.getAttribute('data-pro-no');
    const name = this.getAttribute('data-name');
    const itemTitle = this.getAttribute('data-item-title');
    const portfolioTitle = this.getAttribute('data-portfolio-title');

    console.log('Button data:', { proNo, name, itemTitle, portfolioTitle });

    const modalElement = document.getElementById('screeningModal');
    if (!modalElement) {
        console.error('Modal element not found');
        return;
    }

    modalElement.querySelector('#proNo').value = proNo;
    modalElement.querySelector('#memberName').textContent = name;
    modalElement.querySelector('#itemTitle').textContent = itemTitle;
    modalElement.querySelector('#portfolioTitle').textContent = portfolioTitle;
	//input[name="professorStatus"] 선택자를 사용하여 모든 input 요소를 찾고, checked 속성을 false로 설정
    modalElement.querySelectorAll('input[name="professorStatus"]').forEach(radio => {
        radio.checked = false;
    });

    modalElement.querySelector('#screeningMsg').value = '';

    if (screeningModal) {
        screeningModal.show();
    } else {
        console.error('Modal instance not found');
    }
}

// 심사 결과 저장 함수. 서버에 데이터를 전송하고, 성공 시 모달을 닫고 페이지를 새로 고침.
function handleSaveScreening() {
    const saveButton = document.getElementById('saveScreening');
    saveButton.disabled = true;
	//input[name="professorStatus"] 선택자를 사용하여 체크된 요소를 찾고, 체크된 요소의 value 속성을 status 변수에 설정
    const proNo = document.getElementById('proNo').value;
    const status = document.querySelector('input[name="professorStatus"]:checked')?.value;
    const message = document.getElementById('screeningMsg').value;
	//saveButton.disabled 속성을 true로 설정하여 버튼을 비활성화하고, fetch 함수를 사용하여 서버에 데이터를 전송
    if (!status) {
        alert('심사 결과를 선택해주세요.');
        saveButton.disabled = false;
        return;
    }
	//content-type 헤더를 application/json으로 설정하고, JSON.stringify 함수를 사용하여 데이터를 JSON 문자
    fetch('/reviewPro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            pro: proNo,
            professorStatus: status,
            screeningMsg: message || ''
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('서버 응답 오류');
        return response.text();
    })
    .then(() => {
        screeningModal.hide();
        alert('심사가 완료되었습니다.');
        window.location.reload();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('처리 중 오류가 발생했습니다.');
    })
    .finally(() => {
        saveButton.disabled = false;
    });
}
//domcontentloaded 이벤트가 발생하면 initializeScreeningManagement 함수를 호출하여 심사 관리를 초기화
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, initializing screening management');
    initializeScreeningManagement();
});
//
const observer = new MutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
        if (mutation.addedNodes.length) {
            console.log('DOM changed, reinitializing screening management');
            initializeScreeningManagement();
        }
    });
});

const dashboardContent = document.getElementById('dashboard-content');
if (dashboardContent) {
    observer.observe(dashboardContent, { childList: true, subtree: true });
}
