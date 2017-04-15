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
