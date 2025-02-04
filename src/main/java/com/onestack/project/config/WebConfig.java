package com.onestack.project.config;

import com.onestack.project.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.onestack.project.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private MemberMapper memberMapper;

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

		registry.addResourceHandler("/resources/files/**")
			// file: 프로토콜을 사용하면 업로드한 이미지가 바로 보인다.
			.addResourceLocations("file:./src/main/resources/static/files/").setCachePeriod(1); // 캐쉬 지속시간(초)
		
		registry.addResourceHandler("/images/**")
		// 이미지 경로
		.addResourceLocations("classpath:/static/images/")
		// 캐쉬 지속시간(초)
		.setCachePeriod(3600);
	}

	  @Override
	  public void addInterceptors(InterceptorRegistry registry) {
		  registry.addInterceptor(new LoginCheckInterceptor(memberMapper))
	  		   	  .addPathPatterns("/**") // 인터셉터를 적용할 경로
	              .excludePathPatterns( // 로그인 없이 접근 가능한 경로
						"/**" , "/" ,"/main", "/images/**" , "/mainPage", "/login", "/loginForm", "/css/**", "/js/**", "/resources/**", "/static/**", "/main_layout"
	              );

	  }



}
