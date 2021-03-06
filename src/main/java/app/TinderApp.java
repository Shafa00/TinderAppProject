package app;

import app.servlets.*;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import java.util.EnumSet;


public class TinderApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8181);
        ServletContextHandler handler = new ServletContextHandler();

        TemplateEngine engine = TemplateEngine.folder("content");

        handler.addServlet(new ServletHolder(new LoginServlet(engine)), "/login");
        handler.addServlet(RegistrationServlet.class, "/reg")
                .getRegistration().setMultipartConfig(new MultipartConfigElement("./image"));
        handler.addServlet(new ServletHolder(new LikedServlet(engine)), "/liked");
        handler.addServlet(new ServletHolder(new DislikedServlet(engine)), "/disliked");
        handler.addServlet(new ServletHolder(new MessagesServlet(engine)), "/messages");
        handler.addServlet(new ServletHolder(new UserServlet(engine)), "/users");
        handler.addServlet(new ServletHolder(new LogoutServlet()), "/logout" );
        handler.addServlet(new ServletHolder(new MainServlet()), "/main");
        handler.addServlet(new ServletHolder(new FrontEndServlet("css")), "/css/*");
        handler.addServlet(new ServletHolder(new FrontEndServlet("image")), "/image/*");

        handler.addFilter(new FilterHolder(new CookieFilter()), "/liked", EnumSet.of(DispatcherType.REQUEST));
        handler.addFilter(new FilterHolder(new CookieFilter()), "/disliked", EnumSet.of(DispatcherType.REQUEST));
        handler.addFilter(new FilterHolder(new CookieFilter()), "/messages", EnumSet.of(DispatcherType.REQUEST));
        handler.addFilter(new FilterHolder(new CookieFilter()), "/users", EnumSet.of(DispatcherType.REQUEST));
        handler.addFilter(new FilterHolder(new CookieFilter()), "/logout", EnumSet.of(DispatcherType.REQUEST));

        server.setHandler(handler);
        server.start();
        server.join();


    }
}
