package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserModel;
import service.*;
import service.impl.*;

import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        UserModel userModel = session == null ? null : (UserModel) session.getAttribute("user");

        if(userModel == null){
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }
        if(userModel.getRole() == null || !userModel.getRole().equals("ADMIN")){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        req.getRequestDispatcher("/jsp/admin.jsp").forward(req, resp);

    }
}
