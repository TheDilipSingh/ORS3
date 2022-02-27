package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.CollegeModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.StudentModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Student functionality Controller. Performs operation for add, update, delete
 * and get Student
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/StudentCtl")
public class StudentCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger(StudentCtl.class);	
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) 
	{
		log.debug("StudentCtl method preload started");
		CollegeModelInt model= ModelFactory.getInstance().getCollegeModel();
		
		try 
		{
			List l=model.list();
			request.setAttribute("collegeList", l);			
		}
		catch (ApplicationException e) 
		{
			log.error(e);
		}
		
		log.debug("StudentCtl method preload ended");
	}
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("StudentCtl method validate Started");
		
		boolean pass=true;
		
        if (DataValidator.isNull(request.getParameter("firstname")))
        {
            request.setAttribute("firstname",PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        }
        else if (!DataValidator.isValidName(request.getParameter("firstname"))) 
        {
        	  request.setAttribute("firstname",PropertyReader.getValue("error.name", "Invalid First"));
              pass = false;
		}
        if (DataValidator.isNull(request.getParameter("lastname"))) 
        {
            request.setAttribute("lastname",PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        }
        else if (!DataValidator.isValidName(request.getParameter("lastname")))
        {
      	  request.setAttribute("lastname",PropertyReader.getValue("error.name", "Invalid Last"));
          pass = false;
        }
        if (DataValidator.isNull(request.getParameter("mobile"))) 
        {
            request.setAttribute("mobile", PropertyReader.getValue("error.require", "Mobile no."));
            pass = false;
        }
        else if (!DataValidator.isMobileNo(request.getParameter("mobile")))
        {
      	  request.setAttribute("mobile", "Mobile No. must be 10 Digit and No. Series start with 6-9");
          pass = false;
        }
        if (DataValidator.isNull(request.getParameter("email"))) 
        {
            request.setAttribute("email", PropertyReader.getValue("error.require", "Email id"));
            pass = false;
        }
        else if (!DataValidator.isEmail(request.getParameter("email"))) 
        {
            request.setAttribute("email", PropertyReader.getValue("error.email", ""));
            pass = false;
        }
        if (DataValidator.isNull(request.getParameter("dob")))
        {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        }
        else if (!DataValidator.isvalidateAge(request.getParameter("dob")))
        {
                request.setAttribute("dob", "Student Age must be Greater then 18 year ");
                pass = false;
        }          
        if (DataValidator.isNull(request.getParameter("collegename"))) 
        {
            request.setAttribute("collegename", PropertyReader.getValue("error.require", "College Name"));
            pass = false;
        } 

        log.debug("StudentCtl Method validate Ended");

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
		log.debug("StudentCtl Method populateBean Started");
		
		StudentDTO dto=new StudentDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		
        dto.setFirstName(DataUtility.getString(request.getParameter("firstname")));

        dto.setLastName(DataUtility.getString(request.getParameter("lastname")));

        dto.setDob(DataUtility.getDate(request.getParameter("dob")));

        dto.setMobileNo(DataUtility.getString(request.getParameter("mobile")));

        dto.setEmail(DataUtility.getString(request.getParameter("email")));

        dto.setCollegeId(DataUtility.getLong(request.getParameter("collegename")));
        
        populateDTO(dto, request);
        
        log.debug("StudentCtl Method populateBean Ended");
        
        return dto;      

	}

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        log.debug("StudentCtl Method doGet Started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        
        StudentModelInt model= ModelFactory.getInstance().getStudentModel();
        
        if(id > 0 || op!=null)
        {
        	StudentDTO dto;
        	
        	try 
        	{
				dto=model.findByPK(id);
				ServletUtility.setBean(dto, request);
			}
        	catch (ApplicationException e) 
        	{
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
        }
        
        ServletUtility.forward(getView(), request, response);
        
        log.debug("StudentCtl Method doGet Ended");
		
	}


    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        log.debug("StudentCtl Method doPost Started");
        
        String op = DataUtility.getString(request.getParameter("operation"));
        long id = DataUtility.getLong(request.getParameter("id"));
        
        StudentModelInt model= ModelFactory.getInstance().getStudentModel();

        if (OP_SAVE.equalsIgnoreCase(op)|| OP_UPDATE.equalsIgnoreCase(op)) 
        {
            StudentDTO dto = (StudentDTO) populateBean(request);
            try
            {
                if (id > 0) 
                {
                    model.update(dto);
                    ServletUtility.setBean(dto, request);
                    ServletUtility.setSuccessMessage("Student is successfully updated",request);
                }
                else 
                {
                    long pk = model.add(dto);
             //      bean.setId(pk);
                    ServletUtility.setSuccessMessage("Student is successfully registered",request);
                }
                
            }
            catch (ApplicationException e) 
            {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            
            }
            catch (DuplicateRecordException e) 
            {
                ServletUtility.setBean(dto, request);
                ServletUtility.setErrorMessage("Student Email Id already exists", request);
            }
        } 
        else if ( OP_RESET.equalsIgnoreCase(op)) 
        {            
         	ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
            return;
         }
        else if (OP_CANCEL.equalsIgnoreCase(op) ) 
        {            
         	ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
            return;
        }
/*
        else if (OP_DELETE.equalsIgnoreCase(op)) {

            StudentBean bean = (StudentBean) populateBean(request);
            try {
                model.delete(bean);
                ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
                return;

            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
*/        ServletUtility.forward(getView(), request, response);

        log.debug("StudentCtl Method doPost Ended");	
		
	}


	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.STUDENT_VIEW;
	}

}
