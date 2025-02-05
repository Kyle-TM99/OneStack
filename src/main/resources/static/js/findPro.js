/* 카테고리 상수화 */
const categorySelect = document.getElementById("categorySelect");
/* 카테고리 변경 시 함수 실행 */
categorySelect.addEventListener("change", changeCategory);
/* 카테고리 변경 함수 */
function changeCategory() {
    const itemNo = categorySelect.value; // value()가 아니라 value로 접근
    if (itemNo) {
        window.location.href = `/findPro?itemNo=${itemNo}`;
    }
}


/* reset 버튼 상수화 */
const resetCategory = document.getElementById("resetCategory");
/* reset 버튼 클릭 시 함수 실행 */
resetCategory.addEventListener("click", resetForm);
/* reset 함수 */
function resetForm() {
    const urlParams = new URLSearchParams(window.location.search);
    const itemNo = parseInt(urlParams.get('itemNo'));
    if(itemNo) {
        window.location.href = `/findPro?itemNo=${itemNo}`;
    }
}


/* filter 조건 상수화 */
const filterInputs = document.querySelectorAll("#categoryFilter input");
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

    console.log("필터 : " + filters.appType);

    return filters;
}

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
    const filters = getFilters(); // 필터 조건
    const sortValue = $("#sortSelect").val(); // 정렬 조건 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const itemNo = parseInt(urlParams.get("itemNo")); // item 번호 가져오기

    currentPage = 1; // 페이지 초기화
    isLoading = true; // 로딩 상태 활성화

    const requestData = {
        filters: filters.appType, // 필터 조건
        sort: sortValue, // 정렬 조건
        itemNo: itemNo, // 아이템 번호
        page: currentPage, // 현재 페이지
        size: pageSize, // 페이지 크기
    };

    console.log("필터 적용 요청 데이터: ", requestData);

    fetch("/proFilter", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData), // 요청 데이터 전달
    })
        .then((response) => response.json())
        .then((response) => {
            // 초기화 처리
            const resultContainer = document.getElementById("proListContainer");
            resultContainer.innerHTML = ""; // 기존 리스트 제거
            initialProNos.clear(); // 중복된 전문가 ID 초기화

            updateResults(response.pros, response.overallAveragePrice);
            currentPage++; // 다음 페이지 준비
            isLoading = false; // 로딩 상태 해제
        })
        .catch((error) => {
            console.error("필터 적용 중 오류 발생:", error);
            isLoading = false;
        });
}

/* 현재 페이지와 로딩 상태를 관리하는 변수 */
let currentPage = 1;
const pageSize = 5; // 한 번에 가져올 데이터 수
let isLoading = false; // 로딩 상태
let initialProNos = new Set(); // 중복 방지용 전문가 번호 저장하기 위한 Set

/* 페이지 로드 이벤트 연결 */
document.addEventListener("DOMContentLoaded", initialLoad);

/* 처음 페이지 로드시 itemNo에 따른 전문가 리스트 출력 - 무한스크롤용 */
function initialLoad() {
    const filters = getFilters(); // 초기 필터 조건
    const urlParams = new URLSearchParams(window.location.search);
    const itemNo = parseInt(urlParams.get("itemNo")); // URL에서 item 번호 가져오기
    const sortValue = $("#sortSelect").val(); // 정렬 조건 가져오기

    const requestData = {
        filters: filters.appType,
        sort: sortValue,
        itemNo: itemNo,
        page: currentPage,
        size: pageSize,
    };

    console.log("초기 로드 요청 데이터:", requestData);

    fetch("/proFilter", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
    })
        .then((response) => response.json())
        .then((response) => {
            // 초기 로드 시 기존 데이터를 초기화
            const resultContainer = document.getElementById("proListContainer");
            resultContainer.innerHTML = ""; // 초기화
            initialProNos.clear(); // 중복 데이터 관리 초기화

            updateResults(response.pros, response.overallAveragePrice);
            currentPage++; // 다음 페이지 준비
        })
        .catch((error) => console.error("초기 로드 중 오류 발생:", error));
}


/* 처음 로드 or 필터링 선택 시 업데이트 */
function updateResults(pros, overallAveragePrice) {
    currentPage = 1;
    initialProNos.clear(); // 중복 데이터 저장 초기화
    const resultContainer = document.getElementById("proListContainer");
    resultContainer.innerHTML = ""; // 기존 리스트 초기화

    /* 직렬화한 데이터가 배열이 아닌 유사 배열이기에 Array.from()함수 필요 */
    Array.from(pros).forEach((pro) => {
        const proDiv = document.createElement("div");
        proDiv.className = "card h-100 shadow-sm mb-2";
        proDiv.innerHTML =
            '    <div class="row card-body" data-url="/proDetail?proNo=' + pro.professional.proNo + '">' +
            '      <div class="col-10">' +
            '        <div>' +
            '          <h5 class="card-title mb-2">' + pro.member.name + '</h5>' +
            '          <span class="card-subtitle text-muted mb-2">' + pro.category.itemTitle + '</span> <br>' +
            '          <span class="badge bg-success">' + pro.professional.career + '</span> <br>' +
            '          <span class="card-text">' + pro.professional.selfIntroduction + '</span> <br>' +
            '          <i class="bi bi-star-fill text-warning"></i>' +
            '          <span class="ms-1 mb-1">' + pro.professional.rate + ' (' + pro.professional.reviewCount + ') </span> <br>' +
            '          <div class="price">평균 가격: <strong>' + pro.professional.averagePrice + '원</strong></div>' +
            '        </div>' +
            '      </div>' +
            '      <div class="col-2 align-items-center">' +
            '        <div class="row ms-1 mb-4">' +
            '          <img src="' + pro.member.memberImage + '" class="rounded-circle" ' +
            '               style="width: 100px; height: 100px;" alt="프로필">' +
            '        </div>' +
            '        <div class="row">' +
            '          <button type="button" class="btn custom-button" id = "btnEstimation" data-pro-no="' + pro.professional.proNo + '">견적 요청</button>' +
            '        </div>' +
            '      </div>' +
            '    </div>';
        resultContainer.appendChild(proDiv);


        /* 모든 카드 전문가 상세보기로 연결 */
        const cards = document.querySelectorAll(".row.card-body");
        cards.forEach((card) => {
            // 카드 클릭 이벤트
            card.addEventListener("click", function (event) {
                // 견적요청 버튼을 클릭한 경우에는 동작하지 않도록 예외 처리
                const isEstimationButton = event.target.closest(".btnEstimation");
                if (!isEstimationButton) {
                    navigateToUrl(card);
                }
            });
        });

        /* 전문가 상세보기로 이동하는 함수 */
        function navigateToUrl(element) {
            const url = element.getAttribute("data-url");
            if (url) {
                window.location.href = url;
            }
        }

    });

    const averagePriceContainer = document.getElementById("overallAveragePrice");
    averagePriceContainer.textContent = `전체 평균 가격: ${overallAveragePrice}원`;
    console.log(averagePriceContainer.textContent);
}




/* 감지 대상 */
const observerTarget = document.querySelector('.target');
const loadingSpinner = document.getElementById('loadingSpinner');

/* observer 초기화 */
const observeIntersection = (target, callback) => {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                callback();
            }
        });
    });
    observer.observe(target);
}

/* 콜백함수 */
function callNextList() {
    if (isLoading) return; // 이미 로딩 중이면 중복 요청 방지
    isLoading = true;

    // 로딩 스피너 보이기
    loadingSpinner.style.display = 'block';

    setTimeout(() => {
        const filters = getFilters(); // 필터링 조건 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        const itemNo = parseInt(urlParams.get('itemNo')); // URL의 itemNo 가져오기
        const sortValue = sortSelect.value; // 정렬 조건 가져오기

        const requestData = {
            filters: filters.appType, // 필터 정보
            sort: sortValue, // 정렬 정보
            itemNo: itemNo, // item 번호
            page: currentPage, // 현재 페이지 번호
            size: pageSize, // 페이지 크기
        };

        console.log("Fetching data for next page: ", requestData);

        fetch("/proFilter", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((response) => {
                if (response && response.pros) {
                    addResults(response.pros, response.overallAveragePrice);

                    // 다음 페이지를 위한 변수를 업데이트
                    if (response.hasMore) {
                        currentPage++;
                    } else {
                        console.log("더 이상 데이터가 없습니다."); // 추가 데이터 없음 표시
                    }
                }
                isLoading = false; // 로딩 상태 해제

                // 로딩 스피너 숨기기
                loadingSpinner.style.display = 'none';
            })
            .catch((error) => {
                console.error("Error fetching next list:", error);
                isLoading = false;

                // 로딩 스피너 숨기기
                loadingSpinner.style.display = 'none';
            });
    }, 800); // 1초(800ms) 후 실행
}


/* observer 초기화 */
observeIntersection(observerTarget, callNextList);

/* addResults 함수 */
function addResults(pros, overallAveragePrice) {
    const resultContainer = document.getElementById("proListContainer");

    // 현재 화면에 렌더링된 전문가 번호를 수집 (중복 방지용)
    document.querySelectorAll("#proListContainer .card").forEach(card => {
        const url = card.getAttribute("data-url");
        if (url) {
            const proNo = url.match(/proNo=(\d+)/)[1];
            initialProNos.add(Number(proNo)); // 기존 데이터 Set에 추가
        }
    });

    /* 직렬화한 데이터가 배열이 아닌 유사 배열이기에 Array.from()함수 필요 */
    Array.from(pros).forEach((pro) => {
        if (initialProNos.has(pro.professional.proNo)) {
            console.log(`중복된 데이터 발견: 전문가 번호 ${pro.professional.proNo}`);
            return; // 중복된 데이터는 렌더링하지 않음
        }

        initialProNos.add(pro.professional.proNo);

        const proDiv = document.createElement("div");
        proDiv.className = "card h-100 shadow-sm mb-2";
        proDiv.innerHTML =
            '    <div class="row card-body" data-url="/proDetail?proNo=' + pro.professional.proNo + '">' +
            '      <div class="col-10">' +
            '        <div>' +
            '          <h5 class="card-title mb-2">' + pro.member.name + '</h5>' +
            '          <span class="card-subtitle text-muted mb-2">' + pro.category.itemTitle + '</span> <br>' +
            '          <span class="badge bg-success">' + pro.professional.career + '</span> <br>' +
            '          <span class="card-text">' + pro.professional.selfIntroduction + '</span> <br>' +
            '          <i class="bi bi-star-fill text-warning"></i>' +
            '          <span class="ms-1 mb-1">' + pro.professional.rate + ' (' + pro.professional.reviewCount + ') </span> <br>' +
            '          <div class="price">평균 가격: <strong>' + pro.professional.averagePrice + '원</strong></div>' +
            '        </div>' +
            '      </div>' +
            '      <div class="col-2 align-items-center">' +
            '        <div class="row ms-1 mb-4">' +
            '          <img src="' + pro.member.memberImage + '" class="rounded-circle" ' +
            '               style="width: 100px; height: 100px;" alt="프로필">' +
            '        </div>' +
            '        <div class="row">' +
            '          <button type="button" class="btn custom-button" id = "btnEstimation" data-pro-no="' + pro.professional.proNo + '">견적 요청</button>' +
            '        </div>' +
            '      </div>' +
            '    </div>';
        resultContainer.appendChild(proDiv);

        /* 모든 카드 전문가 상세보기로 연결 */
        const cards = document.querySelectorAll(".row.card-body");
        cards.forEach((card) => {
            // 카드 클릭 이벤트
            card.addEventListener("click", function (event) {
                // 견적요청 버튼을 클릭한 경우에는 동작하지 않도록 예외 처리
                const isEstimationButton = event.target.closest(".btnEstimation");
                if (!isEstimationButton) {
                    navigateToUrl(card);
                }
            });
        });

        /* 전문가 상세보기로 이동하는 함수 */
        function navigateToUrl(element) {
            const url = element.getAttribute("data-url");
            if (url) {
                window.location.href = url;
            }
        }

        const averagePriceContainer = document.getElementById("overallAveragePrice");
        averagePriceContainer.textContent = `전체 평균 가격: ${overallAveragePrice}원`;
        console.log(averagePriceContainer.textContent);
    });
}
