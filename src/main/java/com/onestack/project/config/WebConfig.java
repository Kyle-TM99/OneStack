package com.onestack.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.onestack.project.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/mainPage").setViewName("views/mainPage");
		
		// 로그인 폼 뷰 전용 컨트롤러 설정 추가
		registry.addViewController("/loginForm").setViewName("member/loginForm");
						
		// 회원가입 폼 뷰 전용 컨트롤러 설정 추가
		registry.addViewController("/joinForm").setViewName("member/joinForm");

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		/*
		 * 기존에 정적 리소스 핸들러의 설정은 그대로 유지하며 새로운 리소스 핸들러 추가 /resources/** 로 요청되는 리소스 요청 설정
		 **/
		registry.addResourceHandler("/resources/files/**")
				// file: 프로토콜을 사용하면 업로드한 이미지가 바로 보인다.
				.addResourceLocations("file:./src/main/resources/static/files/").setCachePeriod(1); // 캐쉬 지속시간(초)

		// 업로드된 썸네일/ 포트폴리오이미지가 접근 가능하도록 설정
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:///C:/uploads/")
				.setCachePeriod(3600);
	}

	  @Override 
	  public void addInterceptors(InterceptorRegistry registry) {
		  registry.addInterceptor(new LoginCheckInterceptor())
	  		   	  .addPathPatterns("/**") // 인터셉터를 적용할 경로
	              .excludePathPatterns( // 로그인 없이 접근 가능한 경로
	            		  "/**"
	              );
	  }



}
