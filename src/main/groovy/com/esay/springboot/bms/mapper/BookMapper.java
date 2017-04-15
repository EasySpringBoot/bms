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
