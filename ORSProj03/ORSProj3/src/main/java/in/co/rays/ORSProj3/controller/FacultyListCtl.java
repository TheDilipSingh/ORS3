package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.FacultyDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.CollegeModelInt;
import in.co.rays.ORSProj3.model.FacultyModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Faculty List functionality Controller. Performs operation for list, search
 * and delete operations of Faculty
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/FacultyListCtl")

public class FacultyListCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;
	
	private static Logger log=Logger.getLogger(FacultyListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request)
    {
    	log.debug("FacultyListCtl method preload started");
    	
    	CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();
    	List<CollegeDTO> clist = null;
	
    	try 
    	{
    		clist = model.list();
		}
    	catch (ApplicationException e) 
    	{
			e.printStackTrace();
		}
    	request.setAttribute("collegeList", clist);
    	
    	log.debug("FacultyListCtl method preload ended");
    }

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		log.debug("FacultyListCtl method populateBean started");

		FacultyDTO dto = new FacultyDTO();

		dto.setFirstName(DataUtility.getString(request.getParameter("firstname")));
	//	bean.setLastName(DataUtility.getString(request.getParameter("lastname")));
		dto.setLoginId(DataUtility.getString(request.getParameter("login")));
		dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeid")));
		/*bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setDateofjoining(DataUtility.getDate(request.getParameter("doj")));*/
		
		log.debug("FacultyListCtl method populateBean ended");
		
		return dto;
	}

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("FacultyListCtl method doGet started");
		
		List list;
		
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		
		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();
		FacultyDTO dto = (FacultyDTO) populateBean(request);
		
		String op = DataUtility.getString(request.getParameter("operation"));
		
		try
		{
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) 
			{
                ServletUtility.setErrorMessage("No record found ", request);
            }
			ServletUtility.setList(list, request);
	        ServletUtility.setPageNo(pageNo, request);
	        ServletUtility.setPageSize(pageSize, request);
	        ServletUtility.forward(getView(), request, response);
	        
        }
		catch (ApplicationException e)
		{
			e.printStackTrace();
        	log.error(e);
			ServletUtility.handleException(e, request, response);
			return ;
			
		}

		log.debug("FacultyListCtl method doGet ended");

	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("FacultyListCtl method doPost started");
		
		List list;
		
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		String op = DataUtility.getString(request.getParameter("operation"));

		FacultyDTO dto = (FacultyDTO)populateBean(request);
		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();

		String[] ids = (String[]) request.getParameterValues("ids");

		
				if (OP_SEARCH.equalsIgnoreCase(op)) 
				{
					pageNo = 1;
				} 
				else if (OP_NEXT.equalsIgnoreCase(op)) 
				{
					pageNo++;
				} 
				else if (OP_PREVIOUS.equalsIgnoreCase(op)) 
				{
					if(pageNo>1)
					{
						pageNo--;
					}
					else
					{
					pageNo=1;
					}
				}
				else if (OP_NEW.equalsIgnoreCase(op)) 
				{
					ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
					return ;
				}				
				else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) 
				{
					ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
					return;
				}
				
				else if (OP_DELETE.equalsIgnoreCase(op)) 
				{	
						pageNo =1;
						if(ids !=null && ids.length !=0)
						{
						FacultyDTO deleteDTO = new FacultyDTO();
							for (String id : ids)
							{
								deleteDTO.setId(DataUtility.getInt(id));
								try 
								{
									model.delete(deleteDTO);
								}
								catch (ApplicationException e)
								{
									e.printStackTrace();
									log.error(e);
									ServletUtility.handleException(e, request, response);
									return;
								}
								ServletUtility.setSuccessMessage("Data Deleted Succesfully", request);
							}
							
						}else
						{
							ServletUtility.setErrorMessage("Select at least one record", request);
						}
					}	
				try 
				{
					list=model.search(dto, pageNo, pageSize);
				
				} catch (ApplicationException e) {
					e.printStackTrace();
					ServletUtility.handleException(e, request, response);
					return;
				}
				
				if(list == null || list.size()==0 && !OP_DELETE.equalsIgnoreCase(op))
				{
					ServletUtility.setErrorMessage("No Record Found", request);
				}
				
				ServletUtility.setList(list, request);
				ServletUtility.setPageNo(pageNo, request);
				ServletUtility.setPageSize(pageSize, request);
				ServletUtility.forward(getView(), request, response);


				
				log.debug("FacultyListCtl method doPost started");

	}


	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView()
	{
		return ORSView.FACULTY_LIST_VIEW;
	}

}
