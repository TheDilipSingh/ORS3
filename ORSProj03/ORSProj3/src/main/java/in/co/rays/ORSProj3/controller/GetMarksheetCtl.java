package in.co.rays.ORSProj3.controller;

import java.io.IOException;

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
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Get Marksheet functionality Controller. Performs operation for Get Marksheet
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/GetMarksheetCtl")

public class GetMarksheetCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger(GetMarksheetCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */	
	protected boolean validate(HttpServletRequest request) 
	{
        log.debug("GetMarksheetCTL Method validate Started");

        boolean pass = true;
        
        if(DataValidator.isNull(request.getParameter("rollNo")))
        {
        	request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll no."));
        	pass=false;
        }
        
        log.debug("GetMarksheetCTL Method validate Ended");
        
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
		log.debug("GetMarksheetCtl Method populateBean Started");
		
		MarksheetDTO dto=new MarksheetDTO();
        
		dto.setId(DataUtility.getLong(request.getParameter("id")));

        dto.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

        dto.setName(DataUtility.getString(request.getParameter("name")));

        dto.setPhysics(DataUtility.getInt(request.getParameter("physics")));

        dto.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));

        dto.setMaths(DataUtility.getInt(request.getParameter("maths")));

        log.debug("GetMarksheetCtl Method populateBean Ended");

        return dto;
	
	}

    /**
     * Concept of Display method
     * 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		ServletUtility.forward(getView(), request, response);		
	}


    /**
     * Concept of Submit Method
     * 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        log.debug("GetMarksheetCtl Method doPost Started");
        String op = DataUtility.getString(request.getParameter("operation"));
        
        MarksheetModelInt model = ModelFactory.getInstance().getMarksheetModel();

        MarksheetDTO dto = (MarksheetDTO) populateBean(request);

        long id = DataUtility.getLong(request.getParameter("id"));
        
        if(OP_GO.equalsIgnoreCase(op))
        {
        	try 
        	{
				dto=model.findByRollNo(dto.getRollNo());
				if(dto!=null)
				{
					ServletUtility.setBean(dto, request);
                    
				}
				else
				{
					ServletUtility.setErrorMessage("RollNo Does Not exists", request);
				}
			}	
        	catch (ApplicationException e) 
        	{
        		e.printStackTrace();
        		log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
        }
        else if (OP_RESET.equalsIgnoreCase(op)) 
        {
        	ServletUtility.redirect(ORSView.GET_MARKSHEET_CTL, request, response);
        	return ;
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
		return ORSView.GET_MARKSHEET_VIEW;
	}

}
