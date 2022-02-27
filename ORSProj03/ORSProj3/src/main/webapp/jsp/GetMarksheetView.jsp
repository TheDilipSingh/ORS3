<%@page import="in.co.rays.ORSProj3.controller.MarksheetCtl"%>
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

<title>Get Marksheet</title>
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

<form action="<%=ORSView.GET_MARKSHEET_CTL%>" method="post" class="col-12 bg-white px-4 py-4 mb-4">

<div class="pb-4">
<h3 class="text-center">Get Marksheet </h3>
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



<button type="submit" name="operation" value="<%=MarksheetCtl.OP_GO %>" class="btn btn-success text-uppercase btn-block">Search</button>
<button type="submit" name="operation" value="<%=MarksheetCtl.OP_RESET %>" class="btn btn-warning text-uppercase btn-block">Reset</button>
<div class="pb-4"></div>

</form>

<br>
<%@include file="Footer.jsp"%>
<br>

</div>
</div>
</div>
 	<%
		if(dto.getRollNo() != null  && dto.getRollNo().trim().length() > 0 ) {
	%>
<div class="table-responsive bg-white mb-5">
<table class="table table-bordered">
<table class="table table-bordered">
<tr align="center" >
<td><h2>Rays Technologies</h2></td>
</tr></table>
				  
<table class="table table-bordered">
<tr>
<th align="center"> Name</th>
<td align="center"> <%=DataUtility.getStringData(dto.getName()) %></td>
<th align="center"> Roll No</th>
<td align="center"> <%=DataUtility.getStringData(dto.getRollNo()) %> </td>
				 						 
</tr>
<tr>
<td align="center"> Status</td>
<th align="center">Regular</th>
<td align="center"> Course</td>
<th align="center">BE</th>				 
</tr>			
</table> 

	<%
	int phy =DataUtility.getInt(DataUtility.getStringData(dto.getPhysics()));
	int chem =DataUtility.getInt(DataUtility.getStringData(dto.getChemistry()));
	int math =DataUtility.getInt(DataUtility.getStringData(dto.getMaths()));
	int total = (phy+chem+math);
	float perc = total/3;
	%>
					  
<table class="table table-bordered">
<tr>
<th align="center" style="width: 25%">Subject</th>
<th align="center" style="width: 25%">Maximum Marks</th>
<th align="center" style="width: 25%">Minimum Marks</th>
<th align="center" style="width: 25%">Marks Obtained</th>
<th align="center" style="width: 25%">Grade</th>
</tr>
<tr>
<td align="center">Physics</td>
<td align="center">100</td>
<td align="center">33</td>
<td align="center"><%=phy %>
				
<%if(phy<33){%>
<span style="color: red">*</span>
<% } %>
</td>
				
<td align="center">
				
	<%	if(phy <=100 && phy >85){ %>      A+
	<%} else if(phy<=85 && phy > 75 ) {%> B+
	<%} else if(phy<=75 && phy > 65 ) {%> B
	<%} else if(phy<=65 && phy > 55 ){ %> C+
	<%} else if(phy<=55 && phy >=33  ){ %>C
				
	<%} else if(phy< 33 && phy >= 0 ) {%><span style="color: red"> Fail</span>
	<% } %>
	</td>
	</tr>
		
	<tr>
	<td align="center">Chemistry</td>
	<td align="center">100</td>
	<td align="center">33</td>
	<td align="center"><%=chem %>
				
	<%if(chem<33){%>
	<span style="color: red">*</span>
	<% } %>	</td>
				
	<td align="center">
				
	<%
		if(chem <=100 && chem >85){ %> A+
	<%} else if(chem<=85 && chem > 75 ) {%>B+
	<%} else if(chem <=75 && chem > 65 ) {%>B
	<%} else if(chem <=65 && chem > 55 ){ %>C+
	<%} else if(chem <=55 && chem >=33  ){ %>C
				
	<%} else if(chem < 33 && chem >= 0 ) {%><span style="color: red"> Fail</span>
	<% } %>
	</td>
	</tr>
					
	<tr>
	<td align="center"> Maths</td>
	<td align="center">100</td>
	<td align="center">33</td>
	<td align="center"><%=math %>
				
	<%if(math<33){%>
	<span style="color: red">*</span>
	<% } %>	</td>
				
	<td align="center">
				
	<%
		if(math <=100 && math >85){ %> A+
	<%} else if(math <=85 && math > 75 ) {%>B+
	<%} else if( math <=75 && math > 65 ) {%>B
	<%} else if(math <=65 && math > 55 ){ %>C+
	<%} else if(math <=55 && math >=33  ){ %>C
				
	<%} else if(math < 33 && math >= 0 ) {%><span style="color: red"> Fail</span>
	<% } %>
	</td>
	</tr>
	</table>	  
			
	<table class="table table-bordered">
	<tr>
	<th>Total</th>
	<th>Percentage</th>
	<th>Division</th>
	<th>Result</th>
	</tr>
	<tr>
	<th><%=total %>
	<% if(total<99 || phy<33|| chem<33|| 	math<33){ %>
	<span style="color: red">*</span>
	<% } %>
	</th>
			
	<th><%=perc %> %</th>
	<th>
	<% if(perc <100 && perc >= 60){ %>1<sup>st</sup>
	<%} else if(perc <60 && perc >=40){ %>2<sup>rd</sup>
	<%} else if(perc <40 && perc >=0){ %>3<sup>rd</sup>
	<%} %>
	</th>
				
	<th>
	<%
	if(phy >=33 && chem>=33 && math >= 33) { %>
	<span style="color: green"> Pass</span>
	<%}else{ %>
	<span style="color: red"> Fail</span>
	<% } %>
				
	</th>
	</tr>
	</table>
			
	<%} %>
	</table>
	</div>
</body>
</html>