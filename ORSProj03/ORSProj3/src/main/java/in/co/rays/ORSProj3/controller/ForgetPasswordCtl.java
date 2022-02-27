package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.RecordNotFoundException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.UserModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;

/**
 * Forget Password functionality Controller. Performs operation for Forget
 * Password
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ForgetPasswordCtl")
public class ForgetPasswordCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;

	private static Logger log=Logger.getLogger(ForgetPasswordCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("ForgetPasswordCtl method validate started");
		
		boolean pass=true;
		
		String login=request.getParameter("login");
		
		if(DataValidator.isNull(login))
		{
			request.setAttribute("login", PropertyReader.getValue("error.require", "Email Id"));
			pass=false;
		}
		else if(!DataValidator.isEmail(login))
		{
			request.setAttribute("login", PropertyReader.getValue("error.email", " "));
			pass=false;
		}
		log.debug("ForgetPasswordCtl method validate ended");
		
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
		log.debug("ForgetPasswordCtl Method populateBean Ended");
		
		UserDTO bean=new UserDTO();
		
        bean.setLogin(DataUtility.getString(request.getParameter("login")));

        log.debug("ForgetPasswordCtl Method populatebean Ended");

        return bean;	
	}
	
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		ServletUtility.forward(getView(), request, response);
		
	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("ForgetPasswordCtl Method doPost started");
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		System.out.println("in post sssssssssssss"+op);
		
		
		UserDTO dto=(UserDTO) populateBean(request);
		
		UserModelInt model=ModelFactory.getInstance().getUserModel();
		
		if(OP_GO.equalsIgnoreCase(op))
		{
			try 
			{
				model.forgetPassword(dto.getLogin());
				
				ServletUtility.setSuccessMessage("Password has been sent to your email id.", request);
			}			
			catch (ApplicationException e) 
			{
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
			catch (RecordNotFoundException e)
			{
				log.error(e);
				ServletUtility.setErrorMessage(e.getMessage(), request);
			}
					
		}
        else if (OP_RESET.equalsIgnoreCase(op)) 
        {        	
        	ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, request, response);
        	return;
		}

		ServletUtility.forward(getView(), request, response);
		
		log.debug("ForgetPasswordCtl Method doPost ended");
		
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
			return ORSView.FORGET_PASSWORD_VIEW;
	}

}
