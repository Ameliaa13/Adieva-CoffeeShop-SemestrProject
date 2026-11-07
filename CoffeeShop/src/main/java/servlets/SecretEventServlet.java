package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.EventModel;
import model.SecretRiddleModel;
import model.UserModel;
import service.SecretRiddleService;
import service.impl.SecretRiddleServiceImpl;
import utils.ValidationUtils;

import java.io.IOException;

@WebServlet("/secret-event")
public class SecretEventServlet extends HttpServlet {

    private final SecretRiddleService secretRiddleService = SecretRiddleServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserModel user = session == null ? null : (UserModel) session.getAttribute("user");

        if(user == null){
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }

        Long eventId = parseId(req.getParameter("id"));

        if(eventId == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        showSecretEvent(req, resp, user, eventId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        UserModel user = session == null ? null : (UserModel) session.getAttribute("user");

        if(user == null){
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }
        Long eventId = parseId(req.getParameter("id"));
        if (eventId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try{
            String action = req.getParameter("action");

            if("regenerate".equals(action)){
                secretRiddleService .regenerate(user.getId(), eventId);
                redirect(req, resp, eventId, "new-riddle");
                return;
            }

            if("answer".equals(action)){
                boolean correct = secretRiddleService.checkAnswer(user.getId(), eventId, req.getParameter("answer"));
                if (correct) {
                    resp.sendRedirect(req.getContextPath() + "/event?id=" + eventId + "&riddle=solved");
                    return;
                }
                redirect(req, resp, eventId, correct ? "correct" : "incorrect");
                return;
            }

            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }catch (ValidationUtils.ValidationException e){
            redirect(req, resp, eventId, "invalid-answer");
        }catch (RuntimeException e){
            getServletContext().log("Не удалось обработать секретное мероприятие.", e);
        }
        redirect(req, resp, eventId, "error");
    }


    private void showSecretEvent(HttpServletRequest req, HttpServletResponse resp, UserModel user, Long eventId) throws ServletException, IOException{
        try{
            EventModel event = secretRiddleService.getSecretEvent(eventId);
            SecretRiddleModel riddle = secretRiddleService.getCurrentOrGenerate(user.getId(), eventId);

            req.setAttribute("event", event);
            req.setAttribute("riddle", riddle);
            req.setAttribute("resultMessage", getResultMessage(req.getParameter("result")));
            req.getRequestDispatcher("/jsp/secret-event.jsp").forward(req, resp);
        }catch (ValidationUtils.ValidationException e){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }catch (RuntimeException e){
            getServletContext().log("Не удалось загрузить загадку.", e);
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Не удалось получить загадку. Попробуй позже.");
        }
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

    private void redirect(HttpServletRequest req, HttpServletResponse resp, Long eventId, String result) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/secret-event?id=" + eventId + "&result=" + result);
    }

    private String getResultMessage(String result) {
        if (result == null) {
            return null;
        }

        return switch (result) {
            case "new-riddle" -> "Для тебя подготовлена новая загадка.";
            case "correct" -> "Верно! Ты разгадала мероприятие.";
            case "incorrect" -> "Пока не угадала. Попробуй ещё раз.";
            case "invalid-answer" -> "Напиши ответ перед отправкой.";
            case "error" -> "Не удалось выполнить действие. Попробуй позже.";
            default -> null;
        };
    }
}
