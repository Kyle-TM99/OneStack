let isSubmitting = false;

document.addEventListener('DOMContentLoaded', function() {
    const recommendBtns = document.querySelectorAll('.recommend-btn');

    recommendBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const isAuthor = this.getAttribute('data-is-author') === 'true';

            if (isAuthor) {
                alert('본인 게시글에는 추천/비추천을 할 수 없습니다.');
                return;
            }

            const boardNo = this.getAttribute('data-board-no');
            const isLike = this.id === 'communityBoardLike';
            const isActive = this.classList.contains('active');
            const otherBtn = isLike ?
                document.getElementById('communityBoardDislike') :
                document.getElementById('communityBoardLike');

            if (!isActive && otherBtn.classList.contains('active')) {
                alert(`이미 ${isLike ? '비추천' : '추천'}하셨습니다. 취소 후 다시 시도해주세요.`);
                return;
            }

            $.ajax({
                url: '/community/recommend',
                type: 'POST',
                data: {
                    communityBoardNo: boardNo,
                    recommendType: isLike ? 'LIKE' : 'DISLIKE',
                    isCancel: isActive
                },
                success: function(response) {
                    if (response.success) {
                        // 현재 버튼 업데이트
                        const currentIcon = btn.querySelector('i');
                        const currentCount = btn.querySelector('span');

                        btn.classList.toggle('active');
                        if (isLike) {
                            currentIcon.className = btn.classList.contains('active') ?
                                'bi bi-heart-fill text-danger' : 'bi bi-heart';
                            currentCount.textContent = response.likeCount;

                            // 싫어요 버튼 초기화
                            otherBtn.classList.remove('active');
                            otherBtn.querySelector('i').className = 'bi bi-hand-thumbs-down';
                            otherBtn.querySelector('span').textContent = response.dislikeCount;
                        } else {
                            currentIcon.className = btn.classList.contains('active') ?
                                'bi bi-hand-thumbs-down-fill text-primary' : 'bi bi-hand-thumbs-down';
                            currentCount.textContent = response.dislikeCount;

                            // 좋아요 버튼 초기화
                            otherBtn.classList.remove('active');
                            otherBtn.querySelector('i').className = 'bi bi-heart';
                            otherBtn.querySelector('span').textContent = response.likeCount;
                        }
                    }
                },
                error: function(xhr) {
                    if (xhr.status === 401) {
                        alert('로그인이 필요한 기능입니다.');
                        window.location.href = '/login';
                    } else {
                        alert(xhr.responseJSON?.message || '오류가 발생했습니다.');
                    }
                }
            });
        });
    });
});

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
        const replyContent = replyContainer.find('.card-text').text().trim();
        const form = replyContainer.find('.card-body form');
        const replyNo = form.find('input[name="communityReplyNo"]').val();
        const boardNo = form.find('input[name="communityBoardNo"]').val();
        const memberNo = form.find('input[name="memberNo"]').val();

        // 댓글 내용을 수정 가능한 텍스트 영역으로 변경
        replyContainer.find('.card-text').html(`
        <textarea class="form-control edit-reply">${replyContent}</textarea>
        <div class="mt-2">
            <button class="btn btn-primary btn-sm save-edit" 
                    data-reply-no="${replyNo}"
                    data-board-no="${boardNo}"
                    data-member-no="${memberNo}">저장</button>
            <button class="btn btn-secondary btn-sm cancel-edit" 
                    data-original-content="${replyContent}">취소</button>
        </div>
    `);
    });

// 수정 취소
    $(document).on('click', '.cancel-edit', function() {
        const replyContainer = $(this).closest('.row');
        const originalContent = $(this).data('original-content');
        replyContainer.find('.card-text').text(originalContent);
    });

// 수정 저장
    $(document).on('click', '.save-edit', function() {
        const $this = $(this);
        const replyContainer = $this.closest('.row');
        const newContent = replyContainer.find('textarea').val();

        const replyData = {
            communityReplyNo: $this.data('reply-no'),
            communityBoardNo: $this.data('board-no'),
            memberNo: $this.data('member-no'),
            communityReplyContent: newContent
        };

        $.ajax({
            type: 'PATCH',
            url: '/community/reply',
            contentType: 'application/json',
            data: JSON.stringify(replyData),
            success: function(response) {
                replyContainer.find('.card-text').html(newContent);
            },
            error: function(xhr, status, error) {
                console.error('Error:', xhr.responseText);
                alert('댓글 수정에 실패했습니다.');
                // 실패시 원래 내용으로 복구
                const originalContent = replyContainer.find('textarea').val();
                replyContainer.find('.card-text').text(originalContent);
            }
        });
    });

    // 삭제하기 클릭 이벤트
    $(document).on('submit', '.communityReplyDelete form', function(e) {
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