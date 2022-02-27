package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.RoleDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.RoleModelInt;
import in.co.rays.ORSProj3.model.UserModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;

/**
 * Login functionality Controller. Performs operation for Login
 * 
 * @author Dilip Singh
 * @version 1.0 Copyright (c) SunilOS
 */

@WebServlet("/LoginCtl")

public class LoginCtl extends BaseCtl {
	
	private static final long serialVersionUID = 1L;
       
    public static final String OP_REGISTER = "Register";
    public static final String OP_SIGN_IN = "SignIn";
    public static final String OP_SIGN_UP = "SignUp";
    public static final String OP_LOG_OUT = "logout";

    private static Logger log=Logger.getLogger("LoginCtl.class");
    
    /**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
    protected boolean validate(HttpServletRequest request)
    {
    	log.debug("LoginCtl method validate started");
    	
    	boolean pass=true;
    	
    	String op=request.getParameter("operation");
    	if(OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op))
    	{
    		return pass;
    	}
    	
    	String login=request.getParameter("login");
    	
    	if(DataValidator.isNull(login))
    	{
    		request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
    		pass=false;
    	}
    	else if(!DataValidator.isEmail(login))
    	{
    		request.setAttribute("login", PropertyReader.getValue("error.email", ""));
    		pass=false;
    	}
    	
    	if(DataValidator.isNull(request.getParameter("password")))
    	{
    		request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
    		pass=false;
    	}
    
    	log.debug("LoginCtl method validate ended");
    	
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
    	log.debug("LoginCtl method populateBean started");
    	
    	UserDTO bean=new UserDTO();
    	
    	bean.setId(DataUtility.getLong(request.getParameter("id")));
    	bean.setLogin(DataUtility.getString(request.getParameter("login")));
    	bean.setPassword(DataUtility.getString(request.getParameter("password")));
    	
    	log.debug("LoginCtl method populateBean ended");
    	
    	return bean;
    	
    }

	/**
	 * Contains display logic
	 * 
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	log.debug("LoginCtl method doGet started");
    	
    	HttpSession session=request.getSession(true);
    	
    	String op=DataUtility.getString(request.getParameter("operation"));
    		
    	
		if (OP_LOG_OUT.equals(op))
		{

			session.invalidate();
			ServletUtility.setSuccessMessage("User Logout Succesfully", request);
			ServletUtility.forward(getView(), request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		
    	log.debug("LoginCtl method doGet ended");
	}

	/**
	 * Contains Submit logics
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
	
    	HttpSession session=request.getSession(true);
    	log.debug("LoginCtl method doPost started");
    	
    	String op=DataUtility.getString(request.getParameter("operation"));
    	
    	UserModelInt model=ModelFactory.getInstance().getUserModel();
    	RoleModelInt role=ModelFactory.getInstance().getRoleModel();
    	
    	// long id=DataUtility.getLong(request.getParameter("id"));
    	
		if (OP_SIGN_IN.equalsIgnoreCase(op)) {
			UserDTO bean = (UserDTO) populateBean(request);

		
			try 
			{
				bean = model.authenticate(bean.getLogin(), bean.getPassword());
				ServletUtility.setBean(bean, request);

				if (bean != null) 
				{
					session.setAttribute("user", bean);
					long rollId = bean.getRoleId();
					RoleDTO rolebean = role.findByPK(rollId);

					if (rolebean != null) 
					{
						session.setAttribute("role", rolebean.getName());
					}

					// Code of The URI
					String str = (String) session.getAttribute("URI");
					if (str == null || "null".equalsIgnoreCase(str)) 
					{
						ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
						return;
					}
					else 
					{
						ServletUtility.redirect(str, request, response);
						return;
					}
				}
				else 
				{
					 bean = (UserDTO) populateBean(request);
					 ServletUtility.setBean(bean, request);
				     ServletUtility.setErrorMessage("Invalid Login id and Password", request);
				
				}
			} 
			catch (ApplicationException e) 
			{
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} 
		else if (OP_SIGN_UP.equalsIgnoreCase(op)) 
		{
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
			return;
		}
    	
    	ServletUtility.forward(getView(), request, response);
    	
    	log.debug("LoginCtl method doPost ended");
    	
	}

    /**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.LOGIN_VIEW;
	}

}
