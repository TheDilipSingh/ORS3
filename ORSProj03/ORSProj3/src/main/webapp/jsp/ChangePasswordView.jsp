<%@page import="in.co.rays.ORSProj3.controller.ChangePasswordCtl"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
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

<title>Change Password</title>
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
  
<div class="container-fluid mt-2">
<div class="row justify-content-center">
<div class="col-md-4">

<form action="<%=ORSView.CHANGE_PASSWORD_CTL %>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime())%>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime())%>">


<div class="pb-4">
<h3 class="text-center">Change Password</h3>
</div>


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
<label for="oldPassword">Old Password <span class="text-danger">*</span></label>
<br>
<i class="fas fa-key icon"></i>
<input type="password" class="form-control" placeholder="Enter old password" id="oldPassword" name="oldPassword" value="<%=DataUtility.getStringData(dto.getPassword()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("oldPassword", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("oldPassword", request) %>
</div>
<%} %>

<div class="form-group">
<label for="newPassword">New Password <span class="text-danger">*</span></label>
<br>
<i class="fas fa-key icon"></i>
<input type="password" class="form-control" placeholder="Enter new password" id="newPassword" name="newPassword" value="<%=DataUtility.getStringData(dto.getPassword()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("newPassword", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("newPassword", request) %>
</div>
<%} %>

<div class="form-group">
<label for="confirmPassword">Confirm Password <span class="text-danger">*</span></label>
<br>
<i class="fas fa-key icon"></i>
<input type="password" class="form-control" placeholder="Confirm password" id="confirmPassword" name="confirmPassword" value="<%=DataUtility.getStringData(dto.getConfirmPassword()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("confirmPassword", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("confirmPassword", request) %>
</div>
<% } %>



<button type="submit" name="operation" value="<%=ChangePasswordCtl.OP_SAVE %>" class="btn btn-success text-uppercase btn-block">Save</button>
<button type="submit" name="operation" value="<%=ChangePasswordCtl.OP_CHANGE_MY_PROFILE %>" class="btn btn-warning text-uppercase btn-block">Change Profile</button>

</form>

<br>
<%@include file="Footer.jsp"%>
<br>

</div>
</div>
</div>

</body>
</html>