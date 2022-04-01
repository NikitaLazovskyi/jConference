package com.conference.servlets;

import com.conference.connection.DBCPool;
import com.conference.dao.EventDAO;
import com.conference.dao.TagDAO;
import com.conference.dao.UserDAO;
import com.conference.entity.Event;
import com.conference.entity.Tag;
import com.conference.entity.User;
import com.conference.util.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "UpdateEvent", value = "/UpdateEvent")
public class UpdateEvent extends HttpServlet {
    public static final Logger logger = LoggerFactory.getLogger(UpdateEvent.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBCPool pool = (DBCPool) request.getServletContext().getAttribute("pool");
        Connection connection = pool.getConnection();
        String locale = (String) request.getSession().getAttribute("lang");
        List<Tag> tags = new TagDAO().selectForLocale(connection, locale);
        int eventId;
        try {
            eventId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("Error");
            return;
        }

        EventDAO edao = new EventDAO();
        Event event = edao.select(connection, "id", eventId, "1", 0, "id", locale).get(0);
        request.setAttribute("event", event);
        request.setAttribute("tags", tags);
        pool.putBackConnection(connection);
        request.getRequestDispatcher("update-event.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBCPool pool = (DBCPool) request.getServletContext().getAttribute("pool");
        Connection connection = pool.getConnection();
        TagDAO tdao = new TagDAO();
        List<Tag> allTags = tdao.select(connection);
        List<Tag> tagOfEvent = new ArrayList<>();
        for (Tag tag : allTags) {
            String tagParameter = request.getParameter("tag" + tag.getId());
            if (tagParameter != null) {
                tagOfEvent.add(tag);
            }
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String topic = request.getParameter("topic");
        String date = request.getParameter("date");
        String fromtime = request.getParameter("fromtime");
        String totime = request.getParameter("totime");
        String location = request.getParameter("location");
        Event event = new Event(id, topic, tagOfEvent, fromtime, totime, date, location, 1);
        EventDAO edao = new EventDAO();
        Map<String, String> changes = edao.updateEvent(connection, event);
        Set<User> recipients = edao.selectRecipients(connection, event.getId());
        MailSender.getInstance().sendChangesMessages(changes, recipients, event);

        if (changes != null && !changes.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("SUCCESSFUL UPDATING EVENT - TOPIC:{}, DATE:{}, TIME:{}, LOCATION:{}", topic, date, fromtime + "-" + totime, location);
            }
            response.sendRedirect("Profile");
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("FAILURE UPDATING EVENT - TOPIC:{}, DATE:{}, TIME:{}, LOCATION:{}", topic, date, fromtime + "-" + totime, location);
            }
            request.setAttribute("message", "Oops. Something goes wrong");
            request.getRequestDispatcher("error-page.jsp").forward(request, response);
        }
        pool.putBackConnection(connection);
    }
}
