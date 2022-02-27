<%@page import="java.util.LinkedHashMap"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
<%@page import="in.co.rays.ORSProj3.controller.TimeTableCtl"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.ORSProj3.dto.TimeTableDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">

<title>Add/Update Timetable</title>
<style type="text/css">
body {
	background: url('/ORSProj3/img/455165.jpg') no-repeat center center fixed;
	background-size: cover;
	}

input[type=text], select {
	margin-bottom: 15px;
	padding-left: 35px;
}


input[type=password], select {
	margin-bottom: 15px;
	padding-left: 35px;
}

.form-group i {
	position: absolute;
}

.input-group i {
	position: relative;
}

.icon {
	margin-left: -5px;
	padding: 12px;
	min-width: 40px;
}

</style>
</head>

<body>
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.TimeTableDTO" scope="request"/>
<%@include file="Header.jsp" %>

	<%
	List <TimeTableDTO> courseList =(List<TimeTableDTO>)request.getAttribute("CourseList"); 
	List <TimeTableDTO> subjectList = (List<TimeTableDTO>)request.getAttribute("SubjectList");
	%>
	  
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  
 // var d = new Date();
  function disableSunday(d){
	  var day = d.getDay();
	  if(day==0)
	  {
	   return [false];
	  }
	  else
	  {
		  return [true];
	  }
  }
  
  $( function()
		  {
	  $( "#datepicker4" ).datepicker(
		{
		  changeMonth :true,
		  changeYear :true,
		  yearRange :'0:+2',
		  dateFormat:'dd-mm-yy',

// Disable for Sunday
		  beforeShowDay : disableSunday,		  
// Disable for back date
		  minDate : 0   
	  });
  } );
  </script>

<div class="container-fluid mt-2">
<div class="row justify-content-center">
<div class="col-md-4">

<form action="<%=ORSView.TIMETABLE_CTL %>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime())%>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime())%>">


<% if (dto != null && dto.getId() > 0 ) {%>
<div class="pb-4">
<h3 class="text-center">Update Timetable </h3>
</div>
<% } else { %>
<div class="pb-4">
<h3 class="text-center">Add Timetable </h3>
</div>
<% } %>

<%if(DataValidator.isNotNull(ServletUtility.getSuccessMessage(request))) { %>

<div class="alert alert-success">
<button type="button" class="close" data-dismiss="alert">&times;</button>
<span><i class="fas fa-info-circle text-success"></i> </span>
<%=ServletUtility.getSuccessMessage(request) %>
</div>
<% } %>

<%if(DataValidator.isNotNull(ServletUtility.getErrorMessage(request))) { %>

<div class="alert alert-danger alert-dismissible">
<button type="button" class="close" data-dismiss="alert">&times;</button>
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage(request) %>
</div>
<% } %>



<div class="form-group">
<label for="courseId">Course <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-graduation-cap"   style="margin-left: -4px"></i>
</span>
</div>
<%= HTMLUtility.getList("courseId", String.valueOf(dto.getCourseId()), courseList) %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("courseId", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("courseId", request) %>
</div>
<%} %>


<div class="form-group">
<label for="subjectId">Subject <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-book"   style="margin-left: -4px"></i>
</span>
</div>
<%= HTMLUtility.getList("subjectId", String.valueOf(dto.getSubjectId()), subjectList) %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("subjectId", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("subjectId", request) %>
</div>
<%} %>


<div class="form-group">
<label for="semester">Semester <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="far fa-calendar-check"   style="margin-left: -4px"></i>
</span>
</div>
<%
		LinkedHashMap<String , String> map = new LinkedHashMap< String , String>();
		map.put("1st","1st");
		map.put("2nd","2nd");
		map.put("3rd","3rd");
		map.put("4th","4th");
		map.put("5th","5th");
		map.put("6th","6th");
		map.put("7th","7th");
		map.put("8th","8th");
	
		String htmlList = HTMLUtility.getList("semester", dto.getSemester(), map);
	%>
	<%=htmlList %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("semester", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("semester", request) %>
</div>
<%} %>


<div class="form-group">
<label for="datepicker4">Exam Date <span class="text-danger">*</span></label>
<br>
<i class="fas fa-calendar-alt icon"></i>
<input type="text" class="form-control bg-white" placeholder="Exam Date" readonly id="datepicker4" name="ExDate" value="<%=DataUtility.getDateString(dto.getExamDate())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("ExDate", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("ExDate", request) %>
</div>
<%} %>


<div class="form-group">
<label for="ExTime">Exam Time <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-clock"   style="margin-left: -4px"></i>
</span>
</div>
	<%
		LinkedHashMap<String , String > map1 = new LinkedHashMap<String  ,String >();
		map1.put("08:00 AM to 11:00 AM","08:00 AM to 11:00 AM");
		map1.put("12:00 PM to 03:00 PM","12:00 PM to 03:00 PM");
		map1.put("04:00 PM to 07:00 PM","04:00 PM to 07:00 PM");
	
		String htmlList1 = HTMLUtility.getList("ExTime", dto.getExamTime(), map1);
	%>
	<%=htmlList1 %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("ExTime", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("ExTime", request) %>
</div>
<%} %>



<%if (dto != null && dto.getId() > 0) {%>

<button type="submit" name="operation" value="<%=TimeTableCtl.OP_UPDATE %>" class="btn btn-success text-uppercase btn-block">Update</button>
<button type="submit" name="operation" value="<%=TimeTableCtl.OP_CANCEL %>" class="btn btn-warning text-uppercase btn-block">Cancel</button>
<% }  else {%>
<button type="submit" name="operation" value="<%=TimeTableCtl.OP_SAVE %>" class="btn btn-success text-uppercase btn-block">Save</button>
<button type="submit" name="operation" value="<%=TimeTableCtl.OP_RESET %>" class="btn btn-warning text-uppercase btn-block">Reset</button>
<% } %>
</form>

<br>
<%@include file="Footer.jsp"%>
<br>

</div>
</div>
</div>
</body>
</html>