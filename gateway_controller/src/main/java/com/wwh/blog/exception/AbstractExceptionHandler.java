package com.wwh.blog.exception;

import com.alibaba.fastjson2.JSONObject;
import com.wwh.springcloud.exception.RsaException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public abstract class AbstractExceptionHandler {

    protected JSONObject buildErrorMap(Throwable ex) {
        JSONObject json = new JSONObject();
        if (ex instanceof RsaException || ex instanceof IllegalArgumentException) {
            if (StringUtils.isNoneBlank(ex.getMessage())) {
                json.put("msg", ex.getMessage());
            } else {
                json.put("msg", "无效的请求");
            }
        } else {
            json.put("code", HttpStatus.BAD_REQUEST.value());
            json.put("msg", "未知错误，请联系管理员");
        }
        return json;
    }
}
