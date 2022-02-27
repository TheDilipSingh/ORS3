package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.FacultyDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.CollegeModelInt;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.FacultyModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.SubjectModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Faculty functionality Controller. Performs operation for add, update, delete
 * and get operations of Faculty
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/FacultyCtl")

public class FacultyCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger(FacultyCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) 
	{
		log.debug("CourseCtl method preload started");
		CourseModelInt cmodel=ModelFactory.getInstance().getCourseModel();
		CollegeModelInt comodel=ModelFactory.getInstance().getCollegeModel();
		SubjectModelInt smodel=ModelFactory.getInstance().getSubjectModel(); 
		
		try
		{
			List<CourseDTO> clist=cmodel.list();
			List<CollegeDTO> colist=comodel.list();
			List<SubjectDTO> slist=smodel.list();
			
			request.setAttribute("CourseList", clist);
			request.setAttribute("CollegeList", colist);
			request.setAttribute("SubjectList", slist);
		} 
		catch (ApplicationException e) 
		{
			log.error(e);
			e.printStackTrace();
		}
		
		log.debug("CourseCtl method preload ended");

	}
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("FacultyCtl method validate started");
		boolean pass = true;
		if(DataValidator.isNull(request.getParameter("firstname")))
		{
			request.setAttribute("firstname", PropertyReader.getValue("error.require", "First Name"));
			 pass = false;
		}
		else if (!DataValidator.isValidName(request.getParameter("firstname")))
		{
			request.setAttribute("firstname", PropertyReader.getValue("error.name", "Invalid First"));
			 pass = false;
		}
		if(DataValidator.isNull(request.getParameter("lastname")))
		{
			request.setAttribute("lastname", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
		}
		else if (!DataValidator.isValidName(request.getParameter("lastname")))
		{
			request.setAttribute("lastname", PropertyReader.getValue("error.name", "Invalid Last"));
			pass = false;			
		}
		if (DataValidator.isNull(request.getParameter("gender"))) 
		{
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("doj"))) 
		{
			request.setAttribute("doj", PropertyReader.getValue("error.require", "Date of Joining"));
			pass = false;
		}
		if(DataValidator.isNull(request.getParameter("qualification")))
		{
			request.setAttribute("qualification", PropertyReader.getValue("error.require", "Qualification"));
			pass = false;
		}
		if(DataValidator.isNull(request.getParameter("loginid")))
		{
			request.setAttribute("loginid", PropertyReader.getValue("error.require", "Login id"));
			pass = false;
		}
		else if (!DataValidator.isEmail(request.getParameter("loginid"))) 
		{
			request.setAttribute("loginid", PropertyReader.getValue("error.email", ""));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobileno")))
		{
			request.setAttribute("mobileno", PropertyReader.getValue("error.require", "Mobile no."));
			pass = false;
		}
		else if (!DataValidator.isMobileNo(request.getParameter("mobileno")))
		{
			request.setAttribute("mobileno", "Mobile No. must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("collegeid"))) 
		{
			request.setAttribute("collegeid", PropertyReader.getValue("error.require", "College Name"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("courseid")))
		{
			request.setAttribute("courseid", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("subjectid"))) 
		{
			request.setAttribute("subjectid", PropertyReader.getValue("error.require", "Subject Name"));
			pass = false;
		}
		

		log.debug("FacultyCtl method validate ended");
		
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
		log.debug("FacultyCtl method PopulateBean started");

		FacultyDTO dto = new FacultyDTO();
	
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setFirstName(DataUtility.getString(request.getParameter("firstname")));
		dto.setLastName(DataUtility.getString(request.getParameter("lastname")));
		dto.setGender(DataUtility.getString(request.getParameter("gender")));
		dto.setDateOfJoining((DataUtility.getDate(request.getParameter("doj"))));
		dto.setQualification(DataUtility.getString(request.getParameter("qualification")));
		dto.setLoginId(DataUtility.getString(request.getParameter("loginid")));
		dto.setMobileno(DataUtility.getString(request.getParameter("mobileno")));
		dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeid")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseid")));
		dto.setSubjectId(DataUtility.getLong(request.getParameter("subjectid")));
		populateDTO(dto, request);

		log.debug("FacultyCtl method PopulateBean ended");

		return dto;
	}

	
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		log.debug("FacultyCtl method doGet started");
		String op = DataUtility.getString(request.getParameter("operation"));
		

		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();
		Long id = DataUtility.getLong(request.getParameter("id"));
		
		if(id>0 || op!= null)
		{
			FacultyDTO dto;
			try
			{
				dto = model.findByPK(id);
				ServletUtility.setBean(dto, request);
			
			} 
			catch (ApplicationException e) 
			{
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		log.debug("FacultyCtl method doGet ended");

		ServletUtility.forward(getView(), request, response);

	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("FacultyCtl method doPost started");
		
		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));	
			

		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) 
		{
			FacultyDTO dto = (FacultyDTO) populateBean(request);
			
			try
			{
			if(id > 0)
			{
				model.update(dto);
				ServletUtility.setBean(dto, request);
				ServletUtility.setSuccessMessage("Faculty Successfully Updated", request);
			}
			else
			{
			long pk = model.add(dto);
			ServletUtility.setSuccessMessage("Faculty Successfully Registered", request);
			}
			
			
			}
			catch(ApplicationException e)
			{
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return ; 
			}
			catch (DuplicateRecordException e)
			{
				ServletUtility.setBean(dto, request);
				ServletUtility.setErrorMessage("Faculty email id already exist", request);
			}
		}
		/*else if (OP_DELETE.equalsIgnoreCase(op)) {
			FacultyBean bean =(FacultyBean) populateBean(request);
	
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
	}*/	
		else if (OP_RESET.equalsIgnoreCase(op)) 
		{
				ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
				return ;
		}			
	else if (OP_CANCEL.equalsIgnoreCase(op) ) 
	{
		ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
		return ;
	}			
		ServletUtility.forward(getView(), request, response);
		log.debug("FacultyCtl method doPost ended");
	}


	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.FACULTY_VIEW;
	}

}
