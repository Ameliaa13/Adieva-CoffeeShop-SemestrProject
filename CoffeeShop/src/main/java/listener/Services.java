package listener;

import jakarta.servlet.ServletContext;

import java.util.Map;

public final class Services {

    private static final String ATTRIBUTE = "services";

    private final Map<Class<?>, Object> services;

    Services(Map<Class<?>, Object> services) {
        this.services = Map.copyOf(services);
    }

    static void bind(ServletContext context, Services services) {
        context.setAttribute(ATTRIBUTE, services);
    }

    static void unbind(ServletContext context) {
        context.removeAttribute(ATTRIBUTE);
    }

    public static Services from(ServletContext context) {
        Object services = context.getAttribute(ATTRIBUTE);

        if (services == null) {
            throw new IllegalStateException("Сервисы не инициализированы: AppContextListener не отработал.");
        }

        return (Services) services;
    }

    public <T> T get(Class<T> type) {
        Object service = services.get(type);

        if (service == null) {
            throw new IllegalStateException("Сервис не зарегистрирован: " + type.getSimpleName());
        }

        return type.cast(service);
    }

    int size() {
        return services.size();
    }
}