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
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of CollegeModel
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class CollegeModelHibImpl implements CollegeModelInt {
	
	private static Logger log=Logger.getLogger(CollegeModelHibImpl.class);

	/**
	 * Add a College
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(CollegeDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("CollegeModelHibImpl method add started");
		long pk=0;
		CollegeDTO duplicateCollegeName = findByName(dto.getName());
		
		if(duplicateCollegeName != null)
		{
			throw new DuplicateRecordException("College name already exist");
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
			throw new ApplicationException("Exception : Exception in add College" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("CollegeModelHibImpl method add ended");
		
		return pk;
	}

	/**
	 * Delete a College
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(CollegeDTO dto) throws ApplicationException {
		
		log.debug("CollegeModelHibImpl method delete started");
		
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
			
			throw new ApplicationException("Exception in delete College"+ e.getMessage()); 
		}
		finally
		{
			session.close();
		}

		log.debug("CollegeModelHibImpl method delete ended");
	}

	/**
	 * Find College by Name
	 * @param name
	 * @return
	 * @throws ApplicationException
	 */
	public CollegeDTO findByName(String name) throws ApplicationException {
		
		log.debug("CollegeModelHibImpl method findByName started");
		
		Session session = null;
		CollegeDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CollegeDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();
			if (list.size() > 0)
			{
				dto = (CollegeDTO) list.get(0);
			}
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting College by name" + e.getMessage());
			
		}
		finally 
		{
			session.close();
		}
		
		log.debug("CollegeModelHibImpl method findByName ended");
		
		return dto;
	}

	/**
	 * Find College by PK
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public CollegeDTO findByPK(long pk) throws ApplicationException {
		
		log.debug("CollegeModelHibImpl method findByPK started");
		Session session = null;
		CollegeDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			dto = (CollegeDTO) session.get(CollegeDTO.class, pk);
		} 
		catch (HibernateException e) 
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting college by PK" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("CollegeModelHibImpl method findByPK ended");
		
		return dto;
	}

	/**
	 * Update a College
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(CollegeDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("CollegeModelHibImpl method update started");
		
		Session session = null;
		Transaction transaction = null;
		
		CollegeDTO dtoExist = findByName(dto.getName());
		
		if (dtoExist != null && dtoExist.getId() != dto.getId())
		{
			throw new DuplicateRecordException("College is already exist");
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
			throw new ApplicationException("Exception in updating college" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("CollegeModelHibImpl method update ended");
		
	}

	/**
	 * Search College
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CollegeDTO dto) throws ApplicationException {

		log.debug("CollegeModelHibImpl method search started");
		
		
		log.debug("CollegeModelHibImpl method search ended");
		
		
		return search(dto, 0, 0);
	}

	/**
	 * Search College with Pagination
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CollegeDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("CollegeModelHibImpl method search started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CollegeDTO.class);
			
			if (dto.getId() > 0)
			{
				criteria.add(Restrictions.eq("id", dto.getId()));
			}			
			if (dto.getName() != null && dto.getName().length() > 0)
			{
				criteria.add(Restrictions.like("name", dto.getName() + "%"));
			}
			if (dto.getAddress() != null && dto.getAddress().length() > 0) 
			{
                criteria.add(Restrictions.like("address", dto.getAddress() + "%"));
            }
            if (dto.getState() != null && dto.getState().length() > 0) 
            {
                criteria.add(Restrictions.like("state", dto.getState() + "%"));
            }
            if (dto.getCity() != null && dto.getCity().length() > 0) 
            {
                criteria.add(Restrictions.like("city", dto.getCity() + "%"));
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
			throw new ApplicationException("Exception in searching College" + e.getMessage());
		}
		finally
		{
			session.close();
		}

		log.debug("CollegeModelHibImpl method search ended");
		
		return list;
	}

	/**
	 * Get List of College
	 * @return
	 * @throws ApplicationException
	 */
	public List list() throws ApplicationException {

		log.debug("CollegeModelHibImpl method list started");
		
		log.debug("CollegeModelHibImpl method list ended");
		
		return list(0,0);
	}

	/**
	 * Get List of College with Pagination
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("CollegeModelHibImpl method list started");
		
		Session session = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CollegeDTO.class);
			
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
			throw new ApplicationException("Exception in College List" + e.getMessage());
		}
		finally 
		{
			session.close();
		}

		log.debug("CollegeModelHibImpl method list ended");
		return list;
	}
	public static void main(String[] args) throws ApplicationException, DuplicateRecordException {
		
		CollegeModelHibImpl studentModel = new CollegeModelHibImpl();
		List list = studentModel.list();
		Iterator iterator = list.iterator();
		CollegeDTO dto = null;
		while(iterator.hasNext())
			{
					dto = (CollegeDTO) iterator.next();
					
					System.out.println(dto.getName()+ " " +dto.getPhoneNo());
					
			}
	/*	CollegeDTO dto = new CollegeDTO();
		
		dto.setName("VPS");
		dto.setAddress("sfas");
		dto.setCity("indore");
		dto.setPhoneNo("98654");
		dto.setState("m.p");
		dto.setModifiedBy("me");
		dto.setCreatedBy(null);
		dto.setCreatedDateTime(null);
		dto.setModifiedDateTime(null);
		studentModel.add(dto);*/
		
	}
	
}
