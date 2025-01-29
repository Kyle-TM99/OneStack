// 핸드폰 번호 자동 하이픈 적용 함수
function formatPhoneNumber(phone) {
    phone = phone.replace(/[^0-9]/g, ""); // 숫자 이외의 문자 제거
    if (phone.length <= 3) {
        return phone;
    } else if (phone.length <= 7) {
        return phone.replace(/(\d{3})(\d{1,4})/, "$1-$2");
    } else {
        return phone.replace(/(\d{3})(\d{4})(\d{1,4})/, "$1-$2-$3");
    }
}
// 통합 스크립트 - 회원 관리 및 심사 관리 관련
document.addEventListener('DOMContentLoaded', function () {

    initializeCommonEvents();
    initializeCheckboxes();
    initializeMemberManagement();
    initializeScreeningManagement();
    initializeScreeningModification();

            // ** 입력 필드 길이 제한 적용**
            const inputFields = [
                { id: "memberName", max: 30,  type:"textOnly"},
                { id: "memberId", max: 50 },
                { id: "memberPass", max: 100 },
                { id: "nickname", max: 20 },
                { id: "zipcode", max: 5 },
                { id: "address", max: 50 },
                { id: "address2", max: 50 },
                { id: "memberEmail", max: 30 },
                { id: "phone", max: 13, type:"phoneOnly"},
                { id: "stackName", max: 20 }
            ];

            inputFields.forEach(field => {
                    const input = document.getElementById(field.id);
                    if (input) {
                        input.setAttribute("maxlength", field.max); // HTML에서도 길이 제한 적용

                        input.addEventListener("input", function () {
                            // ** 입력 길이 초과 방지**
                            if (this.value.length > field.max) {
                                alert(`${field.id}은 최대 ${field.max}자까지 입력 가능합니다.`);
                                this.value = this.value.substring(0, field.max);
                            }

                            // **숫자만 입력 가능 (핸드폰 번호)**
                            if (field.type === "numberOnly" && /[^0-9-]/.test(this.value)) {
                                alert("숫자만 입력해주세요.");
                                this.value = this.value.replace(/[^0-9-]/g, "");  // 숫자와 `-`만 남기기
                            }

                            // **숫자 입력 방지 (이름)**
                            if (field.type === "textOnly" && /\d/.test(this.value)) {
                                alert("이름에는 숫자를 포함할 수 없습니다.");
                                this.value = this.value.replace(/\d/g, "");  // 숫자 제거
                            }
                        });
                    }
                });

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
function initializeMemberManagement() {
    document.querySelectorAll('.member-edit-btn').forEach(button => {
        button.addEventListener('click', function () {
            const row = this.closest('tr');

            // `data-*` 속성을 사용하여 정확한 데이터 가져오기
            const memberData = {
                name: row.getAttribute('data-name'),
                nickname: row.getAttribute('data-nickname'),
                id: row.getAttribute('data-id'),
                type: row.getAttribute('data-type'),
                email: row.getAttribute('data-email'),
                phone: row.getAttribute('data-phone'),
                address: row.getAttribute('data-address'),
                address2: row.getAttribute('data-address2'),
                status: row.getAttribute('data-status'),
                joinDate: row.getAttribute('data-join-date'),
                banEndDate: row.getAttribute('data-ban-end-date'),
                memberNo: row.getAttribute('data-member-no')
            };

            console.log("바인딩된 memberData:", memberData);
            openMemberModal(memberData);
        });
    });

  document.getElementById('editInformation')?.addEventListener('click', function () {
      const memberNo = window.currentMemberNo;
      if (!memberNo) {
          alert('회원 번호가 누락되었습니다.');
          return;
      }
    // ** 핸드폰 번호 입력 시 자동 하이픈 적용**
    const phoneInput = document.getElementById("phone");
    if (phoneInput) {
        phoneInput.setAttribute("maxlength", "13"); // HTML에서도 길이 제한 적용
        phoneInput.addEventListener("input", function () {
            this.value = formatPhoneNumber(this.value);
        });
    }

      const updatedData = {
              memberNo: memberNo,
              name: document.getElementById('memberName').value.trim(),
              nickname: document.getElementById('nickname').value.trim(),
              memberId: document.getElementById('memberId').value.trim(),
              type: document.getElementById('memberType').value,
              email: document.getElementById('memberEmail').value.trim(),
              phone: phoneInput.value.trim(),
              address: document.getElementById('address').value.trim(),
              address2: document.getElementById('address2').value.trim(),
              status: document.getElementById('memberStatus').value
          };

      // 기간 정지(1)일 경우 `banEndDate` 포함
      if (updatedData.status == "1") {
          const banEndDate = document.getElementById("banEndDate").value.trim();
          if (!banEndDate) {
              alert("정지 종료일을 선택해주세요.");
              return;
          }
          updatedData.banEndDate = banEndDate; // 서버로 보낼 데이터에 추가
      } else {
          updatedData.banEndDate = null; // 기간 정지가 아니면 `null`
      }

      console.log('Updated member data:', updatedData);

      fetch('/adminPage/updateMember', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify(updatedData),
      })
      .then(response => {
          if (!response.ok) {
              throw new Error('회원 정보 수정 실패');
          }
          return response.json();  // JSON 응답 처리
      })
      .then(data => {
          console.log('Update successful:', data);
          alert(data.message);
          location.reload();
      })
      .catch(error => {
          console.error('Error updating member:', error);
          alert(error.message);
      });
  });
}

function openMemberModal(memberData) {
    window.currentMemberNo = memberData.memberNo;
    document.getElementById('memberName').value = memberData.name;
    document.getElementById('nickname').value = memberData.nickname;
    document.getElementById('memberId').value = memberData.id;
    document.getElementById('memberEmail').value = memberData.email;
        // 핸드폰 번호 자동 형식 적용 (초기 값 설정)
      const phoneInput = document.getElementById('phone');
      if (phoneInput) {
          phoneInput.value = formatPhoneNumber(memberData.phone || "");

          // 기존 리스너 제거 후 새로 추가 (중복 방지)
          phoneInput.removeEventListener("input", handlePhoneInput);
          phoneInput.addEventListener("input", handlePhoneInput);
      }
    document.getElementById('address').value = memberData.address;
    document.getElementById('address2').value = memberData.address2;


    // 회원 유형
    const memberTypeElement = document.getElementById('memberType');
        if (memberTypeElement) {
            const typeValue = memberData.type; // "0", "1", "2" (문자열일 수도 있음)
            console.log("회원 유형 값 확인:", typeValue); // 디버깅 로그

            if (typeValue == "0") {
                memberTypeElement.value = "0"; // 초보자
            } else if (typeValue == "1") {
                memberTypeElement.value = "1"; // 전문가
            } else {
                memberTypeElement.value = "2"; // 심사중
            }
        }

        // 회원 상태 (memberStatus)
        const memberStatusElement = document.getElementById('memberStatus');
        if (memberStatusElement) {
            const statusValue = memberData.status;
            console.log("회원 상태 값 확인:", statusValue); // 디버깅 로그

            if (statusValue == "0") {
                memberStatusElement.value = "0"; // 활성화
            } else if (statusValue == "1") {
                memberStatusElement.value = "1"; // 비활성화
            } else if (statusValue == "2") {
                memberStatusElement.value = "2"; // 정지
            } else {
                memberStatusElement.value = "3"; // 탈퇴
            }

        // 기간 정지일 경우 정지 종료일 필드 표시
        toggleBanDate();
        // 오늘 이후 날짜만 선택 가능하도록 설정
        setMinBanDate();
    }
    document.getElementById('joinDate').value = memberData.joinDate;

    // 정지 종료일 필드
    document.getElementById('banEndDate').value = memberData.banEndDate || "";

    const modalElement = document.getElementById('memberModal');
    modalElement.classList.add('show');
    modalElement.style.display = 'block';
    document.body.classList.add('modal-open');
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop fade show';
    document.body.appendChild(backdrop);
}
// 핸드폰 번호 입력 이벤트 핸들러 (중복 제거 및 적용)
function handlePhoneInput(event) {
    event.target.value = formatPhoneNumber(event.target.value);
}

function setMinBanDate() {
    const banEndDateInput = document.getElementById("banEndDate");
    const today = new Date().toISOString().split("T")[0];
    banEndDateInput.setAttribute("min", today);
}

function toggleBanDate() {
    const statusSelect = document.getElementById("memberStatus");
    const banDateContainer = document.getElementById("banDateContainer");
    const banEndDateInput = document.getElementById("banEndDate");

    if (statusSelect.value == "1") {
        banDateContainer.style.display = "block";
    } else {
        banDateContainer.style.display = "none";
        banEndDateInput.value = "";
    }
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
