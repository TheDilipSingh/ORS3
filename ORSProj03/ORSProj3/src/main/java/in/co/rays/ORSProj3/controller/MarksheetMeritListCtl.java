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
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.MarksheetModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Marksheet Merit List functionality Controller. Performance operation of
 * Marksheet Merit List
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */

@WebServlet("/ctl/MarksheetMeritListCtl")

public class MarksheetMeritListCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
	
	private static Logger log=Logger.getLogger(MarksheetMeritListCtl.class);
      
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request) 
	{
		MarksheetDTO dto=new MarksheetDTO();
		
		return dto;
	}

    /**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MarksheetMeritListCtl method doGet Started");
		
		int pageNo=1;
		int pageSize=DataUtility.getInt(PropertyReader.getValue("page.size"));
	//	MarksheetBean bean=(MarksheetBean) populateBean(request);
	//	String op=DataUtility.getString(request.getParameter("operation"));
		
		MarksheetModelInt model=ModelFactory.getInstance().getMarksheetModel();
		List list=null;
		try 
		{
			list=model.getMeritList(pageNo, pageSize);
			ServletUtility.setList(list, request);			
		} 
		catch (ApplicationException e) 
		{
			log.error(e);
			e.printStackTrace();
			ServletUtility.handleException(e, request, response);
			return;
		}
		if(list==null || list.size() == 0)
		{
			ServletUtility.setErrorMessage("No record found ", request);
		}
		
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);

		
		log.debug("MarksheetMeritListCtl method doGet ended");

	}

    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MarksheetMeritListCtl method doPost Started");
		
        String op = DataUtility.getString(request.getParameter("operation"));
        
        /* List list = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader
                .getValue("page.size")) : pageSize;
        MarksheetBean bean = (MarksheetBean) populateBean(request);
        MarksheetModel model = new MarksheetModel();
        try {*/
            if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
                return;
            }
            
            if (OP_GO.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.JASPER_CTL, request, response);
                return;
            }
        /*    list = model.getMeritList(pageNo, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found ", request);
            }
            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(ORSView.MARKSHEET_MERIT_LIST_VIEW, request,
                    response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
            return;
        }*/
		
		log.debug("MarksheetMeritListCtl method doPost ended");
	
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView()
	{		
		return ORSView.MARKSHEET_MERIT_LIST_VIEW;
	}

}
