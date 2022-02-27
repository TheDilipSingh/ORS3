package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.RoleModelInt;
import in.co.rays.ORSProj3.model.UserModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * * User functionality Controller. Performs operation for add, update and get
 * User
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/UserCtl")

public class UserCtl extends BaseCtl {
	
	private static final long serialVersionUID = 1L;

	private static Logger log=Logger.getLogger("UserCtl.class");
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request)
	{
		RoleModelInt model=ModelFactory.getInstance().getRoleModel();
		try 
		{
			List rlist=model.list();
			request.setAttribute("roleList", rlist);
		}
		catch (ApplicationException e) 
		{
			log.error(e);
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
		log.debug("UserRegistrationCtl Method validate started");
		
		boolean pass=true;
		
		String login=request.getParameter("login");
		String dob=request.getParameter("dob");
		String op=DataUtility.getString(request.getParameter("operation"));
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
        	request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile no."));
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
        	System.out.println("validate"+" "+"confirm"+""+"password");
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
            if(OP_UPDATE.equalsIgnoreCase(op) ) {
            	pass = true;
            }
        }
        
        if (DataValidator.isNull(request.getParameter("gender"))) 
        {
        	System.out.println("validate"+""+"gender");
            request.setAttribute("gender",PropertyReader.getValue("error.require", "Gender"));
            pass = false;
        }
        
        if (DataValidator.isNull(request.getParameter("roleId"))) 
        {
        	System.out.println("validate"+""+"role");
            request.setAttribute("roleId",PropertyReader.getValue("error.require", "Role"));
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
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.confirmpassword", "Password and Confirm Password"));
            pass = false;
        }
        
        log.debug("UserRegistrationCtl Method validate Ended");
        
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

        log.debug("UserCtl Method populatedto Started");

        UserDTO dto = new UserDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));

        dto.setRoleId(DataUtility.getLong(request.getParameter("roleId")));

        dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));

        dto.setLastName(DataUtility.getString(request.getParameter("lastName")));

        dto.setLogin(DataUtility.getString(request.getParameter("login")));

        dto.setPassword(DataUtility.getString(request.getParameter("password")));

        dto.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

        dto.setGender(DataUtility.getString(request.getParameter("gender")));

        dto.setDob(DataUtility.getDate(request.getParameter("dob")));

        dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
        
        populateDTO(dto,request);

        log.debug("UserCtl Method populatedto Ended");

        return dto;
    }

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	
	{
		log.debug("UserCtl Method doGet Started");
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		UserModelInt model=ModelFactory.getInstance().getUserModel();
		
		long id=DataUtility.getLong(request.getParameter("id"));
		
		if(id>0 || op!=null)
		{
			UserDTO dto;
			
			try
			{
				dto=model.findByPK(id);
				ServletUtility.setBean(dto, request);				
			} 
			catch (ApplicationException e) 
			{
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			}			
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("UserCtl method doGet ended");
	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("UserCtl Method doPost started");
		System.out.println("do post started");
		String op=DataUtility.getString(request.getParameter("operation"));
		long id=DataUtility.getLong(request.getParameter("id"));
		UserModelInt model=ModelFactory.getInstance().getUserModel();
		UserDTO dto=(UserDTO) populateBean(request);
		if(OP_SAVE.equalsIgnoreCase(op)||OP_UPDATE.equalsIgnoreCase(op))
		{
						
			try 
			{
				if(id>0)
				{
					model.update(dto);
					ServletUtility.setSuccessMessage("User Updated Successfully", request);
					ServletUtility.setBean(dto, request);
				}
				else
				{
					long pk=model.add(dto);
					//dto.setId(pk);
					ServletUtility.setSuccessMessage("User Registered Successfully", request);
				}
				
				
				
			}
			catch (ApplicationException e) 
			{
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;			
			}
			catch (DuplicateRecordException e)
			{
				e.printStackTrace();
				ServletUtility.setBean(dto, request);
				ServletUtility.setErrorMessage("Login id already exist", request);
			}
			
		}
    	else if ( OP_RESET.equalsIgnoreCase(op)) 
    	{
            ServletUtility.redirect(ORSView.USER_CTL, request, response);
            return;
        }
        	else if (OP_CANCEL.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) 
        {
            ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
                return;
        }
		
		ServletUtility.forward(getView(), request, response);
		
		log.debug("UserCtl Method doPost ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{		
		return ORSView.USER_VIEW;
	}

}
