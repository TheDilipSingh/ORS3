package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Course functionality Controller. Performs operation for add, update, delete
 * and get operations of Course
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/CourseCtl")

public class CourseCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;
    
	private static Logger log=Logger.getLogger(CourseCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request)
	{
		log.debug("CourseCtl method validate started");
		
		boolean pass = true;
		
		if (DataValidator.isNull(request.getParameter("name")))
		{
			request.setAttribute("name", PropertyReader.getValue("error.require", "Course Name"));
			pass = false ;
		}
		else if(!DataValidator.isName(request.getParameter("name")))
		{
			request.setAttribute("name", PropertyReader.getValue("error.name", "Invalid Course"));
			pass = false ;
		}

		if (DataValidator.isNull(request.getParameter("duration")))
		{
			request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
			pass = false ;
		}
		
		if (DataValidator.isNull(request.getParameter("description")))
		{
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false ;
			
		}
		
		log.debug("CourseCtl validate End");
		return pass;	
	}
	
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request)
	{
		log.debug("CourseCtl method populateBean started");
		CourseDTO dto = new CourseDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setDuration(DataUtility.getString(request.getParameter("duration")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
	
		populateDTO(dto, request);
		log.debug("CourseCtl method PopulateBean ended");
		return dto;
	}
	
    /**
     * Contains display logic
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("courseCtl method doGet started");
		String op = DataUtility.getString(request.getParameter("operation"));

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		
		if(id>0){
			CourseDTO bean;
			try{
			bean = model.findByPK(id);
			ServletUtility.setBean(bean, request);
			
			}catch(ApplicationException e){
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		
		log.debug("courseCtl method doGet ended");
	}
	
    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("CourseCtl method doPost started ");
		String op = DataUtility.getString(request.getParameter("operation"));

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		long id = DataUtility.getLong(request.getParameter("id"));
	
		if(OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op))
		{
			CourseDTO bean =(CourseDTO) populateBean(request);
		try
		{
			if(id>0)
			{		
					model.update(bean);
					ServletUtility.setBean(bean, request);
			}
			else
			{
				 long pk = model.add(bean);
				// bean.setId(pk);
			}
				
			ServletUtility.setSuccessMessage("Course is Successfully saved", request);
		
		}
		catch(ApplicationException e )
		{
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} 
		catch (DuplicateRecordException e) 
		{
			ServletUtility.setBean(bean, request);
			ServletUtility.setErrorMessage("Course Name Already Exist", request);			
		}		
		}
		else if (OP_CANCEL.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}
		else if (OP_RESET.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("CourseCtl method doPost ended");		
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{		
		return ORSView.COURSE_VIEW;
	}

}
