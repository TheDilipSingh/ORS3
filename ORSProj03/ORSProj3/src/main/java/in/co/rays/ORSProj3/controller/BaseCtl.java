package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Base controller class of project. It contain (1) Generic operations (2)
 * Generic constants (3) Generic work flow
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */

public abstract class BaseCtl extends HttpServlet 

{
	private static final long serialVersionUID = 1L;
    
	public static final String OP_SAVE="Save";
    public static final String OP_CANCEL = "Cancel";
    public static final String OP_DELETE = "Delete";
	public static final String OP_UPDATE = "Update";
	public static final String OP_RESET = "Reset";
    public static final String OP_LIST = "List";
    public static final String OP_SEARCH = "Search";
    public static final String OP_VIEW = "View";
    public static final String OP_NEXT = "Next";
    public static final String OP_PREVIOUS = "Previous";
    public static final String OP_NEW = "New";
    public static final String OP_GO = "Go";
    public static final String OP_BACK = "Back";
    public static final String OP_LOG_OUT = "Logout";
    public static final String OP_CHANGE_MY_PASSWORD = "Change Password";
    public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";
    
	/**
	 * Success message key constant
	 */
    public static final String MSG_SUCCESS = "success";
    
	/**
	 * Error message key constant
	 */
    public static final String MSG_ERROR = "error";
    
    
	public BaseCtl()
    {
        super();
    }
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request)
	{
		return true;
	}
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request)
	{
		
	}
	
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request)
	{
		return null;
	}
	
	/**
	 * Populates Generic attributes in DTO
	 * 
	 * @param dto
	 * @param request
	 * @return  object of the bean
	 */
	protected BaseDTO populateDTO(BaseDTO dto,HttpServletRequest request)
	{
		String createdBy=request.getParameter("createdBy");
		String modifiedBy=null;
		
		UserDTO userBean=(UserDTO) request.getSession().getAttribute("user");
		if(userBean==null)
		{
			createdBy="root";
			modifiedBy="root";
		}
		else
		{
			modifiedBy=userBean.getLogin();
			if("null".equalsIgnoreCase("createdBy") || DataValidator.isNull(createdBy))
			{
				createdBy=modifiedBy;		
			}
		}
		
		dto.setCreatedBy(createdBy);
		dto.setModifiedBy(modifiedBy);
		
		long cdt=DataUtility.getLong(request.getParameter("createdDatetime"));
		if(cdt>0)
		{
			dto.setCreatedDateTime(DataUtility.getTimestamp(cdt));
		}
		else
		{
			dto.setCreatedDateTime(DataUtility.getCurrentTimestamp());
		}
		
		dto.setModifiedDateTime(DataUtility.getCurrentTimestamp());
		
		return dto;
	}
	
	/**
	 * Call the service method of this Controller
	 * 
	 * @return
	 */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	preload(request);
    	
    	String op=DataUtility.getString(request.getParameter("operation"));
    	System.out.println("operation"+" "+op);
		if (DataValidator.isNotNull(op) && !OP_CANCEL.equalsIgnoreCase(op) && !OP_VIEW.equalsIgnoreCase(op) && !OP_DELETE.equalsIgnoreCase(op) && !OP_RESET.equalsIgnoreCase(op))
		{
    		if(!validate(request))
    		{
    			System.out.println("validate me fasa");
    			BaseDTO bean=populateBean(request);
    			ServletUtility.setBean(bean, request);
    			ServletUtility.forward(getView(), request, response);
    			return;
    		}
    	}
		System.out.println(op+ " " +",,,into Base CTL,,,,"+" "+request);
    	super.service(request, response);
    }
    
	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
    protected abstract String getView();

}
