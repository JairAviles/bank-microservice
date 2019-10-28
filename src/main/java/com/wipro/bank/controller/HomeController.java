package com.wipro.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ModelAndView swaggerUi() {
        return new ModelAndView("redirect:" + "/swagger-ui.html");
    }
}
