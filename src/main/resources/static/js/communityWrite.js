function quilljsediterInit(){
    var option = {
        modules: {
            toolbar: [
                [{header: [1,2,false] }],
                ['bold', 'italic', 'underline'],
                ['image', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }]
            ]
        },
        placeholder: '자세한 내용을 입력해 주세요!',
        theme: 'snow'
    };

    quill = new Quill('#editor', option);
    quill.on('text-change', function() {
        document.getElementById("quill_html").value = quill.root.innerHTML;
    });

    quill.getModule('toolbar').addHandler('image', function () {
        selectLocalImage();
    });
}

/* 이미지 콜백 함수 */

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    console.log("input.type " + fileInput.type);

    fileInput.click();

    fileInput.addEventListener("change", function () {  // change 이벤트로 input 값이 바뀌면 실행
        const formData = new FormData();
        const file = fileInput.files[0];
        formData.append('uploadFile', file);

        $.ajax({
            type: 'post',
            enctype: 'multipart/form-data',
            url: '/board/register/imageUpload',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: function (data) {
                const range = quill.getSelection(); // 사용자가 선택한 에디터 범위
                data.uploadPath = data.uploadPath.replace(/\\/g, '/');
                quill.insertEmbed(range.index, 'image', "/board/display?fileName=" + data.uploadPath +"/"+ data.uuid +"_"+ data.fileName);

            },
            error: function (err) {
                console.log(err);
            }
        });

    });
}

quilljsediterInit();

document.addEventListener("DOMContentLoaded", function() {

    $("#updateForm").on("submit", function() {
        if($("#communityBoardTitle").val().length <= 0) {
            alert("제목이 입력되지 않았습니다.\n제목을 입력해주세요");
            $("#communityBoardTitle").focus();
            return false;
        }
        if($("#communityBoardContent").val().length <= 0) {
            alert("내용이 입력되지 않았습니다.\n내용을 입력해주세요");
            $("#communityBoardContent").focus();
            return false;
        }
    });


    // 게시글 쓰기 폼 유효성 검사
    const communityWriteForm = document.getElementById("communityWriteForm");

    communityWriteForm.addEventListener("submit", function(event) {
        // 제목 유효성 검사
        const communityBoardTitle = document.getElementById("communityBoardTitle");
        if (communityBoardTitle.value.trim().length <= 0) {
            alert("제목이 입력되지 않았습니다.\n제목을 입력해주세요");
            communityBoardTitle.focus();
            event.preventDefault(); // 폼 제출을 중단
            return;
        }
        // 제목 유효성 검사
        const communityBoardContent = document.getElementById("communityBoardContent");
        if (communityBoardContent.value.trim().length <= 0) {
            alert("내용이 입력되지 않았습니다.\n내용을 입력해주세요");
            communityBoardTitle.focus();
            event.preventDefault(); // 폼 제출을 중단
            return;
        }
    });

    document.getElementById("detailUpdate").addEventListener("click", function() {
        // communityCheckForm의 action과 method 속성 설정
        var communityCheckForm = document.getElementById("communityCheckForm");
        communityCheckForm.setAttribute("action", "updateForm");
        communityCheckForm.setAttribute("method", "post");

        // 폼 제출
        communityCheckForm.submit();
    });
});

// 드롭다운 토글 함수를 전역 함수로 정의
function toggleDropdown(element) {
    // 클릭된 요소의 다음 형제 요소(드롭다운 메뉴)의 표시 상태 토글
    const dropdownMenu = element.nextElementSibling;

    // 모든 드롭다운 메뉴 닫기
    const allDropdownMenus = document.querySelectorAll('.communityDropdown');
    allDropdownMenus.forEach(menu => {
        if (menu !== dropdownMenu) {
            menu.style.display = 'none';
        }
    });

    // 현재 드롭다운 메뉴 토글
    dropdownMenu.style.display = dropdownMenu.style.display === 'none' ? 'block' : 'none';
}

// DOMContentLoaded 이벤트 리스너 추가
document.addEventListener('DOMContentLoaded', function() {
    // 문서 전체에 클릭 이벤트 리스너 추가
    document.addEventListener('click', function(event) {
        const dropdownContainers = document.querySelectorAll('.communityDropdownContainer');

        dropdownContainers.forEach(container => {
            const dropdownMenu = container.querySelector('.communityDropdown');

            // 클릭된 요소가 드롭다운 컨테이너 내부가 아니라면 드롭다운 메뉴 닫기
            if (!container.contains(event.target)) {
                dropdownMenu.style.display = 'none';
            }
        });
    });
});