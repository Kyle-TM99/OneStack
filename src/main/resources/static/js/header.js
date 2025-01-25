document.addEventListener('DOMContentLoaded', function() {
  // 왼쪽 메뉴 요소와 서브메뉴, 모달 창 관련 요소들 선택
  const leftMenu = document.querySelector('.left-menu');
  const submenuContainer = document.querySelector('.submenu-container');
  const chatModal = document.getElementById('chatModal');
  const notificationModal = document.getElementById('notificationModal');
  const chatIcon = document.querySelector('.right-menu .nav-link:first-child');
  const notificationIcon = document.querySelector('.right-menu .nav-link:last-child');

  // 왼쪽 메뉴에 마우스를 올렸을 때 서브메뉴를 표시
  leftMenu.addEventListener('mouseenter', function() {
    submenuContainer.classList.add('show');  // 서브메뉴를 보이게 함
  });

  // 왼쪽 메뉴에서 마우스를 떼었을 때 서브메뉴를 숨김
  leftMenu.addEventListener('mouseleave', function(e) {
    if (!e.relatedTarget || !e.relatedTarget.closest('.submenu-container')) {
      submenuContainer.classList.remove('show');  // 서브메뉴를 숨김
    }
  });

  // 서브메뉴에 마우스를 올렸을 때 서브메뉴를 계속 표시
  submenuContainer.addEventListener('mouseenter', function() {
    submenuContainer.classList.add('show');
  });

  // 서브메뉴에서 마우스를 떼었을 때 서브메뉴를 숨김
  submenuContainer.addEventListener('mouseleave', function() {
    submenuContainer.classList.remove('show');
  });

  // 채팅 아이콘 클릭 시 채팅 모달을 띄우는 코드
  chatIcon?.addEventListener('click', function(e) {
    e.preventDefault();  // 기본 동작 방지
    const chatModalInstance = new bootstrap.Modal(chatModal, { backdrop: false });
    chatModalInstance.show();  // 채팅 모달을 표시
  });

  // 알림 아이콘 클릭 시 알림 모달을 띄우는 코드
  notificationIcon?.addEventListener('click', function(e) {
    e.preventDefault();  // 기본 동작 방지
    const notificationModalInstance = new bootstrap.Modal(notificationModal, { backdrop: false });
    notificationModalInstance.show();  // 알림 모달을 표시
  });

  // 모달 위치를 아이콘의 중앙에 맞추기 위한 함수
  const adjustModalPosition = (modalElement, iconElement) => {
    modalElement.addEventListener('show.bs.modal', function () {
      const modalDialog = this.querySelector('.modal-dialog');
      const rect = iconElement.getBoundingClientRect();  // 아이콘의 위치와 크기
      const iconAbsoluteCenter = rect.left + (rect.width / 2);  // 아이콘의 중앙
      const modalWidth = 350;  // 모달의 너비

      modalDialog.style.position = 'fixed';  // 고정 위치로 설정
      modalDialog.style.width = `${modalWidth}px`;  // 모달 너비 설정
      modalDialog.style.left = `${iconAbsoluteCenter - (modalWidth / 2)}px`;  // 아이콘의 중앙에 모달을 맞춤
      modalDialog.style.margin = '0';  // 여백 제거
      modalDialog.style.transform = 'none';  // 변형 설정 해제
    });
  };

  // 채팅 모달과 알림 모달에 대해 각각 위치 조정 함수 실행
  adjustModalPosition(chatModal, chatIcon);
  adjustModalPosition(notificationModal, notificationIcon);

  // 모달 창 외부를 클릭하면 모달을 닫는 기능
  document.addEventListener('click', function(e) {
    const closeModalIfOutsideClick = (modalElement, iconElement) => {
      if (modalElement.classList.contains('show') &&
          !e.target.closest('.modal-dialog') &&
          !iconElement.contains(e.target)) {
        const modalInstance = bootstrap.Modal.getInstance(modalElement);  // 모달 인스턴스 가져오기
        modalInstance.hide();  // 모달 닫기
      }
    };
    closeModalIfOutsideClick(chatModal, chatIcon);  // 채팅 모달 외부 클릭 시 닫기
    closeModalIfOutsideClick(notificationModal, notificationIcon);  // 알림 모달 외부 클릭 시 닫기
  });

  // 개발 카테고리 클릭 시 해당 서브메뉴 토글 (보이기/숨기기)
  const developmentHeader = document.getElementById('development-header');
  const developmentSubmenu = document.querySelector('.development-submenu');

  developmentHeader.addEventListener('click', function() {
    if (developmentSubmenu.style.display === 'none' || developmentSubmenu.style.display === '') {
      developmentSubmenu.style.display = 'block';  // 서브메뉴 보이기
    } else {
      developmentSubmenu.style.display = 'none';  // 서브메뉴 숨기기
    }
  });

  // 데이터 카테고리 클릭 시 해당 서브메뉴 토글 (보이기/숨기기)
  const dataHeader = document.getElementById('data-header');
  const dataSubmenu = document.querySelector('.data-submenu');

  dataHeader.addEventListener('click', function() {
    if (dataSubmenu.style.display === 'none' || dataSubmenu.style.display === '') {
      dataSubmenu.style.display = 'block';  // 서브메뉴 보이기
    } else {
      dataSubmenu.style.display = 'none';  // 서브메뉴 숨기기
    }
  });
});
