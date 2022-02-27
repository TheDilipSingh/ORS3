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
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.CollegeModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * College List functionality Controller. Performs operation for list, search
 * and delete operations of College
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/CollegeListCtl")
public class CollegeListCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CollegeListCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	 protected void preload(HttpServletRequest request)
	    {
	    	log.debug("CollegeListCtl method preload started");
	    	
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
	    	
	    	log.debug("CollegeListCtl method preload ended");
	    }

	 /**
		 * Populates bean object from request parameters
		 * 
		 * @param request
		 * @return
		 */ 
	protected BaseDTO populateBean(HttpServletRequest request) {
		CollegeDTO dto = new CollegeDTO();

		dto.setId(DataUtility.getLong(request.getParameter("collegeid")));
		dto.setCity(DataUtility.getString(request.getParameter("city")));

		return dto;
	}

    /**
     * Contains display logic
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CollegeListCtl Method doGet Started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		CollegeDTO dto = (CollegeDTO) populateBean(request);
		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();
		String[] ids = request.getParameterValues("ids");

		List list = null;

		try {
			list = model.search(dto, pageNo, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("CollegeListCtl Method doGet Ended");
	}

    /**
     * Contains submit logic
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CollegeListCtl Method doPost Started");

		List list = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CollegeDTO dto = (CollegeDTO) populateBean(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
			return;
		}
		else if (OP_DELETE.equalsIgnoreCase(op)) 
		{
			System.out.println("======>>>>");
			pageNo = 1;
			if (ids != null && ids.length > 0) 
			{
				CollegeDTO deleteDTO = new CollegeDTO();
				// UserBean deletebean = new UserBean();
				for (String id : ids) {
					System.out.println("operation" + op + "in collegeListCtl");
					deleteDTO.setId(DataUtility.getInt(id));
					try {
						model.delete(deleteDTO);
					} catch (ApplicationException e) {
						e.printStackTrace();
						ServletUtility.handleException(e, request, response);
						return;
					}
					ServletUtility.setSuccessMessage("College Data Successfully Deleted", request);

				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {
			list = model.search(dto, pageNo, pageSize);
		} catch (ApplicationException e) {
			e.printStackTrace();
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		// ServletUtility.setList(list, request);

		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage("No record found ", request);
		}

		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);

		log.debug("CollegeListCtl Method doPost Ended");

	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() {

		return ORSView.COLLEGE_LIST_VIEW;
	}

}
