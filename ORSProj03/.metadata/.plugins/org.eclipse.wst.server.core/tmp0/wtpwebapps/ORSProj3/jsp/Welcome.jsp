<%@page import="in.co.rays.ORSProj3.controller.ORSView"%>
<%@page import="in.co.rays.ORSProj3.dto.RoleDTO"%>
<%@page import="in.co.rays.ORSProj3.dto.UserDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" type="image/png" href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16"/>

<style>

body { 
  background: url('/ORSProj3/img/455165.jpg') no-repeat center center fixed; 
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.container{
  margin-top: 150px;
}
</style>
<title> Welcome Page </title>
</head>
<body>
<%@include file="Header.jsp" %>
<div class="container">
<h1 align="center" class="t-50 text-center display-3 text-body ">Welcome to ORS
</h1>

<%
UserDTO beanUserBean=(UserDTO)session.getAttribute("user");
if(beanUserBean!=null)
{
if(beanUserBean.getRoleId()==RoleDTO.STUDENT)
{
%>
<h2 align="center">
<font style="size: 10px; color:cyan">
<a href="<%=ORSView.GET_MARKSHEET_CTL%>">Click here to view your Marksheet</a>
</font>
</h2>
<%
}
}
%>
</div>
<%@include file="Footer.jsp" %>
</body>
</html>