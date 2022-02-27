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
import in.co.rays.ORSProj3.exception.RecordNotFoundException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.UserModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Change Password functionality Controller. Performs operation for Change
 * Password
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */

@WebServlet("/ctl/ChangePasswordCtl")

public class ChangePasswordCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
    
	private static Logger log=Logger.getLogger(ChangePasswordCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
        log.debug("ChangePasswordCtl Method validate Started");

        boolean pass = true;

        String op = request.getParameter("operation");

        if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op))
        {
            return pass;
        }
        if (DataValidator.isNull(request.getParameter("oldPassword")))
        {
            request.setAttribute("oldPassword",PropertyReader.getValue("error.require", "Old Password"));
            pass = false;
        }
        else if (!DataValidator.isPassword(request.getParameter("oldPassword")))
        {
        	request.setAttribute("oldPassword", PropertyReader.getValue("error.password", "Incorrect"));
        	pass = false;
        }
        if (DataValidator.isNull(request.getParameter("newPassword"))) 
        {
            request.setAttribute("newPassword",PropertyReader.getValue("error.require", "New Password"));
            pass = false;
        }
        else if (!DataValidator.isPassword(request.getParameter("newPassword"))) 
        {
        	request.setAttribute("newPassword", "Password should contain 8 letter with alphanumeric and special Character");
        	pass = false;
        }
        if (DataValidator.isNull(request.getParameter("confirmPassword"))) 
        {
            request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
            pass = false;
        }
        if (!request.getParameter("newPassword").equals(request.getParameter("confirmPassword")) && !"".equals(request.getParameter("confirmPassword"))) 
        {
           request.setAttribute("confirmPassword", PropertyReader.getValue("error.confirmpassword", "New Password and Confirm Password"));
           pass = false;
        }

        log.debug("ChangePasswordCtl Method validate Ended");

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
        log.debug("ChangePasswordCtl Method populateBean Started");

        UserDTO dto = new UserDTO();

        dto.setPassword(DataUtility.getString(request.getParameter("oldPassword")));

        dto.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

        populateDTO(dto, request);

        log.debug("ChangePasswordCtl Method populateBean Ended");

        return dto;
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
		log.debug("ChangePasswordCtl Method doPost Started");		
		HttpSession session=request.getSession(true);
        String op = DataUtility.getString(request.getParameter("operation"));

        UserModelInt model=ModelFactory.getInstance().getUserModel();
        
        UserDTO dto=(UserDTO) populateBean(request);
        
        UserDTO userDTO=(UserDTO) session.getAttribute("user");
		
		String newPassword=DataUtility.getString(request.getParameter("newPassword"));
		
		long id=userDTO.getId();
		
		if(OP_SAVE.equalsIgnoreCase(op))
		{
			try 
			{
				boolean flag=model.changePassword(id, dto.getPassword(), newPassword);
				
				if(flag==true)
				{
					dto=model.findByLogin(userDTO.getLogin());
					session.setAttribute("user", dto);
					ServletUtility.setBean(dto, request);
					ServletUtility.setSuccessMessage("Password changed successfully.", request);
				}
				
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
                ServletUtility.setErrorMessage("Old Password is Invalid", request);
            }
        }
		
		else if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) 
		{
            ServletUtility.redirect(ORSView.MY_PROFILE_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        
        log.debug("ChangePasswordCtl Method doPost Ended");
				
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.CHANGE_PASSWORD_VIEW;
	}

}
