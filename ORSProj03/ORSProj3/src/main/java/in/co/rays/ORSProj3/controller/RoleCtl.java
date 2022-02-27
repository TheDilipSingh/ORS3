package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.RoleDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.RoleModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Role functionality Controller. Performs operation for add, update and get
 * Role
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/RoleCtl")

public class RoleCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
    
	private static Logger log=Logger.getLogger(RoleCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("RoleCtl Method validate Started");
		
		boolean pass=true;
		
		if(DataValidator.isNull(request.getParameter("name")))
		{
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass=false;
		}
		else if (!DataValidator.isName(request.getParameter("name"))) 
		{
       	 request.setAttribute("name", PropertyReader.getValue("error.name", "Invalid"));
         pass = false;
		}
		
		if(DataValidator.isNull(request.getParameter("description")))
		{
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass=false;
		}
		
		log.debug("RoleCtl Method validate Ended");
		
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
		log.debug("RoleCtl Method populatebean Started");
		
		RoleDTO bean=new RoleDTO();
		
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		
		bean.setName(DataUtility.getString(request.getParameter("name")));
		
        bean.setDescription(DataUtility.getString(request.getParameter("description")));
		
		populateDTO(bean, request);
		
		log.debug("RoleCtl Method populatebean Ended");
		
		return bean;
		
	}
	
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("RoleCtl Method doGet Started");
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		RoleModelInt model=ModelFactory.getInstance().getRoleModel();
		
		long id=DataUtility.getLong(request.getParameter("id"));
		
		if(id > 0 || op!=null)
		{
			RoleDTO dto;
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
		
		log.debug("RoleCtl Method doGet Ended");
						
	}


    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("RoleCtl Method doGet Started");
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		RoleModelInt model= ModelFactory.getInstance().getRoleModel();
		
		long id=DataUtility.getLong(request.getParameter("id"));
		
        if (OP_SAVE.equalsIgnoreCase(op)|| OP_UPDATE.equalsIgnoreCase(op)) 
        {

            RoleDTO bean = (RoleDTO) populateBean(request);

            try
            {
                if (id > 0) 
                {
                    model.update(bean);
                    ServletUtility.setBean(bean, request);

                }
                else 
                {
                    long pk = model.add(bean);
                    bean.setId(pk);
                }

                ServletUtility.setSuccessMessage("Role is successfully saved", request);

            } 
            catch (ApplicationException e) 
            {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                ServletUtility.setErrorMessage("Role already exists", request);                
                return;
            } 
            catch (DuplicateRecordException e) 
            {
                ServletUtility.setBean(bean, request);
                ServletUtility.setErrorMessage("Role already exists", request);
            }

        } 
        else if (OP_DELETE.equalsIgnoreCase(op)) 
        {

            RoleDTO bean = (RoleDTO) populateBean(request);
            try 
            {
                model.delete(bean);
                ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
                return;
            }
            catch (ApplicationException e) 
            {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }

        } 
        else if (OP_CANCEL.equalsIgnoreCase(op))
        {
            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("RoleCtl Method doPOst Ended");
				
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
	
		return ORSView.ROLE_VIEW;
	}

}
