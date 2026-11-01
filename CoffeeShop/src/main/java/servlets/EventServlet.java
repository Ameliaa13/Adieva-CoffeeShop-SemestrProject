package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.EventService;
import service.impl.EventServiceImpl;

import java.io.IOException;

@WebServlet("/events")
public class EventServlet extends HttpServlet {

    private final EventService eventService = EventServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("events", eventService.getUpcomingEvents());

        req.getRequestDispatcher("/jsp/events.jsp").forward(req, resp);
    }
}
