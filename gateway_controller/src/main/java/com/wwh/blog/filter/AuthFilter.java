package com.wwh.blog.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author: wwh
 * @date: 2022/8/19
 * @description:
 */
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Value("#{'${auth.skip.urls:}'.split(',')}")
    private List<String> skipAuthUrls;

    /**
     * 存放token信息的请求头属性
     */
    public static final String TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        //2.获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        //3.判断 是否需要直接放行
        log.info("uri: {}", request.getURI());
        if (skipAuthUrls.contains(request.getURI().toString())) {
            return chain.filter(exchange);
        }

        //4 校验
        //4.1 从头header中获取令牌数据
        String token = request.getHeaders().getFirst(TOKEN);

        if (StringUtils.isEmpty(token)) {
            //4.2 从cookie中中获取令牌数据
            HttpCookie first = request.getCookies().getFirst(TOKEN);
            if (first != null) {
                token = first.getValue();//就是令牌的数据
            }
        }

        if (StringUtils.isEmpty(token)) {
            //4.3 从请求参数中获取令牌数据
            token = request.getQueryParams().getFirst(TOKEN);
        }

        if (StringUtils.isEmpty(token)) {
            //4.4. 如果没有数据    没有登录,要重定向到登录到页面
            response.setStatusCode(HttpStatus.FORBIDDEN);//403
            // 4.5. 在响应头里添加鉴权失败信息
            response.getHeaders().set("auth_error", "xxxx");
            return response.setComplete();
        }


        //TODO 5 解析令牌数据 ( 判断解析是否正确,正确 就放行 ,否则 结束)

        try {

            log.info("authed user info: {}", "*****");
        } catch (Exception e) {
            log.error("parse token error:", e);
            //解析失败
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // 6 将token添加到头信息，传递给后续的服务
        request.mutate().header(TOKEN, token);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
