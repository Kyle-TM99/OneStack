document.addEventListener('DOMContentLoaded', function () {
  // 모달 메뉴 동작
  function setupModalHover(triggerId, modalId) {
    const trigger = document.getElementById(triggerId);
    const modal = new bootstrap.Modal(document.getElementById(modalId));

    trigger.addEventListener('click', () => {
      modal.show();
    });
  }

  setupModalHover('chatMenu', 'chatModal');
  setupModalHover('notificationMenu', 'notificationModal');

  const chatButton = document.querySelector('#chatMenu');
  const notificationButton = document.querySelector('#notificationMenu');
  const chatModal = document.querySelector('#chatModal .modal-dialog');
  const notificationModal = document.querySelector('#notificationModal .modal-dialog');

  // 채팅 모달이 열릴 때 위치 조정
  $('#chatModal').on('show.bs.modal', function() {
    const buttonRect = chatButton.getBoundingClientRect();
    chatModal.style.left = buttonRect.left + 'px';
    chatModal.style.top = (buttonRect.bottom + 5) + 'px'; // 버튼 아래 5px 간격
    chatModal.style.transform = 'none';

    // 알림 모달이 열려있는 경우
    if ($('#notificationModal').hasClass('show')) {
      const notificationRect = notificationButton.getBoundingClientRect();
      notificationModal.style.left = (notificationRect.left - 320) + 'px';
      notificationModal.style.top = (notificationRect.bottom + 5) + 'px';
    }
  });

  // 알림 모달이 열릴 때 위치 조정
  $('#notificationModal').on('show.bs.modal', function() {
    const buttonRect = notificationButton.getBoundingClientRect();
    notificationModal.style.left = buttonRect.left + 'px';
    notificationModal.style.top = (buttonRect.bottom + 5) + 'px'; // 버튼 아래 5px 간격
    notificationModal.style.transform = 'none';

    // 채팅 모달이 열려있는 경우
    if ($('#chatModal').hasClass('show')) {
      const chatRect = chatButton.getBoundingClientRect();
      chatModal.style.left = (chatRect.left - 320) + 'px';
      chatModal.style.top = (chatRect.bottom + 5) + 'px';
    }
  });

  // 창 크기가 변경될 때 위치 조정
  window.addEventListener('resize', function() {
    if ($('#chatModal').hasClass('show')) {
      const buttonRect = chatButton.getBoundingClientRect();
      chatModal.style.left = buttonRect.left + 'px';
      chatModal.style.top = (buttonRect.bottom + 5) + 'px';
    }
    if ($('#notificationModal').hasClass('show')) {
      const buttonRect = notificationButton.getBoundingClientRect();
      notificationModal.style.left = buttonRect.left + 'px';
      notificationModal.style.top = (buttonRect.bottom + 5) + 'px';
    }
  });
});
