package com.esay.springboot.bms.advice

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView

/**
 * Created by jack on 2017/4/16.
 */
@ControllerAdvice
class ExceptionHandlerAdvice {

    //表示捕捉到所有的异常，你也可以捕捉一个你自定义的异常
    @ExceptionHandler(value = Exception.class)
    ModelAndView exception(Exception exception) {
        ModelAndView modelAndView = ModelAndView("error")//error页面
        modelAndView.addObject("errorMessage", exception.message)
        modelAndView.addObject("stackTrace", exception.stackTrace)

        modelAndView
    }

}
