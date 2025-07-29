package top.alexjtech.educationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EducationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationApiApplication.class, args);
        System.out.println("=================================");
        System.out.println("Education API 启动成功!");
        System.out.println("访问地址: http://localhost:8080/api");
        System.out.println("API文档页面 http://localhost:8080/api/swagger-ui.html");
        System.out.println("API文档JSON http://localhost:8080/api/v3/api-docs");
        System.out.println("=================================");
    }

}