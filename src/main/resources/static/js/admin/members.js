// 통합 스크립트 - 회원 관리 및 심사 관리 관련

document.addEventListener('DOMContentLoaded', function () {
    initializeCommonEvents();
    initializeCheckboxes();
    initializeMemberManagement();
    initializeScreeningManagement();
    initializeScreeningModification();

    const observer = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            if (mutation.addedNodes.length) {
                console.log('DOM changed, reinitializing components');
                initializeCheckboxes();
                initializeMemberManagement();
                initializeScreeningManagement();
                initializeScreeningModification();
            }
        });
    });

    const dashboardContent = document.getElementById('dashboard-content');
    if (dashboardContent) {
        observer.observe(dashboardContent, { childList: true, subtree: true });
    }
});

// 공통 닫기 버튼 이벤트 초기화
function initializeCommonEvents() {
    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('close-custom-btn')) {
            const modalId = e.target.getAttribute('data-modal-id');
            const modalElement = document.getElementById(modalId);
            if (modalElement) {
                closeModal(modalElement);
            }
        }
    });
}

// 모달 닫기 함수
function closeModal(modalElement) {
    modalElement.classList.remove('show');
    modalElement.style.display = 'none';
    document.body.classList.remove('modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) backdrop.remove();
}

function initializeCheckboxes() {
    const deleteButton = document.querySelector('#deleteButton');
    const headerCheckbox = document.querySelector('thead input[type="checkbox"]');
    const rowCheckboxes = document.querySelectorAll('tbody input[type="checkbox"]');
    const deleteCount = document.querySelector('#deleteCount');

    if (!deleteButton || !headerCheckbox || rowCheckboxes.length === 0) {
        console.log('Required elements not found');
        return;
    }

    // 헤더 체크박스 동작
    headerCheckbox.addEventListener('change', function () {
        rowCheckboxes.forEach(checkbox => {
            checkbox.checked = headerCheckbox.checked;
        });
        updateDeleteButton();
    });

    // 개별 체크박스 동작
    rowCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            const allChecked = Array.from(rowCheckboxes).every(cb => cb.checked);
            headerCheckbox.checked = allChecked;
            updateDeleteButton();
        });
    });

    // 삭제 버튼 및 선택 항목 수 업데이트
    function updateDeleteButton() {
        const checkedCheckboxes = Array.from(rowCheckboxes).filter(checkbox => checkbox.checked);
        const isAnyChecked = checkedCheckboxes.length > 0;

        deleteButton.style.display = isAnyChecked ? 'block' : 'none';
        if (deleteCount) {
            deleteCount.textContent = isAnyChecked ? `${checkedCheckboxes.length}개 선택됨` : '';
        }
    }

    // 초기 버튼 상태 업데이트
    updateDeleteButton();
}


// 회원 관리 초기화 함수
function initializeMemberManagement() {
    document.querySelectorAll('.member-edit-btn').forEach(button => {
        button.addEventListener('click', function () {
            const row = this.closest('tr');
            const memberNo = row.getAttribute('data-member-no');
            const memberData = {
                name: row.cells[1].textContent,
                id: row.cells[2].textContent,
                type: row.cells[3].textContent,
                email: row.cells[4].textContent,
                status: row.cells[5].textContent,
                joinDate: row.cells[6].textContent,
                memberNo: memberNo // memberNo 추가
            };
            console.log("memberData:", memberData);
            openMemberModal(memberData);
        });
    });

   document.getElementById('editInformation')?.addEventListener('click', function () {
       const memberNo = window.currentMemberNo;
       if (!memberNo) {
           alert('회원 번호가 누락되었습니다.');
           return;
       }
       const updatedData = {
           memberNo: memberNo,
           type: document.getElementById('memberType').value,
           status: document.getElementById('memberStatus').value
       };
       console.log('Updated member data:', updatedData);

       // 서버로 데이터 전송
       fetch('/adminPage/updateMember', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json',
           },
           body: JSON.stringify(updatedData),
       })
       .then(response => {
           if (!response.ok) {
               throw new Error('Failed to update member.');
           }
           return response.json();
       })
       .then(data => {
           console.log('Update successful:', data);
           alert('회원 정보가 성공적으로 수정되었습니다.');
       })
       .catch(error => {
           console.error('Error updating member:', error);
           alert('수정 중 오류가 발생했습니다.');
       });
   });
   }

function openMemberModal(memberData) {
    window.currentMemberNo = memberData.memberNo;
    document.getElementById('memberName').value = memberData.name;
    document.getElementById('memberId').value = memberData.id;
    document.getElementById('memberEmail').value = memberData.email;
    document.getElementById('joinDate').value = memberData.joinDate;

   console.log('memberData:', memberData);

        // 회원 유형 처리
        const memberTypeElement = document.getElementById('memberType');
        if (memberTypeElement) {
            const memberTypeValue = memberData.type === '초보자' ? '0' :
                                    memberData.type === '전문가' ? '1' : '2';
            memberTypeElement.value = memberTypeValue;
        }

        // 회원 상태 처리
        const memberStatusElement = document.getElementById('memberStatus');
        if (memberStatusElement) {
            const memberStatusValue = memberData.status === '활성화' ? '0' :
                                      memberData.status === '비활성화' ? '1' :
                                      memberData.status === '정지' ? '2' : '3';
            memberStatusElement.value = memberStatusValue;
        }

        // 콘솔 출력 확인
        console.log('회원 유형 값:', memberTypeElement ? memberTypeElement.value : '선택자 오류');
        console.log('회원 상태 값:', memberStatusElement ? memberStatusElement.value : '선택자 오류');

    document.getElementById('joinDate').value = memberData.joinDate;

    const modalElement = document.getElementById('memberModal');
    modalElement.classList.add('show');
    modalElement.style.display = 'block';
    document.body.classList.add('modal-open');
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop fade show';
    document.body.appendChild(backdrop);
}

// 심사 관리 초기화 함수
function initializeScreeningManagement() {
    const screeningButtons = document.querySelectorAll('.screening-btn');
    console.log('Found screening buttons:', screeningButtons.length);

    screeningButtons.forEach(btn => {
        btn.addEventListener('click', handleScreeningClick);
    });

    const saveButton = document.getElementById('saveScreening');
    if (saveButton) {
        saveButton.addEventListener('click', handleSaveScreening);
    }
}

function handleScreeningClick(e) {
    e.preventDefault();
    console.log('Screening button clicked');

    const proNo = this.getAttribute('data-pro-no');
    const name = this.getAttribute('data-name');
    const itemTitle = this.getAttribute('data-item-title');
    const portfolioTitle = this.getAttribute('data-portfolio-title');

    console.log('Button data:', { proNo, name, itemTitle, portfolioTitle });

    const modalElement = document.getElementById('screeningModal');
    modalElement.querySelector('#proNo').value = proNo;
    modalElement.querySelector('#memberName').textContent = name;
    modalElement.querySelector('#itemTitle').textContent = itemTitle;
    modalElement.querySelector('#portfolioTitle').textContent = portfolioTitle;

    modalElement.querySelectorAll('input[name="professorStatus"]').forEach(radio => {
        radio.checked = false;
    });

    modalElement.querySelector('#screeningMsg').value = '';

    modalElement.classList.add('show');
    modalElement.style.display = 'block';
    document.body.classList.add('modal-open');
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop fade show';
    document.body.appendChild(backdrop);
}

function handleSaveScreening() {
    const saveButton = document.getElementById('saveScreening');
    saveButton.disabled = true;

    const proNo = document.getElementById('proNo').value;
    const status = document.querySelector('input[name="professorStatus"]:checked')?.value;
    const message = document.getElementById('screeningMsg').value;

    if (!status) {
        alert('심사 결과를 선택해주세요.');
        saveButton.disabled = false;
        return;
    }

    fetch('/reviewPro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            proNo: proNo,
            professorStatus: status,             
            screeningMsg: message || ''
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('서버 응답 오류');
        return response.text();
    })
    .then(() => {
        const modalElement = document.getElementById('screeningModal');
        closeModal(modalElement);

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

// 심사 수정 모달 관련 초기화 함수
function initializeScreeningModification() {
    const modScreeningButtons = document.querySelectorAll('.modScreening-btn');
    if (modScreeningButtons.length === 0) {
        console.warn('No buttons found with class "modScreening-btn"');
        return;
    }

    modScreeningButtons.forEach(button => {
        button.addEventListener('click', function () {
            const proNo = this.getAttribute('data-pro-no');
            const name = this.getAttribute('data-name');
            const itemTitle = this.getAttribute('data-item-title');
            const proDate = this.getAttribute('data-pro-date');

            const modalElement = document.getElementById('modScreeningModal');
            if (!modalElement) {
                console.error('Modal element with id "modScreeningModal" not found');
                return;
            }

            // 모달 데이터 설정
            modalElement.querySelector('#proNo').value = proNo;
            modalElement.querySelector('#name').textContent = name;
            modalElement.querySelector('#itemTitle').textContent = itemTitle;
            modalElement.querySelector('#proDate').textContent = proDate;

            // 라디오 버튼 초기화
            modalElement.querySelectorAll('input[name="professorStatus"]').forEach(radio => {
                radio.checked = false;
            });

            // 메모 필드 초기화
            modalElement.querySelector('#screeningMsg').value = '';

            // 모달 표시
            modalElement.classList.add('show');
            modalElement.style.display = 'block';
            document.body.classList.add('modal-open');
            const backdrop = document.createElement('div');
            backdrop.className = 'modal-backdrop fade show';
            document.body.appendChild(backdrop);
        });
    });

    const closeButton = document.querySelector('.close-custom-btn[data-modal-id="modScreeningModal"]');
    if (closeButton) {
        closeButton.addEventListener('click', function () {
            const modalElement = document.getElementById('modScreeningModal');
            closeModal(modalElement);
        });
    } else {
        console.warn('Close button with data-modal-id="modScreeningModal" not found');
    }

    const saveButton = document.getElementById('saveModScreening');
    if (saveButton) {
        saveButton.addEventListener('click', handleSaveModifiedScreening);
    } else {
        console.warn('Save button with id "saveModScreening" not found');
    }
}

// 심사 수정 저장 처리 함수
function handleSaveModifiedScreening() {
    const saveButton = document.getElementById('saveModScreening');
    if (!saveButton) return;

    saveButton.disabled = true;

    const proNo = document.getElementById('proNo').value;
    const status = document.querySelector('input[name="professorStatus"]:checked')?.value;
    const message = document.getElementById('screeningMsg').value;

    if (!status) {
        alert('승인 또는 거부를 선택해주세요.');
        saveButton.disabled = false;
        return;
    }

    fetch('/updateReviewPro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            proNo: proNo,
            professorStatus: status,
            screeningMsg: message || ''
        })
    })
        .then(response => {
            if (!response.ok) throw new Error('서버 응답 오류');
            return response.text();
        })
        .then(() => {
            const modalElement = document.getElementById('modScreeningModal');
            closeModal(modalElement);

            alert('심사 수정이 완료되었습니다.');
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('수정 중 오류가 발생했습니다.');
        })
        .finally(() => {
            saveButton.disabled = false;
        });
}
