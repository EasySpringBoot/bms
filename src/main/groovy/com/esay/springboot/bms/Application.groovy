package com.esay.springboot.bms

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by jack on 2017/4/15.
 */
@SpringBootApplication
@MapperScan('com.esay.springboot.bms.mapper')
class Application {
    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }
}
