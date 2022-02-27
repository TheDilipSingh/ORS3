package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.dto.TimeTableDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.SubjectModelInt;
import in.co.rays.ORSProj3.model.TimeTableModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Time Table functionality Controller. Performs operation for add, update, delete
 * and get operations of Time Table
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/TimeTableCtl")

public class TimeTableCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;

	private static Logger log=Logger.getLogger(TimeTableCtl.class);

	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request)
	{
		log.debug("TimeTableCtl method preload started");
		CourseModelInt cmodel = ModelFactory.getInstance().getCourseModel();
		SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
		List<CourseDTO> clist = new ArrayList<CourseDTO>();
		List<SubjectDTO> slist = new ArrayList<SubjectDTO>();
	
		try
		{
			clist = cmodel.list();
			slist = smodel.list();
			request.setAttribute("CourseList", clist);
			request.setAttribute("SubjectList", slist);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		log.debug("TimeTableCtl method preload ended");
	}

	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request)
	{
		log.debug("TimeTableCtl method validate started");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("courseId")))
		{
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("subjectId")))
		
		{
			request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("semester")))
		{
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("ExDate")))
		{
			request.setAttribute("ExDate", PropertyReader.getValue("error.require", "Exam Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("ExTime"))) 
		{
			request.setAttribute("ExTime", PropertyReader.getValue("error.require", "Exam Time"));
			pass = false;
		}
		
		/*if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", "Please Select Description");
			pass = false;
		}*/

		log.debug("TimeTableCtl method preload ended");
		
		return pass;
	}
	
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected TimeTableDTO populateBean(HttpServletRequest request)
	{
		log.debug("TimeTableCtl method populateBean started");
	
		TimeTableDTO dto = new TimeTableDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		
		dto.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
	//	bean.setSubjectName(DataUtility.getString(request.getParameter("subjectname")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
//		bean.setCourseName(DataUtility.getString(request.getParameter("coursename")));
		dto.setSemester(DataUtility.getString(request.getParameter("semester")));
	//	bean.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setExamDate(DataUtility.getDate(request.getParameter("ExDate")));
		dto.setExamTime(DataUtility.getString(request.getParameter("ExTime")));
		System.out.println("<<<<<<__________>>>>>>>>");
		System.out.println(request.getParameter("ExDate"));
		System.out.println("<<<<<<__________>>>>>>>>");
		populateDTO(dto, request);
		log.debug("TimeTableCtl method populateBean ended");
		return dto;
	}

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("TimeTableCtl method doGet started");
		long id = DataUtility.getLong(request.getParameter("id"));
	
		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
		TimeTableDTO dto = null;
		if (id > 0) 
		{
			try 
			{
				dto = model.findByPk(id);
				ServletUtility.setBean(dto, request);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
			}
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("TimeTableCtl method doGet ended");


	}


    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("TimeTableCtl method doPost started");

		List list;
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
	
		
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) 
		{
			TimeTableDTO bean = (TimeTableDTO)populateBean(request);
			try 
			{
				if(id>0)
				{
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage(" TimeTable is Successfully Saved", request);
				}
				else
				{
				model.add(bean);
				ServletUtility.setSuccessMessage(" TimeTable is Successfully Saved", request);
				} 
				
				
				
			}
			catch (ApplicationException e)
			{
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
			}
			catch (DuplicateRecordException e)
			{
				e.printStackTrace();
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Time Table already Exists", request);
			}
		}
		else if (OP_CANCEL.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.TIMETABLE_LIST_CTL, request, response);
			return;
		}
		else if ( OP_RESET.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.TIMETABLE_CTL, request, response);
			return;
		}
			
		ServletUtility.forward(getView(), request, response);
		
		log.debug("TimeTableCtl method doPost ended");

	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.TIMETABLE_VIEW;
	}

}
