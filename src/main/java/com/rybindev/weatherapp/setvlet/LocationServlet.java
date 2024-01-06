package com.rybindev.weatherapp.setvlet;

import com.rybindev.weatherapp.ApplicationContext;
import com.rybindev.weatherapp.ThymeleafConfiguration;
import com.rybindev.weatherapp.dto.UserDto;
import com.rybindev.weatherapp.service.UserLocationService;
import com.rybindev.weatherapp.service.WeatherService;
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

import static com.rybindev.weatherapp.util.UrlPath.*;

@WebServlet(LOCATIONS)
public class LocationServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    private UserLocationService userLocationService;

    private WeatherService weatherService;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        userLocationService = ApplicationContext.userLocationService;
        weatherService = ApplicationContext.weatherService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);

        String query = req.getParameter("query");
        context.setVariable("locations", weatherService.getWeatherLocation(query).getList());
        templateEngine.process("search", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto user = (UserDto) req.getSession().getAttribute("user");

        if (user != null) {
            switch (req.getParameter("action")) {
                case "add" -> {
                    try {
                        String latitude = req.getParameter("latitude");
                        String longitude = req.getParameter("longitude");

                        userLocationService.addUserLocation(user.getId(), Float.valueOf(latitude), Float.valueOf(longitude));

                        resp.sendRedirect(HOME);
                    } catch (Exception e) {
                        Throwable exception = e;

                        while (exception != null) {
                            if (exception instanceof ConstraintViolationException) {
                                resp.sendRedirect(HOME);
                                return;
                            }
                            exception = exception.getCause();
                        }

                        throw e;
                    }
                }
                case "delete" -> {
                    String locationId = req.getParameter("locationId");
                    userLocationService.deleteUserLocation(user.getId(), Long.valueOf(locationId));
                    resp.sendRedirect(HOME);
                }
            }

        }

    }

}
