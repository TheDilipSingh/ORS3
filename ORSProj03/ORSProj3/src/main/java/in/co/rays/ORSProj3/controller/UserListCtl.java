package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.RoleDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.RoleModelInt;
import in.co.rays.ORSProj3.model.UserModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * User List functionality Controller. Performs operation for list, search and
 * delete operations of User
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/UserListCtl")

public class UserListCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
	
	private static Logger log=Logger.getLogger(UserListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
    protected void preload(HttpServletRequest request)
    {
    	log.debug("UserListCtl method preload started");
    	
    	RoleModelInt model = ModelFactory.getInstance().getRoleModel();
    	List<RoleDTO> rlist = null;
	
    	try 
    	{
			rlist = model.list();
		}
    	catch (ApplicationException e) 
    	{
			e.printStackTrace();
		}
    	request.setAttribute("RoleList", rlist);
    	
    	log.debug("UserListCtl method preload ended");
    }

    /**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		log.debug("UserListCtl method populateBean started");
		
		UserDTO dto=new UserDTO();
		
		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		dto.setRoleId(DataUtility.getLong(request.getParameter("roleid")));
		dto.setLogin(DataUtility.getString(request.getParameter("login")));

		log.debug("UserListCtl method populateBean started");
		
		return dto;	

	}
 
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("UserListCtl method doGet started");
		
		List list=null;
		int pageNo=1;
		int pageSize=DataUtility.getInt(PropertyReader.getValue("page.size"));
		UserDTO dto=(UserDTO) populateBean(request);
	//	String op=DataUtility.getString(request.getParameter("operation"));
	//	String[] ids=request.getParameterValues("ids");
		UserModelInt model=ModelFactory.getInstance().getUserModel();
		try 
		{
			list=model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if(list==null || list.size()==0)
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
		
		log.debug("UserListCtl method doPOst ended");
	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		log.debug("UserListCtl method doPost started");
		
		List list=null;
		int pageNo=DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize=DataUtility.getInt(request.getParameter("pageSize"));
		
		pageNo=(pageNo==0) ? 1:pageNo;
		pageSize=(pageSize==0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		UserDTO dto=(UserDTO) populateBean(request);
		String op=DataUtility.getString(request.getParameter("operation"));
		String[] ids=request.getParameterValues("ids");
		UserModelInt model=ModelFactory.getInstance().getUserModel();
		
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
        ServletUtility.redirect(ORSView.USER_CTL, request, response);
        return;
        }
        else if (OP_RESET.equalsIgnoreCase(op)) 
        {
            ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
            return;
        }
        else if (OP_DELETE.equalsIgnoreCase(op)) 
        {
        	pageNo = 1;
        	if (ids != null && ids.length > 0) 
        	{
            UserDTO deleteDTO = new UserDTO();
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
                ServletUtility.setSuccessMessage("User deleted successfully", request);       
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
    if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op) )
    {
        ServletUtility.setErrorMessage("No record found ", request);
    }
    ServletUtility.setList(list, request);
    ServletUtility.setPageNo(pageNo, request);
    ServletUtility.setPageSize(pageSize, request);
    ServletUtility.forward(getView(), request, response);
		
	log.debug("UserListCtl method doPost ended");
		
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.USER_LIST_VIEW;
	}

}
