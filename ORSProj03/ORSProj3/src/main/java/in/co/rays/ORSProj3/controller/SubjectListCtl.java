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
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.model.CourseModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.SubjectModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Subject List functionality Controller. Performs operation for list, search
 * operations of Subject
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/SubjectListCtl")

public class SubjectListCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;
    
	private static Logger log=Logger.getLogger(SubjectListCtl.class);

	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) 
	{
		log.debug("SubjectListCtl method preload started");

		
		SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
		CourseModelInt cmodel = ModelFactory.getInstance().getCourseModel();

		List<SubjectDTO> list = null;
		List<CourseDTO> list2 = null;

		try 
		{
			list = smodel.list();
			list2 = cmodel.list();
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}

		request.setAttribute("subjectList", list);
		request.setAttribute("courseList", list2);
		
		log.debug("SubjectListCtl method preload ended");
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	protected BaseDTO populateBean(HttpServletRequest request)
	{
		log.debug("SubjectListCtl method populateBean started");

		SubjectDTO dto = new SubjectDTO();

		dto.setId(DataUtility.getLong(request.getParameter("subjectname")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("coursename")));

		populateDTO(dto, request);
		
		log.debug("SubjectListCtl method populateBean ended");
		
		return dto;
	}

	/**
	 * Contains display logics
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("SubjectListCtl method doGet started");

		List list = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		SubjectDTO dto = (SubjectDTO) populateBean(request);
		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();

		try 
		{
			list = model.search(dto, pageNo, pageSize);
			
			if (list == null || list.size() == 0) 
			{
				ServletUtility.setErrorMessage("No Record Found", request);
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

		log.debug("SubjectListCtl method doGet ended");
	
	}


	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("SubjectListCtl method doPost started");

		List list=null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");
		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO dto = (SubjectDTO) populateBean(request);

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
			if (pageNo > 1) 
			{
				pageNo--;
			}
			else
			{
				pageNo = 1;
			}
		}
		else if (OP_NEW.equalsIgnoreCase(op)) 
		{
			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
			return;
		}
		else if (OP_DELETE.equalsIgnoreCase(op)) 
		{
			pageNo = 1;
			if (ids != null && ids.length > 0) 
			{
				SubjectDTO deleteDTO = new SubjectDTO();

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
					ServletUtility.setSuccessMessage("Subject Deleted Successfully ", request);
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
			e.printStackTrace();
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) 
		{
			ServletUtility.setErrorMessage("No Record Found", request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);


	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.SUBJECT_LIST_VIEW;
	}

}
