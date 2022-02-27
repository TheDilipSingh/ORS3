package in.co.rays.ORSProj3.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.internal.SessionImpl;

import in.co.rays.ORSProj3.util.HibDataSource;
import in.co.rays.ORSProj3.util.JDBCDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 * Jasper Controller is used to add reporting capability in Application
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
@WebServlet(name = "JasperCtl", urlPatterns = "/ctl/JasperCtl")
public class JasperCtl extends BaseCtl {

	/**
     * Concept of Submit Logic
     * 
     */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		doGet(req, resp);
	}

	/**
     * Concept of Display Logic
     * 
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("====================>>>>>>>>>>>>>>>>>>...sinside jasper ctl");

		Connection conn = null;
		JasperReport jr = null;
		JasperDesign jd = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.ORSProj3.bundle.System");
			String database = rb.getString("DATABASE");
			if ("HIBERNATE".equalsIgnoreCase(database)) {
				
				Session session = HibDataSource.getSession();
				SessionImpl sessionImpl = (SessionImpl) session;
				
				try {
					conn = (Connection) sessionImpl.connection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if ("JDBC".equalsIgnoreCase(database)) {
				conn = JDBCDataSource.getConnection();
			}
			Map map = new HashMap();
			String path = getServletContext().getRealPath("/WEB-INF/");
			jd = JRXmlLoader.load(path + "/Jasper.jrxml");
			jr = JasperCompileManager.compileReport(jd);

			byte[] byteStream = JasperRunManager.runReportToPdf(jr, map, conn);
			java.io.OutputStream os = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setContentLength(byteStream.length);
			os.write(byteStream, 0, byteStream.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	protected String getView() {

		return ORSView.JASPER_CTL;
	}

}
