package com.hurryclear.usercenter_backend_app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hurryclear.usercenter_backend_app.mapper")

public class BackendAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendAppApplication.class, args);
    }

}
