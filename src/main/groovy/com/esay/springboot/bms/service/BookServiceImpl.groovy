package com.esay.springboot.bms.service

import com.esay.springboot.bms.domain.Book
import com.esay.springboot.bms.mapper.BookMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by jack on 2017/4/15.
 */
@Service
class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper

    List<Book> findByState(String state) {
        bookMapper.findByState(state)
    }


    List<Book> findAll() {
        bookMapper.findAll()
    }

    Book insert(Book book) {
        bookMapper.insert(book)
    }

}
