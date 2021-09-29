package com.example.gccoffee.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AdminAdvice {
    private static final Logger logger = LoggerFactory.getLogger(AdminAdvice.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView common(Exception exception) {
        logger.info(exception.toString());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/errors/error_common");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }
}
