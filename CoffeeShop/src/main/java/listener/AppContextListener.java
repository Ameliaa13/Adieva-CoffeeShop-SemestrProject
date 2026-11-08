package listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.*;
import service.impl.*;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        Map<Class<?>, Object> map = new HashMap<>();

        register(map, AuthService.class, AuthServiceImpl.getInstance());
        register(map, CartItemService.class, CartItemServiceImpl.getInstance());
        register(map, EventRegistrationService.class, EventRegistrationServiceImpl.getInstance());
        register(map, EventService.class, EventServiceImpl.getInstance());
        register(map, FavoriteService.class, FavoriteServiceImpl.getInstance());
        register(map, MenuService.class, MenuServiceImpl.getInstance());
        register(map, OrderService.class, OrderServiceImpl.getInstance());
        register(map, ProfileService.class, ProfileServiceImpl.getInstance());
        register(map, PromotionService.class, PromotionServiceImpl.getInstance());
        register(map, SecretRiddleService.class, SecretRiddleServiceImpl.getInstance());

        Services services = new Services(map);
        Services.bind(context, services);

        context.log("Приложение запущено. Инициализировано сервисов: " + services.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        Services.unbind(context);
        context.log("Приложение остановлено.");
    }

    private <T> void register(Map<Class<?>, Object> map, Class<T> type, T service) {
        map.put(type, service);
    }
}