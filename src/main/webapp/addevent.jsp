<%@ page import="com.conference.bean.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.conference.dao.UserDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- font-family: 'Poiret One', cursive; -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poiret+One&display=swap" rel="stylesheet">
    <!-- font-family: 'Nixie One', cursive; -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nixie+One&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

    <link rel="stylesheet" href="styles.css">
    <title>Create event</title>
</head>
<body>
  <nav class="container-xl navbar sticky-top rowsa">
    <!-- Navbar content -->
    <div>
      <a class="btn btn-grey" href="homepage.jsp">jConference</a>
      <a class="btn btn-info" href="">FAQ</a>
      <a class="btn btn-info" href="events.jsp">Events</a>
    </div>
    <div>
      <form class="d-flex">
        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" style="width: 30rem;">
        <button class="btn btn-outline-info" type="submit">Search</button>
      </form>
    </div>
    <div>
      <a class="btn btn-info" href="profile.jsp">Profile</a>
      <a class="btn btn-blue" href="">RU</a>
    </div>
  </nav>

  <section class="container-xl col">
    <!--Add event-->
    <div class="margin col reg-sec addevent">
      <h1 class="display-6">Create event</h1>
        <form id="event-form" class="col margin" action="${pageContext.request.contextPath}/AddEvent" method="post">
          <div style="margin-top: 1rem; line-height: 0">
            <h4>Topic</h4>
            <input name="topic" class="form-control reg" type="text" placeholder="" aria-label="Search" required>
          </div>
          <div style="margin-top: 1rem; line-height: 0">
            <h4>Description</h4>
            <textarea name="description" class="form-control reg" rows="4" cols="50" form="event-form" required></textarea>
          </div>
          <div style="margin-top: 1rem; line-height: 0">
            <h4>Speaker</h4>
            <select name="speaker" class="form-control reg"  required>
              <%!List<User> speakers;%>
              <%speakers = new UserDAO().selectSpeakers();%>
              <%for (User speaker : speakers) {%>
              <option value="<%=speaker.getId()%>"><%=speaker.getName() + " " + speaker.getLastname() + " " + speaker.getId()%></option>
              <%}%>
            </select>
          </div>
          <div style="margin-top: 1rem; line-height: 0">
            <h4>Date</h4>
            <input name="date" class="form-control reg" type="date" placeholder="" aria-label="Search" required>
          </div>
          <div style="margin-top: 1rem; line-height: 0">
            <h4>Time</h4>
            <input name="time" class="form-control reg" type="time" placeholder="" aria-label="Search" required>
          </div>
          <div style="margin-top: 1rem; line-height: 0">
            <h4>Address</h4>
            <input name="location" class="form-control reg" type="text" placeholder="" aria-label="Search" required>
          </div>
          <div style="margin-top: 1rem; line-height: 0">
            <div class="rad-div">
              <div class="form-check">
                <input value="online" class="form-check-input" type="radio" name="condition" id="flexRadioDefault1" checked>
                <label class="form-check-label" for="flexRadioDefault1">
                  Online meeting
                </label>
              </div>
              <div class="form-check">
                <input value="offline" class="form-check-input" type="radio" name="condition" id="flexRadioDefault2">
                <label class="form-check-label" for="flexRadioDefault2">
                  Offline meeting
                </label>
              </div>
            </div>
          </div>
          <p></p> <button class="btn btn-info" type="submit">Confirm</button>
        </form>
    </div>
  </section>

  <!--Footer-->
  <section class="container-xl rowsb">
    <div id="reg-sec" class="margin col">
      All rights reserved 
    </div>
  </section>


    <script src="https://code.iconify.design/2/2.1.1/iconify.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>