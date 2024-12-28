package com.onestack.project.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

// 접속자가 로그인 상태인지 체크하는 인터셉터
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		log.info("##########LoginCheckInterceptor - preHandle()##########");
		// 현재 세션에 저장된 loginMsg 속성을 삭제
		HttpSession session = request.getSession();
		session.removeAttribute("loginMsg");
				
		// 세션에 isLogin란 이름의 속성이 없으면 로그인 상태가 아님
		if(request.getSession().getAttribute("isLogin") == null) {
			// 로그인 상태가 아니라면 로그인 폼으로 리다이렉트 시킨다.
			response.sendRedirect("loginForm");
			session.setAttribute("loginMsg", "로그인이 필요한 서비스");
			return false;
		}		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("##########LoginCheckInterceptor - postHandle()##########");		

		response.setHeader("Cache-Control", "no-cache");
	}
	

	@Override
	public void afterCompletion(HttpServletRequest request, 
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("##########LoginCheckInterceptor - afterCompletion()##########");
	}	
}
