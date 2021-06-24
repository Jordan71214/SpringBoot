package com.example.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "logProcessTimeFilterWithAnnotation")
public class LogProcessTimeFilterWithAnnotation extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        long processTime = System.currentTimeMillis() - startTime;

        System.out.println(processTime + "ms. I'm created with annotation.");
    }
}
