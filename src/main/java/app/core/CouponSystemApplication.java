package app.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import app.core.JWT.JwtUtil;
import app.core.filters.LoginFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CouponSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponSystemApplication.class, args);

	}

	@Bean
	public FilterRegistrationBean<LoginFilter> tokenFilterRegistration(JwtUtil jwtUtil) {
		FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<LoginFilter>();
		LoginFilter tokenFilter = new LoginFilter(jwtUtil);
		filterRegistrationBean.setFilter(tokenFilter);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

}
