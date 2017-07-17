package com.sword.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

  @RequestMapping({ "/index", "/" })
  public String index(ModelMap model) {
    return "redirect:/stocks";
  }
}
