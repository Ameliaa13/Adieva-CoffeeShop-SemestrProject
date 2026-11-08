package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.Services;
import model.EventModel;
import service.EventService;
import service.PromotionService;
import enums.EventVisibility;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class HomeServlet extends HttpServlet {

    private EventService eventService;
    private PromotionService promotionService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        eventService = services.get(EventService.class);
        promotionService = services.get(PromotionService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("promotions", promotionService.getActivePromotions());

        List<EventModel> upcomingEvents = eventService.getUpcomingEvents();

        EventModel nextEvent = upcomingEvents.stream()
                .filter(event -> event.getVisibility() == EventVisibility.OPEN)
                .findFirst()
                .orElse(null);

        req.setAttribute("nextEvent", nextEvent);

        req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
    }
}