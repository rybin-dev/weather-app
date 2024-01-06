package com.rybindev.weatherapp.setvlet;

import com.rybindev.weatherapp.ApplicationContext;
import com.rybindev.weatherapp.ThymeleafConfiguration;
import com.rybindev.weatherapp.dto.LoginUserDto;
import com.rybindev.weatherapp.dto.UserDto;
import com.rybindev.weatherapp.service.UserService;
import com.rybindev.weatherapp.validator.Error;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.List;

import static com.rybindev.weatherapp.util.UrlPath.*;

@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        userService = ApplicationContext.userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);

        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        LoginUserDto loginUserDto = new LoginUserDto(email, password);

        userService.login(loginUserDto)
                .ifPresentOrElse(
                        user -> onLoginSuccess(user, req, resp),
                        () -> onLoginFail(req, resp)
                );

    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("errors", List.of(Error.of("login.fail","Email or password is not valid.")));
        req.setAttribute("email",req.getParameter("email"));
        doGet(req,resp);
    }

    @SneakyThrows
    private void onLoginSuccess(UserDto user, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user",user);
        resp.sendRedirect("/");
    }
}
