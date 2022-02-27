<%@page import="in.co.rays.ORSProj3.model.StudentModelJDBCImpl"%>
<%@page import="in.co.rays.ORSProj3.controller.StudentListCtl"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
<%@page import="in.co.rays.ORSProj3.dto.StudentDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.ORSProj3.dto.CollegeDTO"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.ORSProj3.util.ServletUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataValidator"%>
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
<script src="<%=ORSView.APP_CONTEXT %>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT %>/js/Checkbox11.js"></script>

<title>Student List</title>
<style type="text/css">
body {
	background: url('/ORSProj3/img/455165.jpg') no-repeat center center fixed;
	background-size: cover;
	}

input[type=text], select {
	margin-bottom: 0px;
	padding-left: 0px;
	padding-right: 0px
}


.form-group i {
	position: relative;
}

.input-group i {
	position: relative;
}

.icon {
	margin-left: 5px;
	padding: 30px;
	min-width: 40px;
}

</style>
</head>

<body>
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.StudentDTO" scope="request"/>
<%@include file="Header.jsp" %>

<div class="container-fluid bg-white border border-primary pt-2">

<form action="<%=ORSView.STUDENT_LIST_CTL%>" method="post" class="form-inline justify-content-center py-2">

<div class="col-12 pb-2">
<h3 class="text-center">Student List</h3>
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


<%
	List<CollegeDTO> clist = (List<CollegeDTO>) request.getAttribute("collegeList");
%>

<%
	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1)* pageSize ) + 1;
	
	List list = ServletUtility.getList(request);
	Iterator<StudentDTO> it = list.iterator();
	
	if(list.size() != 0) {
%>
<div class="row">

<div class="col-sm pr-3">
<div class="form-group">
<label for="firstName">&nbsp;First Name :</label>
<div class="input-group">
<div class="input-group-prepend ml-1">
<span class="input-group-text bg-white form-control">
<i class="fas fa-info-circle"   style="padding-top: 5px;"></i>
</span>
</div>
<input type="text" class="form-control border-left-0 w-100 p-3" placeholder="Enter First Name" id="firstName" name="firstName" value="<%=DataUtility.getStringData(dto.getFirstName())%>">
</div>
</div>
</div>
&emsp;&emsp;&emsp;&nbsp;
<div class="col-sm">
<div class="form-group">
<label for="collegeid"	>&nbsp;College Name :</label>
<div class="input-group">
<div class="input-group-prepend ml-1">
<span class="input-group-text bg-white">
<i class="fas fa-university"   style="margin-left: -4px"></i>
</span>
</div>
<%=HTMLUtility.getList("collegeid", String.valueOf(dto.getCollegeId()), clist) %>
</div>
</div>
</div>
&emsp;&emsp;&emsp;&emsp;
<div class="col-sm">
<div class="form-group">
<label for="email">&nbsp;Email id :</label>
<div class="input-group">
<div class="input-group-prepend ml-1">
<span class="input-group-text bg-white form-control">
<i class="fas fa-envelope"   style="padding-top: 5px"></i>
</span>
</div>
<input type="text" class="form-control border-left-0 w-100 p-3" placeholder="Enter Email id" id="email" name="email" value="<%=DataUtility.getStringData(dto.getEmail()) %>">
</div>
</div>
</div>

<div class="col-sm mt-4 text-center">
<div>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_SEARCH %>" class="btn btn-success text-uppercase mr-3" style="position:static;">Search</button>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_RESET %>" class="btn btn-warning text-uppercase px-3" style="position: static;">Reset</button>
</div>
</div>
</div>

<div class="table-responsive my-2">
<table class="table table-hover text-center table-light">
<thead class="thead-light">
<tr>
<th><label>Select all</label><input type="checkbox" id="select_all" name="select"></th>
<th>S.No.</th>
<th>College</th>
<th>First Name</th>
<th>Last Name</th>
<th>Date Of Birth</th>
<th>Mobile no. </th>
<th>Email id</th>
<th>Edit</th>
</tr>
</thead>

<%  
	while (it.hasNext())
	{
		dto = it.next();
%>

<tbody>
<tr>
<td><input type="checkbox" class="checkbox" name="ids" value="<%=dto.getId()%>"></td>
<td><%=index++%></td>
<td><%=dto.getCollegeName()%></td>
<td><%=dto.getFirstName()%></td>
<td><%=dto.getLastName()%></td>
<td><%=DataUtility.getDateString(dto.getDob())%></td>
<td><%=dto.getMobileNo()%></td>
<td><%=dto.getEmail()%></td>
<td><a href="StudentCtl?id=<%=dto.getId()%>">Edit</a></td>
</tr>
<%
	}
%>
</tbody>
</table>
</div>
<div class="container-fluid pt-n4 mb-4">
<div class="row">

<div class="col">
<%if (pageNo == 1) { %>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_PREVIOUS %>" disabled class="btn btn-primary  btn-small ml-n3 text-uppercase">PREVIOUS</button>
<%} else { %>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_PREVIOUS %>" class="btn btn-primary btn-small ml-n3 text-uppercase">PREVIOUS</button>
<% } %>
</div>

<div class="col text-center">
<button type="submit" name="operation" value="<%=StudentListCtl.OP_DELETE %>"  class="btn btn-small btn-danger mx-n3 text-uppercase">Delete</button>
</div>

<div class="col text-center">
<button type="submit" name="operation" value="<%=StudentListCtl.OP_NEW %>"  class="btn btn-small btn-success mx-n3 text-uppercase">New</button>
</div>

<% StudentModelJDBCImpl model = new StudentModelJDBCImpl(); %>
<div class="col text-right">
<%if (list.size() < pageSize || model.nextPK() -1 == dto.getId()) {%>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_NEXT %>" disabled class="btn btn-primary btn-small ml-n3 text-uppercase">Next</button>
<% } else { %>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_NEXT %>" class="btn btn-primary btn-small ml-n3 text-uppercase">Next</button>
<% } %>
</div>

<input type="hidden" name="pageNo" value="<%=pageNo%>"> 
<input type="hidden" name="pageSize" value="<%=pageSize%>">

<% } if(list.size() == 0) { %>
<div class="col-12 text-center">
<br>
<button type="submit" name="operation" value="<%=StudentListCtl.OP_BACK %>" class="btn btn-primary btn-small text-uppercase">Back</button>
<br>
</div>
<% } %>
</div>


</form>
</div>
<br>
<%@include file="Footer.jsp" %>
<br>
</body>
</html>