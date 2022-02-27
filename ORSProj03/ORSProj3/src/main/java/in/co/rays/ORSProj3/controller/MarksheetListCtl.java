package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.MarksheetDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.MarksheetModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.StudentModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 Marksheet List functionality Controller. Performs operation for list, search
 * and delete operations of Marksheet
 * 
 * @author Dilip Singh
 * @version 1.0
 * @Copyright (c) SunilOS
 */

@WebServlet("/ctl/MarksheetListCtl")
public class MarksheetListCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;

	private static Logger log=Logger.getLogger(MarksheetListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	   protected void preload(HttpServletRequest request)
	    {
	    	log.debug("MarksheetListCtl method preload started");
	    	
	    	StudentModelInt model = ModelFactory.getInstance().getStudentModel();
	    	List<MarksheetDTO> slist = null;
		
	    	try 
	    	{
	    		slist = model.list();
			}
	    	catch (ApplicationException e) 
	    	{
				e.printStackTrace();
			}
	    	request.setAttribute("studentList", slist);
	    	
	    	log.debug("MarksheetListCtl method preload ended");
	    }

	   
	   /**
		 * Populates bean object from request parameters
		 * 
		 * @param request
		 * @return
		 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		log.debug("MarksheetListCtl method populateBean Started");
		
		MarksheetDTO dto=new MarksheetDTO();
		
		dto.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		dto.setStudentId(DataUtility.getLong(request.getParameter("studentid")));
		
		log.debug("MarksheetListCtl method populateBean ended");
		
		return dto;
			
	}

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MarksheetListCtl method doGet Started");
		
		int pageNo= DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize= DataUtility.getInt(request.getParameter("pageSize"));
		
		pageNo=(pageNo==0) ? 1:pageNo; 
		pageSize=(pageSize==0) ? DataUtility.getInt(PropertyReader.getValue("page.size")):pageSize;
		
		MarksheetDTO dto=(MarksheetDTO) populateBean(request);
        String [] ids = request.getParameterValues("ids");
		
		List list=null;
		
		MarksheetModelInt model= ModelFactory.getInstance().getMarksheetModel();
		
		try 
		{
			list=model.search(dto, pageNo, pageSize);
			
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
			e.printStackTrace();
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
				
		log.debug("MarksheetListCtl method doGet ended");

	}


    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MarksheetListCtl method doPost started");
		
        List list = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;

        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        MarksheetDTO dto = (MarksheetDTO) populateBean(request);

        String op = DataUtility.getString(request.getParameter("operation"));
        
        String[] ids=request.getParameterValues("ids");
        
        MarksheetModelInt model= ModelFactory.getInstance().getMarksheetModel();
        
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
        ServletUtility.redirect(ORSView.MARKSHEET_CTL, request, response);
        return;
        }

        else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) 
        {
        ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request, response);
        return;        
        }
        else if (OP_DELETE.equalsIgnoreCase(op)) 
        {
        pageNo = 1;
        if (ids != null && ids.length > 0) 
        {
            MarksheetDTO deleteDTO = new MarksheetDTO();
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
					ServletUtility.handleException(e, request, response);
					return;
				}
            }
            ServletUtility.setSuccessMessage("Marksheet deleted successfully", request);
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
		e.printStackTrace();
		ServletUtility.handleException(e, request, response);
		return;
	}
    ServletUtility.setList(list, request);
    if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) 
    {
        ServletUtility.setErrorMessage("No record found ", request);
    }
    ServletUtility.setList(list, request);
    ServletUtility.setPageNo(pageNo, request);
    ServletUtility.setPageSize(pageSize, request);
    ServletUtility.forward(getView(), request, response);
        
        log.debug("MarksheetListCtl doPost ended");

	}


	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView()
	{		
		return ORSView.MARKSHEET_LIST_VIEW;
	}

}
