<%@page import="in.co.rays.ORSProj3.model.MarksheetModelJDBCImpl"%>
<%@page import="in.co.rays.ORSProj3.controller.MarksheetListCtl"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
<%@page import="in.co.rays.ORSProj3.dto.MarksheetDTO"%>
<%@page import="java.util.Iterator"%>
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

<title>Marksheet Merit List</title>
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
<jsp:useBean id="dto" class="in.co.rays.ORSProj3.dto.MarksheetDTO" scope="request"/>
<%@include file="Header.jsp" %>

<div class="container-fluid bg-white border border-primary pt-2">

<form action="<%=ORSView.MARKSHEET_MERIT_LIST_CTL%>" method="post" class="form-inline justify-content-center py-2">

<div class="col-12 pb-2">
<h3 class="text-center">Marksheet Merit List</h3>
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
	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1)* pageSize ) + 1;
	
	List list = ServletUtility.getList(request);
	Iterator<MarksheetDTO> it = list.iterator();
	
	if(list.size() != 0) {
%>

<div class="table-responsive my-2">
<table class="table table-hover text-center table-light">
<thead class="thead-light">
<tr>
<th>S.No.</th>
<th>Roll no.</th>
<th>Name</th>
<th>Physics</th>
<th>Chemistry</th>
<th>Maths</th>
<th>Total</th>
<th>Percentage</th>
</tr>
</thead>

<%  
	while (it.hasNext())
	{
		dto = it.next();
		
		int phy = DataUtility.getInt(DataUtility.getStringData(dto.getPhysics()));
		int chem = DataUtility.getInt(DataUtility.getStringData(dto.getChemistry()));
		int math = DataUtility.getInt(DataUtility.getStringData(dto.getMaths()));
		int total = (phy+chem+math);
		float perc = total/3;
%>

<tbody>
<tr>
<td><%=index++%></td>
<td><%=dto.getRollNo()%></td>
<td><%=dto.getName()%></td>
<td><%=dto.getPhysics()%></td>
<td><%=dto.getChemistry()%></td>
<td><%=dto.getMaths()%></td>
<td><%=total%></td>
<td><%=perc%><span>%</span></td>
</tr>
<%
	}
%>
</tbody>
</table>
</div>

<input type="hidden" name="pageNo" value="<%=pageNo%>"> 
<input type="hidden" name="pageSize" value="<%=pageSize%>">



<div class="col-12 text-center">
<br>
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_GO %>" class="btn btn-primary btn-small text-uppercase">Print</button>
&emsp;&emsp;
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_BACK %>" class="btn btn-dark btn-small text-uppercase">Back</button>
<br>
</div>

<% } %>


</div>


</form>
</div>
<br>
<%@include file="Footer.jsp" %>
<br>
</body></html>