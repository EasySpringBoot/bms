第8_章： Spring Boot集成Groovy混合Java开发
====

本章我们使用SpringBoot集成Groovy混合Java开发一个极简的RestAPI。 数据库使用mysql，ORM层使用mybatis，模板引擎使用freemarker，构建工具使用Gradle。

关于Groovy语言，我们在上一章已经简单介绍了。本章就不再多说。



##新建Gradle工程，配置build.gradle依赖


![](http://upload-images.jianshu.io/upload_images/1233356-2edee6ca1631b302.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



![](http://upload-images.jianshu.io/upload_images/1233356-5c3bf7c2c183c98b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们得到一个标准的gradle工程，目录如下：


![](http://upload-images.jianshu.io/upload_images/1233356-b27cee962c038487.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


由于我们勾选了Groovy支持，gradle依赖如下：

```
group 'com.easy.springboot'
version '1.0-SNAPSHOT'

apply plugin: 'groovy'
apply plugin: 'java'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    compile 'org.codehaus.groovy:groovy-all:2.3.11'
    testCompile group: 'junit', name: 'junit', version: '4.11'
    testCompile group: 'junit', name: 'junit', version: '4.12'
}

```


##添加SpringBoot依赖


boot-plugin


```
apply plugin: 'org.springframework.boot'
```

freemarker-starter


```
    compile('org.springframework.boot:spring-boot-starter-web')
    compile('org.springframework.boot:spring-boot-starter-freemarker')
```
mybatis-spring-boot-starter

```
compile('org.mybatis.spring.boot:mybatis-spring-boot-starter:1.1.1')
```

mysql jdbc驱动


```
compile('mysql:mysql-connector-java:6.0.5')
```

构建脚本

```
buildscript {
    ext {
        springBootVersion = '1.5.2.RELEASE'
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}
```

我们可以看出，在构建脚本里，dependencies里面依赖了spring-boot-gradle-plugin，其版本是我们使用的SpringBoot的版本。

SpringBoot Gradle 插件是SpringBoot针对 Gradle定制的工具, 可以帮助我们打包（jar，war），运行Spring Boot 应用，进行依赖管理等。

##配置数据库DataSource

创建application.yml文件,配置数据库信息：

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bms?serverTimezone=UTC&useSSL=false
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

##写领域模型类

```
package com.esay.springboot.bms.domain
/**
 * Created by jack on 2017/4/15.
 */
class Book {
    Long id;
    String name;
    String isbn;
    String author;
    String press;
//    Date in_date;
//    Date out_date;
    Date inDate;
    Date outDate;
    String state;
}

```

我们以前使用mybatis开启数据库字段自动映射驼峰命名规则java属性，是通过下面的xml配置：
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
 <settings>
  <setting name="mapUnderscoreToCamelCase" value="true"/>
 </settings>
</configuration>
```

对应的，我们使用注解的方式

```
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
```

其中，@Primary注解的功能：当自动装配Bean时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常。

如果不标记，会报如下错误：

```
Field properties in org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration required a single bean, but 2 were found:
 - mybatisProperties: defined by method 'mybatisProperties' in class path resource [com/esay/springboot/bms/config/MybatisConfig.class]
 - mybatis-org.mybatis.spring.boot.autoconfigure.MybatisProperties: defined in null


 Action:

 Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```

通过这个错误日志，我们可以更直观的看出@Primary注解的功能。

##Mapper层代码

```
package com.esay.springboot.bms.mapper;

import java.util.List;

import com.esay.springboot.bms.domain.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by jack on 2017/4/15.
 */
@Mapper
public interface BookMapper {
    @Select("select * from book where state = #{state}")
    List<Book> findByState(@Param("state") String state);

    @Select("select * from book")
    List<Book> findAll();

    @Insert({
        "insert into book",
        "set name = #{b.name},",
        "author = #{b.author},",
        "isbn = #{b.isbn},",
        "inDate = #{b.inDate},",
        "outDate = #{b.outDate},",
        "press = #{b.press},",
        "state = #{b.state}"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
        //使用@Options注解的userGeneratedKeys 和keyProperty属性让数据库产生auto_increment（自增长）列的值，然后将生成的值设置到输入参数对象的属性中。
    Book insert(@Param("b") Book book) throws RuntimeException;

}

```


##写控制器Controller层
```
package com.esay.springboot.bms.controller

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.esay.springboot.bms.domain.Book
import com.esay.springboot.bms.service.BookService
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by jack on 2017/4/15.
 */
@Controller
class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/book")
    @ResponseBody
    List<Book> findByState(@RequestParam(value = "state", required = false) String state) {
        if (StringUtils.isEmpty(state)) {
            List<Book> all = bookService.findAll()
            println(JSON.toJSONString(all,SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue))
//            println(new JsonOutput().toJson(all))
            bookService.findAll()
        } else {
            bookService.findByState(state)
        }
    }

    @GetMapping("/bookPage")
    String findAll(Model model) {
        List<Book> books = bookService.findAll()
        model.addAttribute("books", books)
        "book/list"
    }


}

```

##写视图View层
```
<!DOCTYPE html>
<html lang="zh">
<body>
<br>
<div>
<#list books as book>
    <p></p>
    <li>书名： ${book.name}</li>
    <li>作者： ${book.author}</li>
    <li>出版社： ${book.press}</li>
    <li>借出时间： ${book.outDate?string('yyyy/MM/dd HH:mm:ss')}</li>
    <li>还书时间： ${book.inDate?string('yyyy/MM/dd HH:mm:ss')}</li>
    <li>状态： ${book.state}</li>
</#list>
</div>
</body>

</html>

```


Freemarker日期格式化使用：
```
    <li>借出时间： ${book.outDate?string('yyyy/MM/dd HH:mm:ss')}</li>
    <li>还书时间： ${book.inDate?string('yyyy/MM/dd HH:mm:ss')}</li>
```
##运行测试

命令行运行

```
gradle bootRun
```
启动成功， 浏览器访问：http://localhost:8009/bookPage
你将看到类似如下页面：


![](http://upload-images.jianshu.io/upload_images/1233356-60934dd6d31fac18.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



访问Rest API接口：http://localhost:8009/book?state=NORMAL
我们可以看到如下输出：

```
[
  {
    "id": 1,
    "name": "极简SpringBoot教程",
    "isbn": "88888888",
    "author": "陈光剑",
    "press": "电子工业出版社",
    "inDate": 1492299756000,
    "outDate": 1492299756000,
    "state": "NORMAL"
  }
]

```

#小结


本章工程源代码：https://github.com/EasySpringBoot/bms
