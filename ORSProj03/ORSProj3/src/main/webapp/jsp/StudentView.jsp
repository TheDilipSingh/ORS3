<%@page import="in.co.rays.ORSProj3.controller.StudentCtl"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
<%@page import="java.util.List"%>
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

<title>Student Registration</title>
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
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.StudentDTO" scope="request"/>
<%@include file="Header.jsp" %>
  
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    $("#datepicker2").datepicker({
      changeMonth: true,
      changeYear: true,
	  yearRange:'-58:-18',
	  dateFormat:'dd-mm-yy',
	//  mindefaultDate : "01-01-1962"
    });
  });
  </script>
<div class="container-fluid mt-2">
<div class="row justify-content-center">
<div class="col-md-4">

<form action="<%=ORSView.STUDENT_CTL%>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<%
   List l = (List) request.getAttribute("collegeList");
%>

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime())%>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime())%>">


<% if (dto != null && dto.getId() > 0 ) {%>
<div class="pb-4">
<h3 class="text-center">Update Student</h3>
</div>
<% } else { %>
<div class="pb-4">
<h3 class="text-center">Add Student</h3>
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
<label for="firstname">First Name <span class="text-danger">*</span></label>
<br>
<i class="fas fa-info-circle icon"></i>
<input type="text" class="form-control" placeholder="Enter First Name" id="firstname" name="firstname" value="<%=DataUtility.getStringData(dto.getFirstName())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("firstname", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("firstname", request) %>
</div>
<%} %>


<div class="form-group">
<label for="lastname">Last Name <span class="text-danger">*</span></label>
<br>
<i class="fas fa-info-circle icon"></i>
<input type="text" class="form-control" placeholder="Enter Last Name" id="lastname" name="lastname" value="<%=DataUtility.getStringData(dto.getLastName()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("lastname", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("lastname", request) %>
</div>
<%} %>


<div class="form-group">
<label for="email">Email id <span class="text-danger">*</span></label>
<br>
<i class="fas fa-envelope icon"></i>
<input type="text" class="form-control" placeholder="Enter Email id" id="email" name="email" value="<%=DataUtility.getStringData(dto.getEmail()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("email", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("email", request) %>
</div>
<%} %>


<div class="form-group">
<label for="collegename">College Name <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-university" style="margin-left: -4px"></i>
</span>
</div>
<%= HTMLUtility.getList("collegename", String.valueOf(dto.getCollegeId()), l) %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("collegename", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("collegename", request) %>
</div>
<%} %>


<div class="form-group">
<label for="datepicker2">Date of Birth <span class="text-danger">*</span></label>
<br>
<i class="fas fa-calendar-alt icon"></i>
<input type="text" class="form-control bg-white" placeholder="Date of Birth" readonly id="datepicker2" name="dob" value="<%=DataUtility.getDateString(dto.getDob())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("dob", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("dob", request) %>
</div>
<%} %>

<div class="form-group">
<label for="mobile">Mobile no. <span class="text-danger">*</span></label>
<br>
<i class="fas fas fa-mobile-alt icon"></i>
<input type="text" class="form-control" placeholder="Enter Mobile no." maxlength="10" id="mobile" name="mobile" value="<%=DataUtility.getStringData(dto.getMobileNo()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("mobile", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("mobile", request) %>
</div>
<%} %>


<%if (dto != null && dto.getId() > 0) {%>

<button type="submit" name="operation" value="<%=StudentCtl.OP_UPDATE %>" class="btn btn-success text-uppercase btn-block">Update</button>
<button type="submit" name="operation" value="<%=StudentCtl.OP_CANCEL %>" class="btn btn-warning text-uppercase btn-block">Cancel</button>
<% }  else {%>
<button type="submit" name="operation" value="<%=StudentCtl.OP_SAVE %>" class="btn btn-success text-uppercase btn-block">Save</button>
<button type="submit" name="operation" value="<%=StudentCtl.OP_RESET %>" class="btn btn-warning text-uppercase btn-block">Reset</button>
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