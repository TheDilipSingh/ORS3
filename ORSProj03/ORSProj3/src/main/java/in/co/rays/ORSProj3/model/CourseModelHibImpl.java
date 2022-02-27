package in.co.rays.ORSProj3.model;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of CourseModel
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class CourseModelHibImpl implements CourseModelInt {

	private static Logger log=Logger.getLogger(CourseModelHibImpl.class);
	
	/**
	 * Add a Course
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(CourseDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("CourseModelHibImpl method add started");
		long pk=0;
		CourseDTO duplicateCourse = findByName(dto.getName());
		
		if(duplicateCourse != null)
		{
			throw new DuplicateRecordException("Course already exist");
		}
		
		Session session = HibDataSource.getSession();
		Transaction transaction = null;
		
		try 
		{
			transaction = session.beginTransaction();
			session.save(dto);
			pk = dto.getId();
			transaction.commit();
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			
			if(transaction != null)
			{
				transaction.rollback();
			}
			throw new ApplicationException("Exception : Exception in add Course" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("CourseModelHibImpl method add ended");
		
		return pk;
	}

	/**
	 * Delete a Course
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(CourseDTO dto) throws ApplicationException {
		
		log.debug("CourseModelHibImpl method delete started");
		
		Session session = null;
		Transaction transaction = null;
		
		try 
		{
			session = HibDataSource.getSession();
			transaction = session.beginTransaction();
			
			session.delete(dto);
			
			transaction.commit();
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			
			if(transaction != null)
			{
				transaction.rollback();
			}
			
			throw new ApplicationException("Exception in delete Courses"+ e.getMessage()); 
		}
		finally
		{
			session.close();
		}

		log.debug("CourseModelHibImpl method delete ended");
	}

	/**
	 * Find a Course by Name
	 * @param name
	 * @return
	 * @throws ApplicationException
	 */
	public CourseDTO findByName(String name) throws ApplicationException {
		
		log.debug("CourseModelHibImpl method findByName started");
		
		Session session = null;
		CourseDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CourseDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();
			if (list.size() > 0)
			{
				dto = (CourseDTO) list.get(0);
			}
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting course by name" + e.getMessage());
			
		}
		finally 
		{
			session.close();
		}
		
		log.debug("CourseModelHibImpl method findByName ended");
		
		return dto;
	}

	/**
	 * Find a Course by PK
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public CourseDTO findByPK(long pk) throws ApplicationException {
		
		log.debug("CourseModelHibImpl method findByPK started");
		Session session = null;
		CourseDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			dto = (CourseDTO) session.get(CourseDTO.class, pk);
		} 
		catch (HibernateException e) 
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Course by PK" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("CourseModelHibImpl method findByPK ended");
		
		return dto;
	}

	/**
	 * Uodate a Course
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(CourseDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("CourseModelHibImpl method update started");
		
		Session session = null;
		Transaction transaction = null;
		
		CourseDTO dtoExist = findByName(dto.getName());
		
		if (dtoExist != null && dtoExist.getId() != dto.getId())
		{
			throw new DuplicateRecordException("Course already exist");
		}
		
		try 
		{
			session = HibDataSource.getSession();
			transaction = session.beginTransaction();
			session.update(dto);
			transaction.commit();
		}
		catch (HibernateException e) 
		{
			log.error("Database Exception..", e);
			
			if (transaction != null)
			{
				transaction.rollback();
			}
			throw new ApplicationException("Exception in updating Course" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("CourseModelHibImpl method update ended");
		
	}

	/**
	 * Search Course
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CourseDTO dto) throws ApplicationException {
		
		return search(dto, 0, 0);
	}

	/**
	 * Search Course with Pagination
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CourseDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("CourseModelHibImpl method search started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CourseDTO.class);
			
			if(dto!=null)
			{
				if(dto.getId()>0)
				{
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getName()!= null && dto.getName().length()>0)
				{
					criteria.add(Restrictions.like("name", dto.getName() +"%"));
				}
				if(dto.getDescription()!=null && dto.getName().length()>0)
				{
					criteria.add(Restrictions.like("description", dto.getDescription() +"%"));
				}
				if(dto.getDuration()!=null && dto.getDuration().length() >0)
				{
					criteria.add(Restrictions.like("duration", dto.getDuration() +"%"));
				}
			}
            
            if(pageSize > 0)
            {
            	criteria.setFirstResult((pageNo - 1)* pageSize);
            	criteria.setMaxResults(pageSize);
            }
            
            list = criteria.list();
			
		}
		catch (HibernateException e) 
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in searching Course" + e.getMessage());
		}
		finally
		{
			session.close();
		}

		log.debug("CourseModelHibImpl method search ended");
		
		return list;
	}

	/**
	 * Get List of Course
	 * @return
	 * @throws ApplicationException
	 */
	public List list() throws ApplicationException {
	
		return list(0,0);
	}

	/**
	 * Get List of Course with Pagination
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("CourseModelHibImpl method list started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CourseDTO.class);
			
			if (pageSize > 0)
			{
				pageNo = ((pageNo - 1) * pageSize);
				
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);							
			}
			
			list = criteria.list();			
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in Course List" + e.getMessage());
		}
		finally 
		{
			session.close();
		}

		log.debug("CollegeModelHibImpl method list ended");
		return list;
	}

	
	
}
