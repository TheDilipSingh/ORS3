package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.util.ServletUtility;

/**
 * ErrorCtl forwards user to the error page in case of any Error occurred
 * @author Dilip Singh
 * @version 1.0 Copyright (c) SunilOS
 */

@WebServlet("/ctl/ErrorCtl")
public class ErrorCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
    private static Logger log= Logger.getLogger(ErrorCtl.class);   
 
    /**
     * Contains Display logics
     */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			log.debug("ErrorCtl Method doGet started");
			
			ServletUtility.forward(getView(), request, response);
			
			log.debug("ErrorCtl Method doGet ended");
	}

		 /**
	     * Contains Submit logics
	     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		log.debug("ErrorCtl Method doPost started");
		
		ServletUtility.forward(getView(), request, response);

		log.debug("ErrorCtl Method doPost ended");	
	}

	
	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
		return ORSView.ERROR_VIEW;
	}
	
}
