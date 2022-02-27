package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.RoleDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.UserModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * User registration functionality Controller. Performs operation for User
 * Registration
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/UserRegistrationCtl")
public class UserRegistrationCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
       
	public static final String OP_SIGN_UP="SignUp";
	
	private static Logger log=Logger.getLogger(UserRegistrationCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("UserRegistrationCtl Method validate started");
		
		boolean pass=true;
		
		String login=request.getParameter("login");
		String dob=request.getParameter("dob");
		
        if (DataValidator.isNull(request.getParameter("firstName"))) 
        {
        	System.out.println("validate"+""+"firstName");
            request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
            pass = false;
        }
        else if (!DataValidator.isValidName(request.getParameter("firstName"))) 
        {
        	System.out.println("validate"+""+"invalid firstName");
        	request.setAttribute("firstName", PropertyReader.getValue("error.name", "Invalid First") );
			pass = false;
		}        
        
        if (DataValidator.isNull(request.getParameter("lastName")))
        {
        	System.out.println("validate"+""+"lastName");
            request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
            pass = false;
        }
        else if (!DataValidator.isValidName(request.getParameter("lastName")))
        {
        	System.out.println("validate"+""+"invalid lasttName");
        	request.setAttribute("lastName", PropertyReader.getValue("error.name", "Invlid Last") );
			pass = false;
        }        
  
        if (DataValidator.isNull(login)) 
        {
        	System.out.println("validate"+""+"login");
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
        }
        else if (!DataValidator.isEmail(login)) 
        {
        	System.out.println("validate"+""+"invalid login");
            request.setAttribute("login", PropertyReader.getValue("error.email", ""));
            pass = false;
        }
        
        if (DataValidator.isNull(request.getParameter("mobileNo")))
        {
        	System.out.println("validate"+""+"moblieno");
        	request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
        	pass = false;
		}
        else if (!DataValidator.isMobileNo(request.getParameter("mobileNo")))
        {
        	System.out.println("validate"+""+"invalid mobileno");
			request.setAttribute("mobileNo", "Mobile No. contain 10 Digits & Series start with 6-9");
			pass = false;
		}
        
        if (DataValidator.isNull(request.getParameter("password"))) 
        {
        	System.out.println("validate"+""+"password");
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
        }
        else if (!DataValidator.isPassword(request.getParameter("password")))
        {
        	System.out.println("validate"+""+"invalid password");
        	request.setAttribute("password", "Password contain 8 letters with alpha-numeric & special Character");
        	pass = false;
        }
        if (DataValidator.isNull(request.getParameter("confirmPassword"))) 
        {
        	System.out.println("validate"+""+"confrmpass");
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        }
        
        if (DataValidator.isNull(request.getParameter("gender"))) 
        {
        	System.out.println("validate"+""+"gender");
            request.setAttribute("gender",PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }
        if (DataValidator.isNull(dob))
        {
        	System.out.println("validate"+""+"dob");
            request.setAttribute("dob",PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
        } /*else if (!DataValidator.isDate(dob)) {
        	request.setAttribute("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
            pass = false;
        }*/
        else if (!DataValidator.isvalidateAge(dob)) 
        {
        	System.out.println("validate"+""+"invalid dob");
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Minimum Age 18 year"));
            pass = false;
        }
        if (!request.getParameter("password").equals( request.getParameter("confirmPassword")) && !"".equals(request.getParameter("confirmPassword"))) 
        {
        	System.out.println("validate"+""+"invalid confpassword");
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.confirmpassword", "Password & Confirm Password"));
            pass = false;
        }
        
        log.debug("UserRegistrationCtl Method validate Ended");
        
        return pass;

	}
	
	/**
	 * Populates Generic attributes in DTO
	 * 
	 * @param dto
	 * @param request
	 * @return  object of the bean
	 */
	protected BaseDTO populatedto(HttpServletRequest request) 
	{
		log.debug("UserRegistrationCtl Method populatedto started");
		
		UserDTO dto=new UserDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setRoleId(RoleDTO.STUDENT);
		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));
        dto.setLastName(DataUtility.getString(request.getParameter("lastName")));
        dto.setLogin(DataUtility.getString(request.getParameter("login")));
        dto.setPassword(DataUtility.getString(request.getParameter("password")));
        dto.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
        dto.setGender(DataUtility.getString(request.getParameter("gender")));
        dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        dto.setDob(DataUtility.getDate(request.getParameter("dob")));

        populateDTO(dto, request);

        log.debug("UserRegistrationCtl Method populatedto Ended");

        return dto;
		
	}
	
	
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("UserRegistrationCtl Method doGet started");
		
		ServletUtility.forward(getView(), request, response);
		
		log.debug("UserRegistrationCtl Method doGet ended");
	}

	
    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("UserRegistrationCtl Method doPost started");
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		UserModelInt model=ModelFactory.getInstance().getUserModel();
		
		long id=DataUtility.getLong(request.getParameter("id"));
		
		if(OP_SIGN_UP.equalsIgnoreCase(op))
		{
			UserDTO dto=(UserDTO) populatedto(request);
			
			try 
			{
				long pk=model.registerUser(dto);
				dto.setId(pk);
			//	request.getSession().setAttribute("Userdto", dto);
                ServletUtility.setSuccessMessage("User Successfully Registered", request);
                ServletUtility.forward(getView(), request, response);
				return;				
			} 
			catch (ApplicationException e) 
			{
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;		
			}
			catch (DuplicateRecordException e)
			{
				log.error(e);
				ServletUtility.setBean(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
				ServletUtility.forward(getView(), request, response);
			}			
		}
		else if (OP_RESET.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
		}
		
		log.debug("UserRegistrationCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.USER_REGISTRATION_VIEW;
	}

}
