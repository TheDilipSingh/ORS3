package in.co.rays.ORSProj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.util.ServletUtility;


/**
 * Welcome functionality Controller. Performs operation for Show Welcome page
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet("/WelcomeCtl")
public class WelcomeCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
       
	private static Logger log=Logger.getLogger("WelcomeCtl.class");

	/**
     * Contains Display logics
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	
		System.out.println("in welcome ctl");
		log.debug("WelcomeCtl method doGet started");
		ServletUtility.forward(ORSView.WELCOME_VIEW, request, response);
		log.debug("WelcomeCtl method doGet ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() 
	{
	
		return ORSView.WELCOME_VIEW;
	}

}
