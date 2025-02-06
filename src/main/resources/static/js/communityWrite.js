document.addEventListener("DOMContentLoaded", function() {

    // 삭제 버튼 클릭 이벤트 처리
    document.querySelectorAll('.delete-community-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();

            if (confirm('정말 삭제하시겠습니까?')) {
                const form = this.closest('form');
                form.submit();
            }
        });
    });

    // 폼 유효성 검사 (updateForm)
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
    if (communityWriteForm) {
        communityWriteForm.addEventListener("submit", function(event) {
            // 제목 유효성 검사
            const communityBoardTitle = document.getElementById("communityBoardTitle");
            if (communityBoardTitle.value.trim().length <= 0) {
                alert("제목이 입력되지 않았습니다.\n제목을 입력해주세요");
                communityBoardTitle.focus();
                event.preventDefault(); // 폼 제출을 중단하기
                return;
            }
            // 내용 유효성 검사
            const communityBoardContent = document.getElementById("communityBoardContent");
            if (communityBoardContent.value.trim().length <= 0) {
                alert("내용이 입력되지 않았습니다.\n내용을 입력해주세요");
                communityBoardContent.focus();
                event.preventDefault(); // 폼 제출을 중단
                return;
            }
        });
    }

    // 상세 업데이트 버튼 클릭 이벤트 처리
    const detailUpdateButton = document.getElementById("detailUpdate");
    if (detailUpdateButton) {
        detailUpdateButton.addEventListener("click", function() {
            var communityCheckForm = document.getElementById("communityCheckForm");
            if (communityCheckForm) {
                communityCheckForm.setAttribute("action", "updateForm");
                communityCheckForm.setAttribute("method", "post");
                communityCheckForm.submit();
            }
        });
    }

    document.addEventListener('click', function(event) {
        const dropdownContainers = document.querySelectorAll('.communityDropdownContainer');
        dropdownContainers.forEach(container => {
            const dropdownMenu = container.querySelector('.communityDropdown');
            if (!container.contains(event.target)) {
                dropdownMenu.style.display = 'none';
            }
        });
    });

});
