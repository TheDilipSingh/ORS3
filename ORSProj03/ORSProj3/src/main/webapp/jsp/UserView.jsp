<%@page import="in.co.rays.ORSProj3.controller.UserCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
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

<title>User Registration</title>
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
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.UserDTO" scope="request"/>
<%@include file="Header.jsp" %>
  
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    $("#datepicker1").datepicker({
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

<form action="<%=ORSView.USER_CTL %>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<%
   List l = (List) request.getAttribute("roleList");
%>

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime())%>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime())%>">


<% if (dto != null && dto.getId() > 0 ) {%>
<div class="pb-4">
<h3 class="text-center">Update User </h3>
</div>
<% } else { %>
<div class="pb-4">
<h3 class="text-center">Add User </h3>
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
<label for="firstName">First Name <span class="text-danger">*</span></label>
<br>
<i class="fas fa-info-circle icon"></i>
<input type="text" class="form-control" placeholder="Enter First Name" id="firstName" name="firstName" value="<%=DataUtility.getStringData(dto.getFirstName())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("firstName", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("firstName", request) %>
</div>
<%} %>


<div class="form-group">
<label for="lastName">Last Name <span class="text-danger">*</span></label>
<br>
<i class="fas fa-info-circle icon"></i>
<input type="text" class="form-control" placeholder="Enter Last Name" id="lastName" name="lastName" value="<%=DataUtility.getStringData(dto.getLastName()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("lastName", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("lastName", request) %>
</div>
<%} %>


<div class="form-group">
<label for="email">Login id <span class="text-danger">*</span></label>
<br>
<i class="fas fa-envelope icon"></i>
<input type="text" class="form-control" placeholder="Enter Email id" id="email" name="login" value="<%=DataUtility.getStringData(dto.getLogin()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("login", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("login", request) %>
</div>
<%} %>


<div class="form-group">
<label for="gender">Gender <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-venus-mars"   style="margin-left: -4px"></i>
</span>
</div>
<%
HashMap map=new HashMap();
map.put("F", "Female");
map.put("M", "Male");


String htmlList=HTMLUtility.getList("gender", dto.getGender(), map);
%>
<%=htmlList%>
<!-- <select class="custom-select custom-select-md border-left-0" style="padding-left: 3px;">
<option value="" disabled selected>Gender</option>
<option> Male </option>
<option> Female </option>
</select> -->
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("gender", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("gender", request) %>
</div>
<%} %>

<div class="form-group">
<label for="roleId">Role <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-user-tag"   style="margin-left: -4px"></i>
</span>
</div>
<%= HTMLUtility.getList("roleId", String.valueOf(dto.getRoleId()), l) %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("roleId", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("roleId", request) %>
</div>
<%} %>

<div class="form-group">
<label for="udate">Date of Birth <span class="text-danger">*</span></label>
<br>
<i class="fas fa-calendar-alt icon"></i>
<input type="text" class="form-control bg-white" placeholder="Date of Birth" readonly id="datepicker1" name="dob" value="<%=DataUtility.getDateString(dto.getDob())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("dob", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("dob", request) %>
</div>
<%} %>

<div class="form-group">
<label for="mobileNo">Mobile no. <span class="text-danger">*</span></label>
<br>
<i class="fas fas fa-mobile-alt icon"></i>
<input type="text" class="form-control" placeholder="Enter Mobile no." maxlength="10" id="mobileNo" name="mobileNo" value="<%=DataUtility.getStringData(dto.getMobileNo()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("mobileNo", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("mobileNo", request) %>
</div>
<%} %>


<%if (dto!=null && dto.getId() > 0) {%>

<div class="form-group">
<input type="hidden" class="form-control" placeholder="Enter password" id="password" name="password" value="<%=DataUtility.getStringData(dto.getPassword()) %>">
</div>
<div class="form-group">
<input type="hidden" class="form-control" placeholder="Confirm password" id="confirmPassword" name="confirmPassword" value="<%=DataUtility.getStringData(dto.getConfirmPassword()) %>">
</div>
<% } else {%>

<div class="form-group">
<label for="password">Password <span class="text-danger">*</span></label>
<br>
<i class="fas fa-key icon"></i>
<input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="<%=DataUtility.getStringData(dto.getPassword()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("password", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("password", request) %>
</div>
<%} %>


<div class="form-group">
<label for="password">Confirm Password <span class="text-danger">*</span></label>
<br>
<i class="fas fa-key icon"></i>
<input type="password" class="form-control" placeholder="Confirm password" id="confirmPassword" name="confirmPassword" value="<%=DataUtility.getStringData(dto.getConfirmPassword()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("confirmPassword", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("confirmPassword", request) %>
</div>
<%} %>

<% } %>

<%if (dto != null && dto.getId() > 0) {%>

<button type="submit" name="operation" value="<%=UserCtl.OP_UPDATE %>" class="btn btn-success text-uppercase btn-block">Update</button>
<button type="submit" name="operation" value="<%=UserCtl.OP_CANCEL %>" class="btn btn-warning text-uppercase btn-block">Cancel</button>
<% }  else {%>
<button type="submit" name="operation" value="<%=UserCtl.OP_SAVE %>" class="btn btn-success text-uppercase btn-block">Save</button>
<button type="submit" name="operation" value="<%=UserCtl.OP_RESET %>" class="btn btn-warning text-uppercase btn-block">Reset</button>
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