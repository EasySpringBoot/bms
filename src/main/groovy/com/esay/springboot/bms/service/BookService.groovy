package com.esay.springboot.bms.service

import com.esay.springboot.bms.domain.Book

/**
 * Created by jack on 2017/4/15.
 */
interface BookService {
    List<Book> findByState(String state)

    List<Book> findAll()

    Book insert(Book book)
}
