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
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.MarksheetModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.model.StudentModelInt;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Marksheet functionality Controller. Performs operation for add, update,
 * delete and get Marksheet
 * 
 * @author Dilip Singh
 * @version 1.0 Copyright (c) SunilOS
 */

@WebServlet("/ctl/MarksheetCtl")

public class MarksheetCtl extends BaseCtl
{
	private static final long serialVersionUID = 1L;

	private static Logger log=Logger.getLogger(MarksheetCtl.class);
	
	/**
	 * Loads list and other data required to display at HTML form
	 * 
	 * @param request
	 */
	protected void preload(HttpServletRequest request) 
	{
		log.debug("MarksheetCtl Method preload started");
		
		StudentModelInt model=ModelFactory.getInstance().getStudentModel();
		
		try 
		{
			List l=model.list();
			request.setAttribute("studentList", l);
		} 
		catch (ApplicationException e)
		{
			log.error(e);
		}
		
		log.debug("MarksheetCtl Method validate ended");
	}
	
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 
	{
		log.debug("MarksheetCtl Method validate started");
		
		boolean pass=true;
		
		if (DataValidator.isNull(request.getParameter("rollNo"))) 
		{
			request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll Number"));
			pass = false;
		} 
		else if (!DataValidator.isRollNo(request.getParameter("rollNo")))
		{
			request.setAttribute("rollNo", "Roll No. Should be in 00EC0000");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("studentId"))) 
		{
			request.setAttribute("studentId", PropertyReader.getValue("error.require", "Student Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("physics")))
		{
			request.setAttribute("physics", PropertyReader.getValue("error.require", "Physics Marks"));
			pass = false;

		}
		else if (DataUtility.getInt(request.getParameter("physics")) < 0)
		{
			request.setAttribute("physics", "Marks can not be less then 0 ");
			pass = false;
		}
		else if (DataUtility.getInt(request.getParameter("physics")) > 100)
		{
			request.setAttribute("physics", "Marks can not be more then 100");
			pass = false;
		}
		else if (DataValidator.isNotNull(request.getParameter("physics"))&& !DataValidator.isInteger(request.getParameter("physics"))) 
		{
			request.setAttribute("physics", PropertyReader.getValue("error.integer", "Physics Marks"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("chemistry")))
		{
			request.setAttribute("chemistry", PropertyReader.getValue("error.require", "Chemistry Marks"));
			pass = false;
		} 
		else if (DataUtility.getInt(request.getParameter("chemistry")) > 100)
		{
			request.setAttribute("chemistry", "Marks can not be more then 100");
			pass = false;
		}
		else if (DataUtility.getInt(request.getParameter("chemistry")) < 0) 
		{
			request.setAttribute("chemistry", "Marks can not be less then 0  ");
			pass = false;
		}
		else if (DataValidator.isNotNull(request.getParameter("chemistry"))&& !DataValidator.isInteger(request.getParameter("chemistry")))
		{
			request.setAttribute("chemistry", PropertyReader.getValue("error.integer", "Chemistry Marks"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("maths")))
		{
			request.setAttribute("maths", PropertyReader.getValue("error.require", "Maths Marks"));
			pass = false;
		} 
		else if (DataUtility.getInt(request.getParameter("maths")) > 100)
		{
			request.setAttribute("maths", "Marks can not be more then 100");
			pass = false;
		} 
		else if (DataUtility.getInt(request.getParameter("maths")) < 0)
		{
			request.setAttribute("maths", "Marks can not be less then 0 ");
			pass = false;
		} 
		else if (DataValidator.isNotNull(request.getParameter("maths"))&& !DataValidator.isInteger(request.getParameter("maths")))
		{
			request.setAttribute("maths", PropertyReader.getValue("error.integer", "Maths Marks"));
			pass = false;
		}
		
		log.debug("MarksheetCtl method validate ended");
		
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
		 log.debug("MarksheetCtl method populateBean started");
		 
		 MarksheetDTO dto=new MarksheetDTO();
		 
		 dto.setId(DataUtility.getLong(request.getParameter("id")));
		 dto.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		 dto.setName(DataUtility.getString(request.getParameter("name")));
		 dto.setPhysics(DataUtility.getInt(request.getParameter("physics")));
		 dto.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
		 dto.setMaths(DataUtility.getInt(request.getParameter("maths")));
		 dto.setStudentId(DataUtility.getLong(request.getParameter("studentId")));
		 
		 populateDTO(dto, request);
		 
	     log.debug("MarksheetCtl method populatebean Ended");

	     return dto;
	
	}
	
	/**
	 * Contains Display logics
	 */	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        log.debug("MarksheetCtl Method doGet Started");

        String op=DataUtility.getString(request.getParameter("operation"));
        
        MarksheetModelInt model=ModelFactory.getInstance().getMarksheetModel();
        
        long id=DataUtility.getLong(request.getParameter("id"));
        
        if (id > 0 || op != null)
        {
        	MarksheetDTO dto;
        	try 
        	{
				dto=model.findByPK(id);
				ServletUtility.setBean(dto, request);
			}
        	catch (ApplicationException e)
        	{
        		e.printStackTrace();
        		log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
        }
        
        ServletUtility.forward(getView(), request, response);
        
        log.debug("MarksheetCtl Method doGet Ended");
		
	}

	/**
	 * Contains Submit logics
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("MarksheetCtl Method doPost Started");
		
        String op=DataUtility.getString(request.getParameter("operation"));
        
        MarksheetModelInt model=ModelFactory.getInstance().getMarksheetModel();
        
        MarksheetDTO bean=(MarksheetDTO) populateBean(request);
        
        long id=DataUtility.getLong(request.getParameter("id"));
        
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op))
		{
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
					
				//	bean.setId(pk);
				}
				ServletUtility.setSuccessMessage("Marksheet saved successfully ", request);

			} 
			catch (ApplicationException e) 
			{
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			}
			catch (DuplicateRecordException e) 
			{
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Roll no already exists", request);
			}

		}
		else if (OP_RESET.equalsIgnoreCase(op)) 
		{
			ServletUtility.redirect(ORSView.MARKSHEET_CTL, request, response);
			return;
		}
		else if (OP_CANCEL.equalsIgnoreCase(op))
		{
			ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, request, response);
			return;
		}
		
		ServletUtility.forward(getView(), request, response);
        
        log.debug("MarksheetCtl Method doPost Ended");       
        		
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{	
		return ORSView.MARKSHEET_VIEW;
	}

}
