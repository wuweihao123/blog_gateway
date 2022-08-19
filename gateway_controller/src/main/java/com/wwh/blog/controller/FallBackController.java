package com.wwh.blog.controller;

import com.wwh.springcloud.pojo.ResultMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wwh
 * @date: 2022/8/19
 * @description:
 */
@RestController
public class FallBackController {

    @RequestMapping("/defaultFallback")
    public ResultMessage defaultFallback() {
        return new ResultMessage(999,"timeOut",false,"");
    }
}
