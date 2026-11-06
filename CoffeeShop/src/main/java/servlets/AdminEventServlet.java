package servlets;

import enums.EventStatus;
import enums.EventVisibility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.EventModel;
import service.EventService;
import service.impl.EventServiceImpl;
import utils.AdminServletUtils;
import utils.ValidationUtils;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Locale;

@WebServlet("/admin/events")
public class AdminEventServlet extends HttpServlet {

    private final EventService eventService = EventServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        showEvents(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("save".equals(action)) {
                saveEvent(req);
            } else if ("delete".equals(action)) {
                deleteEvent(req);
            } else {
                throw new IllegalArgumentException("Неизвестное действие.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/events");

        } catch (ValidationUtils.ValidationException | IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showEvents(req, resp);

        } catch (RuntimeException e) {
            getServletContext().log("Не удалось выполнить действие с мероприятием.", e);
            req.setAttribute("errorMessage", "Не удалось выполнить действие. Проверь данные и попробуй ещё раз.");
            showEvents(req, resp);
        }
    }

    private void showEvents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("events", eventService.getAllEvents());

        String idParameter = req.getParameter("id");

        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long id = AdminServletUtils.requireLong(req, "id", "Номер мероприятия");

                eventService.getEventById(id).ifPresentOrElse(
                        event -> req.setAttribute("editingEvent", event),
                        () -> req.setAttribute("errorMessage", "Мероприятие не найдено.")
                );

            } catch (ValidationUtils.ValidationException | IllegalArgumentException e) {
                req.setAttribute("errorMessage", e.getMessage());
            }
        }

        req.getRequestDispatcher("/jsp/admin/events.jsp").forward(req, resp);
    }

    private void saveEvent(HttpServletRequest req) {
        String idParameter = req.getParameter("id");

        EventModel event = EventModel.builder()
                .title(AdminServletUtils.requireText(req, "title", "Название"))
                .description(AdminServletUtils.emptyToNull(req.getParameter("description")))
                .startTime(parseStartTime(req))
                .durationMinutes(parseOptionalInteger(
                        req.getParameter("durationMinutes"), "Длительность"))
                .capacity(parseRequiredInteger(req, "capacity", "Количество мест"))
                .visibility(parseVisibility(req))
                .status(parseStatus(req))
                .imageUrl(AdminServletUtils.emptyToNull(req.getParameter("imageUrl")))
                .build();

        if (idParameter == null || idParameter.isBlank()) {
            eventService.createEvent(event);
            return;
        }

        Long id = AdminServletUtils.requireLong(req, "id", "Номер мероприятия");

        if (!eventService.updateEventById(id, event)) {
            throw new IllegalArgumentException("Мероприятие для изменения не найдено.");
        }
    }

    private void deleteEvent(HttpServletRequest req) {
        Long id = AdminServletUtils.requireLong(req, "id", "Номер мероприятия");

        if (!eventService.deleteEventById(id)) {
            throw new IllegalArgumentException("Мероприятие для удаления не найдено.");
        }
    }

    private LocalDateTime parseStartTime(HttpServletRequest req) {
        String value = AdminServletUtils.requireText(req, "startTime", "Дата и время");

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Укажи корректные дату и время мероприятия.");
        }
    }

    private Integer parseRequiredInteger(HttpServletRequest req, String parameterName, String fieldTitle) {
        String value = AdminServletUtils.requireText(req, parameterName, fieldTitle);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("В поле «" + fieldTitle + "» должно быть целое число.");
        }
    }

    private Integer parseOptionalInteger(String value, String fieldTitle) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("В поле «" + fieldTitle + "» должно быть целое число.");
        }
    }

    private EventVisibility parseVisibility(HttpServletRequest req) {
        String value = AdminServletUtils.requireText(req, "visibility", "Видимость");

        try {
            return EventVisibility.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Выбери корректную видимость мероприятия.");
        }
    }

    private EventStatus parseStatus(HttpServletRequest req) {
        String value = AdminServletUtils.requireText(req, "status", "Статус");

        try {
            return EventStatus.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Выбери корректный статус мероприятия.");
        }
    }
}