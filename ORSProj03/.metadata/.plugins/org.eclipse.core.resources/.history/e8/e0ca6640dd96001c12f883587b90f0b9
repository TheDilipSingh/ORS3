<%@page import="in.co.rays.ORSProj3.controller.ForgetPasswordCtl"%>
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

<title>Forget Password</title>
<style type="text/css">
body {
	background: url('/ORSProj3/img/455165.jpg') no-repeat center center fixed;
	background-size: cover;
	}


input[type=email], select {
	margin-bottom: 15px;
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

<div class="container-fluid mt-5">
<div class="row justify-content-center">
<div class="col-md-4">

<form action="<%=ORSView.FORGET_PASSWORD_CTL %>" method="post" class="col-12 bg-white px-4 py-5 mb-4">

<div class="pb-4">
<h3 class="text-center">Forgot your password ? </h3>
<small>Enter your Email address and we'll send you password via email.</small>
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
<label for="email">Login id <span class="text-danger">*</span></label>
<br>
<i class="fas fa-envelope icon"></i>
<input type="email" class="form-control" placeholder="Enter Email id" id="email" name="login" value="<%=DataUtility.getStringData(dto.getLogin()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("login", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("login", request) %>
</div>
<%} %>


<button type="submit" name="operation" value="<%=ForgetPasswordCtl.OP_GO%>" class="btn btn-primary text-uppercase btn-block">Send</button>
<button type="submit" name="operation" value="<%=ForgetPasswordCtl.OP_RESET%>" class="btn btn-warning text-uppercase btn-block">Reset</button>

</form>

<br>
<%@include file="Footer.jsp"%>
<br>

</div>
</div>
</div>
</body>
</html>