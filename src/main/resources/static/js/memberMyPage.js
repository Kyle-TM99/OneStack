document.addEventListener("DOMContentLoaded", function() {
	// 프로필 이미지 변경 및 미리보기
	const profileImageInput = document.getElementById('profileImage');
	const previewImage = document.getElementById('previewImage');
	let uploadedImage = null;

	if (profileImageInput) {
		profileImageInput.addEventListener('change', function(e) {
			const file = e.target.files[0];
			if (file) {
				// 미리보기 표시
				const reader = new FileReader();
				reader.onload = function(e) {
					previewImage.src = e.target.result;
				}
				reader.readAsDataURL(file);
				uploadedImage = file;
			}
		});
	}

	// 폼 제출 처리
	const memberUpdateForm = document.getElementById('memberUpdateForm');
	if (memberUpdateForm) {
		memberUpdateForm.addEventListener('submit', function(e) {
			e.preventDefault();

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
				success: function(response) {
					if(response.success) {
						alert('회원 정보가 성공적으로 수정되었습니다.');
						window.location.href = '/updateMemberForm';
					} else {
						alert(response.message || '회원 정보 수정 중 오류가 발생했습니다.');
					}
				},
				error: function(xhr) {
					alert('회원 정보 수정 중 오류가 발생했습니다.');
				}
			});
		});
	}

	// 각 필드의 유효성 상태 추적
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


	// 사이드바 네비게이션 클릭 이벤트 수정
	$('.sidebar .nav-link').click(function(e) {
		e.preventDefault();
		$('.sidebar .nav-link').removeClass('active');
		$(this).addClass('active');

		const contentType = $(this).data('content');

		// 내 활동 클릭시 post 타입으로 시작하도록 수정
		if (contentType === 'activity') {
			loadContent(contentType, 'post');  // post 타입을 기본값으로 전달
		} else {
			loadContent(contentType);
		}
	});

	// 활동 카드의 각 항목 클릭 이벤트
	$('.activity-card span .stats-overview .stat-content').click(function() {
		const type = $(this).data('type');
		$('.sidebar .nav-link').removeClass('active');

		switch (type) {
			case 'profile':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="profile"]').addClass('active');
				loadContent('profile');
				break;
			case 'Request':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="Request"]').addClass('active');
				loadContent('Request');
				break;
			case 'review':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="review"]').addClass('active');
				loadContent('review');
				break;
			case 'post':
			case 'postComment':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="activity"]').addClass('active');
				loadContent('activity', 'post');
				break;
			case 'question':
			case 'questionComment':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="activity"]').addClass('active');
				loadContent('activity', 'question');
				break;
			case 'Empathy':
				$('.sidebar .nav-link[data-content="activity"]').addClass('active');
				loadContent('activity', 'likes');
				break;
		}
	});

	// 컨텐츠 로드 함수
	function loadContent(contentType, subType = null) {
		if (contentType === 'profile') {
			$.ajax({
				url: '/ajax/member/getMemberInfo',
				type: 'GET',
				success: function(memberData) {
					const contentMap = {
						payment: `
                                             <div class="mb-4">
                                                   <div class="expert-list">
                                                         <div class="expert-list-item">
                                                               <div class="stepper-wrapper mb-4">
                                                                     <div class="stepper-item completed">
                                                                           <div class="step-counter">1</div>
                                                                           <div class="step-name">매칭</div>
                                                                     </div>
                                                                     <div class="stepper-item completed">
                                                                           <div class="step-counter">2</div>
                                                                           <div class="step-name">진행</div>
                                                                     </div>
                                                                     <div class="stepper-item active">
                                                                           <div class="step-counter">3</div>
                                                                           <div class="step-name">확정</div>
                                                                     </div>
                                                                     <div class="stepper-item">
                                                                           <div class="step-counter">4</div>
                                                                           <div class="step-name">결제</div>
                                                                     </div>
                                                               </div>
                                                               <div class="expert-info d-flex align-items-center">
                                                                     <img src="/images/default-profile.png" alt="전문가 이미지" class="expert-image">
                                                                     <div class="expert-details flex-grow-1 mx-4">
                                                                           <div class="d-flex align-items-center gap-2 mb-2">
                                                                                 <span class="badge">카테고리</span>
                                                                                 <span class="badge bg-light text-dark">상세 카테고리</span>
                                                                           </div>
                                                                           <h5 class="mb-2">전문가 이름</h5>
                                                                           <p class="text-muted mb-0">전문가 한 줄 소개</p>
                                                                     </div>
                                                                     <div class="expert-action">
                                                                           <a href="#" class="custom-button checkPDF">조율 내용 확인</a>
                                                                     </div>
                                                               </div>
                                                         </div>
                                                   </div>
                                             </div>
                                       `,
					};
					$('.content-container').html(contentMap[contentType]);
				},
				error: function() {
					alert('회원정보를 불러오는데 실패했습니다.');
				}
			});
		}

		if (contentType === 'Request') {
			$.ajax({
				url: '/ajax/member/getMemberRequest',
				type: 'GET',
				dataType: 'json',
				success: function(response) {
					console.log('서버 응답 전체:', response);
					console.log('memberEstimations:', response.memberEstimations);
					console.log('proEstimations:', response.proEstimations);

					let requestHtml = '<div class="mb-4">';

					if (!response || !response.success) {
						requestHtml += `
							<div class="alert alert-danger">
								견적 요청 목록을 불러오는데 실패했습니다.
							</div>`;
					} else {
						// 일반 회원 견적 요청 목록
						if (response.memberEstimations && Array.isArray(response.memberEstimations)) {
							requestHtml += `<h4 class="mb-3">보낸 견적 요청</h4>`;
							if (response.memberEstimations.length === 0) {
								requestHtml += `
									<div class="alert alert-info">
										아직 보낸 견적 요청이 없습니다.
									</div>`;
							} else {
								response.memberEstimations.forEach(estimation => {
									requestHtml += `
									<div class="expert-list">
										<div class="expert-list-item">
											<div class="quote-details mt-4">
												<div class="row">
													<div class="col-9">
														<ul class="list-unstyled">
															<li><strong>견적 번호:</strong> ${estimation.estimationNo}</li>
															<li><strong>회원 이름:</strong> ${estimation.memberName}</li>
															<li><strong>견적 내용:</strong> ${estimation.estimationContent}</li>
															<li><strong>희망 금액:</strong> ${estimation.estimationPrice ? estimation.estimationPrice.toLocaleString() : '0'}원</li>
															<li><strong>기타 건의사항:</strong> ${estimation.estimationMsg || '없음'}</li>
														</ul>
													</div>
													<div class="col-3 d-flex align-items-center"> 
														<div class="expert-action text-center w-100">
															<button class="btn custom-button w-100" 
																	onclick="startChat(${estimation.estimationNo})" 
																	${estimation.estimationIsread ? 'disabled' : ''}>
																${estimation.estimationIsread ? '채팅중' : '매칭하기'}
															</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>`;
								});
							}
						}

						// 전문가 견적 요청 목록
						if (response.proEstimations && Array.isArray(response.proEstimations)) {
							requestHtml += `<h4 class="mb-3 mt-4">받은 견적 요청</h4>`;
							if (!response.proEstimations || response.proEstimations.length === 0) {
								requestHtml += `
									<div class="alert alert-info">
										아직 받은 견적 요청이 없습니다.
									</div>`;
							} else {
								response.proEstimations.forEach(estimation => {
									requestHtml += `
									<div class="expert-list">
										<div class="expert-list-item">
											<div class="quote-details mt-4">
												<div class="row">
													<div class="col-9">
														<ul class="list-unstyled">
															<li><strong>견적 번호:</strong> ${estimation.estimationNo}</li>
															<li><strong>회원 이름:</strong> ${estimation.memberName}</li>
															<li><strong>견적 내용:</strong> ${estimation.estimationContent}</li>
															<li><strong>희망 금액:</strong> ${estimation.estimationPrice ? estimation.estimationPrice.toLocaleString() : '0'}원</li>
															<li><strong>기타 건의사항:</strong> ${estimation.estimationMsg || '없음'}</li>
														</ul>
													</div>
													<div class="col-3 d-flex align-items-center"> 
														<div class="expert-action text-center w-100">
															<button class="btn custom-button w-100" 
																	onclick="startChat(${estimation.estimationNo})" 
																	${estimation.estimationIsread ? 'disabled' : ''}>
																${estimation.estimationIsread ? '채팅중' : '매칭하기'}
															</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>`;
								});
							}
						}
					}

					requestHtml += '</div>';
					$('.content-container').html(requestHtml);
				},
				error: function(xhr, status, error) {
					console.error('Ajax 오류:', {xhr, status, error});
					alert('견적 요청 목록을 불러오는데 실패했습니다.');
				}
			});
		} else if (contentType === 'portfolio') {
			$.ajax({
				url: '/ajax/member/portfolio',
				type: 'GET',
				success: function (portfolio) {
					const contentMap = {
						portfolioHtml: `
                                       <div class="portfolio-list">
                                          <div class="card" style="width: 18rem;">
										  <img src="..." class="card-img-top" alt="전문가 포트폴리오 썸네일">
										  <div class="card-body">
											<h5 class="card-title">포트폴리오 제목</h5>
											<p class="card-text">포트폴리오 내용</p>
											<input type="checkbox" class="btn-check" id="btn-check-2" checked autocomplete="off">
											<label class="btn custom-button2" for="btn-check-2">Checked</label>
										  </div>
										</div>
                                    `,
					};
					$('.content-container').html(contentMap[contentType]);
				},
				error: function() {
					alert('회원정보를 불러오는데 실패했습니다.');
				}
			});
		} else if (contentType === 'review') {
			// 세션에서 현재 로그인한 회원의 번호를 가져오는 AJAX 요청
			$.ajax({
				url: '/ajax/member/getMemberInfo',
				type: 'GET',
				success: function(memberData) {
					$.ajax({
						url: '/ajax/member/myPageReview',
						type: 'GET',
						success: function(response) {
							if (response.success) {
								let reviewHtml = response.data.map(review => `
                                       <div class="review-list">
                                          <div class="card" style="width: 100%; max-width: 100%;">
                                             <div class="list-item mx-5 my-3">
                                             <div class="row mb-2">
                                                              <div class="col-4 category-tags">
                                                                  <span class="badge bg-light text-dark"></span>
                                                              </div>
                                                          </div>
                                                <div class="row mb-3">
                                                   <div class="col-12 rating mb-2">
                                                   ${generateStarRating(review.reviewRate)}
                                                      <span class="ms-2" style="color:#1a1a1a">${review.reviewRate}</span>
                                                   </div>
                                                </div>
                                                <div class="row mb-3">
                                                   <div class="col-12">
                                                      <p class="content">${review.reviewContent || '리뷰 내용 없음'}</p>
                                                   </div>
                                                </div>
                                                <div class="row">
                                                   <div class="col-12 text-end">
                                                      <small class="text-muted">${formatDate(review.reviewDate)}</small>
                                                   </div>
                                                </div>
                                             </div>
                                          </div>
                                       </div>
                                    `).join('');

								// 리뷰가 없는 경우 처리
								if (response.data.length === 0) {
									reviewHtml = `
                                                  <div class="text-center py-5">
                                                      <p class="text-muted">작성된 리뷰가 없습니다.</p>
                                                  </div>
                                              `;
								}

								$('.content-container').html(reviewHtml);
							}
						},
						error: function() {
							alert('리뷰를 불러오는 데 실패했습니다.');
						}
					});
				}
			});
		} else if (contentType === 'activity') {
			fetch(`/ajax/member/myPageActivity?type=${subType || 'post'}`)
				.then(response => {
					if (!response.ok) {
						throw new Error('Network response was not ok');
					}
					return response.json();
				})
				.then(response => {
					console.log('서버 응답:', response);
					if (response.success) {
						let html = '';

						// 탭 메뉴 생성
						html += `
                    <div class="row d-flex mb-5">
                        <div class="nav justify-content-around">
                            <a href="#" class="nav-link" data-content="activity" data-subtype="post">자유글</a>
                            <a href="#" class="nav-link" data-content="activity" data-subtype="question">질문글</a>
                            <a href="#" class="nav-link" data-content="activity" data-subtype="postComment">댓글</a>
                            <a href="#" class="nav-link" data-content="activity" data-subtype="questionComment">답변</a>
                            <a href="#" class="nav-link" data-content="activity" data-subtype="likes">공감</a>
                        </div>
                    </div>
                `;

						// 컨텐츠 생성
						html += '<div class="activity-content">';

						if (!response.data || response.data.length === 0) {
							html += '<p class="text-center my-5">활동 내역이 없습니다.</p>';
						} else {
							if (subType === 'likes') {
								html += generateLikesHTML(response.data);
							} else {
								response.data.forEach(item => {
									switch(subType) {
										case 'post':
											html += generatePostHTML(item);
											break;
										case 'question':
											html += generateQuestionHTML(item);
											break;
										case 'postComment':
											html += generateCommentHTML(item);
											break;
										case 'questionComment':
											html += generateReplyHTML(item);
											break;
									}
								});
							}
						}

						html += '</div>';
						document.querySelector('.content-container').innerHTML = html;

						// 탭 클릭 이벤트 리스너 추가
						document.querySelectorAll('.nav-link').forEach(link => {
							link.addEventListener('click', function(e) {
								e.preventDefault();
								const content = this.getAttribute('data-content');
								const subType = this.getAttribute('data-subtype') || 'post';

								if (content === 'activity') {
									loadContent(content, subType);
								}
							});
						});
					} else {
						console.error('서버 응답 실패:', response.message);
						alert(response.message || '데이터를 불러오는데 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('서버 통신 오류:', error);
					alert('서버 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
				});

		} else if (contentType === 'payment') {
			const paymentTemplate = `
                           <div class="mb-4">
                                 <div class="expert-list">
                                       <div class="expert-list-item">
                                             <div class="stepper-wrapper mb-4">
                                                   <div class="stepper-item completed">
                                                         <div class="step-counter">1</div>
                                                         <div class="step-name">매칭</div>
                                                   </div>
                                                   <div class="stepper-item completed">
                                                         <div class="step-counter">2</div>
                                                         <div class="step-name">진행</div>
                                                   </div>
                                                   <div class="stepper-item active">
                                                         <div class="step-counter">3</div>
                                                         <div class="step-name">확정</div>
                                                   </div>
                                                   <div class="stepper-item">
                                                         <div class="step-counter">4</div>
                                                         <div class="step-name">결제</div>
                                                   </div>
                                             </div>
                                             <div class="expert-info d-flex align-items-center">
                                                   <img src="/images/default-profile.png" alt="전문가 이미지" class="expert-image">
                                                   <div class="expert-details flex-grow-1 mx-4">
                                                         <h5 class="mb-2">전문가 이름</h5>
                                                         <p class="text-muted mb-0">전문가 한 줄 소개</p>
                                                      </div>
                                                      <div class="expert-action">
                                                            <a href="#" class="custom-button checkPDF">조율 내용 확인</a>
                                                      </div>
                                             </div>
                                       </div>
                                 </div>
                           </div>
                        `;
			$('.content-container').html(paymentTemplate);
		}
	}

	// 별점 생성 헬퍼 함수
	function generateStarRating(rating) {
		const fullStars = Math.floor(rating);
		const halfStar = rating % 1 >= 0.5 ? 1 : 0;
		const emptyStars = 5 - fullStars - halfStar;

		let starHtml = '';

		// 꽉 찬 별
		for (let i = 0; i < fullStars; i++) {
			starHtml += '<i class="bi bi-star-fill"></i>';
		}

		// 반 별
		if (halfStar) {
			starHtml += '<i class="bi bi-star-half"></i>';
		}

		// 빈 별
		for (let i = 0; i < emptyStars; i++) {
			starHtml += '<i class="bi bi-star"></i>';
		}

		return starHtml;
	}

	// 날짜 포맷팅 헬퍼 함수
	function formatDate(dateString) {
		const date = new Date(dateString);
		return date.toLocaleDateString('ko-KR', {
			year: 'numeric',
			month: '2-digit',
			day: '2-digit'
		});
	}

	// HTML 생성 헬퍼 함수들
	function generatePostHTML(memberMyPageCommunity) {
		return `
               <div class="card mb-3">
                  <div class="card-body">
                     <h5 class="card-title">${memberMyPageCommunity.community.communityBoardTitle}</h5>
                     <p class="card-text">${memberMyPageCommunity.community.communityBoardContent}</p>
                     <div class="d-flex justify-content-between align-items-center">
                        <div>
                           <span class="me-2"><i class="bi bi-eye"></i> ${memberMyPageCommunity.community.communityView}</span>
                           <span class="me-2"><i class="bi bi-heart"></i> ${memberMyPageCommunity.community.communityBoardLike}</span>
                           <span class="me-2"><i class="bi bi-hand-thumbs-down"></i> ${memberMyPageCommunity.community.communityBoardDislike}</span>
                        </div>
                        <small class="text-muted">${formatDate(memberMyPageCommunity.community.communityBoardRegDate)}</small>
                     </div>
                  </div>
               </div>
            `;
	}

	function generateQuestionHTML(memberWithQnA) {
		return `
               <div class="card mb-3">
                  <div class="card-body">
                     <div class="d-flex justify-content-between">
                        <h5 class="card-title">${memberWithQnA.qna.qnaBoardTitle}</h5>
                        <span class="badge ${memberWithQnA.qna.qnaAdoptionStatus ? 'bg-success' : 'bg-secondary'}">
                           ${memberWithQnA.qna.qnaAdoptionStatus ? '채택완료' : '채택대기'}
                        </span>
                     </div>
                     <p class="card-text">${memberWithQnA.qna.qnaBoardContent}</p>
                     <div class="d-flex justify-content-between align-items-center">
                        <div>
                           <span class="me-2"><i class="bi bi-eye"></i> ${memberWithQnA.qna.qnaView}</span>
                           <span class="me-2"><i class="bi bi-heart"></i> ${memberWithQnA.qna.qnaBoardLike}</span>
                        </div>
                        <small class="text-muted">${formatDate(memberWithQnA.qna.qnaBoardRegDate)}</small>
                     </div>
                  </div>
               </div>
            `;
	}

	function generateCommentHTML(comWithComReply) {
		return `
               <div class="card mb-3">
                  <div class="card-body">
                     <h6 class="card-subtitle mb-2 text-muted">${comWithComReply.community.communityBoardTitle}</h6>
                     <p class="card-text">${comWithComReply.communityReply.communityReplyContent}</p>
                     <div class="d-flex justify-content-between align-items-center">
                        <div>
                           <span class="me-2"><i class="bi bi-heart"></i> ${comWithComReply.communityReply.communityReplyLike}</span>
                           <span><i class="bi bi-hand-thumbs-down"></i> ${comWithComReply.communityReply.communityReplyDislike}</span>
                        </div>
                        <small class="text-muted">${formatDate(comWithComReply.communityReply.communityReplyRegDate)}</small>
                     </div>
                  </div>
               </div>
            `;
	}

	function generateReplyHTML(memberWithQnAReply) {
		return `
               <div class="card mb-3">
                  <div class="card-body">
                     <h6 class="card-subtitle mb-2 text-muted">${memberWithQnAReply.qnaReply.qnaBoardTitle}</h6>
                     <p class="card-text">${memberWithQnAReply.qnaReply.qnaReplyContent}</p>
                     <div class="d-flex justify-content-between align-items-center">
                        <div>
                           <span class="me-2"><i class="bi bi-heart"></i> ${memberWithQnAReply.qnaReply.qnaReplyLike}</span>
                        </div>
                        <small class="text-muted">${formatDate(memberWithQnAReply.qnaReply.qnaReplyRegDate)}</small>
                     </div>
                  </div>
               </div>
            `;
	}


	function generateLikesHTML(items) {
		let html = '';
		let hasLikes = false;

		// 데이터를 타입별로 분류
		const communityLikes = items.filter(item => item.communityBoardNo);
		const qnaLikes = items.filter(item => item.qnaBoardNo);
		const communityReplyLikes = items.filter(item => item.communityReplyNo);
		const qnaReplyLikes = items.filter(item => item.qnaReplyNo);

		// 게시글 공감
		if (communityLikes.length > 0) {
			html += '<h4 class="mt-4 mb-3">자유글</h4>';
			communityLikes.forEach(community => {
				html += `
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">${community.communityBoardTitle}</h5>
                        <p class="card-text">${community.communityBoardContent}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="me-2"><i class="bi bi-heart-fill text-danger"></i> ${community.communityBoardLike}</span>
                                <span><i class="bi bi-hand-thumbs-down"></i> ${community.communityBoardDislike}</span>
                            </div>
                            <small class="text-muted">${formatDate(community.communityBoardRegDate)}</small>
                        </div>
                    </div>
                </div>
            `;
			});
			hasLikes = true;
		}

		// 질문글 공감
		if (qnaLikes.length > 0) {
			html += '<h4 class="mt-4 mb-3">질문글</h4>';
			qnaLikes.forEach(question => {
				html += `
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">${question.qnaBoardTitle}</h5>
                        <p class="card-text">${question.qnaBoardContent}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="me-2"><i class="bi bi-heart-fill text-danger"></i> ${question.qnaBoardLike}</span>
                            </div>
                            <small class="text-muted">${formatDate(question.qnaBoardRegDate)}</small>
                        </div>
                    </div>
                </div>
            `;
			});
			hasLikes = true;
		}

		// 댓글 공감
		if (communityReplyLikes.length > 0) {
			html += '<h4 class="mt-4 mb-3">댓글</h4>';
			communityReplyLikes.forEach(reply => {
				html += `
                <div class="card mb-3">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">${reply.communityBoardTitle}</h6>
                        <p class="card-text">${reply.communityReplyContent}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="me-2"><i class="bi bi-heart-fill text-danger"></i> ${reply.communityReplyLike}</span>
                                <span class="me-2"><i class="bi bi-hand-thumbs-down"></i></i> ${reply.communityReplyDislike}</span>
                            </div>
                            <small class="text-muted">${formatDate(reply.communityReplyRegDate)}</small>
                        </div>
                    </div>
                </div>
            `;
			});
			hasLikes = true;
		}

		// 답변 공감
		if (qnaReplyLikes.length > 0) {
			html += '<h4 class="mt-4 mb-3">답변</h4>';
			qnaReplyLikes.forEach(reply => {
				html += `
                <div class="card mb-3">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">${reply.qnaBoardTitle}</h6>
                        <p class="card-text">${reply.qnaReplyContent}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="me-2"><i class="bi bi-heart-fill text-danger"></i> ${reply.qnaReplyLike}</span>
                            </div>
                            <small class="text-muted">${formatDate(reply.qnaReplyRegDate)}</small>
                        </div>
                    </div>
                </div>
            `;
			});
			hasLikes = true;
		}

		return hasLikes ? html : '<p class="text-center my-5">공감한 내역이 없습니다.</p>';
	}

	// 초기 컨텐츠 로드
	loadContent('payment');

	// 탭 클릭 이벤트 처리
	$('.nav-link').click(function() {
		const content = $(this).data('content');
		if (content === 'profile') {
			// 다른 탭의 active 상태 제거
			$('.sidebar .nav-link').removeClass('active');
			// profile 탭 active 상태로 변경
			$(this).addClass('active');

			// 프로필 페이지를 빈 상태로 표시
			$('.content-container').empty();
			// 비밀번호 확인 모달 표시
			$('#passwordCheckModal').modal('show');
			return false;
		}
		loadContent(content);
	});

	// 프로필 HTML 생성 함수
	function generateProfileHTML(memberData) {
		return `
               <div class="profile-form-container">
                     <div class="container d-flex justify-content-center">
                           <div style="width: 100%; max-width: 500px;">
                                 <form id="memberUpdateForm" enctype="multipart/form-data" class="profile-form" method="post" action="/updateMember">

                                       <div class="row mb-4">
                                             <div class="col-12">
                                                   <div class="profile-image-wrapper text-center">
                                                         <div class="position-relative d-inline-block">
                                                               <img src="${memberData.memberImage ? '/resources/files/' + memberData.memberImage : '/images/default-profile.png'}" 
                                                                alt="프로필" 
                                                                class="profile-image-container"
                                                                style="width: 150px; height: 150px; border-radius: 15px; object-fit: cover;">
                                                         <input type="file" id="profileImage" name="profileImage" class="d-none" accept="image/*">
                                                         <button type="button" class="btn btn-sm btn-edit-profile" 
                                                                     onclick="$('#profileImage').click()"
                                                                     style="position: absolute; bottom: 0; right: 0;">
                                                               <i class="bi bi-pencil-fill"></i>
                                                         </button>
                                                      </div>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="name">&nbsp;이름 *</label> 
                                                   <input id="name" name="name" type="text" class="form-control mb-2" value="${memberData.name}" maxlength="5" required>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="memberId">&nbsp;아이디</label>
                                                   <input id="memberId" name="memberId" type="text" class="form-control" value="${memberData.memberId}" readonly>
                                             </div>
                                       </div>
                                       
                                       

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="nickname">&nbsp;닉네임</label>
                                                   <input type="text" class="form-control" id="nickname" name="nickname" value="${memberData.nickname}" readonly>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="birth">&nbsp;생년월일</label>
                                                   <input id="birth" name="birth" type="date" class="form-control" value="${memberData.birth}" readonly>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label>&nbsp;성별</label>
                                                   <div class="d-flex mb-2">
                                                         <div class="form-check me-4">
                                                               <input class="form-check-input" type="radio" name="gender" id="male" value="male" ${memberData.gender === 'male' ? 'checked' : ''} disabled>
                                                               <label class="form-check-label" for="male">남성</label>
                                                         </div>
                                                         <div class="form-check">
                                                               <input class="form-check-input" type="radio" name="gender" id="female" value="female" ${memberData.gender === 'female' ? 'checked' : ''} disabled>
                                                               <label class="form-check-label" for="female">여성</label>
                                                         </div>
                                                   </div>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="email">&nbsp;이메일 *</label>
                                                   <input type="email" class="form-control mb-2" id="email" name="email" value="${memberData.email}" required>
                                                   <div class="form-check">
                                                      <input class="form-check-input" type="checkbox" id="emailConsent" name="emailConsent" ${memberData.emailGet ? 'checked' : ''}>
                                                      <label class="form-check-label" for="emailConsent">이메일 수신에 동의합니다.</label>
                                                   </div>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="phone">&nbsp;전화번호 *</label>
                                                   <input type="tel" class="form-control mb-2" id="phone" name="phone" value="${memberData.phone}" required>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="zipcode">&nbsp;우편번호 *</label>
                                                   <div class="input-group mb-2">
                                                      <input type="text" class="form-control" id="zipcode" name="zipcode" value="${memberData.zipcode}" readonly required>
                                                      <button type="button" class="btn custom-button" id="addressSearchBtn2">주소찾기</button>
                                                   </div>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="address">&nbsp;주소 *</label>
                                                   <input type="text" class="form-control mb-2" id="address" name="address" value="${memberData.address}" readonly required>
                                             </div>
                                       </div>

                                       <div class="row mb-3">
                                             <div class="form-field">
                                                   <label for="address2">&nbsp;상세주소 *</label>
                                                   <input type="text" class="form-control mb-2" id="address2" name="address2" value="${memberData.address2}" required>
                                             </div>
                                       </div>

                                       <button type="button" class="btn custom-button w-100" id="changePasswordBtnShow">비밀번호 변경</button>
                                       
                                       <input type="submit" value="정보 수정" class="btn custom-button2 w-100 mt-4">
                                 </form>
                           </div>
                     </div>
               </div>
         `;
	}

	// 비밀번호 확인 폼 제출 시
	$('#passwordCheckForm').on('submit', function(e) {
		e.preventDefault();
		const password = $('#currentPassword').val();

		$.ajax({
			url: '/ajax/member/checkPassword',
			type: 'POST',
			data: { pass: password },
			success: function(response) {
				if (response.valid) {
					$('#passwordCheckModal').modal('hide');
					// 회원 정보 가져와서 프로필 페이지 표시
					$.ajax({
						url: '/ajax/member/getMemberInfo',
						type: 'GET',
						success: function(memberData) {
							$('.content-container').html(generateProfileHTML(memberData));
							// 폼이 로드된 후 표시 애니메이션 적용
							$('.profile-form-container').fadeIn();
						},
						error: function() {
							alert('회원정보를 불러오는데 실패했습니다.');
						}
					});
				} else {
					$('#passwordError').text('비밀번호가 일치하지 않습니다.');
				}
			},
			error: function() {
				$('#passwordError').text('서버 오류가 발생했습니다.');
			}
		});
	});

	// 프로필 이미지 변경 시 미리보기
	$(document).on('change', '#profileImage', function(e) {
		const file = e.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				$('.profile-image-container').attr('src', e.target.result);
			}
			reader.readAsDataURL(file);
		}
	});


	// 다음 지도 API
	$(document).on('click', '#addressSearchBtn2', function() {
		console.log('주소 찾기 버튼 클릭됨');  // 버튼 클릭 로그

		if (typeof daum === 'undefined') {
			console.error('Daum 우편번호 서비스 로드 실패');
			alert('주소 검색 서비스를 불러오는데 실패했습니다.');
			return;
		}
	if (profileImageInput) {
		profileImageInput.addEventListener('change', function(e) {
			const file = e.target.files[0];
			if (file) {
				const reader = new FileReader();
				reader.onload = function(e) {
					document.querySelector('.profile-image-container').src = e.target.result;
				}
				reader.readAsDataURL(file);
			}
		});
	}

	// 모달이 자동으로 열리지 않도록 초기화
	$('#changePasswordModalShow').modal('hide'); // 페이지 로드 시 모달 숨기기

	// 비밀번호 변경 버튼 클릭 시 모달 열기
		$('#changePasswordBtnShow').on('click', function() {
			$('#changePasswordModalShow').modal('show');
		});


	// 비밀번호 유효성 검사 패턴
	const patterns = {
		password: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/
	};

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
				data: { estimationNo: estimationNo },
				success: function(response) {
					alert('매칭이 완료되었습니다.');
					// 견적 목록 새로고침
					loadContent('Request');
				},
				error: function() {
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
				data: { estimationNo: estimationNo },
				success: function(response) {
					if (response.success) {
						// 채팅 페이지로 이동
						window.location.href = `/chat/room/${response.roomId}`;
					} else {
						alert(response.message || '채팅 시작에 실패했습니다.');
					}
				},
				error: function() {
					alert('채팅 시작 중 오류가 발생했습니다.');
				}
			});
		}
	}
});