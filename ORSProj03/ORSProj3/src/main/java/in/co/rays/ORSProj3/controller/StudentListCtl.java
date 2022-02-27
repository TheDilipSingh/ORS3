package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.CollegeModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.StudentModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Student List functionality Controller. Performs operation for list, search
 * and delete operations of Student
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/StudentListCtl")
public class StudentListCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger(StudentListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request)
	    {
	    	log.debug("StudentListCtl method preload started");
	    	
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
	    	
	    	log.debug("StudentListCtl method preload ended");
	    }

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		log.debug("StudentListCtl method populateBean started");
		StudentDTO dto=new StudentDTO();
		
		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));
	//	bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		dto.setEmail(DataUtility.getString(request.getParameter("email")));
		dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeid")));
		
		log.debug("StudentListCtl method populateBean ended");
		
		return dto;		
	}
	
    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("StudentListCtl method doGet started");
		List list=null;
		int pageNo=1;
		int pageSize=DataUtility.getInt(PropertyReader.getValue("page.size"));
		
		StudentDTO dto= (StudentDTO) populateBean(request);
		
		String op=DataUtility.getString(request.getParameter("operation"));
		
		StudentModelInt model=ModelFactory.getInstance().getStudentModel();
		
		try 
		{
		
			list=model.search(dto, pageNo, pageSize);
		//	ServletUtility.setList(list, request);
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
		
		log.debug("StudentListCtl method doGet ended");
	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("StudentListCtl method doPost started");

		List list=null;
		int pageNo=DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize=DataUtility.getInt(request.getParameter("pageSize"));
		pageNo=(pageNo==0)? 1 : pageNo;
		pageSize=(pageSize==0)? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		StudentDTO dto=(StudentDTO) populateBean(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
        StudentModelInt model = ModelFactory.getInstance().getStudentModel();
        
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
            pageNo--;
        }
        else if (OP_NEW.equalsIgnoreCase(op))
        {
			ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
			return;
		}
        else if (OP_RESET.equalsIgnoreCase(op))
        {
			ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
			return;
		}  
        else if (OP_DELETE.equalsIgnoreCase(op))
        {
            pageNo = 1;
            if (ids != null && ids.length > 0)
            {
            	StudentDTO deleteDTO = new StudentDTO();
           
                for (String id : ids)
                {
                    deleteDTO.setId(DataUtility.getInt(id));
                    try 
                    {
        				model.delete(deleteDTO);
						
					} catch (ApplicationException e)
                    {
						e.printStackTrace();
						ServletUtility.handleException(e, request, response);
						return;
					}
                    ServletUtility.setSuccessMessage(" Student Data Successfully Deleted", request);
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

        log.debug("StudentListCtl method doPost ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.STUDENT_LIST_VIEW;
	}

}
