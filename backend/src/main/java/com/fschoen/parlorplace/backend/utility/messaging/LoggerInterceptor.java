package com.fschoen.parlorplace.backend.utility.messaging;

import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.handler.*;

import javax.servlet.http.*;

@Slf4j
@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        log.info("Received {} at {}", request.getMethod(), request.getRequestURL());
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        log.info("Completed {} at {} with status {}", request.getMethod(), request.getRequestURL(), response.getStatus());
    }

}
