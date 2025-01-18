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
        const urlParams = new URLSearchParams(window.location.search);
        const itemNo = parseInt(urlParams.get('itemNo'));

        if (itemNo) {
            window.location.href = `/findPro?itemNo=${itemNo}`;
        }
    });


    /* filter 조건 상수화 */
    const filterInputs = document.querySelectorAll("#categoryFilter input");

    /* 정렬 조건 상수화 */
    const sortSelect = document.getElementById("sortSelect");

    /* filter가 변경될 때마다 applyFilters 함수 실행 */
    filterInputs.forEach((input) => {
        input.addEventListener("change", applyFilters);
    });

    /* 정렬 조건이 변경될 때마다 applyFilters 함수 실행 */
    sortSelect.addEventListener("change", applyFilters);

    /* applyFilters 함수 */
    function applyFilters() {
        const filters = getFilters();
        const urlParams = new URLSearchParams(window.location.search);
        const itemNo = parseInt(urlParams.get('itemNo'));
        const sortValue = $("#sortSelect").val();

        const requestData = {
            filters: filters.appType,
            sort: sortValue,
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

        filters.appType = Array.from(appType).map((input) => {
            // name 속성에서 숫자 부분을 추출 (예: proAnswer1 -> 1)
            const surveyNo = input.name.match(/\d+/)[0];  // 숫자 부분 추출

            // 'pro_answer' + surveyNo 형식으로 field를 설정하고 value를 추가
            return {
                field: `pro_answer${surveyNo}`,  // pro_answer1, pro_answer2 등으로 필드 이름 설정
                value: input.value,  // 선택된 value 값
            };
        });

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
                '    <div class="row card-body" data-url="/proDetail?proNo=' + pro.professional.proNo + '">' +
                '      <div class="col-10">' +
                '        <div>' +
                '          <h5 class="card-title mb-2">' + pro.member.name + '</h5>' +
                '          <span class="card-subtitle text-muted mb-2">' + pro.category.itemTitle + '</span> <br>' +
                '          <span class="badge bg-primary me-2 mb-2">' + pro.member.stackName + '</span>' +
                '          <span class="badge bg-success">' + pro.professional.career + '</span> <br>' +
                '          <span class="card-text">' + pro.professional.selfIntroduction + '</span> <br>' +
                '          <i class="bi bi-star-fill text-warning"></i>' +
                '          <span class="ms-1 mb-1">' + pro.professional.rate + ' (' +  pro.professional.reviewCount + ') </span> <br>' +
                '          <div class="price">평균 가격: <strong>' + pro.professional.averagePrice + '원</strong></div>' +
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

    /* 전문가 상세보기로 이동하는 함수 */
    function navigateToUrl(element) {
        const url = element.getAttribute("data-url");
        if (url) {
            window.location.href = url;
        }
    }

    /* 전문가 카드가 클릭 시 navigateToUrl 함수 실행 */
    document.addEventListener("click", function (event) {
        const target = event.target.closest(".row.card-body");
        // 클릭한 요소가 .row.card-body인지 확인
        if (target) {
            navigateToUrl(target);
        }
    });




});