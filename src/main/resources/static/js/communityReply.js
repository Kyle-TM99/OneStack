let isSubmitting = false;

// 드롭다운 토글 함수
function toggleDropdown(element) {
    const dropdownMenu = $(element).siblings('.communityDropdown');
    $('.communityDropdown').not(dropdownMenu).hide();
    dropdownMenu.toggle();
}

$(document).ready(function() {
    $('#replyForm').off('submit').on('submit', function(event) {
        event.preventDefault();

        if (isSubmitting) {
            return false;
        }

        isSubmitting = true;

        $.ajax({
            type: 'POST',
            url: '/community/reply',
            data: JSON.stringify({
                communityBoardNo: $(this).find('input[name="communityBoardNo"]').val(),
                memberNo: $(this).find('input[name="memberNo"]').val(),
                communityReplyContent: $(this).find('textarea[name="communityReplyContent"]').val()
            }),
            contentType: 'application/json',
            success: function(response) {
                $('#replyForm').after(`
        <div class="row mt-4">
            <div class="col-auto dropdown-container communityDropdownContainer">
                <i class="bi bi-three-dots" onclick="toggleDropdown(this)"></i>
                <div class="dropdown-menu communityDropdown" style="display: none;">
                    <a class="dropdown-item communityDropdownItem" href="#">수정하기</a>
                    <form action="#" method="post" style="display:inline;">
                        <input type="hidden" name="communityReplyNo" value="${response.communityReplyNo}"/>
                        <input type="hidden" name="memberNo" value="${response.memberNo}"/>
                        <button type="submit" class="dropdown-item communityDropdownItem">삭제하기</button>
                    </form>
                    <a class="dropdown-item communityDropdownItem" href="#">신고하기</a>
                </div>
            </div>
            <div class="col">
                <div class="card mb-2">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">${response.nickname}</h6>
                        <p class="card-text">${response.communityReplyContent}</p>
                        <div class="d-flex gap-2 text-muted">
                            <small>${response.communityReplyRegDate}</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `);
                $('#replyForm')[0].reset();
            },
            error: function(xhr, status, error) {
                alert('댓글 등록에 실패했습니다. 다시 시도해 주세요.');
            },
            complete: function() {
                isSubmitting = false;
            }
        });
    });

    // 수정하기 클릭 이벤트
    $(document).on('click', '.communityDropdownItem[href="#"]', function(e) {
        e.preventDefault();
        const replyContainer = $(this).closest('.row');
        const replyContent = replyContainer.find('.card-text').text();
        const replyNo = $(this).closest('form').find('input[name="communityReplyNo"]').val();

        // 댓글 내용을 수정 가능한 텍스트 영역으로 변경
        replyContainer.find('.card-text').html(`
            <textarea class="form-control edit-reply">${replyContent}</textarea>
            <div class="mt-2">
                <button class="btn btn-primary btn-sm save-edit" data-reply-no="${replyNo}">저장</button>
                <button class="btn btn-secondary btn-sm cancel-edit">취소</button>
            </div>
        `);
    });

    // 수정 취소
    $(document).on('click', '.cancel-edit', function() {
        const replyContainer = $(this).closest('.row');
        const originalContent = replyContainer.find('textarea').val();
        replyContainer.find('.card-text').text(originalContent);
    });

    // 수정 저장
    $(document).on('click', '.save-edit', function() {
        const replyNo = $(this).data('reply-no');
        const newContent = $(this).closest('.card-text').find('textarea').val();
        const replyContainer = $(this).closest('.row');

        $.ajax({
            type: 'PATCH',
            url: '/community/reply',
            contentType: 'application/json',
            data: JSON.stringify({
                communityReplyNo: replyNo,
                communityReplyContent: newContent
            }),
            success: function(response) {
                replyContainer.find('.card-text').text(newContent);
            },
            error: function() {
                alert('댓글 수정에 실패했습니다.');
            }
        });
    });

    // 삭제하기 클릭 이벤트
    $(document).on('submit', '.communityDropdownContainer form', function(e) {
        e.preventDefault();
        if (!confirm('정말 삭제하시겠습니까?')) return;

        const replyNo = $(this).find('input[name="communityReplyNo"]').val();
        const replyContainer = $(this).closest('.row');

        $.ajax({
            type: 'DELETE',
            url: '/community/reply',
            data: {
                communityReplyNo: replyNo
            },
            success: function() {
                replyContainer.remove();
            },
            error: function() {
                alert('댓글 삭제에 실패했습니다.');
            }
        });
    });

});