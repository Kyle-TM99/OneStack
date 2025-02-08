// 허용된 휴대폰 번호 앞자리 (국번)
const validPrefixes = ["010", "011", "016", "017", "018", "019"];

// 핸드폰 번호 자동 하이픈 적용 함수
function formatPhoneNumber(phone) {
    phone = phone.replace(/[^0-9]/g, ""); // 숫자 이외의 문자 제거

    // 숫자 개수가 11자리를 초과하지 않도록 제한
    if (phone.length > 11) {
        phone = phone.substring(0, 11);
    }

    if (phone.length <= 3) {
        return phone;
    } else if (phone.length <= 7) {
        return phone.replace(/(\d{3})(\d{1,4})/, "$1-$2");
    } else {
        return phone.replace(/(\d{3})(\d{4})(\d{1,4})/, "$1-$2-$3");
    }
}
// 핸드폰 번호 입력 이벤트 핸들러 (숫자만 입력, 13자리 제한, 자동 하이픈 추가)
function handlePhoneInput(event) {
    let value = event.target.value.replace(/[^0-9]/g, ""); // 숫자만 허용

    // 3자리 이상 입력되었을 때, 유효한 국번인지 체크
    if (value.length >= 3) {
        let prefix = value.substring(0, 3);
        if (!validPrefixes.includes(prefix)) {
            alert(`유효하지 않은 휴대폰 번호 앞자리입니다. (${validPrefixes.join(", ")})`);
            event.target.value = "";
            return;
        }
    }

    // 숫자 개수를 11자리로 제한
    if (value.length > 11) {
        alert("핸드폰 번호는 11자리 숫자로 입력해야 합니다.");
        value = value.substring(0, 11);
    }

    event.target.value = formatPhoneNumber(value);

    // `-` 포함된 상태에서 13자리를 초과하면 입력 차단
    if (event.target.value.length > 13) {
        alert("핸드폰 번호는 '-' 포함 최대 13자리까지만 입력 가능합니다.");
        event.target.value = event.target.value.substring(0, 13);
    }
}
function handleMemberIdInput(event) {
    let value = event.target.value;

    // 1️⃣ 한글 및 특수문자 제거 (영문 + 숫자만 허용)
    if (/[^a-zA-Z0-9]/.test(value)) {
        alert("아이디는 영문과 숫자만 입력 가능합니다.");
        event.target.value = value.replace(/[^a-zA-Z0-9]/g, ""); // 한글 및 특수문자 제거
    }

    // 2️⃣ 최대 길이 제한 적용 (50자)
    if (value.length > 50) {
        alert("아이디는 최대 50자까지 입력 가능합니다.");
        setTimeout(() => { event.target.value = value.substring(0, 50); }, 10);
    }
}
// 통합 스크립트 - 회원 관리 및 심사 관리 관련
document.addEventListener('DOMContentLoaded', function () {
    initializeCommonEvents();
    initializeCheckboxes();
    initializeMemberManagement();
    initializeScreeningManagement();
    initializeScreeningModification();

    const inputFields = [
        { id: "memberName", max: 30, type: "name" },
        { id: "memberId", max: 50, type: "text" },
        { id: "memberPass", max: 100, type: "text" },
        { id: "nickname", max: 20, type: "text" },
        { id: "zipcode", max: 5, type: "number" },
        { id: "address", max: 50, type: "text" },
        { id: "address2", max: 50, type: "text" },
        { id: "memberEmail", max: 30, type: "text" },
        { id: "phone", max: 13, type: "phone" }, // 핸드폰 입력 필드
        { id: "stackName", max: 20, type: "text" }
    ];

   // ✅ `inputFields`를 사용하여 필드별 이벤트 적용
       inputFields.forEach(field => {
           const input = document.getElementById(field.id);
           if (input) {
               input.addEventListener("input", function () {
                   let value = this.value;

                   // 최대 길이 제한 적용
                   if (value.length > field.max) {
                       alert(`${field.id}은(는) 최대 ${field.max}자까지 입력 가능합니다.`);
                       setTimeout(() => { this.value = value.substring(0, field.max); }, 10);
                   }

                   // 이름 (한글만 허용)
                   if (field.type === "name" && /[^가-힣\s]/.test(value)) {
                       alert("이름에는 숫자, 영문, 특수문자를 포함할 수 없습니다.");
                       setTimeout(() => { this.value = value.replace(/[^가-힣\s]/g, ""); }, 10);
                   }
                   // 숫자만 입력 가능한 필드 체크
                   if (field.type === "number" && /\D/.test(value)) {
                       alert("숫자만 입력해주세요.");
                       setTimeout(() => { this.value = value.replace(/\D/g, ""); }, 10);
                   }
               });
           }
       });
           // ✅ 핸드폰 번호 입력 필드에 이벤트 리스너 등록 (중복 제거)
           const phoneInput = document.getElementById("phone");
           if (phoneInput) {
               phoneInput.removeEventListener("input", handlePhoneInput);
               phoneInput.addEventListener("input", handlePhoneInput, { once: true });
           }

           // ✅ MutationObserver (중복 실행 방지)
           const observer = new MutationObserver(function (mutations) {
               let shouldReinitialize = false;
               mutations.forEach(function (mutation) {
                   if (mutation.addedNodes.length) {
                       shouldReinitialize = true;
                   }
           const memberIdInput = document.getElementById("memberId");
           if (memberIdInput) {
               memberIdInput.removeEventListener("input", handleMemberIdInput); // 중복 방지
               memberIdInput.addEventListener("input", handleMemberIdInput, { once: true });
           }
               });
           if (shouldReinitialize) {
                console.log('DOM 변경 감지됨, 컴포넌트 재초기화');
                initializeCheckboxes();
                initializeMemberManagement();
                initializeScreeningManagement();
                initializeScreeningModification();
            }
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

// 모달 닫기 함수(수정)
//function closeModal(modalElement) {
//    modalElement.classList.remove('show');
//    modalElement.style.display = 'none';
//    document.body.classList.remove('modal-open');
//    const backdrop = document.querySelector('.modal-backdrop');
//    if (backdrop) backdrop.remove();
//}

function closeModal(modalElement) {
    if (!modalElement) return;

    modalElement.classList.remove('show');
    modalElement.style.display = 'none';
    document.body.classList.remove('modal-open');

    // ✅ 회색 배경(`modal-backdrop`)이 두 번 삭제되지 않도록 보장
    setTimeout(() => {
        document.querySelectorAll('.modal-backdrop').forEach(backdrop => {
            backdrop.remove();
        });
    }, 10);
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
//function initializeMemberManagement() {
//    document.querySelectorAll('.member-edit-btn').forEach(button => {
//        button.addEventListener('click', function () {
//            const row = this.closest('tr');
//
//            // `data-*` 속성을 사용하여 정확한 데이터 가져오기
//            const memberData = {
//                name: row.getAttribute('data-name'),
//                nickname: row.getAttribute('data-nickname'),
//                id: row.getAttribute('data-id'),
//                type: row.getAttribute('data-type'),
//                email: row.getAttribute('data-email'),
//                phone: row.getAttribute('data-phone'),
//                address: row.getAttribute('data-address'),
//                address2: row.getAttribute('data-address2'),
//                status: row.getAttribute('data-status'),
//                joinDate: row.getAttribute('data-join-date'),
//                banEndDate: row.getAttribute('data-ban-end-date'),
//                memberNo: row.getAttribute('data-member-no'),
//                memberStop: row.getAttribute('data-member-stop')
//            };
//
//            console.log("바인딩된 memberData:", memberData);
//            openMemberModal(memberData);
//        });
//    });
//
//// 회원정보 수정
// document.getElementById('editInformation')?.addEventListener('click', function () {
//     const memberNo = parseInt(window.currentMemberNo);
//     if (!memberNo) {
//         alert('회원 번호가 누락되었습니다.');
//         return;
//     }
//
//     const phoneInput = document.getElementById("phone");
//     if (phoneInput) {
//         phoneInput.setAttribute("maxlength", "13");
//         phoneInput.addEventListener("input", function () {
//             this.value = formatPhoneNumber(this.value);
//         });
//     }
//
//     const updatedData = {
//         memberNo: memberNo,
//         name: document.getElementById('memberName').value.trim(),
//         nickname: document.getElementById('nickname').value.trim(),
//         memberId: document.getElementById('memberId').value.trim(),
//         memberType: document.getElementById('memberType').value,  // 🔥 숫자로 변환
//         email: document.getElementById('memberEmail').value.trim(),
//         phone: phoneInput.value.trim(),
//         address: document.getElementById('address').value.trim(),
//         address2: document.getElementById('address2').value.trim(),
//         memberStatus: document.getElementById('memberStatus').value,
//         banEndDate: null,
//         memberStop: null
//     };
//
//     const banEndDateInput = document.getElementById("banEndDate").value.trim();
//     const memberStopInput = document.getElementById("memberStop") ? document.getElementById("memberStop").value.trim() : "";
//
//     if (updatedData.memberStatus === "1") {
//         // ✅ 기간 정지 상태일 경우, 정지 종료일 필수 입력
//         if (!banEndDateInput) {
//             alert("정지 종료일을 선택해주세요.");
//             return;
//         }
//         updatedData.banEndDate = banEndDateInput;
//         updatedData.memberStop = null; // 기간 정지일 경우 정지 사유 초기화
//     } else if (updatedData.memberStatus === "2") {
//         // ✅ 영구 정지 상태일 경우, 정지 사유 필수 입력
//         if (!memberStopInput) {
//             alert("정지 사유를 입력해주세요.");
//             return;
//         }
//         updatedData.memberStop = memberStopInput;
//         updatedData.banEndDate = null; // 영구 정지일 경우 정지 종료일 초기화
//     }
//
//// ✅ 필수 입력값 확인 (banEndDate 또는 memberStopReason은 제외)
//     for (let key in updatedData) {
//         if (!updatedData[key] && key !== "banEndDate" && key !== "memberStop") {
//             alert(`${key} 값을 입력해주세요.`);
//             return;
//         }
//     }
//
//
//     console.log('Updated member data:', updatedData);
//
//     fetch('/adminPage/updateMember', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(updatedData),
//     })
//     .then(response => response.json())
//     .then(data => {
//         if (data.message.includes("실패")) {
//             alert(data.message);
//         } else {
//             alert(data.message);
//             location.reload();
//         }
//     })
//     .catch(error => {
//         console.error('Error updating member:', error);
//         alert("회원 정보 수정 중 오류 발생");
//     });
// });
//}

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
                memberNo: row.getAttribute('data-member-no'),
                memberStop: row.getAttribute('data-member-stop')
            };

            console.log("바인딩된 memberData:", memberData);
            openMemberModal(memberData);
        });
    });

    // ✅ 기존 이벤트 제거 후 한 번만 등록 (중복 실행 방지)
    const editButton = document.getElementById('editInformation');
    if (editButton) {
        editButton.removeEventListener('click', handleEditInformation); // 기존 이벤트 제거
        editButton.addEventListener('click', handleEditInformation); // 한 번만 등록
    }
}

function handleEditInformation(event) {
    event.preventDefault(); // 기본 동작 방지

    const editButton = document.getElementById('editInformation');
    if (editButton.disabled) return; // ✅ 이미 비활성화된 경우 실행 안 함
    editButton.disabled = true; // ✅ 클릭 후 즉시 비활성화 (중복 실행 방지)

    const memberNo = parseInt(window.currentMemberNo);
    if (!memberNo) {
        alert('회원 번호가 누락되었습니다.');
        editButton.disabled = false;
        return;
    }

    const phoneInput = document.getElementById("phone");
    if (phoneInput) {
        phoneInput.setAttribute("maxlength", "13");
        phoneInput.addEventListener("input", function () {
            this.value = formatPhoneNumber(this.value);
        });
    }

    const updatedData = {
        memberNo: memberNo,
        name: document.getElementById('memberName').value.trim(),
        nickname: document.getElementById('nickname').value.trim(),
        memberId: document.getElementById('memberId').value.trim(),
        memberType: document.getElementById('memberType').value,
        email: document.getElementById('memberEmail').value.trim(),
        phone: phoneInput.value.trim(),
        address: document.getElementById('address').value.trim(),
        address2: document.getElementById('address2').value.trim(),
        memberStatus: document.getElementById('memberStatus').value,
        banEndDate: null,
        memberStop: null
    };

    const banEndDateInput = document.getElementById("banEndDate").value.trim();
    const memberStopInput = document.getElementById("memberStop") ? document.getElementById("memberStop").value.trim() : "";

    if (updatedData.memberStatus === "1" && !banEndDateInput) {
        alert("정지 종료일을 선택해주세요.");
        editButton.disabled = false;
        return;
    }

    if (updatedData.memberStatus === "2" && !memberStopInput) {
        alert("정지 사유를 입력해주세요.");
        editButton.disabled = false;
        return;
    }

    if (updatedData.memberStatus === "1") updatedData.banEndDate = banEndDateInput;
    if (updatedData.memberStatus === "2") updatedData.memberStop = memberStopInput;

    console.log('Updated member data:', updatedData);

    fetch('/adminPage/updateMember', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedData),
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);
        if (!data.message.includes("실패")) {
            location.reload();
        }
    })
    .catch(error => {
        console.error('Error updating member:', error);
        alert("회원 정보 수정 중 오류 발생");
    })
    .finally(() => {
        editButton.disabled = false; // ✅ 요청 완료 후 다시 활성화
    });
}


function openMemberModal(memberData) {
    window.currentMemberNo = memberData.memberNo;
    document.getElementById('memberName').value = memberData.name?.trim() || "";
    document.getElementById('nickname').value = memberData.nickname?.trim() || "";
    document.getElementById('memberId').value = memberData.id?.trim() || "";
    document.getElementById('memberEmail').value = memberData.email?.trim() || "";


     // ✅ 핸드폰 번호 자동 형식 적용 및 이벤트 중복 제거
     const phoneInput = document.getElementById('phone');
     if (phoneInput) {
         phoneInput.value = formatPhoneNumber(memberData.phone || "");

         // 기존 이벤트 제거 후 새 이벤트 추가
         phoneInput.removeEventListener("input", handlePhoneInput);
         phoneInput.addEventListener("input", handlePhoneInput);
      }
    document.getElementById('address').value = memberData.address?.trim() || "";
    document.getElementById('address2').value = memberData.address2?.trim() || "";


    // 회원 유형
    const memberTypeElement = document.getElementById('memberType');
        if (memberTypeElement) {
            const typeValue = memberData.type; // "0", "1", "2"
            console.log("회원 유형 값 확인:", typeValue); // 디버깅 로그

            if (typeValue === "0") {
                memberTypeElement.value = "0"; // 초보자
            } else if (typeValue === "1") {
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

            if (statusValue === "0") {
                memberStatusElement.value = "0"; // 활성화
            } else if (statusValue === "1") {
                memberStatusElement.value = "1"; // 비활성화
            } else if (statusValue === "2") {
                memberStatusElement.value = "2"; // 정지
            } else {
                memberStatusElement.value = "3"; // 탈퇴
            }

        // 기간 정지일 경우 정지 종료일 필드 표시
        toggleBanDate();

    }
        document.getElementById('joinDate').value = memberData.joinDate;

        // ✅ `banEndDate` 변환 (`yyyy-MM-dd HH:mm:ss` → `yyyy-MM-dd`)
        if (memberData.banEndDate) {
            let banDate = new Date(memberData.banEndDate);
            let formattedDate = banDate.toISOString().split('T')[0]; // 🔥 'yyyy-MM-dd' 형식으로 변환
            document.getElementById('banEndDate').value = formattedDate;
        } else {
            document.getElementById('banEndDate').value = ""; // 🚀 값이 없으면 비워둠
        }

        // ✅ 오늘 이후 날짜만 선택 가능하도록 설정
        setMinBanDate();

        // ✅ 모달 표시
        const modalElement = document.getElementById('memberModal');
        modalElement.classList.add('show');
        modalElement.style.display = 'block';
        document.body.classList.add('modal-open');
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-backdrop fade show';
        document.body.appendChild(backdrop);
    }

function setMinBanDate() {
    const banEndDateInput = document.getElementById("banEndDate");
    const today = new Date().toISOString().split("T")[0];
    banEndDateInput.setAttribute("min", today);
}

function toggleBanDate() {
    const memberStatus = document.getElementById("memberStatus").value;
    const banDateContainer = document.getElementById("banDateContainer");
    const memberStopContainer = document.getElementById("memberStopContainer");

    // 정지 상태 선택 시, 정지 종료일과 정지 사유 입력란을 표시
    if (memberStatus === "1") {
        banDateContainer.style.display = "block";
        memberStopContainer.style.display = "none";
    } else if (memberStatus === "2") {
        banDateContainer.style.display = "none";
        memberStopContainer.style.display = "block";
    } else {
        banDateContainer.style.display = "none";
        memberStopContainer.style.display = "none";
    }
}


//     function toggleBanDate() {
//     const statusSelect = document.getElementById("memberStatus");
//     const banDateContainer = document.getElementById("banDateContainer");
//     const banEndDateInput = document.getElementById("banEndDate");
//
//     if (statusSelect.value == "1") {
//         banDateContainer.style.display = "block";
//     } else {
//         banDateContainer.style.display = "none";
//         banEndDateInput.value = "";
//     }
// }

// 심사 관리 초기화 함수
function initializeScreeningManagement() {
    const screeningButtons = document.querySelectorAll('.screening-btn');
    console.log('Found screening buttons:', screeningButtons.length);

    screeningButtons.forEach(btn => {
        btn.addEventListener('click', handleScreeningClick);
    });

    const saveButton = document.getElementById('saveScreening');
    if (saveButton) {
        saveButton.addEventListener('click', handleSaveScreening, { once: true });
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
        .then((message) => {
            alert(message);
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('심사 처리 중 오류가 발생했습니다.');
        })
        .finally(() => {
            saveButton.disabled = false;
        });
}

// 심사 수정 저장 처리 함수
// function handleSaveModifiedScreening() {
//     const saveButton = document.getElementById('saveModScreening');
//     if (!saveButton) return;
//
//     saveButton.disabled = true;
//
//     const proNo = document.getElementById('proNo').value;
//     const status = document.querySelector('input[name="professorStatus"]:checked')?.value;
//     const message = document.getElementById('screeningMsg').value;
//
//     if (!status) {
//         alert('승인 또는 거부를 선택해주세요.');
//         saveButton.disabled = false;
//         return;
//     }
//
//     fetch('/updateReviewPro', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify({
//             proNo: proNo,
//             professorStatus: status,
//             screeningMsg: message || ''
//         })
//     })
//         .then(response => {
//             if (!response.ok) throw new Error('서버 응답 오류');
//             return response.text();
//         })
//         .then(() => {
//             const modalElement = document.getElementById('modScreeningModal');
//             closeModal(modalElement);
//
//             alert('심사 수정이 완료되었습니다.');
//             window.location.reload();
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('수정 중 오류가 발생했습니다.');
//         })
//         .finally(() => {
//             saveButton.disabled = false;
//         });

