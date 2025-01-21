package com.onestack.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PayController {

    /* 결제 요청 폼 */
    @GetMapping("/payForm")
    public String getPayForm(Model model) {

        return "views/payForm";
    }

    /* 결제 완료 폼 */
    @GetMapping("/payDoneForm")
    public String getPayDoneForm(Model model) {
        return "views/payDoneForm";
    }

}
