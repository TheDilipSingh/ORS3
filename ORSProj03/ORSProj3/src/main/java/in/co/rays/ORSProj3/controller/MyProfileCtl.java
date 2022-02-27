package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
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
 * My Profile functionality Controller. Performs operation for update your
 * Profile
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */

@WebServlet("/ctl/MyProfileCtl")

public class MyProfileCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
    
	private static Logger log=Logger.getLogger(MyProfileCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("MyProfileCtl Method validate Started");
	System.out.println("validate in");
		
		boolean pass=true;
		
		String op = DataUtility.getString(request.getParameter("operation"));
		
		if(OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op))
		{
			return pass;
		}
		
		if(DataValidator.isNull(request.getParameter("firstName")))
		{
			System.out.println("validate in 1");
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass=false;
			System.out.println("validate in1");
		}
        else if (!DataValidator.isValidName(request.getParameter("firstName"))) 
        {
        	System.out.println("validate in2");
        	  request.setAttribute("firstName",PropertyReader.getValue("error.name", "First"));
              pass = false;
              System.out.println("validate in2");
		}
		
		if(DataValidator.isNull(request.getParameter("lastName")))
		{System.out.println("validate in3");
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass=false;System.out.println("validate in3");
		}
		 else if (!DataValidator.isValidName(request.getParameter("lastName")))
	    {
			 System.out.println("validate in4");
			 request.setAttribute("lastName",PropertyReader.getValue("error.name", "Last"));
			 pass = false;
			 System.out.println("validate in4");
	    }
		if(DataValidator.isNull(request.getParameter("gender")))
		{
			System.out.println("validate in5");
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass=false;
			System.out.println("validate in5");
		}
		
		if (DataValidator.isNull(request.getParameter("mobileNo"))) 
		{
			System.out.println("validate in6");
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile no."));
			pass = false;
			System.out.println("validate in6");
		}
		else if (!DataValidator.isMobileNo(request.getParameter("mobileNo")))
		{
			System.out.println("validate in7");
			request.setAttribute("mobileNo", "Mobile no. must be 10 Digit and No. Series start with 6-9");
			pass = false;
			System.out.println("validate in7");
		}
		
        if (DataValidator.isNull(request.getParameter("dob")))
        {
        	System.out.println("validate in8");
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
            pass = false;
            System.out.println("validate in8");
        }
        else if (!DataValidator.isvalidateAge(request.getParameter("dob")))
        {
        	System.out.println("validate in9");
                request.setAttribute("dob", "Student Age must be Greater then 18 years ");
                pass = false;
                System.out.println("validate in9");
        }
		
		log.debug("MyProfileCtl Method validate Ended");
		System.out.println("validate out" + pass);
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
		log.debug("MyProfileCtl Method populatebean Started");
		System.out.println("populate in");
		
		UserDTO dto=new UserDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		
        dto.setLogin(DataUtility.getString(request.getParameter("login")));

        dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));

        dto.setLastName(DataUtility.getString(request.getParameter("lastName")));

        dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));

        dto.setGender(DataUtility.getString(request.getParameter("gender")));

        dto.setDob(DataUtility.getDate(request.getParameter("dob")));
        
        populateDTO(dto, request);
        
        System.out.println("populate out");
        return dto;
        
        
        
	}
	
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MyprofileCtl Method doGet Started");
		
		HttpSession session=request.getSession(true);
		
		UserDTO userDTO=(UserDTO) session.getAttribute("user");
		
		long id=userDTO.getId();
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		UserModelInt model= ModelFactory.getInstance().getUserModel();
		
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
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
				
		}
		
		ServletUtility.forward(getView(), request, response);
		
		log.debug("MyProfileCtl Method doGet Ended");
		
		
	}

    /**
     * Contains Submit Logic
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MyprofileCtl Method doPost Started");
		
		System.out.println("00000000000000");
		HttpSession session=request.getSession(true);
		
		UserDTO userDTO=(UserDTO) session.getAttribute("user");
		
		long id=userDTO.getId();
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		UserModelInt model= ModelFactory.getInstance().getUserModel();
		
		System.out.println(op +"=====");
		if(OP_SAVE.equalsIgnoreCase(op))
		{
			UserDTO dto=(UserDTO) populateBean(request);
			
			try 
			{
				if(id>0)
				{
					userDTO.setFirstName(dto.getFirstName());
                    userDTO.setLastName(dto.getLastName());
                    userDTO.setGender(dto.getGender());
                    userDTO.setMobileNo(dto.getMobileNo());
                    userDTO.setDob(dto.getDob());
                    
                    model.update(userDTO);
				}
				
				ServletUtility.setBean(dto, request);
				ServletUtility.setSuccessMessage("Profile updated successfully.", request);
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
				e.printStackTrace();
				ServletUtility.setBean(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);			
			}
		}
		
		else if (OP_CHANGE_MY_PASSWORD.equalsIgnoreCase(op)) 
		{

            ServletUtility.redirect(ORSView.CHANGE_PASSWORD_CTL, request, response);
            return;

        }
		
		 ServletUtility.forward(getView(), request, response);

	     log.debug("MyProfileCtl Method doPost Ended");
		
		}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.MY_PROFILE_VIEW;
	}

}
