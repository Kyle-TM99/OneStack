$(document).ready(function() {

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


	const patterns = {
		password: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/
	};

	const validators = {
		validateInput: function(value, pattern, fieldName) {
			if (!pattern.test(value)) {
				return `${fieldName}의 형식이 올바르지 않습니다.`;
			}
			return null;
		}
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
			case 'portfolio':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="portfolio"]').addClass('active');
				loadContent('portfolio');
				break;
			case 'studentManagement':
				$('.sidebar .nav-link .stats-overview .stat-content[data-content="studentManagement"]').addClass('active');
				loadContent('studentManagement');
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
				success: function(memberData) {
					const contentMap = {
						Request: `
								<div class="mb-4">
									<div class="expert-list">
										<div class="expert-list-item">
											<div class="quote-details mt-4">
												<ul class="list-unstyled">
													<li><strong>회원 이름:</strong> 김철수</li>
													<li><strong>전문가 이름:</strong> 박지훈</li>
													<li><strong>종목 번호:</strong> 123456</li>
													<li><strong>견적 내용:</strong> 모바일 앱 개발</li>
													<li><strong>희망 금액:</strong> 8,000,000원</li>
													<li><strong>기타 건의사항:</strong> UI/UX 디자인에 신경 써 주세요.</li>
													<li><strong>견적 확인 여부:</strong> <span class="badge bg-warning text-dark">대기중</span></li>
												</ul>
											</div>
								
											<!-- 매칭 버튼 -->
											<div class="expert-action text-center mt-3">
												<a href="#" class="custom-button checkPDF">매칭</a>
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
		} else if (contentType === 'portfolio') {
			// 세션에서 현재 로그인한 회원의 번호를 가져오는 AJAX 요청
			$.ajax({
				url: '/ajax/member/getMemberInfo',
				type: 'GET',
				success: function(memberData) {
					// 회원 번호로 리뷰 조회
					$.ajax({
						url: '/ajax/member/myPagePortfolio',
						type: 'GET',
						success: function(response) {
							if (response.success) {
								let portfolioHtml = response.data.map(portfolio => `
													<div class="row d-flex justify-content-end"><i class="bi bi-plus-lg"></i></div>
													<div class="portfolio-list">
														<div class="card" style="width: 18rem;">
														<div class="dropdown">
														  <button class="btn btn-secondary dropdown-toggle d-flex justify-content-end" type="button" data-bs-toggle="dropdown" aria-expanded="false">
															<i class="bi bi-three-dots-vertical"></i>
														  </button>
														  <ul class="dropdown-menu">
															<li><a class="dropdown-item" href="#">공개</a> / <a class="dropdown-item" href="#">비공개</a></li>
															<li><a class="dropdown-item" href="#">수정</a></li>
															<li><a class="dropdown-item" href="#">삭제</a></li>
														  </ul>
														</div>
														  <img src="..." class="card-img-top" alt="...">
														  <div class="card-body">
															<p class="card-text">${portfolio.portfolioTitle}</p>
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

								// 컨테이너에 리뷰 HTML 추가
								$('.content-container').html(reviewHtml);
							} else {
								alert(response.message || '리뷰를 불러오는 데 실패했습니다.');
							}
						},
						error: function() {
							alert('리뷰를 불러오는 데 실패했습니다.');
						}
					});
				},
				error: function() {
					alert('회원 정보를 불러오는 데 실패했습니다.');
				}
			});
		} else if (contentType === 'review') {
			// 세션에서 현재 로그인한 회원의 번호를 가져오는 AJAX 요청
			$.ajax({
				url: '/ajax/member/getMemberInfo',
				type: 'GET',
				success: function(memberData) {
					// 회원 번호로 리뷰 조회
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

								// 컨테이너에 리뷰 HTML 추가
								$('.content-container').html(reviewHtml);
							} else {
								alert(response.message || '리뷰를 불러오는 데 실패했습니다.');
							}
						},
						error: function() {
							alert('리뷰를 불러오는 데 실패했습니다.');
						}
					});
				},
				error: function() {
					alert('회원 정보를 불러오는 데 실패했습니다.');
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
					console.log('서버 응답:', response); // 응답 데이터 확인
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
			// payment 템플릿 렌더링
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

		// 데이터 구조 확인
		console.log('공감 데이터:', items);

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

													<button type="button" class="btn custom-button w-100" id="changePasswordBtn">비밀번호 변경</button>
													
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

		new daum.Postcode({
			oncomplete: function(data) {
				console.log('주소 선택 완료:', data); // API 호출 로그
				$("#zipcode").val(data.zonecode);
				$("#address").val(data.roadAddress);
				$("#address2").focus();
				validationState.address = true;
			},
			width: '100%',
			height: '100%'
		}).open();
	});

	$(document).on('input', '#address2', function() {
		console.log('상세주소 입력됨:', $(this).val());
		validationState.address = $(this).val().trim() !== "";
	});

	// 비밀번호 변경 버튼 클릭 이벤트
	$(document).on('click', '#changePasswordBtn', function() {
		$('#changePasswordModal').modal('show');
	});

	$('#newPassword').on('input', function() {
		const password = $(this).val();
		const errorMsg = validators.validateInput(password, patterns.password, "비밀번호");
		if (errorMsg) {
			$(this).addClass('is-invalid');
			$('#newPasswordError').text(errorMsg);
		} else {
			$(this).removeClass('is-invalid').addClass('is-valid');
			$('#newPasswordError').text('');
		}
	});

	$('#confirmPassword').on('input', function() {
		const confirmPassword = $(this).val();
		const newPassword = $('#newPassword').val();
		if (confirmPassword !== newPassword) {
			$(this).addClass('is-invalid');
			$('#confirmPasswordError').text('비밀번호가 일치하지 않습니다.'); // Passwords do not match
		} else {
			$(this).removeClass('is-invalid').addClass('is-valid');
			$('#confirmPasswordError').text('');
		}
	});


// 비밀번호 변경 폼 제출 이벤트 핸들러
	$('#passwordChangeForm').on('submit', function(e) {
		e.preventDefault(); // 폼의 기본 제출 동작 중단

		const currentPassword2 = $('#currentPassword2').val();
		const newPassword = $('#newPassword').val();

		// 현재 비밀번호 입력 확인
		if (!currentPassword2) {
			alert('현재 비밀번호를 입력해주세요.');
			return;
		}

		// 비밀번호 유효성 검사
		if (!validationState.newPassword) {
			alert('새 비밀번호가 유효하지 않습니다. 비밀번호는 8자 이상 16자 이하이며, 영문, 숫자, 특수문자가 모두 포함되어야 합니다.');
			return;
		}

		// 비밀번호 확인 일치 검사
		if (!validationState.passwordMatch) {
			alert('비밀번호 확인이 일치하지 않습니다.');
			return;
		}

		// 현재 비밀번호 확인 AJAX 요청
		$.ajax({
			url: '/ajax/member/checkPassword',
			type: 'POST',
			data: { pass: currentPassword }, // 현재 비밀번호를 전송
			success: function(response) {
				if (response.valid) {
					// 현재 비밀번호가 유효한 경우 비밀번호 변경 요청
					$.ajax({
						url: '/ajax/member/updatePassword',
						type: 'POST',
						data: {
							currentPassword: currentPassword,
							newPassword: newPassword // 새 비밀번호 전달
						},
						success: function (response) {
							if (response.success) {
								alert('비밀번호가 성공적으로 변경되었습니다.');
								$('#changePasswordModal').modal('hide');
								$('#passwordChangeForm')[0].reset();
							} else {
								alert(response.message || '현재 비밀번호가 일치하지 않습니다.');
							}
						},
						error: function () {
							alert('서버 오류가 발생했습니다.');
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

// 변경하기 버튼 클릭 이벤트
	$('#savePasswordBtn').on('click', function() {
		$('#passwordChangeForm').submit(); // 폼 제출
	});

// 모달 닫힐 때 초기화
	$('#changePasswordModal').on('hidden.bs.modal', function() {
		$('#passwordChangeForm')[0].reset();
		$('.is-valid, .is-invalid').removeClass('is-valid is-invalid');
		$('.invalid-feedback').text('');
	});
});