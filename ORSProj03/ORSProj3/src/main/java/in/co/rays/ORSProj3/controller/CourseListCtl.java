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
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Course List functionality Controller. Performs operation for list, search
 * and delete operations of Course
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/CourseListCtl")

public class CourseListCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger(CourseListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) 
	{

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		List<CourseDTO> clist = null;

		try
		{
			clist = model.list();
		}
		catch (ApplicationException e) 
		{
			e.printStackTrace();
		}
		request.setAttribute("courseList", clist);
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		CourseDTO dto = new CourseDTO();
		dto.setId(DataUtility.getLong(request.getParameter("cname")));
		populateDTO(dto, request);
		return dto;
	}
	
    /**
     * Contains display logic
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		log.debug("CourseCtl method doGet started");
		List list= null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CourseDTO dto = (CourseDTO)populateBean(request);
		CourseModelInt model = ModelFactory.getInstance().getCourseModel();

		String op = DataUtility.getString(request.getParameter("operation"));

		try 
		{
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, request);

			if (list == null || list.size() == 0)
			{
				ServletUtility.setErrorMessage("No record Found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} 
		catch (ApplicationException e) 
		{
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		
		log.debug("CourseCtl method doGet of ended");
	}

    /**
     * Contains submit logic
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("CourseCtl method doGet started");

		List list;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");
		CourseDTO dto = (CourseDTO) populateBean(request);
		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		
		if (OP_SEARCH.equalsIgnoreCase(op)) 
		{
			pageNo = 1;
		}
		else if (OP_NEXT.equalsIgnoreCase(op)) 
		{
			pageNo++;
		}
		else if (OP_PREVIOUS.equalsIgnoreCase(op)) 
		{
			pageNo--;
		}
		else if (OP_NEW.equalsIgnoreCase(op)) 
		{
			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			return;
		}
		else if (OP_RESET.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}
		else if(OP_DELETE.equalsIgnoreCase(op))
		{
			pageNo=1;
			if(ids !=null && ids.length > 0)
			{
				CourseDTO deleteDTO=new CourseDTO();
				for(String id:ids)
				{
					deleteDTO.setId(DataUtility.getLong(id));
					try
					{
						model.delete(deleteDTO);						
					}
					catch (ApplicationException e)
					{
						e.printStackTrace();
						ServletUtility.handleException(e, request, response);
						return;
					}
					ServletUtility.setSuccessMessage("Course Deleted Successfully", request);
				}				
			}
			else
			{
				ServletUtility.setErrorMessage("Select at least one record", request);				
			}
		}
		
		try 
		{
			list=model.search(dto, pageNo, pageSize);
			ServletUtility.setBean(dto, request);
		}
		catch (ApplicationException e) 
		{
			e.printStackTrace();
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op))
		{
			ServletUtility.setErrorMessage("No record Found", request);
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
	protected String getView() 
	{
		return ORSView.COURSE_LIST_VIEW;
	}

}
