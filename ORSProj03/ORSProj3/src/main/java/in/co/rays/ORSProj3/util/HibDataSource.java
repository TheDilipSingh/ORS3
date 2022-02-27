package in.co.rays.ORSProj3.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Hib DataSource is a factory of Hibernate session
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */
public class HibDataSource {
	
	private static SessionFactory sessionFactory = null;
	
	/**
	 * Create instance of Session Factory
	 * @return
	 */
	public static SessionFactory getSessionFactory()
	{
		if(sessionFactory == null)
		{
			sessionFactory = new Configuration().configure().buildSessionFactory();			
		}
		
		return sessionFactory;
	}

	/**
	 * Create instance of Session
	 * @return
	 */
	public static Session getSession()
	{
		Session session=getSessionFactory().openSession();
		return session;
	}
	
	/**
	 * Close the Session
	 * @param session
	 */
	public static void closeSession(Session session)
	{
		if(session != null)
		{
			session.close();
		}
	}
	
}
