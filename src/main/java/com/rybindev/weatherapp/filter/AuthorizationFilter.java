package com.rybindev.weatherapp.filter;

import com.rybindev.weatherapp.dto.UserDto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static com.rybindev.weatherapp.util.UrlPath.*;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {
    private static final Set<String> PUBLIC_PATH = Set.of(LOGIN, REGISTRATION, LOCATIONS, ERROR);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();

        if (isPublicPath(uri) || isUserLoggedIn(request)) {
            chain.doFilter(request, response);
        }else {
            String referer = ((HttpServletRequest) request).getHeader("referer");
            ((HttpServletResponse) response).sendRedirect(referer != null ? referer : LOGIN);
        }
    }

    private boolean isUserLoggedIn(ServletRequest request) {
        UserDto user = (UserDto)((HttpServletRequest) request).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }
}
