//package com.thuongmaidientu.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    private AdminAuthInterceptor adminAuthInterceptor;
//	
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // Cho tất cả endpoint
//            .allowedOrigins("http://localhost")  // Cho phép gọi từ frontend
//            .allowedMethods("GET", "POST", "PUT", "DELETE")
//            .allowedHeaders("*")
//            .allowCredentials(true);
//    }
//    public WebConfig() {
//        System.out.println("✅ WebConfig loaded");
//    }
//    
////    @Bean
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(adminAuthInterceptor)
////                .addPathPatterns("/quan-tri/**"); // chỉ các URL cần bảo vệ
//    	registry.addInterceptor(adminAuthInterceptor).addPathPatterns("/Spring-mvc/**");
//
//    }
//}
