package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.SubjectModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;

/**
 * Subject functionality Controller. Performs operation for add, update, delete
 * and get operations of Subject
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */

@WebServlet("/ctl/SubjectCtl")

public class SubjectCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(SubjectCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request)
	{
		
		CourseModelInt cmodel = ModelFactory.getInstance().getCourseModel();
		
		try 
		{
		List cList = cmodel.list();
		request.setAttribute("courseList", cList);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request)
	{
		log.debug("SubjectCtl method validate started");

		boolean pass= true;
		
		if(DataValidator.isNull(request.getParameter("name")))
		{
			request.setAttribute("name", PropertyReader.getValue("error.require", "Subject Name"));
			pass = false;
		}
		else if (! DataValidator.isName(request.getParameter("name")))
		{
			request.setAttribute("name", PropertyReader.getValue("error.name", "Invalid Subject"));
			pass = false;
		}
		if(DataValidator.isNull(request.getParameter("description")))
		{
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}
		
		if(DataValidator.isNull(request.getParameter("coursename")))
		{
			request.setAttribute("coursename", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		log.debug("SubjectCtl method validate started");

		return pass;
	}
	
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected SubjectDTO populateBean(HttpServletRequest request)
	{
		log.debug("SubjectCtl method populateBean started");

		SubjectDTO dto = new SubjectDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setSubjectName(DataUtility.getString(request.getParameter("name")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("coursename")));
		
		populateDTO(dto, request);
		
		log.debug("SubjectCtl method populateBean ended");

		return dto;
		
	}


    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("SubjectCtl method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		
		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO dto = null;
		long id =DataUtility.getLong(request.getParameter("id"));
		
		if(id > 0 || op != null)
		{
			try 
			{
				dto = model.findByPK(id);
				ServletUtility.setBean(dto, request);
			} 
				catch (ApplicationException e) 
			{
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
			}
		}

		log.debug("SubjectCtl method doGet ended");
		ServletUtility.forward(getView(), request, response);
	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("SubjectCtl method doPost started");
		
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		
		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();		
		
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {	
			SubjectDTO dto = (SubjectDTO)populateBean(request);

		try
		{	
			if(id > 0)
			{
				model.update(dto);
				ServletUtility.setBean(dto, request);
				ServletUtility.setSuccessMessage(" Subject succesfully updated ", request);
			}
			else
			{
				long pk = model.add(dto);
		//		bean.setId(pk);
				ServletUtility.setSuccessMessage(" Subject succesfully saved ", request);
			}
			
			
		}
		catch(ApplicationException e)
		{
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (DuplicateRecordException e) {
			ServletUtility.setBean(dto, request);
			ServletUtility.setErrorMessage("Subject name already Exsist", request);
			}
		}
		else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			return;
		}
		else if (OP_CANCEL.equalsIgnoreCase(op) ) {
			ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
			return;
		}
/*		else if (OP_DELETE.equalsIgnoreCase(op)) {
			SubjectBean bean =  populateBean(request);
			try {
				model.delete(bean);
			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return ; 
			}
		}*/
		
	
		ServletUtility.forward(getView(), request, response);
		log.debug("SubjectCtl Method doPost ended");
	}
	
	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.SUBJECT_VIEW;
	}
}
