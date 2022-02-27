package in.co.rays.ORSProj3.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.FacultyDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of FacultyModel
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class FacultyModelHibImpl implements FacultyModelInt {

	private static Logger log = Logger.getLogger(FacultyModelHibImpl.class);
	
	/**
	 * Add a Faculty
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 *
	 * 
	 */
	public long add(FacultyDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("FacultyModelHibImpl method add started");
		long pk=0;
		FacultyDTO duplicateStudent = findByEmail(dto.getLoginId());
		
		if(duplicateStudent != null)
		{
			throw new DuplicateRecordException("EmailId already exist");
		}
		
		CollegeModelInt cmodel = ModelFactory.getInstance().getCollegeModel();
		CollegeDTO cdto = cmodel.findByPK(dto.getCollegeId());
		dto.setCollegeName(cdto.getName());

		SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO sdto = smodel.findByPK(dto.getSubjectId());
		dto.setSubjectName(sdto.getSubjectName());
		    
		CourseModelInt coumodel = ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto =    coumodel.findByPK(dto.getCourseId());
		dto.setCourseName(coudto.getName());
		
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
			throw new ApplicationException("Exception : Exception in add Faculty" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("FacultyModelHibImpl method add ended");
		
		return pk;
	}

	/**
	 * Delete a Faculty
	 * 
	 * @param dto
	 * 
	 */
	public void delete(FacultyDTO dto) throws ApplicationException {
		
		log.debug("FacultyModelHibImpl method delete started");
		
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
			
			throw new ApplicationException("Exception in delete Faculty"+ e.getMessage()); 
		}
		finally
		{
			session.close();
		}

		log.debug("FacultyModelHibImpl method delete ended");
	}

	/**
	 * Find Faculty by Email
	 * 
	 * @param EmailId
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public FacultyDTO findByEmail(String emailId) throws ApplicationException {
		
		log.debug("FacultyModelHibImpl method findByEmailId started");
		
		Session session = null;
		FacultyDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(FacultyDTO.class);
			criteria.add(Restrictions.eq("loginId", emailId));
			List list = criteria.list();
			if (list.size() > 0)
			{
				dto = (FacultyDTO) list.get(0);
			}
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Faculty by email" + e.getMessage());
			
		}
		finally 
		{
			session.close();
		}
		
		log.debug("FacultyModelHibImpl method findByEmailId ended");
		
		return dto;
	}

	/**
	 * Find Faculty by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public FacultyDTO findByPK(long pk) throws ApplicationException {
		
		log.debug("FacultyModelHibImpl method findByPK started");
		Session session = null;
		FacultyDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			dto = (FacultyDTO) session.get(FacultyDTO.class, pk);
		} 
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Faculty by PK" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("FacultyModelHibImpl method findByPK ended");
		
		return dto;
	}

	/**
	 * Update a Faculty
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(FacultyDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("FacultyModelHibImpl method update started");
		
		CollegeModelInt cmodel = ModelFactory.getInstance().getCollegeModel();
		CollegeDTO cdto = cmodel.findByPK(dto.getCollegeId());
		dto.setCollegeName(cdto.getName());

		SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO sdto = smodel.findByPK(dto.getSubjectId());
		dto.setSubjectName(sdto.getSubjectName());
		    
		CourseModelInt coumodel = ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto =    coumodel.findByPK(dto.getCourseId());
		dto.setCourseName(coudto.getName());
		
		Session session = null;
		Transaction transaction = null;
		
		FacultyDTO dtoExist = findByEmail(dto.getLoginId());
		
		if (dtoExist != null && dtoExist.getId() != dto.getId())
		{
			throw new DuplicateRecordException("EmailId already exist");
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
			e.printStackTrace();
			
			if (transaction != null)
			{
				transaction.rollback();
			}
			throw new ApplicationException("Exception in updating Faculty" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("FacultyModelHibImpl method update ended");
		
	}

	/**
	 * Search Faculty
	 * 
	 * @param dto
	 *            : Search Parameters
	 * 
	 */
	public List search(FacultyDTO dto) throws ApplicationException {
		
		return search(dto, 0, 0);
	}

	/**
	 * Search Faculty with pagination
	 * 
	 * @return list : List of Users
	 * @param dto
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * 
	 */
	public List search(FacultyDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("FacultyModelHibImpl method search started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(FacultyDTO.class);
			
            if (dto!=null) 
    		{
    			if (dto.getId()>0) 
    			{
    				criteria.add(Restrictions.eq("id", dto.getId()));
    			}
    			if (dto.getFirstName()!=null && dto.getFirstName().length()>0) 
    			{
    				criteria.add(Restrictions.like("firstName", dto.getFirstName() + "%"));
    			}
    			if (dto.getLastName()!=null && dto.getLastName().length()>0) 
    			{
    				criteria.add(Restrictions.like("lastName", dto.getLastName() + "%"));
    			}
    			if (dto.getLoginId()!=null && dto.getLoginId().length()>0) 
    			{
    				criteria.add(Restrictions.like("loginId", dto.getLoginId() + "%"));
    			}
    			if (dto.getCollegeId() > 0)
    			{
    				criteria.add(Restrictions.eq("collegeId", dto.getCollegeId()));
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
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in search Faculty" + e.getMessage());
		}
		finally
		{
			session.close();
		}

		log.debug("FacultyModelHibImpl method search ended");
		
		return list;
	}

	/**
	 * Get List of Faculty
	 * 
	 * @return list : List of Faculty
	 * 
	 */
	public List list() throws ApplicationException {
		
		return list(0,0);
	}

	/**
	 * Get List of Faculty with pagination
	 * 
	 * @return list : List of Faculty
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 *
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("FacultyModelHibImpl method list started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(FacultyDTO.class);
			
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
			throw new ApplicationException("Exception in Facultys List");
		}
		finally 
		{
			session.close();
		}

		log.debug("FacultyModelHibImpl method list ended");
		return list;
	}

	
}
