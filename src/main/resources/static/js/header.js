document.addEventListener('DOMContentLoaded', function() {
  // 메뉴와 서브메뉴 요소 선택
  const leftMenu = document.querySelector('.left-menu');
  const submenuContainer = document.querySelector('.submenu-container');
  const navLinks = document.querySelectorAll('.left-menu .nav-link');
  const categoryHeaders = document.querySelectorAll('.category-header');

  // 메뉴 호버 이벤트
  if (leftMenu && submenuContainer) {
    // 메뉴에 마우스 진입 시
    leftMenu.addEventListener('mouseenter', () => {
      submenuContainer.classList.add('show');
    });

    // 메뉴에서 마우스 이탈 시
    leftMenu.addEventListener('mouseleave', (e) => {
      // 서브메뉴로 이동하는 경우가 아니라면 숨김
      if (!e.relatedTarget || !e.relatedTarget.closest('.submenu-container')) {
        submenuContainer.classList.remove('show');
      }
    });

    // 서브메뉴 영역 이벤트
    submenuContainer.addEventListener('mouseenter', () => {
      submenuContainer.classList.add('show');
    });

    submenuContainer.addEventListener('mouseleave', () => {
      submenuContainer.classList.remove('show');
    });
  }

  // 카테고리 토글 이벤트
  categoryHeaders.forEach(header => {
    const title = header.querySelector('h6');
    const submenu = header.querySelector('div[class$="-submenu"]');
    const icon = header.querySelector('.bi-chevron-down');

    if (title && submenu) {
      title.addEventListener('click', () => {
        // 다른 모든 서브메뉴 닫기
        categoryHeaders.forEach(otherHeader => {
          if (otherHeader !== header) {
            const otherSubmenu = otherHeader.querySelector('div[class$="-submenu"]');
            const otherIcon = otherHeader.querySelector('.bi-chevron-down');
            if (otherSubmenu) {
              otherSubmenu.style.display = 'none';
              if (otherIcon) otherIcon.style.transform = 'rotate(0deg)';
            }
          }
        });

        // 현재 서브메뉴 토글
        const isVisible = submenu.style.display === 'block';
        submenu.style.display = isVisible ? 'none' : 'block';
        if (icon) {
          icon.style.transform = isVisible ? 'rotate(0deg)' : 'rotate(180deg)';
        }
      });
    }
  });
});
