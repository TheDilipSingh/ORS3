package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.BaseDTO;
import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.model.CollegeModelInt;
import in.co.rays.ORSProj3.model.ModelFactory;
import in.co.rays.ORSProj3.util.DataUtility;
import in.co.rays.ORSProj3.util.DataValidator;
import in.co.rays.ORSProj3.util.PropertyReader;
import in.co.rays.ORSProj3.util.ServletUtility;



/**
 * College functionality Controller. Performs operation for add, update, delete
 * and get College
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/ctl/CollegeCtl")
public class CollegeCtl extends BaseCtl 
{
	private static final long serialVersionUID = 1L;       

	private static Logger log=Logger.getLogger(CollegeCtl.class);
	
	/**
	 * Validates input data entered by User
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validate(HttpServletRequest request) 

	{

        log.debug("CollegeCtl Method validate Started");

        boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name")))
		{
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}
		else if (!DataValidator.isName(request.getParameter("name"))) 
		{
			request.setAttribute("name", PropertyReader.getValue("error.name", "Invalid"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("address"))) 
		{
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("state"))) 
		{
			request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
			pass = false;
		}
		else if (!DataValidator.isName(request.getParameter("state"))) 
		{
			request.setAttribute("state", PropertyReader.getValue("error.name", "Invalid State"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("city"))) 
		{
			request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
			pass = false;
		}
		else if (!DataValidator.isName(request.getParameter("city"))) 
		{
			request.setAttribute("city", PropertyReader.getValue("error.name", "Invalid City"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("phoneNo"))) 
		{
			request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "Mobile no."));
			pass = false;
		}
		else if (!DataValidator.isMobileNo(request.getParameter("phoneNo")))
		{
			request.setAttribute("phoneNo", "Mobile No. must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}

        log.debug("CollegeCtl Method validate Ended");

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

        log.debug("CollegeCtl Method populateBean Started");

        CollegeDTO dto = new CollegeDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));

        dto.setName(DataUtility.getString(request.getParameter("name")));

        dto.setAddress(DataUtility.getString(request.getParameter("address")));

        dto.setState(DataUtility.getString(request.getParameter("state")));

        dto.setCity(DataUtility.getString(request.getParameter("city")));

        dto.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

        populateDTO(dto, request);

        log.debug("CollegeCtl Method populateBean Ended");

        return dto;
    }
    
	/**
	 * Contains display logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		log.debug("CollegeCtl Method doGet Started");
		
        String op = DataUtility.getString(request.getParameter("operation"));
        
        CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0 || op!=null)
        {
            CollegeDTO dto;
            try
            {
                dto = model.findByPK(id);
                ServletUtility.setBean(dto, request);
            } catch (ApplicationException e) 
            {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        
        ServletUtility.forward(getView(), request, response);
		
        log.debug("CollegeCtl Method doGet Ended");
	}


    /**
     * Contains Submit logics
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        log.debug("CollegeCtl Method doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));
        
        CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();

        long id = DataUtility.getLong(request.getParameter("id"));
        
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) 
        {

            CollegeDTO dto = (CollegeDTO) populateBean(request);

            try 
            {
                if (id > 0)
                {
                    model.update(dto);
                    
                } else 
                {
                    long pk = model.add(dto);
                  //bean.setId(pk);
                }
               ServletUtility.setSuccessMessage("College successfully saved", request);
            } 
            catch (ApplicationException e) 
            {
				e.printStackTrace();
            	log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            } 
            catch (DuplicateRecordException e)
            {
                ServletUtility.setBean(dto, request);
                ServletUtility.setErrorMessage("College Name already exists", request);
            }

        } 
		else if ( OP_RESET.equalsIgnoreCase(op)) 
		{

			ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
			return;
		}
		else if (OP_CANCEL.equalsIgnoreCase(op) )
		{

			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, request, response);
			return;
		}
		else if (OP_DELETE.equalsIgnoreCase(op)) {

			CollegeDTO bean = (CollegeDTO) populateBean(request);
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.COLLEGE_CTL, request, response);
				return;

			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} 

        ServletUtility.forward(getView(), request, response);

        log.debug("CollegeCtl Method doPost Ended");
        
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
	
		return ORSView.COLLEGE_VIEW;
	}

}
