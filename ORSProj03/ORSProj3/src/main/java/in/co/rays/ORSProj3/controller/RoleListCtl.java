package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.RoleDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.RoleModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Role List functionality Controller. Performs operation for list, search
 * operations of Role
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */

@WebServlet("/ctl/RoleListCtl")
public class RoleListCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger(RoleListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) 
	{

		RoleModelInt model = ModelFactory.getInstance().getRoleModel();
		List<CourseDTO> rlist = null;

		try
		{
			rlist = model.list();
		}
		catch (ApplicationException e) 
		{
			e.printStackTrace();
		}
		request.setAttribute("roleList", rlist);
	}
	
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		RoleDTO dto=new RoleDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("rname")));
		
		return dto;
	}

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("RoleListCtl method doGet Started");
		List list=null;
		int pageNo=1;
		int pageSize=DataUtility.getInt(PropertyReader.getValue("page.size"));
		RoleDTO bean=(RoleDTO) populateBean(request);
		String op=DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
		RoleModelInt model=ModelFactory.getInstance().getRoleModel();
		try 
		{
			list=model.search(bean, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if(list==null || list.size() == 0)
			{
				ServletUtility.setErrorMessage("No record found", request);
			}
			
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		}
		catch (ApplicationException e) 
		{
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		
		log.debug("RoleListCtl method doGet Ended");

	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("RoleListCtl method doPost started");
		
		List list=null;
		int pageNo=DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize=DataUtility.getInt(request.getParameter("pageSize"));
		pageNo=(pageNo==0) ? 1 : pageNo;
		pageSize=(pageSize==0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		RoleDTO dto=(RoleDTO) populateBean(request);
		String op=DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
		RoleModelInt model=ModelFactory.getInstance().getRoleModel();
		
        if (OP_SEARCH.equalsIgnoreCase(op)) 
        {
            pageNo = 1;
        }
        else if (OP_NEXT.equalsIgnoreCase(op))
        {
            pageNo++;
        }
        else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1)
        {
            pageNo--;
        }
        else if (OP_NEW.equalsIgnoreCase(op)) 
        {
            ServletUtility.redirect(ORSView.ROLE_CTL, request, response);
            return;
        }
        else if (OP_RESET.equalsIgnoreCase(op)) 
        {
            ServletUtility.redirect(ORSView.ROLE_LIST_CTL, request, response);
            return;
        }
        else if (OP_DELETE.equalsIgnoreCase(op)) 
        {
            pageNo = 1;
            if (ids != null && ids.length > 0) 
            {
            	RoleDTO deleteDTO = new RoleDTO();
                
                for (String id : ids) 
                {
                    deleteDTO.setId(DataUtility.getInt(id));
                    try 
                    {
						model.delete(deleteDTO);
					}
                    catch (ApplicationException e) 
                    {
						log.error(e);
			            ServletUtility.handleException(e, request, response);
			            return;
					}
                    ServletUtility.setSuccessMessage("Role is Deleted Successfully ", request);       
                }
            }
            else 
            {
                ServletUtility.setErrorMessage("Select at least one record", request);
            }
        }
        try
        {
        	list = model.search(dto, pageNo, pageSize);
        }
        catch (ApplicationException e)
        {
        	log.error(e);
        	ServletUtility.handleException(e, request, response);
        	return;
        }
        if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) 
        {
        	ServletUtility.setErrorMessage("No record found ", request);
        }
        ServletUtility.setList(list, request);
        ServletUtility.setPageNo(pageNo, request);
        ServletUtility.setPageSize(pageSize, request);
        ServletUtility.forward(getView(), request, response);
		
		log.debug("RoleListCtl method doPost End");
				
	}


	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{	
		return ORSView.ROLE_LIST_VIEW;
	}

}
