package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.dto.TimeTableDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.SubjectModelInt;
import in.co.rays.ORSProj3.model.TimeTableModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Time Table List functionality Controller. Performs operation for list, search and
 * delete operations of Time Table
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/TimeTableListCtl")
public class TimeTableListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TimeTableListCtl.class);

	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) {

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
		List<CourseDTO> list = null;
		List<SubjectDTO> list2 = null;
		try {
			list = model.list();
			list2 = smodel.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("courseList", list);
		request.setAttribute("subjectList", list2);

	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) {
		TimeTableDTO dto = new TimeTableDTO();

//		bean.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("clist")));
		dto.setSubjectId(DataUtility.getInt(request.getParameter("slist")));
	//	bean.setSubjectName(DataUtility.getString(request.getParameter("slist")));
		dto.setExamDate(DataUtility.getDate(request.getParameter("Exdate")));
	//	System.out.println("populate bean==========>>>> " + bean.getExamDate());
		populateDTO(dto, request);
		return dto;
	}
    /**
     * Contains display logics
     */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List list ;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
		TimeTableDTO dto =(TimeTableDTO) populateBean(request);

//		String op = DataUtility.getString(request.getParameter("operation"));
   //   String[] ids = request.getParameterValues("ids");
	    

		try {
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setBean(dto, request);
			
	//		ServletUtility.setList(list, request);
			if (list==null && list.size()==0) {
				ServletUtility.setErrorMessage("No record Found", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);


		} catch (ApplicationException e) {
			e.printStackTrace();
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
	}
    /**
     * Contains Submit logics
     */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list;
		String op = DataUtility.getString(request.getParameter("operation"));

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;


		TimeTableDTO dto = (TimeTableDTO) populateBean(request);	
		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
		String[] ids = (String[]) request.getParameterValues("ids");
				
			        if (OP_SEARCH.equalsIgnoreCase(op)) {
				    pageNo = 1;
					} else if (OP_NEXT.equalsIgnoreCase(op)) {
						pageNo++;	
					} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
						if(pageNo<1){
							pageNo--;
						}else{
							pageNo= 1;
						}	
					}
					else if (OP_NEW.equalsIgnoreCase(op)) 
					{
						ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
						return ;
					}
					
					else if (OP_RESET.equalsIgnoreCase(op)) {
						ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
						return;
					}
					else if (OP_DELETE.equalsIgnoreCase(op)) {
						pageNo=1;
						if (ids != null && ids.length > 0) {
							TimeTableDTO dto2 = new TimeTableDTO();

							for (String id2 : ids) {
								int id1 = DataUtility.getInt(id2);
								dto2.setId(id1);
								try {
									model.delete(dto2);
								} catch (ApplicationException e) {
									e.printStackTrace();
									ServletUtility.handleException(e, request, response);
									return;
								}
								ServletUtility.setSuccessMessage("Data Deleted Succesfully", request);
							}
						
						}else{
							ServletUtility.setErrorMessage("Select at least one Record", request);
						}
					}
			    	try {
						list = model.search(dto, pageNo, pageSize);
						ServletUtility.setBean(dto, request);
					}
					catch(ApplicationException e){	
						ServletUtility.handleException(e, request, response);
						return;
					}
			   if(list==null || list.size()==0 && !OP_DELETE.equalsIgnoreCase(op)) 
			{
				ServletUtility.setErrorMessage("No Record Found", request);
			}
			ServletUtility.setBean(dto, request);
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
	        ServletUtility.forward(getView(), request, response);			
		}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() {
		return ORSView.TIMETABLE_LIST_VIEW;
	}

}
