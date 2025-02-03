window.onload = function () {
    console.log("📌 페이지네이션 스크립트 실행됨!");

    const rowsPerPage = 20; // 한 페이지당 20개 출력
    const tableBody = document.getElementById("memberTableBody");
    const pagination = document.getElementById("pagination");

    if (!tableBody || !pagination) {
        console.error("❌ 테이블 또는 페이지네이션 요소를 찾을 수 없습니다.");
        return;
    }

    const rows = Array.from(tableBody.getElementsByTagName("tr"));

    if (rows.length === 0) {
        console.warn("⚠️ 테이블에 데이터가 없습니다.");
        return;
    }

    const totalPages = Math.ceil(rows.length / rowsPerPage);
    console.log(`✅ 총 ${rows.length}개의 행이 감지됨. 총 ${totalPages} 페이지.`);

    function showPage(page) {
        tableBody.innerHTML = ""; // 기존 데이터 초기화
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        rows.slice(start, end).forEach(row => tableBody.appendChild(row));

        updatePagination(page);
    }

    function updatePagination(activePage) {
        pagination.innerHTML = ""; // 기존 페이지네이션 초기화

        let prevButton = document.createElement("li");
        prevButton.className = `page-item ${activePage === 1 ? "disabled" : ""}`;
        prevButton.innerHTML = `<a class="page-link" href="#">«</a>`;
        prevButton.addEventListener("click", function (e) {
            e.preventDefault();
            if (activePage > 1) showPage(activePage - 1);
        });
        pagination.appendChild(prevButton);

        for (let i = 1; i <= totalPages; i++) {
            let li = document.createElement("li");
            li.className = `page-item ${i === activePage ? "active" : ""}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            li.addEventListener("click", function (e) {
                e.preventDefault();
                showPage(i);
            });
            pagination.appendChild(li);
        }

        let nextButton = document.createElement("li");
        nextButton.className = `page-item ${activePage === totalPages ? "disabled" : ""}`;
        nextButton.innerHTML = `<a class="page-link" href="#">»</a>`;
        nextButton.addEventListener("click", function (e) {
            e.preventDefault();
            if (activePage < totalPages) showPage(activePage + 1);
        });
        pagination.appendChild(nextButton);
    }

    showPage(1); // 첫 페이지 로드
};
