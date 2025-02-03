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
                communityReplyContent: $(this).find('input[name="communityReplyContent"]').val()
            }),
            contentType: 'application/json',
            success: function(response) {
                const newReply = `
    <div class="row mt-4" id="replyList">
        <div class="card mb-2">
            <div class="card-body">
                <form>
                    <input type="hidden" name="communityReplyNo" value="${response.communityReplyNo}">
                    <input type="hidden" name="communityBoardNo" value="${response.communityBoardNo}">
                    <input type="hidden" name="memberNo" value="${response.memberNo}">
                    <input type="hidden" name="communityReplyActivation" value="1">
                </form>
                <div class="row d-flex justify-content-between">
                    <div class="col-auto">
                        <h6 class="card-subtitle mb-2 text-muted">${response.nickname}</h6>
                    </div>
                    <div class="col-auto dropdown-container communityDropdownContainer communityReplyDelete">
                        <i class="bi bi-three-dots" onclick="toggleDropdown(this)"></i>
                        <div class="dropdown-menu communityDropdown" style="display: none;">
                            <a class="dropdown-item communityDropdownItem" href="#">수정하기</a>
                            <form action="/replyDelete" method="post" style="display:inline;">
                                <input type="hidden" name="communityReplyNo" value="${response.communityReplyNo}"/>
                                <input type="hidden" name="memberNo" value="${response.memberNo}"/>
                                <button type="submit" class="dropdown-item communityDropdownItem">삭제하기</button>
                            </form>
                            <a class="dropdown-item communityDropdownItem" href="#">신고하기</a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <p class="card-text">${response.communityReplyContent}</p>
                </div>
                <div class="row">
                    <div class="d-flex gap-2 text-muted">
                        <small>${response.communityReplyRegDate}</small>
                    </div>
                </div>
            </div>
        </div>
    </div>`;

                // replyForm 다음에 새 댓글 추가
                $('#replyForm').closest('.row.mt-4').after(newReply);

                // 폼 초기화
                $('#replyForm')[0].reset();

                // 댓글 수 업데이트
                const commentCount = $('.bi-chat').next('span');
                if (commentCount.length) {
                    commentCount.text(parseInt(commentCount.text()) + 1);
                }
            },
            error: function(xhr, status, error) {
                alert('댓글 등록에 실패했습니다. 다시 시도해 주세요.');
            },
            complete: function() {
                isSubmitting = false;
            }
        });
    });

    $("#replyForm").on("submit", function() {
        if($("#communityBoardTitle").val().length <= 0) {
            alert("댓글 내용이 입력되지 않았습니다.\n댓글을 입력해주세요");
            $("#communityReplyContent").focus();
            return false;
        }
    });

// 수정하기 클릭 이벤트
    $(document).on('click', '.communityDropdownItem[href="#"]', function(e) {
        e.preventDefault();
        const replyRow = $(this).closest('.row.mt-4');
        const replyContent = replyRow.find('.card-text').text().trim();
        const replyForm = replyRow.find('form').first();

        const replyData = {
            replyNo: replyForm.find('input[name="communityReplyNo"]').val(),
            boardNo: replyForm.find('input[name="communityBoardNo"]').val(),
            memberNo: replyForm.find('input[name="memberNo"]').val()
        };

        // 수정 폼으로 변경
        replyRow.find('.card-text').html(`
        <div class="edit-form">
            <textarea class="form-control mb-2">${replyContent}</textarea>
            <div class="d-flex gap-2">
                <button class="btn btn-primary btn-sm save-reply" 
                        data-reply-no="${replyData.replyNo}"
                        data-board-no="${replyData.boardNo}"
                        data-member-no="${replyData.memberNo}">저장</button>
                <button class="btn btn-secondary btn-sm cancel-reply" 
                        data-original-content="${replyContent}">취소</button>
            </div>
        </div>
    `);

        // 드롭다운 메뉴 닫기
        $('.communityDropdown').hide();
    });

// 수정 취소
    $(document).on('click', '.cancel-reply', function() {
        const originalContent = $(this).data('original-content');
        $(this).closest('.card-text').html(originalContent);
    });

// 수정 저장
    $(document).on('click', '.save-reply', function() {
        const $this = $(this);
        const replyRow = $this.closest('.row.mt-4');
        const newContent = replyRow.find('textarea').val().trim();

        if (!newContent) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

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
                replyRow.find('.card-text').html(newContent);
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    alert('로그인이 필요합니다.');
                } else if (xhr.status === 403) {
                    alert('수정 권한이 없습니다.');
                } else {
                    alert('댓글 수정에 실패했습니다.');
                }
                replyRow.find('.card-text').html($this.closest('.edit-form').find('.cancel-reply').data('original-content'));
            }
        });
    });


    // 삭제 이벤트
    $(document).on('submit', '.communityReplyDelete form', function(e) {
        e.preventDefault();
        if (!confirm('정말 삭제하시겠습니까?')) return;

        const replyNo = $(this).find('input[name="communityReplyNo"]').val();
        const replyRow = $(this).closest('.row.mt-4');

        $.ajax({
            type: 'DELETE',
            url: '/community/reply',
            data: { communityReplyNo: replyNo },
            success: function() {
                replyRow.remove();
                // 댓글 수 업데이트
                const commentCount = $('.bi-chat').next('span');
                if (commentCount.length) {
                    const currentCount = parseInt(commentCount.text());
                    commentCount.text(Math.max(0, currentCount - 1));
                }
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    alert('로그인이 필요합니다.');
                } else {
                    alert('댓글 삭제에 실패했습니다.');
                }
            }
        });
    });

});