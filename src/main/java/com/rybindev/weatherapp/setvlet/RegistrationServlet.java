package com.rybindev.weatherapp.setvlet;

import com.rybindev.weatherapp.ApplicationContext;
import com.rybindev.weatherapp.ThymeleafConfiguration;
import com.rybindev.weatherapp.dto.CreateUserDto;
import com.rybindev.weatherapp.exception.ValidationException;
import com.rybindev.weatherapp.service.UserService;
import com.rybindev.weatherapp.validator.Error;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.List;

import static com.rybindev.weatherapp.util.UrlPath.*;

@WebServlet(REGISTRATION)
public class RegistrationServlet extends HttpServlet {
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

        templateEngine.process("registration", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        CreateUserDto createUserDto = new CreateUserDto(email, password);

        try {
            userService.register(createUserDto);
            resp.sendRedirect(LOGIN);
        } catch (ValidationException e) {
            resp.setStatus(400);
            req.setAttribute("errors", e.getErrors());
            doGet(req, resp);
        } catch (Exception e) {
            Throwable exception = e;

            while (exception != null) {
                if (exception instanceof ConstraintViolationException) {
                    resp.setStatus(400);
                    req.setAttribute("errors", List.of(Error.of("email.constraint.violation", "Email already exists.")));
                    doGet(req, resp);
                    return;
                }
                exception = exception.getCause();
            }

            throw e;
        }
    }
}
