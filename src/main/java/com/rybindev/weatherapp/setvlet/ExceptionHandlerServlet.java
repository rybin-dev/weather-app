package com.rybindev.weatherapp.setvlet;

import com.rybindev.weatherapp.ThymeleafConfiguration;
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

@WebServlet(ERROR)
public class ExceptionHandlerServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req,resp);
    }

    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        // Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        // String servletName = (String) req.getAttribute("jakarta.servlet.error.servlet_name");
        Throwable throwable = (Throwable) req.getAttribute("jakarta.servlet.error.exception");

        if (throwable != null) {
            resp.setStatus(500);
            IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                    .buildExchange(req, resp);

            WebContext context = new WebContext(webExchange);

            templateEngine.process("error", context, resp.getWriter());

        }

    }
}
