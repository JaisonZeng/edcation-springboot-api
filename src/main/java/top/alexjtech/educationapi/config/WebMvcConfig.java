package top.alexjtech.educationapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 用于配置静态资源映射
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Value("${app.upload.avatar-dir:uploads/avatars/}")
    private String avatarDir;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 统一通过/uploads/**访问所有上传文件
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
}