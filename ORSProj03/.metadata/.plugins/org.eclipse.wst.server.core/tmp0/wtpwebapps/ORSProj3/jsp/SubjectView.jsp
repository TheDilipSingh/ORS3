<%@page import="in.co.rays.ORSProj3.controller.SubjectCtl"%>
<%@page import="in.co.rays.ORSProj3.dto.CourseDTO"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
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

<title>Add/Update Subject</title>
<style type="text/css">
body {
	background: url('/ORSProj3/img/455165.jpg') no-repeat center center fixed;
	background-size: cover;
	}

input[type=text], select {
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
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.SubjectDTO" scope="request"/>
<%@include file="Header.jsp" %>
  
<%
	List<CourseDTO> l =(List<CourseDTO>) request.getAttribute("courseList");
%>  
  
<div class="container-fluid mt-2">
<div class="row justify-content-center">
<div class="col-md-4">

<form action="<%=ORSView.SUBJECT_CTL%>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime())%>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime())%>">


<% if (dto != null && dto.getId() > 0 ) {%>
<div class="pb-4">
<h3 class="text-center">Update Subject</h3>
</div>
<% } else { %>
<div class="pb-4">
<h3 class="text-center">Add Subject</h3>
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
<label for="coursename">Course <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-graduation-cap"   style="margin-left: -8px"></i>
</span>
</div>
<%
String htmlList = HTMLUtility.getList("coursename", DataUtility.getStringData(dto.getCourseId()), l);
%>
<%=htmlList%>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("coursename", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("coursename", request) %>
</div>
<%} %>


<div class="form-group">
<label for="name">Subject <span class="text-danger">*</span></label>
<br>
<i class="fas fa-book icon"></i>
<input type="text" class="form-control" placeholder="Enter Course title" id="name" name="name" value="<%=DataUtility.getStringData(dto.getSubjectName())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("name", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("name", request) %>
</div>
<%} %>


<div class="form-group">
<label for="description">Description <span class="text-danger">*</span></label>
<br>
<i class="fas fa-info-circle icon"></i>
<input type="text" class="form-control" placeholder="Enter Description" id="description" name="description" value="<%=DataUtility.getStringData(dto.getDescription()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("description", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("description", request) %>
</div>
<%} %>


<%if (dto != null && dto.getId() > 0) {%>

<button type="submit" name="operation" value="<%=SubjectCtl.OP_UPDATE %>" class="btn btn-success text-uppercase btn-block">Update</button>
<button type="submit" name="operation" value="<%=SubjectCtl.OP_CANCEL %>" class="btn btn-warning text-uppercase btn-block">Cancel</button>
<% }  else {%>
<button type="submit" name="operation" value="<%=SubjectCtl.OP_SAVE %>" class="btn btn-success text-uppercase btn-block">Save</button>
<button type="submit" name="operation" value="<%=SubjectCtl.OP_RESET %>" class="btn btn-warning text-uppercase btn-block">Reset</button>
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