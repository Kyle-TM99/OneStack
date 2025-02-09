$(document).ready(function() {
    let uploadedImage = null;

    // 각 필드의 유효성 상태를 추적
    const validationState = {
        name: false,
        memberId: false,
        pass: false,
        passwordMatch: false,
        nickname: false,
        email: false,
        phone: false,
        birth: false,
        gender: false,
        address: false
    };

    // 정규식 패턴 정의
    const patterns = {
        name: /^(?:[가-힣]{1,5}|[A-Za-z]{1,5})$/,
        memberId: /^[A-Za-z0-9$!*_&]{4,20}$/,
        password: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/,
        nickname: /^[A-Za-z0-9가-힣$!*_&]{2,20}$/,
        email: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
        phone: /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/
    };

    // 유효성 검사 함수
    const validators = {
        validateInput: function(value, pattern, fieldName) {
            if (!value) {
                return `${fieldName}을(를) 입력해주세요.`;
            }
            if (!pattern.test(value)) {
                return `올바른 ${fieldName} 형식이 아닙니다.`;
            }
            return null;
        },

        checkDuplicate: function(url, field, value, successCallback) {
            $.ajax({
                url: url,
                type: "POST",
                data: { [field]: value },
                success: function(response) {
                    successCallback(response);
                },
                error: function(xhr, status, error) {
                    $(`#${field}Message`).text("서버 오류가 발생했습니다.").css("color", "red");
                    $(`#${field}`).css("border-color", "red");
                    validationState[field] = false;
                }
            });
        }
    };

    // 아이디 실시간 중복 검사
    $("#memberId").on("keyup", function() {
        const memberId = $(this).val();
        const errorMsg = validators.validateInput(memberId, patterns.memberId, "아이디");

        if (errorMsg) {
            $("#memberIdMessage").text(errorMsg).css("color", "red");
            $(this).css("border-color", "red");
            validationState.memberId = false;
            return;
        }
        // 아이디 중복 검사
        validators.checkDuplicate("/checkId", "memberId", memberId, function(response) {
            const isValid = response.available;
            const message = isValid ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다.";
            const color = isValid ? "green" : "red";

            $("#memberIdMessage").text(message).css("color", color);
            $("#memberId").css("border-color", color);
            validationState.memberId = isValid;
        });
    });


    // 닉네임 실시간 중복 검사
    $("#nickname").on("keyup", function() {
        const nickname = $(this).val();
        const errorMsg = validators.validateInput(nickname, patterns.nickname, "닉네임");

        if (errorMsg) {
            $("#nicknameMessage").text(errorMsg).css("color", "red");
            $(this).css("border-color", "red");
            validationState.nickname = false;
            return;
        }

        validators.checkDuplicate("/checkNickname", "nickname", nickname, function(response) {
            const isValid = response.available;
            const message = isValid ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다.";
            const color = isValid ? "green" : "red";

            $("#nicknameMessage").text(message).css("color", color);
            $("#nickname").css("border-color", color);
            validationState.nickname = isValid;
        });
    });


    // 이메일 실시간 중복 검사
    $("#email").on("keyup", function() {
        const email = $(this).val();
        const errorMsg = validators.validateInput(email, patterns.email, "이메일");

        if (errorMsg) {
            $("#emailMessage").text(errorMsg).css("color", "red");
            $(this).css("border-color", "red");
            validationState.email = false;
            return;
        }

        validators.checkDuplicate("/checkEmail", "email", email, function(response) {
            const isValid = response.available;
            const message = isValid ? "사용 가능한 이메일입니다." : "이미 등록된 이메일입니다.";
            const color = isValid ? "green" : "red";

            $("#emailMessage").text(message).css("color", color);
            $("#email").css("border-color", color);
            validationState.email = isValid;
        });
    });

    // 전화번호 실시간 중복 검사
    $("#phone").on("keyup", function() {
        const phone = $(this).val();
        const errorMsg = validators.validateInput(phone, patterns.phone, "전화번호");

        if (errorMsg) {
            $("#phoneMessage").text(errorMsg).css("color", "red");
            $(this).css("border-color", "red");
            validationState.phone = false;
            return;
        }

        validators.checkDuplicate("/checkPhone", "phone", phone, function(response) {
            const isValid = response.available;
            const message = isValid ? "사용 가능한 전화번호입니다." : "이미 등록된 전화번호입니다.";
            const color = isValid ? "green" : "red";

            $("#phoneMessage").text(message).css("color", color);
            $("#phone").css("border-color", color);
            validationState.phone = isValid;
        });
    });

    // 이름 실시간 유효성 검사
    $("#name").on("input", function() {
        const name = $(this).val();
        if (!patterns.name.test(name)) {
            $("#nameMessage").text("한글 혹은 영어로 1~5자까지만 입력 가능합니다.").css("color", "red");
            $(this).css("border-color", "red");
            validationState.name = false;
        } else {
            $("#nameMessage").text("").css("color", "green");
            $(this).css("border-color", "green");
            validationState.name = true;
        }
    });

    // 비밀번호 유효성 검사
    $("#pass").on("input", function() {
        const password = $(this).val();
        let message = "";
        let isValid = false;

        if (!password) {
            message = "비밀번호를 입력해주세요.";
        } else if (password.length < 8) {
            message = "비밀번호는 8자리 이상이어야 합니다.";
        } else {
            const hasLetter = /[A-Za-z]/.test(password);
            const hasNumber = /[0-9]/.test(password);
            const hasSpecial = /[@$!%*#?&]/.test(password);

            if (!hasLetter || !hasNumber || !hasSpecial) {
                message = "비밀번호는 문, 숫자, 특수문자(@$!%*#?&)를 모두 포함해야 합니다.";
            } else {
                message = "안전한 비밀번호입니다.";
                isValid = true;
            }
        }

        $("#pwMessage").text(message).css("color", isValid ? "green" : "red");
        $(this).css("border-color", isValid ? "green" : "red");
        validationState.pass = isValid;

        // 비밀번호 확인 필드 재검사
        if ($("#memberPwCheck").val()) {
            checkPasswordMatch();
        }
    });

    // 비밀번호 확인
    $("#memberPwCheck").on("input", function() {
        checkPasswordMatch();
    });

    function checkPasswordMatch() {
        const password = $("#pass").val();
        const confirmPassword = $("#memberPwCheck").val();

        if (!confirmPassword) {
            $("#pwMatchMessage").text("비밀번호 확인을 입력해주세요.").css("color", "red");
            validationState.passwordMatch = false;
            return;
        }

        if (password !== confirmPassword) {
            $("#pwMatchMessage").text("비밀번호가 일치하지 않습니다.").css("color", "red");
            validationState.passwordMatch = false;
            return;
        }

        if (!validationState.pass) {
            $("#pwMatchMessage").text("먼저 올바른 비밀번호를 입력해주세요.").css("color", "red");
            validationState.passwordMatch = false;
            return;
        }

        $("#pwMatchMessage").text("비밀번호가 일치합니다.").css("color", "green");
        validationState.passwordMatch = true;
    }

    // 전화번호 자동 하이픈 추가 및 유효성 검사
    $("#phone").on("input", function() {
        let number = $(this).val().replace(/[^0-9]/g, "");

        if (number.length > 3) {
            number = number.length > 7
                ? `${number.substr(0,3)}-${number.substr(3,4)}-${number.substr(7,4)}`
                : `${number.substr(0,3)}-${number.substr(3)}`;
        }

        $(this).val(number);

        // 전화번호 형식이 완성되었을 때만 중복 체크
        if (patterns.phone.test(number)) {
            validators.checkDuplicate("/checkPhone", "phone", number, function(response) {
                const isValid = response.available;
                const message = isValid ? "사용 가능한 전화번호입니다." : "이미 등록된 전화번호입니다.";
                const color = isValid ? "green" : "red";

                $("#phoneMessage").text(message).css("color", color);
                $("#phone").css("border-color", color);
                validationState.phone = isValid;
            });
        } else {
            $("#phoneMessage").text("올바른 전화번호 형식이 아닙니다.").css("color", "red");
            $(this).css("border-color", "red");
            validationState.phone = false;
        }
    });

    // 이메일 유효성 검사
    $("#email").on("input", function() {
        const email = $(this).val();

        if (!email) {
            $("#emailMessage").text("이메일을 입력해주세요.").css("color", "red");
            $(this).css("border-color", "red");
            validationState.email = false;
            return;
        }

        if (!patterns.email.test(email)) {
            $("#emailMessage").text("올바른 이메일 형식이 아닙니다.").css("color", "red");
            $(this).css("border-color", "red");
            validationState.email = false;
            return;
        }

        // 이메일 중복 체크
        validators.checkDuplicate("/checkEmail", "email", email, function(response) {
            const isValid = response.available;
            const message = isValid ? "사용 가능한 이메일입니다." : "이미 등록된 이메일입니다.";
            const color = isValid ? "green" : "red";

            $("#emailMessage").text(message).css("color", color);
            $("#email").css("border-color", color);
            validationState.email = isValid;
        });
    });

    // 생년월일 제한 설정
    $(document).ready(function() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        const maxDate = `${year}-${month}-${day}`;

        $("#birth").attr("max", maxDate);

        $("#birth").on("change", function() {
            const selectedDate = new Date($(this).val());
            if (selectedDate > today) {
                alert("올바른 날짜를 선택해주세요.");
                $(this).val('');
                validationState.birth = false;
            } else {
                validationState.birth = true;
            }
        });
    });

    // 회원가입 폼 제출 전 최종 검증
    $("#joinForm").on("submit", function(e) {
        e.preventDefault();

        // 모든 필수 필드 검증
        const validationChecks = [
            { field: 'name', message: '이름을 확인해주세요.' },
            { field: 'memberId', message: '아이디를 확인해주세요.' },
            { field: 'pass', message: '비밀번호를 확인해주세요.' },
            { field: 'passwordMatch', message: '비밀번호가 일치하지 않습니다.' },
            { field: 'nickname', message: '닉네임을 확인해주세요.' },
            { field: 'email', message: '이메일을 확인해주세요.' },
            { field: 'phone', message: '전화번호를 확인해주세요.' },
            { field: 'birth', message: '생년월일을 입력해주세요.' }
        ];

        // 성별 선택 확인
        if (!$("input[name='gender']:checked").val()) {
            alert("성별을 선택해주세요.");
            return false;
        }

        // 주소 입력 확인
        if (!$("#zipcode").val() || !$("#address").val() || !$("#address2").val()) {
            alert("주소를 모두 입력해주세요.");
            return false;
        }

        // 각 필드별 유효성 검사
        for (const check of validationChecks) {
            if (!validationState[check.field]) {
                alert(check.message);
                $(`#${check.field}`).focus();
                return false;
            }
        }

        // 모든 검증을 통과한 경우 폼 제출
        const formData = {
            memberId: $("#memberId").val(),
            pass: $("#pass").val(),
            name: $("#name").val(),
            nickname: $("#nickname").val(),
            email: $("#email").val(),
            phone: $("#phone").val(),
            zipcode: $("#zipcode").val(),
            address: $("#address").val(),
            address2: $("#address2").val(),
            emailGet: $("#emailGet").is(":checked"),
            gender: $("input[name='gender']:checked").val(),
            birth: $("#birth").val()
        };

        $.ajax({
            url: "/join",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function(response) {
                if(response.status === "success") {
                    alert("회원가입이 완료되었습니다.");
                    window.location.href = "/loginForm";
                } else {
                    alert(response.message || "회원가입에 실패했습니다.");
                }
            },
            error: function(xhr, status, error) {
                console.error("Error:", error);
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    // 다음 지도 API
    $("#addressSearchBtn").on("click", function() {
        new daum.Postcode({
            oncomplete: function(data) {
                $("#zipcode").val(data.zonecode);
                $("#address").val(data.roadAddress);
                $("#address2").focus();

                validationState.address = true;
            }
        }).open();
    });

    $("#address2").on("input", function() {
        validationState.address = $(this).val().trim() !== "";
    });

    // 폼 제출 이벤트 핸들러
    $("#memberUpdateForm").on("submit", function(e) {
        e.preventDefault();

        // 소셜 로그인 사용자는 닉네임만 검증
        if ($("#memberId").length === 0) {  // memberId 필드가 없으면 소셜 로그인 사용자
            if (!patterns.nickname.test($("#nickname").val())) {
                alert("닉네임은 영문, 숫자, 한글 2~20자만 가능합니다.");
                $("#nickname").focus();
                return false;
            }
        } else {
            // 일반 회원의 경우 모든 필드 검증
            if (!patterns.memberId.test($("#memberId").val())) {
                alert("아이디는 영문, 숫자, 특수문자 4~20자만 가능합니다.");
                $("#memberId").focus();
                return false;
            }

            if (!patterns.nickname.test($("#nickname").val())) {
                alert("닉네임은 영문, 숫자, 한글 2~20자만 가능합니다.");
                $("#nickname").focus();
                return false;
            }

            if (!patterns.email.test($("#email").val())) {
                alert("이메일 형식이 올바르지 않습니다.");
                $("#email").focus();
                return false;
            }

            if (!patterns.phone.test($("#phone").val())) {
                alert("전화번호 형식이 올바르지 않습니다.");
                $("#phone").focus();
                return false;
            }
        }

        const formData = new FormData(this);

        // 이미지가 변경되었을 경우만 FormData에 추가
        if (uploadedImage) {
            formData.set('profileImage', uploadedImage);
        }

        $.ajax({
            url: '/updateMember',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response && response.success) {
                    alert(response.message);
                    window.location.href = '/main';
                } else {
                    alert(response.message || '회원 정보 수정에 실패했습니다.');
                    window.location.href = '/updateMemberForm';
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('회원 정보 수정 중 오류가 발생했습니다.');
                window.location.href = '/updateMemberForm';
            }
        });
    });

    // 프로필 이미지 변경 및 미리보기 처리
    const profileImageInput = document.getElementById('profileImage');
    if (profileImageInput) {
        profileImageInput.addEventListener('change', function (e) {
            const file = e.target.files[0];
            if (file) {
                uploadedImage = file;  // 선택된 파일을 uploadedImage에 저장
                const reader = new FileReader();
                reader.onload = function (e) {
                    document.getElementById('previewImage').src = e.target.result;
                }
                reader.readAsDataURL(file);
            }
        });
    }

    // 비밀번호 확인 폼 제출 시
    $('#passwordCheckForm').on('submit', function (e) {
        e.preventDefault();
        const password = $('#currentPassword').val();

        $.ajax({
            url: '/ajax/member/checkPassword',
            type: 'POST',
            data: {pass: password},
            success: function (response) {
                if (response.valid) {
                    $('#passwordCheckModal').modal('hide');
                    // 회원 정보 가져와서 프로필 페이지 표시
                    $.ajax({
                        url: '/ajax/member/getMemberInfo',
                        type: 'GET',
                        success: function (memberData) {
                            $('.content-container').html(generateProfileHTML(memberData));
                            // 폼이 로드된 후 표시 애니메이션 적용
                            $('.profile-form-container').fadeIn();
                        },
                        error: function () {
                            alert('회원정보를 불러오는데 실패했습니다.');
                        }
                    });
                } else {
                    $('#passwordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function () {
                $('#passwordError').text('서버 오류가 발생했습니다.');
            }
        });
    });

    // 프로필 이미지 변경 시 미리보기
    $(document).on('change', '#profileImage', function (e) {
        const file = e.target.files[0];
        if (file) {
            uploadedImage = file;  // 이미지가 선택되었을 때만 uploadedImage 업데이트
            const reader = new FileReader();
            reader.onload = function (e) {
                $('.profile-image-container').attr('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    });

    // 모달이 자동으로 열리지 않도록 초기화
    $('#changePasswordModalShow').modal('hide'); // 페이지 로드 시 모달 숨기기

    // 비밀번호 변경 버튼 클릭 시 모달 열기
    $('#changePasswordBtnShow').on('click', function () {
        $('#changePasswordModalShow').modal('show');
    });

    // 비밀번호 변경 버튼 클릭 시 모달 오픈
    $(document).on('click', '#changePasswordBtnShow', function () {
        $('#changePasswordModalShow').modal('show');
    });

    // 새 비밀번호 유효성 검사
    $(document).on('input', '#newPasswordShow', function () {
        const password = $(this).val().trim();
        let isValid = patterns.password.test(password);

        if (isValid) {
            $(this).removeClass('is-invalid').addClass('is-valid');
            $('#newPasswordErrorShow').text('').hide();
        } else {
            $(this).removeClass('is-valid').addClass('is-invalid');
            $('#newPasswordErrorShow').text('비밀번호는 8~16자의 영문, 숫자, 특수문자를 포함해야 합니다.').show();
        }

        validatePasswordConfirmation();
    });

    // 비밀번호 확인 검증
    $(document).on('input', '#confirmPasswordShow', function () {
        validatePasswordConfirmation();
    });

    function validatePasswordConfirmation() {
        const currentPassword = $('#currentPasswordShow').val().trim();
        const newPassword = $('#newPasswordShow').val().trim();
        const confirmPassword = $('#confirmPasswordShow').val().trim();

        const isCurrentPasswordFilled = currentPassword.length > 0;
        const isNewPasswordValid = patterns.password.test(newPassword);
        const isPasswordMatching = newPassword === confirmPassword;

        // 모든 조건 확인
        if (isCurrentPasswordFilled && isNewPasswordValid && isPasswordMatching) {
            $('#currentPasswordShow').removeClass('is-invalid').addClass('is-valid');
            $('#newPasswordShow').removeClass('is-invalid').addClass('is-valid');
            $('#confirmPasswordShow').removeClass('is-invalid').addClass('is-valid');

            $('#currentPasswordErrorShow, #newPasswordErrorShow, #confirmPasswordErrorShow')
                .text('').hide();

            $('#savePasswordBtnShow').prop('disabled', false);
        } else {
            // 각 입력 필드에 대한 개별 검증
            if (!isCurrentPasswordFilled) {
                $('#currentPasswordShow').addClass('is-invalid');
                $('#currentPasswordErrorShow').text('현재 비밀번호를 입력해주세요.').show();
            }

            if (!isNewPasswordValid) {
                $('#newPasswordShow').addClass('is-invalid');
                $('#newPasswordErrorShow').text('비밀번호는 8~16자의 영문, 숫자, 특수문자를 포함해야 합니다.').show();
            }

            if (!isPasswordMatching) {
                $('#confirmPasswordShow').addClass('is-invalid');
                $('#confirmPasswordErrorShow').text('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.').show();
            }

            $('#savePasswordBtnShow').prop('disabled', true);
        }
    }

    // 각 입력 필드에 이벤트 리스너 추가
    $(document).on('input', '#currentPasswordShow, #newPasswordShow, #confirmPasswordShow', function () {
        validatePasswordConfirmation();
    });

    // 비밀번호 변경 버튼 클릭 이벤트
    $(document).on('click', '#savePasswordBtnShow', function (e) {
        e.preventDefault();

        const currentPassword = $('#currentPasswordShow').val().trim();
        const newPassword = $('#newPasswordShow').val().trim();
        const confirmPassword = $('#confirmPasswordShow').val().trim();

        // 모든 필드 검증
        if (!currentPassword || !newPassword || !confirmPassword) {
            alert('모든 필드를 입력해주세요.');
            return;
        }

        // 새 비밀번호 유효성 검사
        if (!patterns.password.test(newPassword)) {
            alert('새 비밀번호는 8~16자의 영문, 숫자, 특수문자를 포함해야 합니다.');
            return;
        }

        // 새 비밀번호 확인 검사
        if (newPassword !== confirmPassword) {
            alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
            return;
        }

        // 서버에 비밀번호 변경 요청
        $.ajax({
            url: '/ajax/member/changePassword',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                currentPasswordShow: currentPassword,
                newPasswordShow: newPassword,
                confirmPasswordShow: confirmPassword
            }),
            success: function (response) {
                alert(response.message);
                $('#changePasswordModalShow').modal('hide');
                // 모달 초기화
                $('#currentPasswordShow, #newPasswordShow, #confirmPasswordShow').val('');
                $('#currentPasswordShow, #newPasswordShow, #confirmPasswordShow')
                    .removeClass('is-valid is-invalid');
                $('#newPasswordErrorShow, #confirmPasswordErrorShow').text('').hide();
            },
            error: function (xhr) {
                const errorMessage = xhr.responseJSON ?
                    xhr.responseJSON.message :
                    '비밀번호 변경 중 오류가 발생했습니다.';
                alert(errorMessage);
            }
        });
    });

    // 모달 닫힐 때 초기화
    $('#changePasswordModalShow').on('hidden.bs.modal', function () {
        // 입력 필드 초기화
        $('#currentPasswordShow, #newPasswordShow, #confirmPasswordShow').val('');

        // 유효성 검사 클래스 제거
        $('#currentPasswordShow, #newPasswordShow, #confirmPasswordShow')
            .removeClass('is-valid is-invalid');

        // 에러 메시지 숨기기
        $('#newPasswordErrorShow, #confirmPasswordErrorShow').text('').hide();

        // 저장 버튼 비활성화
        $('#savePasswordBtnShow').prop('disabled', true);
    });

    // 매칭 처리 함수
    function handleMatching(estimationNo) {
        if (confirm('해당 견적을 매칭하시겠습니까?')) {
            $.ajax({
                url: '/ajax/member/matchEstimation',
                type: 'POST',
                data: {estimationNo: estimationNo},
                success: function (response) {
                    alert('매칭이 완료되었습니다.');
                    // 견적 목록 새로고침
                    loadContent('Request');
                },
                error: function () {
                    alert('매칭 처리 중 오류가 발생했습니다.');
                }
            });
        }
    }

    // 채팅 시작 함수
    function startChat(estimationNo) {
        if (confirm('해당 견적을 매칭하고 채팅을 시작하시겠습니까?')) {
            $.ajax({
                url: '/ajax/member/startChat',
                type: 'POST',
                data: {estimationNo: estimationNo},
                success: function (response) {
                    if (response.success) {
                        // 채팅 페이지로 이동
                        window.location.href = `/chat/room/${response.roomId}`;
                    } else {
                        alert(response.message || '채팅 시작에 실패했습니다.');
                    }
                },
                error: function () {
                    alert('채팅 시작 중 오류가 발생했습니다.');
                }
            });
        }
    }
});