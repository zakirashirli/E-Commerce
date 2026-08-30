package com.ecommerce;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StorefrontController {
    @GetMapping("/")
    public String home() { return "forward:/home.html"; }
}
