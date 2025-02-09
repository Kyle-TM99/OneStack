document.addEventListener('DOMContentLoaded', () => {
    const STORAGE_KEY = "current_admin_page";
    const SIDEBAR_STORAGE_KEY = "current_sidebar_menu";

    // 로컬 스토리지에서 마지막 방문한 페이지 불러오기
    let lastVisitedPage = localStorage.getItem(STORAGE_KEY);
    if (!lastVisitedPage) {
        lastVisitedPage = "/members"; // 기본 페이지
    }

    // 마지막 방문 페이지 로드
    loadContent(lastVisitedPage, false);

    // 사이드바 클릭 이벤트 리스너 추가
    document.querySelectorAll('.accordion-body a').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 클릭 동작 방지
            const targetUrl = this.getAttribute('data-url');

            // 클릭한 URL을 로컬 스토리지에 저장
            localStorage.setItem(STORAGE_KEY, targetUrl);

            // 콘텐츠 로드
            loadContent(targetUrl);

            // 사이드바의 열린 메뉴 상태 저장
            const openedMenu = document.querySelector('.accordion-collapse.show');
            if (openedMenu) {
                localStorage.setItem(SIDEBAR_STORAGE_KEY, openedMenu.id);
            }

            // 페이지 리로드
            window.location.reload();  // 페이지 새로 고침
        });
    });

    // 페이지 로드 시 사이드바 상태 복원
    const savedSidebarState = localStorage.getItem(SIDEBAR_STORAGE_KEY);
    if (savedSidebarState) {
        const savedElement = document.getElementById(savedSidebarState);
        if (savedElement) {
            const bsCollapse = new bootstrap.Collapse(savedElement, {
                toggle: true
            });
        }
    }

    // 뒤로가기/앞으로 가기 버튼 지원
    window.addEventListener('popstate', function (event) {
        if (event.state && event.state.url) {
            loadContent(event.state.url, false);
        }
    });
});


// 콘텐츠 로드 함수
function loadContent(url, saveToHistory = true) {
    fetch(url, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    })
    .then(response => response.text())
    .then(html => {
        const contentArea = document.getElementById('dashboard-content');
        if (contentArea) {
            contentArea.innerHTML = html;

            // Bootstrap 및 기타 동적 요소 다시 초기화
            reinitializeScripts();

            // URL은 고정하고, 사용자가 이동한 페이지를 저장
            if (saveToHistory) {
                history.pushState({ url: "/adminPage" }, "", "/adminPage");
            }
        }
    })
    .catch(error => {
        console.error('Error loading content:', error);
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    콘텐츠를 로드할 수 없습니다. (${error.message})
                </div>`;
        }
    });
}

// Bootstrap 및 기타 동적 요소 재초기화 (모달, 드롭다운, 툴팁 등)
function reinitializeScripts() {
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => {
        new bootstrap.Tooltip(el);
    });

    document.querySelectorAll('.modal').forEach(modal => {
        new bootstrap.Modal(modal);
    });

    document.querySelectorAll('.accordion').forEach(acc => {
        new bootstrap.Collapse(acc);
    });

    document.querySelectorAll('.dropdown-toggle').forEach(dropdown => {
        new bootstrap.Dropdown(dropdown);
    });
}
