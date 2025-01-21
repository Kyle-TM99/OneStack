document.addEventListener('DOMContentLoaded', () => {
    const headers = document.querySelectorAll('.sortable');

    headers.forEach(header => {
        header.addEventListener('click', () => {
            const table = header.closest('table');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.rows);
            const sortKey = header.getAttribute('data-sort');
            const isAscending = header.classList.contains('sort-asc');

            // 정렬
            rows.sort((a, b) => {
                const aText = a.querySelector(`[data-column="${sortKey}"]`).textContent.trim();
                const bText = b.querySelector(`[data-column="${sortKey}"]`).textContent.trim();
                return isAscending
                    ? aText.localeCompare(bText)
                    : bText.localeCompare(aText);
            });

            // 정렬된 행 재배치
            rows.forEach(row => tbody.appendChild(row));

            // 정렬 방향 토글
            headers.forEach(h => h.classList.remove('sort-asc', 'sort-desc'));
            header.classList.toggle('sort-asc', !isAscending);
            header.classList.toggle('sort-desc', isAscending);
        });
    });
});
