$(function() {
    /* 종목 변경 */
    document.getElementById("categorySelect").addEventListener("change", function() {
        const itemNo = this.value;

        // 카테고리 변경 시 페이지 리로드
        // 선택된 itemNo를 URL의 쿼리 파라미터로 추가하여 페이지를 리로드
        if (itemNo) {
            window.location.href = `/findPro?itemNo=${itemNo}`;
        }
    });



    /* reset 초기화 버튼*/
    $("#resetCategory").on("click", function() {
        // 정렬 버튼 초기화
        $("#sortSelect").val("");

        // 필터 조건 초기화
        let radios = document.querySelectorAll('input[type="radio"]');
        radios.forEach(function(radio) {
            radio.checked = false;
        });

    });


    /* filter 조건 상수화 */
    const filterInputs = document.querySelectorAll("#categoryFilter input");

    /* filter가 변경될 때마다 applyFilters 함수 실행 */
    filterInputs.forEach((input) => {
        input.addEventListener("change", applyFilters);
    });

    /* applyFilters 함수 */
    function applyFilters() {
        const filters = getFilters();
        const urlParams = new URLSearchParams(window.location.search);
        const itemNo = parseInt(urlParams.get('itemNo'));

        const requestData = {
            filters: filters.appType,
            itemNo: itemNo,
        };

        console.log("Request Data: ", requestData);

        fetch("/proFilter", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((pros) => updateResults(pros)) // 결과 업데이트
            .catch((error) => console.error("Error fetching pros:", error));
    }

    /* filter 조건 가져오기 */
    function getFilters() {
        const filters = {};
        const appType = document.querySelectorAll("input[type='radio']:checked");

        filters.appType = Array.from(appType).map((input, index) => ({
            field: `pro_answer${index + 1}`, // pro_answer1, pro_answer2 등으로 매핑
            value: input.value,
        }));

        console.log(filters.appType);

        return filters;
    }

    /* filter 조건에 따라 리스트 업데이트*/
    function updateResults(pros) {
        const resultContainer = document.getElementById("proListContainer");
        resultContainer.innerHTML = ""; // 기존 결과 초기화

        /* 직렬화한 데이터가 배열이 아닌 유사 배열이기에 Array.from()함수 필요 */
        Array.from(pros).forEach((pro) => {
            const proDiv = document.createElement("div");
            proDiv.className = "card h-100 shadow-sm";
            proDiv.innerHTML =
                '    <div class="row card-body">' +
                '      <div class="col-10">' +
                '        <div>' +
                '          <h5 class="card-title mb-2">' + pro.member.name + '</h5>' +
                '          <span class="card-subtitle text-muted mb-2">' + pro.category.itemTitle + '</span> <br>' +
                '          <span class="badge bg-primary me-2 mb-2">' + pro.member.stackName + '</span>' +
                '          <span class="badge bg-success">' + pro.professional.career + '</span> <br>' +
                '          <span class="card-text">' + pro.professional.selfIntroduction + '</span> <br>' +
                '          <i class="bi bi-star-fill text-warning"></i>' +
                '          <span class="ms-1 mb-1">' + pro.professional.rate + '</span> <br>' +
                '          <div class="price">평균 가격: <strong>' + pro.professional.avaragePrice + '원</strong></div>' +
                '        </div>' +
                '      </div>' +
                '      <div class="col-2 align-items-center">' +
                '        <div class="row ms-1 mb-4">' +
                '          <img src="' + (pro.member.profileImage || 'default-profile.jpg') + '" class="rounded-circle" ' +
                '               style="width: 100px; height: 100px;" alt="프로필">' +
                '        </div>' +
                '        <div class="row">' +
                '          <button type="button" class="btn custom-button">견적 요청</button>' +
                '        </div>' +
                '      </div>' +
                '    </div>';


            resultContainer.appendChild(proDiv);
        });
    }


    /* filter 조건과 sort조건에 따른 리스트 반환 */
    $("#sortSelect").on("change", function () {
        const sortValue = $(this).val(); // 선택한 정렬 기준
        const filters = getFilters(); // 기존 필터 조건
        const urlParams = new URLSearchParams(window.location.search);
        const itemNo = parseInt(urlParams.get('itemNo'));

        // 정렬 요청 데이터
        const requestData = {
            filters: filters.appType,
            sort: sortValue,
            itemNo: itemNo,
        };

        // AJAX 요청
        fetch("/proFilterSort", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData), // 필터 및 정렬 조건 전달
        })
            .then((response) => response.json())
            .then((pros) => updateResults(pros)) // 결과 업데이트
            .catch((error) => console.error("Error fetching sorted pros:", error));
    });









});