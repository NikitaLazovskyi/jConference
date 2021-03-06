package com.conference.servlets;

import com.conference.connection.DBCPool;
import com.conference.dao.LanguageDAO;
import com.conference.dao.TagDAO;
import com.conference.service.AddTagService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet(name = "AddTag", value = "/AddTag")
public class AddTag extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> locales = (List<String>) request.getServletContext().getAttribute("locales");
        Map<String, Object> attributes = AddTagService.pack(locales);
        attributes.forEach(request::setAttribute);
        request.getRequestDispatcher("add-tag.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBCPool pool = (DBCPool) request.getServletContext().getAttribute("pool");
        Connection connection = pool.getConnection();
        List<String> locales = (List<String>) request.getServletContext().getAttribute("locales");
        Map<String, String> map = new LanguageDAO().select(connection);
        Map<String, String> languages = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (locales.contains(entry.getKey())) {
                languages.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String, String> tags = new HashMap<>();
        for (Map.Entry<String, String> entry : languages.entrySet()) {
            String locale = entry.getKey();
            String translation = request.getParameter(locale);
            tags.put(locale, translation);
        }
        boolean res = AddTagService.addTag(connection, tags);
        if (res) {
            response.sendRedirect("Profile");
        } else {
            response.sendRedirect("Error");
        }
        pool.putBackConnection(connection);
    }
}
