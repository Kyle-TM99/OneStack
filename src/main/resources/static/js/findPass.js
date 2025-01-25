$(function() {
    let isSubmitting = false;  // 제출 진행 중 여부를 추적하는 변수
    
    $('#findPassForm').on('submit', function(e) {
        e.preventDefault();
        
        // 이미 제출 중이면 중단
        if (isSubmitting) {
            return;
        }
        
        // 제출 시작을 표시
        isSubmitting = true;
        const $submitButton = $(this).find('button[type="submit"]');
        $submitButton.prop('disabled', true);  // 버튼 비활성화
        
        $.ajax({
            url: '/findPass',
            type: 'POST',
            data: {
                memberId: $('#memberId').val(),
                email: $('#email').val()
            },
            success: function(response) {
                if (response.status === 'success') {
                    alert(response.message);
                    window.location.href = '/loginForm';
                } else {
                    alert(response.message);
                    // 실패시 버튼 다시 활성화
                    isSubmitting = false;
                    $submitButton.prop('disabled', false);
                }
            },
            error: function() {
                alert('비밀번호 찾기 처리 중 오류가 발생했습니다.');
                // 에러시 버튼 다시 활성화
                isSubmitting = false;
                $submitButton.prop('disabled', false);
            }
        });
    });
});
