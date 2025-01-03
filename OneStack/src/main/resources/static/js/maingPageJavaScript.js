$(document).ready(function() {
  const menuElements = {
    findMaster: document.getElementById('findMasterMenu'),
    requestQuote: document.getElementById('requestQuoteMenu'),
    community: document.getElementById('communityMenu'),
    chat: document.getElementById('chatMenu'),
    notification: document.getElementById('notificationMenu')
  };

  const megaMenuWrapper = document.querySelector('.mega-menu-wrapper');
  const megaMenuContainer = document.querySelector('.mega-menu-container');

  function hideAllMegaMenus() {
    megaMenuWrapper.style.opacity = '0';
    megaMenuWrapper.style.visibility = 'hidden';
  }

  function showMegaMenu() {
    megaMenuWrapper.style.opacity = '1';
    megaMenuWrapper.style.visibility = 'visible';
    megaMenuContainer.style.display = 'grid';
  }

  [menuElements.findMaster, menuElements.requestQuote, menuElements.community].forEach(menu => {
    menu.addEventListener('mouseenter', () => {
      if (window.innerWidth >= 992) {
        showMegaMenu();
      }
    });

    menu.addEventListener('mouseleave', (e) => {
      if (!e.relatedTarget?.closest('.mega-menu-wrapper')) {
        hideAllMegaMenus();
      }
    });
  });

  megaMenuWrapper.addEventListener('mouseleave', () => {
    if (window.innerWidth >= 992) {
      hideAllMegaMenus();
    }
  });

  [menuElements.chat, menuElements.notification].forEach(menu => {
    menu.addEventListener('mouseenter', () => {
      if (window.innerWidth >= 992) {
        hideAllMegaMenus();
      }
    });
  });

  function setupModal(triggerId, modalId) {
    $(`#${triggerId}`).on('click', function() {
      const modal = new bootstrap.Modal(document.getElementById(modalId));
      modal.show();
    });
  }

  setupModal('chatMenu', 'chatModal');
  setupModal('notificationMenu', 'notificationModal');

  function adjustModalPosition(modalId, buttonId, offset = 0) {
    const button = $(buttonId);
    const modal = $(`${modalId} .modal-dialog`);
    const rect = button[0].getBoundingClientRect();
    
    modal.css({
      'position': 'fixed',
      'left': offset ? rect.left - offset : rect.left,
      'top': rect.bottom + 5,
      'transform': 'none',
      'margin': '0'
    });
  }

  $('#chatModal').on('show.bs.modal', function() {
    adjustModalPosition('#chatModal', '#chatMenu');
    if ($('#notificationModal').hasClass('show')) {
      adjustModalPosition('#notificationModal', '#notificationMenu', 320);
    }
  });

  $('#notificationModal').on('show.bs.modal', function() {
    adjustModalPosition('#notificationModal', '#notificationMenu');
    if ($('#chatModal').hasClass('show')) {
      adjustModalPosition('#chatModal', '#chatMenu', 320);
    }
  });

  const navbarToggler = document.querySelector('.navbar-toggler');
  
  navbarToggler.addEventListener('click', function() {
    if (window.innerWidth < 992) {
      hideAllMegaMenus();
      megaMenuWrapper.classList.toggle('show');
    }
  });

  window.addEventListener('resize', function() {
    if (window.innerWidth >= 992) {
      megaMenuWrapper.classList.remove('show');
    }
    
    if ($('#chatModal').hasClass('show')) {
      adjustModalPosition('#chatModal', '#chatMenu');
    }
    if ($('#notificationModal').hasClass('show')) {
      adjustModalPosition('#notificationModal', '#notificationMenu');
    }
  });

  const themeToggle = $('#themeToggle');
  const themeIcon = themeToggle.find('i');
  
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'dark') {
    $('body').addClass('dark-mode');
    themeIcon.removeClass('fa-moon').addClass('fa-sun');
  }

  themeToggle.on('click', function() {
    $('body').toggleClass('dark-mode');
    
    if ($('body').hasClass('dark-mode')) {
      themeIcon.removeClass('fa-moon').addClass('fa-sun');
      localStorage.setItem('theme', 'dark');
    } else {
      themeIcon.removeClass('fa-sun').addClass('fa-moon');
      localStorage.setItem('theme', 'light');
    }
  });
});