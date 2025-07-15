package com.neu.SP01.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CoreFilter {

	 @Bean
	    public CorsWebFilter corsWebFilter() {
	        //此处使用响应式包
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration corsConfiguration = new CorsConfiguration();
	        //配置跨域
	        corsConfiguration.addAllowedHeader("*");
	        corsConfiguration.addAllowedMethod("*");
	        corsConfiguration.addAllowedOrigin("*");//允许哪些请求来源
	        source.registerCorsConfiguration("/**", corsConfiguration);
	        return new CorsWebFilter(source);
	    }
	

}
