package com.project.asteroidalerting.Config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter((ServletRequest req, ServletResponse res, FilterChain chain) -> {
            HttpServletRequest  request  = (HttpServletRequest)  req;
            HttpServletResponse response = (HttpServletResponse) res;

            response.setHeader("Access-Control-Allow-Origin",          "*");
            response.setHeader("Access-Control-Allow-Methods",         "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers",         "Content-Type, Authorization");
            response.setHeader("Access-Control-Allow-Private-Network", "true");

            // OPTIONS preflight — return immediately with 200
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            chain.doFilter(req, res);
        });
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // run before everything else
        return bean;
    }
}