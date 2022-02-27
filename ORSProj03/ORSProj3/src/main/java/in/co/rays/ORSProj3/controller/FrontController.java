package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.ORSProj3.util.ServletUtility;

/**
 * Main Controller performs session checking and logging operations before
 * calling any application controller. It prevents any user to access
 * application without login.
 * 
 * 
 * @author Dilip Singh
 * @version 1.0 Copyright (c) SunilOS
 */
@WebFilter(urlPatterns = {"/ctl/*" , "/doc/*"})
public class FrontController implements Servlet, Filter {
	private static final long serialVersionUID = 1L;
	/**
	 * Perform filter operations
	 * 
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("user") == null)
		{
			request.setAttribute("message", "Your session has been expired please re-Login");
			
			String str = request.getRequestURI();
			session.setAttribute("URI", str);
			
			ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
			return;
			
		}
		else
		{
			chain.doFilter(req, resp);
		}
	}


	/**
	 * Life cycle method of filter
	 * 
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
	/**
	 * Destroy filter
	 * 
	 */
	public void destroy() {
	}


	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}


	public void init(ServletConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
