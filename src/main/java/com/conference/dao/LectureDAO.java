package com.conference.dao;

import com.conference.entities.Lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LectureDAO  {
    private static final UserDAO udao = new UserDAO();
    private static final EventDAO edao = new EventDAO();

    public boolean insertLecture(Connection c, Lecture lecture) {
        try (PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO lectures (id, topic, status, event, speaker) VALUES (DEFAULT,?,?,?,?)")) {
            ps.setString(1, lecture.getTopic());
            ps.setInt(2, lecture.getStatus());
            ps.setInt(3, lecture.getEvent().getId());
            ps.setInt(4, lecture.getSpeaker().getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertFreeLecture(Connection c, Lecture lecture) {
        try (PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO lectures (topic, status, event) VALUES (?,?,?)")) {
            ps.setString(1, lecture.getTopic());
            ps.setInt(2, lecture.getStatus());
            ps.setInt(3, lecture.getEvent().getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Lecture> select(Connection c, int event, int status) {
        List<Lecture> lectures = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM lectures WHERE (status = ?) AND (event = ?) ORDER BY id")
        ) {
            ps.setInt(1, status);
            ps.setInt(2, event);
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                lectures.add(new Lecture(
                        set.getInt("id"),
                        set.getString("topic"),
                        set.getInt("status"),
                        edao.select(c, "id", set.getInt("event"), "1", 0, "date, fromtime", "en").get(0),
                        udao.getByID(c,set.getInt("speaker"))
                ));
            }
            set.close();
            return lectures;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lecture> selectByStatus(Connection c,int status) {
        List<Lecture> lectures = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM lectures WHERE status = ? ORDER BY id")
        ) {
            ps.setInt(1, status);
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                lectures.add(new Lecture(
                        set.getInt("id"),
                        set.getString("topic"),
                        set.getInt("status"),
                        edao.select(c, "id", set.getInt("event"), "1", 0, "date, fromtime", "en").get(0),
                        udao.getByID(c,set.getInt("speaker"))
                ));
            }
            set.close();
            return lectures;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lecture> selectNotRequested(Connection c, int speaker) {
        List<Lecture> lectures = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM lectures WHERE status = 0 ORDER BY id");
             PreparedStatement req = c.prepareStatement(
                     "SELECT lecture FROM requests WHERE speaker = ?")
        ){
            req.setInt(1,speaker);
            ResultSet requestedSet = req.executeQuery();
            List<Integer> requestedLectures = new ArrayList<>();
            while (requestedSet.next()){
                requestedLectures.add(requestedSet.getInt(1));
            }
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                if (!requestedLectures.contains(id)) {
                    lectures.add(new Lecture(
                            id,
                            set.getString("topic"),
                            set.getInt("status"),
                            edao.select(c, "id", set.getInt("event"), "1", 0, "date, fromtime", "en").get(0),
                            udao.getByID(c,set.getInt("speaker"))
                    ));
                }
            }
            set.close();
            return lectures;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lecture> selectBySpeaker(Connection c, int status, int speaker) {
        List<Lecture> lectures = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM lectures WHERE (status = ?) AND (speaker = ?) ORDER BY id")
        ) {
            ps.setInt(1, status);
            ps.setInt(2, speaker);
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                lectures.add(new Lecture(
                        set.getInt("id"),
                        set.getString("topic"),
                        set.getInt("status"),
                        edao.select(c, "id", set.getInt("event"), "1", 0, "date, fromtime", "en").get(0),
                        udao.getByID(c,set.getInt("speaker"))
                ));
            }
            set.close();
            return lectures;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int selectCount(Connection c, int event, int status) {
        int count = 0;
        try (PreparedStatement ps = c.prepareStatement(
                     "SELECT count(*) FROM lectures WHERE (event = ?) AND (status = ?) ")
        ) {
            ps.setInt(1, event);
            ps.setInt(2, status);
            ResultSet set = ps.executeQuery();
            set.next();
            count = set.getInt(1);
            set.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean rejectOffer(Connection c, int lecture){
        try(PreparedStatement ps = c.prepareStatement(
            "UPDATE lectures SET status = -1 WHERE id = ?")){
            ps.setInt(1,lecture);
            ps.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean acceptOffer(Connection c, int lecture){
        try(PreparedStatement ps = c.prepareStatement(
                    "UPDATE lectures SET status = 3 WHERE id = ?")){
            ps.setInt(1,lecture);
            ps.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
