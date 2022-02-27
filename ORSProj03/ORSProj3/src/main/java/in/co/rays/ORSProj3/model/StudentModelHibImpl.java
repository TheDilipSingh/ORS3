package in.co.rays.ORSProj3.model;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of Student Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class StudentModelHibImpl implements StudentModelInt {
	
	private static Logger log = Logger.getLogger(StudentModelHibImpl.class);
	
    /**
     * Add a Student
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : throws when Student already exists
     */
	public long add(StudentDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("StudentModelHibImpl method add started");
		long pk=0;
		StudentDTO duplicateStudent = findByEmailId(dto.getEmail());
		
		if(duplicateStudent != null)
		{
			throw new DuplicateRecordException("EmailId already exist");
		}
		
    	CollegeModelInt cModel = ModelFactory.getInstance().getCollegeModel();
    	CollegeDTO collegeDTO = cModel.findByPK(dto.getCollegeId());
    	dto.setCollegeName(collegeDTO.getName());
		
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
			throw new ApplicationException("Exception : Exception in add Student" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("StudentModelHibImpl method add ended");
		
		return pk;
	}

    /**
     * Delete a Student
     *
     * @param dto
     * @throws ApplicationException
     */
	public void delete(StudentDTO dto) throws ApplicationException {
		
		log.debug("StudentModelHibImpl method delete started");
		
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
			
			throw new ApplicationException("Exception in delete Student"+ e.getMessage()); 
		}
		finally
		{
			session.close();
		}

		log.debug("StudentModelHibImpl method delete ended");
	}

    /**
     * Find Student by EmailId
     *
     * @param name
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public StudentDTO findByEmailId(String emailId) throws ApplicationException {
		
		log.debug("StudentModelHibImpl method findByEmailId started");
		
		Session session = null;
		StudentDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StudentDTO.class);
			criteria.add(Restrictions.eq("email", emailId));
			List list = criteria.list();
			if (list.size() > 0)
			{
				dto = (StudentDTO) list.get(0);
			}
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Student by email" + e.getMessage());
			
		}
		finally 
		{
			session.close();
		}
		
		log.debug("StudentModelHibImpl method findByEmailId ended");
		
		return dto;
	}

    /**
     * Find Student by PK
     *
     * @param pk
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public StudentDTO findByPK(long pk) throws ApplicationException {
		
		log.debug("StudentModelHibImpl method findByPK started");
		Session session = null;
		StudentDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			dto = (StudentDTO) session.get(StudentDTO.class, pk);
		} 
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting Student by PK" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("StudentModelHibImpl method findByPK ended");
		
		return dto;
	}

    /**
     * Update a Student
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : if updated user record is already exist
     */
	public void update(StudentDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("StudentModelHibImpl method update started");
		
    	CollegeModelInt cModel = ModelFactory.getInstance().getCollegeModel();
    	CollegeDTO collegeDTO = cModel.findByPK(dto.getCollegeId());
    	dto.setCollegeName(collegeDTO.getName());
		
		Session session = null;
		Transaction transaction = null;
		
		StudentDTO dtoExist = findByEmailId(dto.getEmail());
		
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
			throw new ApplicationException("Exception in updating Student" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("StudentModelHibImpl method update ended");
		
	}

    /**
     * Search Student
     *
     * @return list : List of Student
     * @param dto
     *            : Search Parameters
     * @throws ApplicationException
     */
	public List search(StudentDTO dto) throws ApplicationException {

		log.debug("StudentModelHibImpl method search started");
		
		
		log.debug("StudentModelHibImpl method search ended");
		
		
		return search(dto, 0, 0);
	}


    /**
     * Search Student with pagination
     *
     * @return list : List of Student
     * @param dto
     *            : Search Parameters
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List search(StudentDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("StudentModelHibImpl method search started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StudentDTO.class);
			
            if (dto.getId() > 0) 
            {
                criteria.add(Restrictions.eq("id", dto.getId()));
            }
            if (dto.getCollegeId() > 0) 
            {
                criteria.add(Restrictions.eq("collegeId", dto.getCollegeId()));
            }
            if (dto.getFirstName() != null && dto.getFirstName().length() > 0) 
            {
                criteria.add(Restrictions.like("firstName", dto.getFirstName() + "%"));
            }
            if (dto.getLastName() != null && dto.getLastName().length() > 0) 
            {
                criteria.add(Restrictions.like("lastName", dto.getLastName() + "%"));
            }
            if (dto.getDob() != null && dto.getDob().getDate() > 0) 
            {
                criteria.add(Restrictions.eq("dob", dto.getDob()));
            }
            if (dto.getEmail() != null && dto.getEmail().length() > 0) 
            {
                criteria.add(Restrictions.like("email", dto.getEmail() + "%"));
            }
            if (dto.getMobileNo() != null && dto.getMobileNo().length() > 0)
            {
                criteria.add(Restrictions.like("mobileNo", dto.getMobileNo() + "%"));
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
			throw new ApplicationException("Exception in searching Student" + e.getMessage());
		}
		finally
		{
			session.close();
		}

		log.debug("StudentModelHibImpl method search ended");
		
		return list;
	}

    /**
     * Gets List of College
     *
     * @return list : List of College
     * @throws DatabaseException
     */
	public List list() throws ApplicationException {

		log.debug("StudentModelHibImpl method list started");
		
		log.debug("StudentModelHibImpl method list ended");
		
		return list(0,0);
	}

    /**
     * get List of College with pagination
     *
     * @return list : List of College
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("StudentModelHibImpl method list started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StudentDTO.class);
			
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
			throw new ApplicationException("Exception in Student List");
		}
		finally 
		{
			session.close();
		}

		log.debug("StudentModelHibImpl method list ended");
		return list;
	}
	
}
