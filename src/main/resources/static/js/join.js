$(document).ready(function() {
  // 정규식 패턴 정의
  const patterns = {
      name: /^[A-Za-z가-힣]{1,5}$/,  // 1~5자로 제한 (DB VARCHAR(5)에 맞춤)
      memberId: /^[A-Za-z0-9]{4,20}$/,  // 4~20자의 영문, 숫자만 허용
      password: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/,
      nickname: /^[A-Za-z0-9가-힣]{2,10}$/,  // 2~10자의 영문, 한글, 숫자만 허용
      email: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
      phone: /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/
  };

  // 유효성 검사 함수들
  const validators = {
      // 입력값 유효성 검사 함수
      validateInput: function(value, pattern, fieldName) {
          if (!value) {
              return `${fieldName}을(를) 입력해주세요.`;
          }
          if (!pattern.test(value)) {
              return `올바른 ${fieldName} 형식이 아닙니다.`;
          }
          return null;
      },

      // 중복 검사 함수
      checkDuplicate: function(url, field, value, successCallback) {
          $.ajax({
              url: url,
              type: "POST",
              data: { [field]: value },
              success: function(response) {
                  successCallback(response);
              },
              error: function(xhr , status, error) {
                  $(`#${field}Message`).text("서버 오류가 발생했습니다.").css("color", "red");
                  $(`#${field}`).css("border-color", "red").data("valid", false);
				  console.error(error);
              }
          });
      }
  };

  // 주소 검색 이벤트
  $("#addressSearchBtn").click(function() {
      new daum.Postcode({
          oncomplete: function(data) {
              const addr = data.roadAddress;
              const extraAddr = [];
              
              if (data.bname && /[동|로|가]$/g.test(data.bname)) {
                  extraAddr.push(data.bname);
              }
              if (data.buildingName && data.apartment === 'Y') {
                  extraAddr.push(data.buildingName);
              }
              
              const finalAddr = extraAddr.length > 0 
                  ? `${addr} (${extraAddr.join(', ')})` 
                  : addr;

              $("#zipcode").val(data.zonecode);
              $("#address").val(finalAddr);
              $("#address2").focus();
          }
      }).open();
  });

  // 아이디 실시간 중복 검사
  $("#memberId").on("keyup", function() {
      const memberId = $(this).val();
      const errorMsg = validators.validateInput(memberId, patterns.memberId, "아이디");
      
      if (errorMsg) {
          $("#memberIdMessage").text(errorMsg).css("color", "red");
          $(this).css("border-color", "red").data("valid", false);
          return;
      }

      validators.checkDuplicate("/checkId", "memberId", memberId, function(response) {
          const isValid = response.available;
          const message = isValid ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다.";
          const color = isValid ? "green" : "red";
          
          $("#memberIdMessage").text(message).css("color", color);
          $("#memberId").css("border-color", color).data("valid", isValid);
      });
  });

  // 닉네임 실시간 중복 검사
  $("#nickname").on("keyup", function() {
      const nickname = $(this).val();
      const errorMsg = validators.validateInput(nickname, patterns.nickname, "닉네임");
      
      if (errorMsg) {
          $("#nicknameMessage").text(errorMsg).css("color", "red");
          $(this).css("border-color", "red").data("valid", false);
          return;
      }

      validators.checkDuplicate("/checkNickname", "nickname", nickname, function(response) {
          const isValid = response.available;
          const message = isValid ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다.";
          const color = isValid ? "green" : "red";
          
          $("#nicknameMessage").text(message).css("color", color);
          $("#nickname").css("border-color", color).data("valid", isValid);
      });
  });

  // 비밀번호 실시간 유효성 검사 추가
  $("#pass").on("input", function() {
      const password = $(this).val();
      const isValid = patterns.password.test(password);
      
      if (password.length < 8) {
          $("#pwMessage").text("비밀번호는 8자 이상이어야 합니다.").css("color", "red");
          $(this).css("border-color", "red");
      } else if (password.length > 16) {
          $("#pwMessage").text("비밀번호는 16자를 초과할 수 없습니다.").css("color", "red");
          $(this).css("border-color", "red");
      } else if (!isValid) {
          $("#pwMessage").text("영문, 숫자, 특수문자를 모두 포함해야 합니다.").css("color", "red");
          $(this).css("border-color", "red");
      } else {
          $("#pwMessage").text("사용 가능한 비밀번호입니다.").css("color", "green");
          $(this).css("border-color", "green");
      }
  });

  // 기존의 비밀번호 확인 검사 수정
  $("#pass, #memberPwCheck").on("keyup", function() {
      const pw = $("#pass").val();
      const pwCheck = $("#memberPwCheck").val();
      
      if (pw && pwCheck) {
          if (!patterns.password.test(pw)) {
              return;  // 비밀번호가 유효하지 않으면 일치 여부 검사하지 않음
          }
          
          const isMatch = pw === pwCheck;
          const color = isMatch ? "green" : "red";
          const message = isMatch ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다.";
          
          $("#memberPwCheck").css("border-color", color);
          $("#pwMatchMessage").text(message).css("color", color);
      } else {
          $("#memberPwCheck").css("border-color", "");
          $("#pwMatchMessage").text("");
      }
  });

  // 전화번호 형식 자동 변환
  $("#phone").on("input", function() {
      let number = $(this).val().replace(/[^0-9]/g, "").substr(0, 11);
      
      if (number.length > 3) {
          number = number.length > 7 
              ? `${number.substr(0,3)}-${number.substr(3,4)}-${number.substr(7)}`
              : `${number.substr(0,3)}-${number.substr(3)}`;
      }
      
      $(this).val(number);
  });

  // 이름 실시간 유효성 검사 추가
  $("#name").on("input", function() {
      const name = $(this).val();
      const isValid = patterns.name.test(name);
      
      if (name.length > 5) {
          $("#nameMessage").text("이름은 5자를 초과할 수 없습니다.").css("color", "red");
          $(this).css("border-color", "red");
      } else if (!isValid && name.length > 0) {
          $("#nameMessage").text("영문 또는 한글만 입력 가능합니다.").css("color", "red");
          $(this).css("border-color", "red");
      } else {
          $("#nameMessage").text("");
          $(this).css("border-color", "");
      }
  });

  // 전화번호 실시간 중복 검사
  $("#phone").on("keyup", function() {
      const phone = $(this).val();
      const errorMsg = validators.validateInput(phone, patterns.phone, "전화번호");
      
      if (errorMsg) {
          $("#phoneMessage").text(errorMsg).css("color", "red");
          $(this).css("border-color", "red").data("valid", false);
          return;
      }

      if (phone.length >= 12) {  // 전화번호 형식이 완성되었을 때만 중복 체크
          validators.checkDuplicate("/checkPhone", "phone", phone, function(response) {
              const isValid = response.available;
              const message = isValid ? "사용 가능한 전화번호입니다." : "이미 등록된 전화번호입니다.";
              const color = isValid ? "green" : "red";
              
              $("#phoneMessage").text(message).css("color", color);
              $("#phone").css("border-color", color).data("valid", isValid);
          });
      }
  });
  
  
  
  // 생년월일 최대 날짜 설정 (오늘)
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');
      const maxDate = `${year}-${month}-${day}`;
      
      // birth input에 max 속성 설정
      $("#birth").attr("max", maxDate);
      
      // 또는 더 엄격한 제한을 위해 change 이벤트 리스너 추가
      $("#birth").on("change", function() {
          const selectedDate = new Date($(this).val());
          if (selectedDate > today) {
              alert("미래의 날짜는 선택할 수 없습니다.");
              $(this).val('');  // 입력값 초기화
          }
      });
  
  

  // 회원가입 폼 제출
  $("#joinForm").on("submit", function(e) {
      e.preventDefault();
      
      const name = $("#name").val();
      if (name.length > 5) {  // 5자로 제한
          alert("이름은 5자를 초과할 수 없습니다.");
          $("#name").focus();
          return false;
      }

      // 필수 필드 검증
      const requiredFields = {
          zipcode: "우편번호",
          address: "주소",
          address2: "상세주소"
      };

      for (const [field, label] of Object.entries(requiredFields)) {
          if (!$(`#${field}`).val()) {
              alert(`${label}를 입력해주세요.`);
              $(`#${field}`).focus();
              return false;
          }
      }

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
          emailGet: $("#emailConsent").is(":checked") ? true : false,
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
                  alert(response.message);
                  window.location.href = "/loginForm";
              } else {
                  alert(response.message || "회원가입에 실패했습니다.");
              }
          },
          error: function(xhr, status, error) {
              console.error("Error details:", xhr.responseText);
              alert("서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
          }
      });
  });
  
  // 폼 제출 전 모든 필드 유효성 검사
      $("#joinForm").on("submit", function(e) {
          e.preventDefault();
          
          // 필수 필드 유효성 검사
          const requiredFields = {
              name: "이름",
              memberId: "아이디",
              pass: "비밀번호",
              memberPwCheck: "비밀번호 확인",
              nickname: "닉네임",
              birth: "생년월일",
              email: "이메일",
              phone: "전화번호",
              zipcode: "우편번호",
              address: "주소",
              address2: "상세주소"
          };

          // 모든 필드가 유효한지 확인
          let isValid = true;
          
          // 필수 필드 입력 확인
          for (const [field, label] of Object.entries(requiredFields)) {
              const $field = $(`#${field}`);
              if (!$field.val()) {
                  alert(`${label}을(를) 입력해주세요.`);
                  $field.focus();
                  isValid = false;
                  return false;
              }
          }

          // 성별 선택 확인
          if (!$("input[name='gender']:checked").val()) {
              alert("성별을 선택해주세요.");
              isValid = false;
              return false;
          }

          // 각 필드별 유효성 검사 결과 확인
          if (!$("#memberId").data("valid")) {
              alert("아이디 중복 확인이 필요합니다.");
              $("#memberId").focus();
              isValid = false;
              return false;
          }

          if (!$("#nickname").data("valid")) {
              alert("닉네임 중복 확인이 필요합니다.");
              $("#nickname").focus();
              isValid = false;
              return false;
          }

          if (!$("#phone").data("valid")) {
              alert("전화번호 중복 확인이 필요합니다.");
              $("#phone").focus();
              isValid = false;
              return false;
          }

          // 비밀번호 일치 확인
          if ($("#pass").val() !== $("#memberPwCheck").val()) {
              alert("비밀번호가 일치하지 않습니다.");
              $("#memberPwCheck").focus();
              isValid = false;
              return false;
          }

          // 모든 검증을 통과한 경우에만 서버로 전송
          if (isValid) {
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
                  emailGet: $("#emailConsent").is(":checked"),
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
          }
      });
});