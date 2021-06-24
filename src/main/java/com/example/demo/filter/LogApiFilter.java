package com.example.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogApiFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {


        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        filterChain.doFilter(requestWrapper, responseWrapper);

        logAPI(httpServletRequest, httpServletResponse);
        logBody(requestWrapper, responseWrapper);

        responseWrapper.copyBodyToResponse();
    }

    private void logBody(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        String requestBody = getContent(request.getContentAsByteArray());
        System.out.println("Request: " + requestBody);

        String responseBody = getContent(response.getContentAsByteArray());
        System.out.println("Response: " + responseBody);
    }

    private String getContent(byte[] contentAsByteArray) {
        String body = new String(contentAsByteArray);
        return body.replaceAll("[\n\t]", "");
    }

    private void logAPI(HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = response.getStatus();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String param = request.getQueryString();

        if (param != null) {
            uri += "?" + param;
        }

        System.out.println(String.join(" ", String.valueOf(httpStatus), httpMethod, uri));
    }


}
