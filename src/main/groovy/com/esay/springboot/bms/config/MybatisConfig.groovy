package com.esay.springboot.bms.config

import org.mybatis.spring.boot.autoconfigure.MybatisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Created by jack on 2017/4/16.
 */
@Configuration
class MybatisConfig {
    @Bean
    @Primary
    MybatisProperties mybatisProperties() {
        MybatisProperties p = new MybatisProperties()
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration()
        // 开启mybatis开启数据库字段自动映射驼峰命名规则java属性
        config.mapUnderscoreToCamelCase = true
        p.configuration = config

        p

    }
}

/**
 Field properties in org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration required a single bean, but 2 were found:
 - mybatisProperties: defined by method 'mybatisProperties' in class path resource [com/esay/springboot/bms/config/MybatisConfig.class]
 - mybatis-org.mybatis.spring.boot.autoconfigure.MybatisProperties: defined in null


 Action:

 Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
 */
