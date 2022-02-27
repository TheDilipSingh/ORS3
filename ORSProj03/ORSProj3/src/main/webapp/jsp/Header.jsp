<%@page import="in.co.rays.ORSProj3.controller.LoginCtl"%>
<%@page import="in.co.rays.ORSProj3.dto.RoleDTO"%>
<%@page import="in.co.rays.ORSProj3.controller.ORSView"%>
<%@page import="in.co.rays.ORSProj3.dto.UserDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>


</head>
<body>
<% UserDTO userDTO=(UserDTO)session.getAttribute("user");

	boolean userLoggedIn= userDTO != null;
	
	String welcomeMsg = "Hi,";
	
	if(userLoggedIn)
	{
		String role=(String)session.getAttribute("role");
		welcomeMsg +=" "+ userDTO.getFirstName()+" (" +role+ ")";
	}
	else
	{
		welcomeMsg+="  Guest";
	}
%>
<div class="navbar-wrapper">
<nav class="navbar navbar-expand-md bg-white navbar-light">
  <!-- Brand/logo -->
  <a class="navbar-brand" href="<%=ORSView.WELCOME_CTL%>">
    <img src="/ORSProj3/img/customLogo.png" alt="logo" style="width:150px; height:60px; margin-left: -10px; margin-bottom: -10px; margin-top: -10px" >
  </a>
   <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
    <span class="navbar-toggler-icon"></span>
  </button>
         
   <div class="collapse navbar-collapse" id="collapsibleNavbar">
    <!-- ROLE = ADMIN -->
       
   <!-- Links -->
   <%if (userLoggedIn) { %>
   
   
   <%   if(userDTO.getRoleId() == RoleDTO.ADMIN) { %>
   <ul class="nav navbar-nav justify-content-center">
    <!-- Dropdown -->
    <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user"></i>&nbsp;User</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.USER_CTL %>"><i class="fas fa-user-plus"></i>&nbsp;Add User</a>
        <a class="dropdown-item" href="<%=ORSView.USER_LIST_CTL %>"><i class="fas fa-address-book"></i>&nbsp;User List</a>
        </div>
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-file-alt"></i>&nbsp;Marksheet</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.MARKSHEET_CTL %>"><i class="fas fa-folder-plus"></i>&nbsp;Add Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;Marksheet List</a>
        <a class="dropdown-item" href="<%=ORSView.GET_MARKSHEET_CTL %>"><i class="fas fa-graduation-cap"></i>&nbsp;Get Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL %>"><i class="far fa-chart-bar"></i>&nbsp;Marksheet Merit List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-university"></i>&nbsp;College</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.COLLEGE_CTL %>"><i class="fas fa-folder-plus"></i>&nbsp;Add College</a>
        <a class="dropdown-item" href="<%=ORSView.COLLEGE_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;College List</a>
        </div>
    </li>
    
     </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-tag"></i>&nbsp;Role</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.ROLE_CTL %>"><i class="fas fa-folder-plus"></i>&nbsp;Add Role</a>
        <a class="dropdown-item" href="<%=ORSView.ROLE_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;Role List</a>
        </div>
    </li>
    
     </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-graduate"></i>&nbsp;Student</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.STUDENT_CTL %>"><i class="fas fa-user-plus"></i>&nbsp;Add Student</a>
        <a class="dropdown-item" href="<%=ORSView.STUDENT_LIST_CTL %>"><i class="fas fa-users"></i>&nbsp;Student List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book-reader"></i>&nbsp;Course</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.COURSE_CTL %>"><i class="fas fa-book-medical"></i>&nbsp;Add Course</a>
        <a class="dropdown-item" href="<%=ORSView.COURSE_LIST_CTL%>"><i class="fas fa-th-list"></i>&nbsp;Course List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book"></i>&nbsp;Subject</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.SUBJECT_CTL %>"><i class="fas fa-book-medical"></i>&nbsp;Add Subject</a>
        <a class="dropdown-item" href="<%=ORSView.SUBJECT_LIST_CTL %>"><i class="fas fa-th-list"></i>&nbsp;Subject List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-tie"></i>&nbsp;Faculty</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.FACULTY_CTL %>"><i class="fas fa-user-plus"></i>&nbsp;Add Faculty</a>
        <a class="dropdown-item" href="<%=ORSView.FACULTY_LIST_CTL%>"><i class="fas fa-address-book"></i>&nbsp;Faculty List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="far fa-calendar-alt"></i>&nbsp;Timetable</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.TIMETABLE_CTL %>"><i class="far fa-calendar-plus"></i>&nbsp;Add Timetable</a>
        <a class="dropdown-item" href="<%=ORSView.TIMETABLE_LIST_CTL %>"><i class="far fa-calendar-check"></i>&nbsp;Timetable List</a>
        </div>
    </li>
    
       </li>
      <a target="blank" class="nav-link" href="<%=ORSView.JAVA_DOC_VIEW %>" id="navbardrop"><i class="far fa-file-alt"></i>&nbsp;Java Doc</a>
    </li>
    
    
    
  </ul>
 
  <% } %>
  
  <% if (userDTO.getRoleId() == RoleDTO.STUDENT) { %>
  
   <ul class="nav navbar-nav justify-content-center">
    <!-- Dropdown -->
 
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-file-alt"></i>&nbsp;Marksheet</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.GET_MARKSHEET_CTL %>"><i class="fas fa-graduation-cap"></i>&nbsp;Get Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL %>"><i class="far fa-chart-bar"></i>&nbsp;Marksheet Merit List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-university"></i>&nbsp;College</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.COLLEGE_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;College List</a>
        </div>
    </li>
    
    
     </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-graduate"></i>&nbsp;Student</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.STUDENT_LIST_CTL %>"><i class="fas fa-users"></i>&nbsp;Student List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book-reader"></i>&nbsp;Course</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.COURSE_LIST_CTL %>"><i class="fas fa-th-list"></i>&nbsp;Course List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book"></i>&nbsp;Subject</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.SUBJECT_LIST_CTL %>"><i class="fas fa-th-list"></i>&nbsp;Subject List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-tie"></i>&nbsp;Faculty</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.FACULTY_LIST_CTL %>"><i class="fas fa-address-book"></i>&nbsp;Faculty List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="far fa-calendar-alt"></i>&nbsp;Timetable</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.TIMETABLE_LIST_CTL %>"><i class="far fa-calendar-check"></i>&nbsp;Timetable List</a>
        </div>
    </li>
  </ul>
  
  <% } %>
 	
 	<%  if (userDTO.getRoleId() == RoleDTO.KIOSK) { %>
 	
 		   <ul class="nav navbar-nav justify-content-center">
    <!-- Dropdown -->
       <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-file-alt"></i>&nbsp;Marksheet</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.GET_MARKSHEET_CTL %>"><i class="fas fa-graduation-cap"></i>&nbsp;Get Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL %>"><i class="far fa-chart-bar"></i>&nbsp;Marksheet Merit List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-university"></i>&nbsp;College</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.COLLEGE_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;College List</a>
        </div>
    </li>
    
   
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book-reader"></i>&nbsp;Course</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.COURSE_LIST_CTL %>"><i class="fas fa-th-list"></i>&nbsp;Course List</a>
        </div>
    </li>
    
   
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="far fa-calendar-alt"></i>&nbsp;Timetable</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.TIMETABLE_LIST_CTL %>"><i class="far fa-calendar-check"></i>&nbsp;Timetable List</a>
        </div>
    </li>
    
  </ul>
  

 <% } %> 
 <% if (userDTO.getRoleId() == RoleDTO.FACULTY) { %>
 	   <ul class="nav navbar-nav justify-content-center">
    <!-- Dropdown -->
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-file-alt"></i>&nbsp;Marksheet</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.MARKSHEET_CTL %>"><i class="fas fa-folder-plus"></i>&nbsp;Add Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;Marksheet List</a>
        <a class="dropdown-item" href="<%=ORSView.GET_MARKSHEET_CTL %>"><i class="fas fa-graduation-cap"></i>&nbsp;Get Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL %>"><i class="far fa-chart-bar"></i>&nbsp;Marksheet Merit List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-university"></i>&nbsp;College</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.COLLEGE_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;College List</a>
        </div>
    </li>
    
    
     </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-graduate"></i>&nbsp;Student</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.STUDENT_CTL%>"><i class="fas fa-user-plus"></i>&nbsp;Add Student</a>
        <a class="dropdown-item" href="<%=ORSView.STUDENT_LIST_CTL %>"><i class="fas fa-users"></i>&nbsp;Student List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book-reader"></i>&nbsp;Course</a>
      <div class="dropdown-menu dropdown-menu-left">
        <a class="dropdown-item" href="<%=ORSView.COURSE_LIST_CTL%>"><i class="fas fa-th-list"></i>&nbsp;Course List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book"></i>&nbsp;Subject</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.SUBJECT_CTL%>"><i class="fas fa-book-medical"></i>&nbsp;Add Subject</a>
        <a class="dropdown-item" href="<%=ORSView.SUBJECT_LIST_CTL%>"><i class="fas fa-th-list"></i>&nbsp;Subject List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="far fa-calendar-alt"></i>&nbsp;Timetable</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.TIMETABLE_CTL%>"><i class="far fa-calendar-plus"></i>&nbsp;Add Timetable</a>
        <a class="dropdown-item" href="<%=ORSView.TIMETABLE_LIST_CTL%>"><i class="far fa-calendar-check"></i>&nbsp;Timetable List</a>
        </div>
    </li>
    
  </ul>
 
 
 <% } %>
 <%  if (userDTO.getRoleId() == RoleDTO.COLLEGE) { %>
 		  <ul class="nav navbar-nav justify-content-center">
    <!-- Dropdown -->

        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-file-alt"></i>&nbsp;Marksheet</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.MARKSHEET_CTL %>"><i class="fas fa-folder-plus"></i>&nbsp;Add Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_LIST_CTL %>"><i class="fas fa-list"></i>&nbsp;Marksheet List</a>
        <a class="dropdown-item" href="<%=ORSView.GET_MARKSHEET_CTL %>"><i class="fas fa-graduation-cap"></i>&nbsp;Get Marksheet</a>
        <a class="dropdown-item" href="<%=ORSView.MARKSHEET_MERIT_LIST_CTL %>"><i class="far fa-chart-bar"></i>&nbsp;Marksheet Merit List</a>
        </div>
    </li>
    
    
     </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-graduate"></i>&nbsp;Student</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.STUDENT_CTL %>"><i class="fas fa-user-plus"></i>&nbsp;Add Student</a>
        <a class="dropdown-item" href="<%=ORSView.STUDENT_LIST_CTL %>"><i class="fas fa-users"></i>&nbsp;Student List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book-reader"></i>&nbsp;Course</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.COURSE_CTL %>"><i class="fas fa-book-medical"></i>&nbsp;Add Course</a>
        <a class="dropdown-item" href="<%=ORSView.COURSE_LIST_CTL %>"><i class="fas fa-th-list"></i>&nbsp;Course List</a>
        </div>
    </li>
    
        </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-book"></i>&nbsp;Subject</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.SUBJECT_CTL %>"><i class="fas fa-book-medical"></i>&nbsp;Add Subject</a>
        <a class="dropdown-item" href="<%=ORSView.SUBJECT_LIST_CTL %>"><i class="fas fa-th-list"></i>&nbsp;Subject List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="fas fa-user-tie"></i>&nbsp;Faculty</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.FACULTY_CTL%>"><i class="fas fa-user-plus"></i>&nbsp;Add Faculty</a>
        <a class="dropdown-item" href="<%=ORSView.FACULTY_LIST_CTL%>"><i class="fas fa-address-book"></i>&nbsp;Faculty List</a>
        </div>
    </li>
    
    </li>
        <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"><i class="far fa-calendar-alt"></i>&nbsp;Timetable</a>
      <div class="dropdown-menu dropdown-menu-left">
	   <a class="dropdown-item" href="<%=ORSView.TIMETABLE_CTL%>"><i class="far fa-calendar-plus"></i>&nbsp;Add Timetable</a>
        <a class="dropdown-item" href="<%=ORSView.TIMETABLE_LIST_CTL%>"><i class="far fa-calendar-check"></i>&nbsp;Timetable List</a>
        </div>
    </li>
    

  </ul>
 
 
 
 <% } %>
 <% } %>
  <!-- Links -->
   <ul class="nav navbar-nav ml-auto">
    <!-- Dropdown -->
    <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"> <%=welcomeMsg %> </a>
      <div class="dropdown-menu dropdown-menu-right">
       
<%if (userLoggedIn) {%>
              
   <a class="dropdown-item" href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>"><i class="fas fa-sign-out-alt"></i>&nbsp;Logout</a>
        <a class="dropdown-item" href="<%=ORSView.MY_PROFILE_CTL %>"><i class="fas fa-user-edit"></i>&nbsp;My Profile</a>
        <a class="dropdown-item" href="<%=ORSView.CHANGE_PASSWORD_CTL %>"><i class="fas fa-key"></i>&nbsp;Change Password</a>
        
       <% } else {%>
           <a class="dropdown-item" href="<%=ORSView.LOGIN_CTL %>"><i class="fas fa-sign-in-alt"></i>&nbsp;Login</a>
        <a class="dropdown-item" href="<%=ORSView.USER_REGISTRATION_CTL %>"><i class="fas fa-user-plus"></i>&nbsp;User Registration</a>
        <a class="dropdown-item" href="<%=ORSView.FORGET_PASSWORD_CTL %>"><i class="fas fa-key"></i>&nbsp;Forget Password</a>
       <% } %>
       </div>
    </li>
  </ul>
</div>
</nav>
</div>
</body>
</html>