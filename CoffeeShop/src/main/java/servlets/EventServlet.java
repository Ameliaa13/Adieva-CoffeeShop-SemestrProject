package servlets;

import enums.EventRegistrationResult;
import enums.EventStatus;
import enums.EventVisibility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.EventModel;
import model.UserModel;
import service.EventRegistrationService;
import service.EventService;
import service.SecretRiddleService;
import service.impl.EventRegistrationServiceImpl;
import service.impl.EventServiceImpl;
import service.impl.SecretRiddleServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(urlPatterns = {"/events", "/event"})
public class EventServlet extends HttpServlet {

    private final EventService eventService = EventServiceImpl.getInstance();
    private final EventRegistrationService eventRegistrationService = EventRegistrationServiceImpl.getInstance();
    private final SecretRiddleService secretRiddleService = SecretRiddleServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/event".equals(req.getServletPath())) {
            showEvent(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        UserModel user = session == null ? null : (UserModel) session.getAttribute("user");

        req.setAttribute("events", user == null ? eventService.getUpcomingEvents() : eventService.getUpcomingEventsForMembers());
        req.getRequestDispatcher("/jsp/events.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        UserModel user = session == null ? null : (UserModel) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }

        Long eventId = parseId(req.getParameter("id"));
        if (eventId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String action = req.getParameter("action");

        Optional<EventModel> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (eventOptional.get().getVisibility() == EventVisibility.SECRET
                && !secretRiddleService.hasSolvedEvent(user.getId(), eventId)) {
            resp.sendRedirect(req.getContextPath() + "/secret-event?id=" + eventId);
            return;
        }

        if ("register".equals(action)) {
            EventRegistrationResult result = eventRegistrationService.registerForEvent(user.getId(), eventId);

            redirectWithResult(req, resp, eventId, getResultCode(result));
            return;
        }

        if ("cancel".equals(action)) {
            boolean cancelled = eventRegistrationService.cancelRegistration(user.getId(), eventId);

            redirectWithResult(req, resp, eventId, cancelled ? "cancelled" : "cancel-failed");
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void showEvent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long eventId = parseId(req.getParameter("id"));

        if (eventId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<EventModel> eventOptional = eventService.getEventById(eventId);

        if (eventOptional.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        EventModel event = eventOptional.get();

        HttpSession session = req.getSession(false);
        UserModel user = session == null ? null : (UserModel) session.getAttribute("user");

        boolean secretEventSolved = false;
        if (event.getVisibility() == EventVisibility.SECRET) {
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/sign-in");
                return;
            }

            secretEventSolved = secretRiddleService.hasSolvedEvent(user.getId(), eventId);
            if (!secretEventSolved) {
                resp.sendRedirect(req.getContextPath() + "/secret-event?id=" + eventId);
                return;
            }
        }

        int registeredCount = eventRegistrationService.getRegistrationCount(eventId);
        int freePlaces = Math.max(0, event.getCapacity() - registeredCount);

        boolean registered = user != null && eventRegistrationService
                .getRegistration(user.getId(), eventId)
                .isPresent();

        boolean registrationAvailable =
                event.getStatus() == EventStatus.PLANNED
                        && (event.getVisibility() == EventVisibility.OPEN || secretEventSolved)
                        && event.getStartTime().isAfter(LocalDateTime.now());

        req.setAttribute("event", event);
        req.setAttribute("freePlaces", freePlaces);
        req.setAttribute("registered", registered);
        req.setAttribute("canRegister", registrationAvailable && freePlaces > 0);
        String registrationMessage = getMessage(req.getParameter("registration"));
        if (secretEventSolved && "solved".equals(req.getParameter("riddle"))) {
            registrationMessage = "Верно! Ты разгадала мероприятие. Теперь можно посмотреть подробности и записаться.";
        }
        req.setAttribute("registrationMessage", registrationMessage);

        req.getRequestDispatcher("/jsp/event-item.jsp").forward(req, resp);
    }

    private Long parseId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            long id = Long.parseLong(value);
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getResultCode(EventRegistrationResult result) {
        return switch (result) {
            case REGISTERED -> "registered";
            case ALREADY_REGISTERED -> "already-registered";
            case FULL -> "full";
            case EVENT_NOT_AVAILABLE -> "not-available";
        };
    }

    private String getMessage(String result) {
        if (result == null) {
            return null;
        }

        return switch (result) {
            case "registered" -> "Ты записана на мероприятие!";
            case "already-registered" -> "Ты уже записана на это мероприятие.";
            case "full" -> "Свободных мест больше нет.";
            case "not-available" -> "Запись на это мероприятие закрыта.";
            case "cancelled" -> "Запись отменена.";
            case "cancel-failed" -> "Не удалось отменить запись.";
            default -> null;
        };
    }

    private void redirectWithResult(HttpServletRequest req, HttpServletResponse resp, Long eventId, String result) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/event?id=" + eventId + "&registration=" + result);
    }
}
