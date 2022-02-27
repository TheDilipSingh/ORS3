<%@page import="in.co.rays.ORSProj3.controller.LoginCtl"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
<%@page import="in.co.rays.ORSProj3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>

<title>Login Page</title>
<style type="text/css">
body {
	background: url('/ORSProj3/img/455165.jpg') no-repeat center center
		fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
	
}

input[type=text], select {
	margin-bottom: -10px;
	padding-left: 35px;
}

input[type=password], select {
	margin-bottom: -10px;
	padding-left: 35px;
}

.form-group i {
	position: absolute;
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

<div class="container-fluid my-5">
<div class="row justify-content-center">
<div class="col-md-4" >

<form action="<%=ORSView.LOGIN_CTL %>" method="post" class="col-12 bg-white">

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy() %>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime()) %>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime()) %>">


<div class="pt-2 pb-2">
<h3 class="text-center"> Log in </h3>
</div>

<%if(DataValidator.isNotNull(ServletUtility.getSuccessMessage(request))) { %>

<div class="alert alert-success">
<button type="button" class="close" data-dismiss="alert">&times;</button>
<span><i class="fas fa-info-circle text-success"></i></span>
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

<% String str = (String) request.getAttribute("message");
if(DataValidator.isNotNull(str)) { %>

<div class="alert alert-danger alert-dismissible">
<button type="button" class="close" data-dismiss="alert">&times;</button>
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=str %>
</div>
<% } %>

<div class="form-group">
<label for="email">Email <span class="text-danger">*</span></label>
<br>
<i class="fas fa-user icon"></i>
<input type="text" class="form-control" placeholder="Enter email" id="email" name="login" value="<%=DataUtility.getStringData(dto.getLogin())%>">
</div>
<div>

<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("login", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("login", request) %>
</div>
<%} %>



</div>

<div class="form-group">
<label for="password">Password <span class="text-danger">*</span></label>
<br>
<i class="fas fa-key icon"></i>
<input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="<%=DataUtility.getStringData(dto.getPassword()) %>">
</div>
<div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("password", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("password", request) %>
</div>
<%} %>
</div>
<div class="pb-4"></div>


<button type="submit" name="operation" value="<%=LoginCtl.OP_SIGN_IN %>" class="btn btn-primary text-uppercase btn-block">Sign in</button>
<button type="submit" name="operation" value="<%=LoginCtl.OP_SIGN_UP %>" class="btn btn-secondary text-uppercase btn-block">Sign up</button>


<div class="forgot text-center p-4">
<a href="<%=ORSView.FORGET_PASSWORD_CTL%>">Forget Password?</a>
</div>

</form>

</div>
</div>
</div>
<br>
<%@include file="Footer.jsp" %>
<br>
</body>
</html>