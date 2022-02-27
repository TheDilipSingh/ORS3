<%@page import="in.co.rays.ORSProj3.controller.MarksheetCtl"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
<%@page import="java.util.List"%>
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
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">

<title>Add/Update Marksheet</title>
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
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.MarksheetDTO" scope="request"/>
<%@include file="Header.jsp" %>

<div class="container-fluid mt-2">
<div class="row justify-content-center">
<div class="col-md-4">

<form action="<%=ORSView.MARKSHEET_CTL %>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<%
   List l = (List) request.getAttribute("studentList");
%>

<input type="hidden" name="id" value="<%=dto.getId()%>">
<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
<input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDateTime())%>">
<input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDateTime())%>">


<% if (dto != null && dto.getId() > 0 ) {%>
<div class="pb-4">
<h3 class="text-center">Update Marksheet </h3>
</div>
<% } else { %>
<div class="pb-4">
<h3 class="text-center">Add Marksheet </h3>
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
<label for="rollNo">Roll no.<span class="text-danger">*</span></label>
<br>
<i class="far fa-id-badge icon"></i>
<input type="text" class="form-control" placeholder="Enter Roll no." id="rollNo" name="rollNo" value="<%=DataUtility.getStringData(dto.getRollNo())%>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("rollNo", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("rollNo", request) %>
</div>
<%} %>


<div class="form-group">
<label for="studentId">Name <span class="text-danger">*</span></label>
<div class="input-group">
<div class="input-group-prepend">
<span class="input-group-text bg-white">
<i class="fas fa-user"   style="margin-left: -4px"></i>
</span>
</div>
<%= HTMLUtility.getList("studentId", String.valueOf(dto.getStudentId()), l) %>
</div>
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("studentId", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("studentId", request) %>
</div>
<%} %>


<div class="form-group">
<label for="physics">Physics <span class="text-danger">*</span></label>
<br>
<i class="far fa-clipboard icon"></i>
<input type="text" class="form-control" placeholder="Enter Physics Marks" maxlength="3" id="physics" name="physics" value="<%=DataUtility.getStringData(dto.getPhysics()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("physics", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("physics", request) %>
</div>
<%} %>


<div class="form-group">
<label for="chemistry">Chemistry <span class="text-danger">*</span></label>
<br>
<i class="far fa-clipboard icon"></i>
<input type="text" class="form-control" placeholder="Enter Chemistry Marks" maxlength="3" id="chemistry" name="chemistry" value="<%=DataUtility.getStringData(dto.getChemistry()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("chemistry", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("chemistry", request) %>
</div>
<%} %>


<div class="form-group">
<label for="maths">Maths <span class="text-danger">*</span></label>
<br>
<i class="far fa-clipboard icon"></i>
<input type="text" class="form-control" placeholder="Enter Maths Marks" maxlength="3" id="maths" name="maths" value="<%=DataUtility.getStringData(dto.getMaths()) %>">
</div>
<%if (DataValidator.isNotNull(ServletUtility.getErrorMessage("maths", request))) {%>
<div class="text-danger  pb-2">
<span><i class="fas fa-info-circle text-danger"></i> </span>
<%=ServletUtility.getErrorMessage("maths", request) %>
</div>
<%} %>





<%if (dto != null && dto.getId() > 0) {%>

<button type="submit" name="operation" value="<%=MarksheetCtl.OP_UPDATE %>" class="btn btn-success text-uppercase btn-block">Update</button>
<button type="submit" name="operation" value="<%=MarksheetCtl.OP_CANCEL %>" class="btn btn-warning text-uppercase btn-block">Cancel</button>
<% }  else {%>
<button type="submit" name="operation" value="<%=MarksheetCtl.OP_SAVE %>" class="btn btn-success text-uppercase btn-block">Save</button>
<button type="submit" name="operation" value="<%=MarksheetCtl.OP_RESET %>" class="btn btn-warning text-uppercase btn-block">Reset</button>
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