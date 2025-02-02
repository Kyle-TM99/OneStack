document.addEventListener('DOMContentLoaded', function() {
  // 메뉴 요소들 선택
  const leftMenu = document.querySelector('.left-menu');
  const menuItems = document.querySelectorAll('[data-menu]');
  const submenuContainer = document.querySelector('.submenu-container');
  
  let isHovering = false;
  let timeoutId = null;

  // 메뉴 아이템 호버 이벤트
  menuItems.forEach(item => {
    item.addEventListener('mouseenter', function() {
      clearTimeout(timeoutId);
      isHovering = true;
      
      // 모든 메뉴 아이템의 활성 상태 제거
      menuItems.forEach(mi => mi.classList.remove('active'));
      // 현재 메뉴 아이템 활성화
      this.classList.add('active');
      
      // 서브메뉴 표시
      submenuContainer.style.opacity = '1';
      submenuContainer.style.visibility = 'visible';
    });
  });

  // left-menu 영역 호버 이벤트
  leftMenu.addEventListener('mouseenter', function() {
    clearTimeout(timeoutId);
    isHovering = true;
  });

  leftMenu.addEventListener('mouseleave', function() {
    isHovering = false;
    
    // 약간의 지연 후 서브메뉴 숨김 (마우스가 서브메뉴로 이동하는 시간 고려)
    timeoutId = setTimeout(() => {
      if (!isHovering) {
        submenuContainer.style.opacity = '0';
        submenuContainer.style.visibility = 'hidden';
        menuItems.forEach(mi => mi.classList.remove('active'));
      }
    }, 200);
  });

  // 서브메뉴 컨테이너 호버 이벤트
  submenuContainer.addEventListener('mouseenter', function() {
    clearTimeout(timeoutId);
    isHovering = true;
  });

  submenuContainer.addEventListener('mouseleave', function() {
    isHovering = false;
    submenuContainer.style.opacity = '0';
    submenuContainer.style.visibility = 'hidden';
    menuItems.forEach(mi => mi.classList.remove('active'));
  });

  // 개발 카테고리 토글
  const developmentHeader = document.getElementById('development-header');
  const developmentSubmenu = document.querySelector('.development-submenu');
  
  if (developmentHeader && developmentSubmenu) {
    developmentHeader.addEventListener('click', function() {
      const icon = this.querySelector('.bi-chevron-down');
      if (developmentSubmenu.style.display === 'none' || !developmentSubmenu.style.display) {
        developmentSubmenu.style.display = 'block';
        icon.style.transform = 'rotate(180deg)';
      } else {
        developmentSubmenu.style.display = 'none';
        icon.style.transform = 'rotate(0deg)';
      }
    });
  }

  // 데이터 카테고리 토글
  const dataHeader = document.getElementById('data-header');
  const dataSubmenu = document.querySelector('.data-submenu');
  
  if (dataHeader && dataSubmenu) {
    dataHeader.addEventListener('click', function() {
      const icon = this.querySelector('.bi-chevron-down');
      if (dataSubmenu.style.display === 'none' || !dataSubmenu.style.display) {
        dataSubmenu.style.display = 'block';
        icon.style.transform = 'rotate(180deg)';
      } else {
        dataSubmenu.style.display = 'none';
        icon.style.transform = 'rotate(0deg)';
      }
    });
  }
});
