package com.conference.servlets;

import com.conference.entities.User;
import com.conference.service.UpdateUserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UpdateUser", value = "/UpdateUser")
public class UpdateUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String name = request.getParameter("name");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String notify = request.getParameter("notify");
        UpdateUserService.updateUser(name, lastname, email, notify, user);
        response.sendRedirect("Profile");
    }
}
