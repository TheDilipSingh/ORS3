<%@page import="in.co.rays.ORSProj3.model.MarksheetModelJDBCImpl"%>
<%@page import="in.co.rays.ORSProj3.dto.MarksheetDTO"%>
<%@page import="in.co.rays.ORSProj3.controller.MarksheetListCtl"%>
<%@page import="in.co.rays.ORSProj3.util.HTMLUtility"%>
<%@page import="in.co.rays.ORSProj3.util.DataUtility"%>
<%@page import="in.co.rays.ORSProj3.dto.StudentDTO"%>
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

<title>Marksheet List</title>
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

<form action="<%=ORSView.MARKSHEET_LIST_CTL%>" method="post" class="form-inline justify-content-center py-2">

<div class="col-12 pb-2">
<h3 class="text-center">Marksheet List</h3>
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
	List<StudentDTO> slist = (List<StudentDTO>) request.getAttribute("studentList");
%>

<%
	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1)* pageSize ) + 1;
	
	List list = ServletUtility.getList(request);
	Iterator<MarksheetDTO> it = list.iterator();
	
	if(list.size() != 0) {
%>
<div class="row">
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
<div class="col-sm">
<div class="form-group">
<label for="studentid"	>&nbsp;Student Name :</label>
<div class="input-group">
<div class="input-group-prepend ml-1">
<span class="input-group-text bg-white">
<i class="fas fa-user"   style="margin-left: -4px"></i>
</span>
</div>
<%=HTMLUtility.getList("studentid", String.valueOf(dto.getStudentId()), slist) %>
</div>
</div>
</div>
&emsp;&emsp;&emsp;
<div class="col-sm">
<div class="form-group">
<label for="rollNo">&nbsp;Roll no :</label>
<div class="input-group">
<div class="input-group-prepend ml-1">
<span class="input-group-text bg-white form-control">
<i class="far fa-id-badge"   style="padding-top: 5px;"></i>
</span>
</div>
<input type="text" class="form-control border-left-0" style="width: 270px" placeholder="Enter Roll no." id="rollNo" name="rollNo" value="<%=DataUtility.getStringData(dto.getRollNo())%>">
</div>
</div>
</div>

<div class="col-sm mt-4 text-center">
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_SEARCH %>" class="btn btn-success text-uppercase mr-3" style="position:static;">Search</button>
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_RESET %>" class="btn btn-warning text-uppercase px-3" style="position: static;">Reset</button>
</div>

<div class="col-sm"></div>

</div>

<div class="table-responsive my-2">
<table class="table table-hover text-center table-light">
<thead class="thead-light">
<tr>
<th><label>Select all</label><input type="checkbox" id="select_all" name="select"></th>
<th>S.No.</th>
<th>Roll no.</th>
<th>Name</th>
<th>Physics</th>
<th>Chemistry</th>
<th>Maths</th>
<th>Total</th>
<th>Result</th>
<th>Edit</th>
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
%>

<tbody>
<tr>
<td><input type="checkbox" class="checkbox" name="ids" value="<%=dto.getId()%>"></td>
<td><%=index++%></td>
<td><%=dto.getRollNo()%></td>
<td><%=dto.getName()%></td>
<td><%=dto.getPhysics()%></td>
<td><%=dto.getChemistry()%></td>
<td><%=dto.getMaths()%></td>
<td><%=total%></td>
<td><%
		if(phy >=33 && chem>=33 && math >= 33) { %>
		<span class="text-success"> Pass</span>
		<%}else{ %>
		<span class="text-danger"> Fail</span>
		<% } %>
		</td>
<td><a href="MarksheetCtl?id=<%=dto.getId()%>">Edit</a></td>
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
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_PREVIOUS %>" disabled class="btn btn-primary  btn-small ml-n3 text-uppercase">PREVIOUS</button>
<%} else { %>
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_PREVIOUS %>" class="btn btn-primary btn-small ml-n3 text-uppercase">PREVIOUS</button>
<% } %>
</div>

<div class="col text-center">
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_DELETE %>"  class="btn btn-small btn-danger mx-n3 text-uppercase">Delete</button>
</div>

<div class="col text-center">
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_NEW %>"  class="btn btn-small btn-success mx-n3 text-uppercase">New</button>
</div>

<% MarksheetModelJDBCImpl model = new MarksheetModelJDBCImpl(); %>
<div class="col text-right">
<%if (list.size() < pageSize || model.nextPK() -1 == dto.getId()) {%>
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_NEXT %>" disabled class="btn btn-primary btn-small ml-n3 text-uppercase">Next</button>
<% } else { %>
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_NEXT %>" class="btn btn-primary btn-small ml-n3 text-uppercase">Next</button>
<% } %>
</div>

<input type="hidden" name="pageNo" value="<%=pageNo%>"> 
<input type="hidden" name="pageSize" value="<%=pageSize%>">

<% } if(list.size() == 0) { %>
<div class="col-12 text-center">
<br>
<button type="submit" name="operation" value="<%=MarksheetListCtl.OP_BACK %>" class="btn btn-primary btn-small text-uppercase">Back</button>
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