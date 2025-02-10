document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        console.log("📌 페이지네이션 스크립트 실행됨!");

        const tableBody = document.getElementById("memberTableBody");
        const pagination = document.getElementById("pagination");

        console.log("📌 tableBody:", tableBody);
        console.log("📌 pagination:", pagination);

        if (!tableBody || !pagination) {
            console.error("❌ 테이블 또는 페이지네이션 요소를 찾을 수 없습니다.");
            return;
        }

        const rows = Array.from(tableBody.getElementsByTagName("tr"));
        if (rows.length === 0) {
            console.warn("⚠️ 테이블에 데이터가 없습니다.");
            return;
        }

        const rowsPerPage = 20;
        const totalPages = Math.ceil(rows.length / rowsPerPage);
        const pagesPerGroup = 10;  // 그룹당 페이지 수
        let currentGroup = 1;      // 현재 그룹 번호
        console.log(`✅ 총 ${rows.length}개의 행이 감지됨. 총 ${totalPages} 페이지.`);

        function showPage(page) {
            tableBody.innerHTML = "";
            const start = (page - 1) * rowsPerPage;
            const end = start + rowsPerPage;
            rows.slice(start, end).forEach(row => tableBody.appendChild(row));

            updatePagination(page);
        }

        function updatePagination(activePage) {
            pagination.innerHTML = "";

            // 이전 그룹 버튼
            let prevGroupButton = document.createElement("li");
            prevGroupButton.className = `page-item ${currentGroup === 1 ? "disabled" : ""}`;
            prevGroupButton.innerHTML = `<a class="page-link" href="#">« 그룹 이전</a>`;
            prevGroupButton.addEventListener("click", function (e) {
                e.preventDefault();
                if (currentGroup > 1) {
                    currentGroup--;
                    showPage((currentGroup - 1) * pagesPerGroup + 1);  // 그룹의 첫 페이지로 이동
                }
            });
            pagination.appendChild(prevGroupButton);

            // 페이지 번호 버튼 (그룹화)
            const startPage = (currentGroup - 1) * pagesPerGroup + 1;
            const endPage = Math.min(currentGroup * pagesPerGroup, totalPages);
            for (let i = startPage; i <= endPage; i++) {
                let li = document.createElement("li");
                li.className = `page-item ${i === activePage ? "active" : ""}`;
                li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
                li.addEventListener("click", function (e) {
                    e.preventDefault();
                    showPage(i);
                });
                pagination.appendChild(li);
            }

            // 다음 그룹 버튼
            let nextGroupButton = document.createElement("li");
            nextGroupButton.className = `page-item ${currentGroup === Math.ceil(totalPages / pagesPerGroup) ? "disabled" : ""}`;
            nextGroupButton.innerHTML = `<a class="page-link" href="#">다음 그룹 »</a>`;
            nextGroupButton.addEventListener("click", function (e) {
                e.preventDefault();
                if (currentGroup < Math.ceil(totalPages / pagesPerGroup)) {
                    currentGroup++;
                    showPage((currentGroup - 1) * pagesPerGroup + 1);  // 그룹의 첫 페이지로 이동
                }
            });
            pagination.appendChild(nextGroupButton);
        }

        showPage(1);
    }, 100);
});
