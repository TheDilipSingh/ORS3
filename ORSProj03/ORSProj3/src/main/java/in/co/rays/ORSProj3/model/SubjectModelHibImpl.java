package in.co.rays.ORSProj3.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of Subject Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class SubjectModelHibImpl implements SubjectModelInt {
	
	private static Logger log=Logger.getLogger(SubjectModelHibImpl.class);

	/**
	 * Add a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException 
	 * 
	 * 
	 */
	public long add(SubjectDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("SubjectModelHibImpl method add started");
		long pk=0;
		SubjectDTO duplicateSubject = findByName(dto.getSubjectName());
		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		CourseDTO course = model.findByPK(dto.getCourseId());
		dto.setCourseName(course.getName());
		System.out.println("Course Name"+" "+course.getName());
		if(duplicateSubject != null)
		{
			throw new DuplicateRecordException("Subject already exist");
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
		
		log.debug("SubjectModelHibImpl method add ended");
		
		return pk;
	}

	/**
	 * Delete a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 *
	 */
	public void delete(SubjectDTO dto) throws ApplicationException {
		
		log.debug("SubjectModelHibImpl method delete started");
		
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
			
			throw new ApplicationException("Exception in delete Subject"+ e.getMessage()); 
		}
		finally
		{
			session.close();
		}

		log.debug("SubjectModelHibImpl method delete ended");
	}

	/**
	 * Find Subject by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * 
	 */
	public SubjectDTO findByName(String name) throws ApplicationException {
		
		log.debug("SubjectModelHibImpl method findByName started");
		
		Session session = null;
		SubjectDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(SubjectDTO.class);
			criteria.add(Restrictions.eq("subjectName", name));
			List list = criteria.list();
			if (list.size() > 0)
			{
				dto = (SubjectDTO) list.get(0);
			}
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Subject by name" + e.getMessage());
			
		}
		finally 
		{
			session.close();
		}
		
		log.debug("SubjectModelHibImpl method findByName ended");
		
		return dto;
	}

	/**
	 * Find Subject by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public SubjectDTO findByPK(long pk) throws ApplicationException {
		
		log.debug("SubjectModelHibImpl method findByPK started");
		Session session = null;
		SubjectDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			dto = (SubjectDTO) session.get(SubjectDTO.class, pk);
		} 
		catch (HibernateException e) 
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Subject by PK" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("SubjectModelHibImpl method findByPK ended");
		
		return dto;
	}

	/**
	 * Update a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(SubjectDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("SubjectModelHibImpl method update started");
		
		Session session = null;
		Transaction transaction = null;
		
		SubjectDTO dtoExist = findByName(dto.getSubjectName());
		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		CourseDTO course = model.findByPK(dto.getCourseId());
		dto.setCourseName(course.getName());
		System.out.println("Course Name"+" "+course.getName());
		
		if (dtoExist != null && dtoExist.getId() != dto.getId())
		{
			throw new DuplicateRecordException("Subject already exist");
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
			throw new ApplicationException("Exception in updating Subject" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("SubjectModelHibImpl method update ended");
		
	}

	/**
	 * Search Subject
	 * 
	 * @param dto
	 *            : Search Parameters
	 * @throws ApplicationException 
	 * 
	 */
	public List search(SubjectDTO dto) throws ApplicationException {
		
		return search(dto, 0, 0);
	}

	/**
	 * Search Subject with pagination
	 * 
	 * @return list : List of Users
	 * @param dto
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws ApplicationException 
	 * 
	 * 
	 */
	public List search(SubjectDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("SubjectModelHibImpl method search started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(SubjectDTO.class);
			
			if(dto!=null)
			{
				if(dto.getId()>0)
				{
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getSubjectName() != null && dto.getSubjectName().length()>0)
				{
					criteria.add(Restrictions.like("name", dto.getSubjectName() +"%"));
				}
				if(dto.getDescription()!=null && dto.getSubjectName().length()>0)
				{
					criteria.add(Restrictions.like("description", dto.getDescription() +"%"));
				}
				if(dto.getCourseId() > 0)
				{
					criteria.add(Restrictions.eq("courseId", dto.getCourseId()));
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
			throw new ApplicationException("Exception in searching Subject" + e.getMessage());
		}
		finally
		{
			session.close();
		}

		log.debug("SubjectModelHibImpl method search ended");
		
		return list;
	}

	/**
	 * Get List of Subject
	 * 
	 * @return list : List of Subject
	 * @throws ApplicationException 
	 * 
	 */
	public List list() throws ApplicationException {
	
		return list(0,0);
	}

	/**
	 * Get List of Subject with pagination
	 * 
	 * @return list : List of Subject
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws ApplicationException 
	 * 
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("SubjectModelHibImpl method list started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(SubjectDTO.class);
			
			if (pageSize > 0)
			{
				pageNo = ((pageNo - 1) * pageSize) + 1;
				
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);							
			}
			
			list = criteria.list();			
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in Subject List" + e.getMessage());
		}
		finally 
		{
			session.close();
		}

		log.debug("SubjectModelHibImpl method list ended");
		return list;
	}


}
