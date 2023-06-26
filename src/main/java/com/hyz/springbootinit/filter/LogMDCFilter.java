package com.hyz.springbootinit.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * 日志最终过滤器
 *
 * @author hegd
 * @date 2023/06/26
 */
public class LogMDCFilter implements Filter {

    private static final String UNIQUE_ID_NAME = "traceId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        MDC.put(UNIQUE_ID_NAME, UUID.randomUUID().toString().replace("-", ""));
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(UNIQUE_ID_NAME);
        }
    }
}