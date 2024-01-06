package com.rybindev.weatherapp.setvlet;

import com.rybindev.weatherapp.ApplicationContext;
import com.rybindev.weatherapp.ThymeleafConfiguration;
import com.rybindev.weatherapp.dto.UserDto;
import com.rybindev.weatherapp.service.UserLocationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

import static com.rybindev.weatherapp.util.UrlPath.*;

@WebServlet(HOME)
public class HomeServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    private UserLocationService userLocationService;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        userLocationService = ApplicationContext.userLocationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        if (user != null){
            context.setVariable("locations", userLocationService.getUserLocation(user.getId()));
        }


        templateEngine.process("home", context, resp.getWriter());
    }

}
