package in.co.rays.ORSProj3.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.RoleDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of Role Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class RoleModelHibImpl implements RoleModelInt {

	private static Logger log = Logger.getLogger(RoleModelHibImpl.class);
	
    /**
     * Add a Role
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : throws when Role already exists
     */	
	public long add(RoleDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("RoleModelHibImpl method add started");
		
		Session session = HibDataSource.getSession();
		Transaction transaction = null;
		long pk = 0;
		
		RoleDTO duplicateRole = findByName(dto.getName());
		
		if (duplicateRole != null)
		{
			throw new DuplicateRecordException("Role already exist");
		}
		
		try 
		{
			transaction = session.beginTransaction();
			session.save(dto);
			pk=dto.getId();
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
			throw new ApplicationException("Exception in add Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
			
		log.debug("RoleModelHibImpl method add ended");
		return pk;
	}

    /**
     * Update a Role
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : if updated user record is already exist
     */
	public void update(RoleDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("RoleModelHibImpl method update started");
		
		Session session = null;
		Transaction transaction = null;
		
		RoleDTO duplicateRole = findByName(dto.getName());
		
		if(duplicateRole != null)
		{
			throw new DuplicateRecordException("Role already exist");
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
			
			if(transaction != null)
			{
				transaction.rollback();
			}
			throw new ApplicationException("Exception in update Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("RoleModelHibImpl method update ended");
	}

    /**
     * Delete a Role
     *
     * @param dto
     * @throws ApplicationException
     */
	public void delete(RoleDTO dto) throws ApplicationException {

		log.debug("RoleModelHibImpl method delete started");
		
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
			log.error("Database Exception..", e);
			
			if(transaction != null)
			{
				transaction.rollback();
			}
			throw new ApplicationException("Exception in delete Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		log.debug("RoleModelHibImpl method delete ended");
	}

    /**
     * Find Role by Name
     *
     * @param name
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public RoleDTO findByName(String name) throws ApplicationException {
		log.debug("RoleModelHibImpl method findByName started");
		
		Session session = null;
		RoleDTO dto = null;
		
		try
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(RoleDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();
			
			if (list.size() > 0)
			{
				dto = (RoleDTO) list.get(0);
			}
		}
		catch (HibernateException e)
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in findByName Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("RoleModelHibImpl method findByName ended");
		
		return dto;
	}

    /**
     * Find Role by PK
     *
     * @param pk
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public RoleDTO findByPK(long pk) throws ApplicationException {
		log.debug("RoleModelHibImpl method findByPK started");
		
		Session session = null;
		RoleDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(RoleDTO.class);
			criteria.add(Restrictions.eq("id", pk));
			List list = criteria.list();
			if(list.size() > 0)
			{
				dto = (RoleDTO) list.get(0);
			}
		}
		catch (HibernateException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in findByPK Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		log.debug("RoleModelHibImpl method findByPK ended");
		return dto;
	}

    /**
     * Search Role with pagination
     *
     * @return list : List of Role
     * @param dto
     *            : Search Parameters
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List search(RoleDTO dto, int pageNo, int pageSize) throws ApplicationException {
		log.debug("RoleModelHibImpl method search started");
		
		Session session = null;
		List list = null;
		
		try
		{
			session = HibDataSource.getSession();
			Criteria criteria  = session.createCriteria(RoleDTO.class);
			
			if(dto.getId() > 0)
			{
				criteria.add(Restrictions.eq("id", dto.getId()));
			}
			if(dto.getName() != null && dto.getName().length() > 0)
			{
				criteria.add(Restrictions.like("name", dto.getName() + "%"));
			}
			if(dto.getDescription() != null && dto.getDescription().length() > 0)
			{
				criteria.add(Restrictions.like("description", dto.getDescription() + "%"));
			}
			
			if(pageSize > 0)
			{
				criteria.setFirstResult(((pageNo - 1 ) * pageSize ));
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		}
		catch (Exception e) 
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in search Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		log.debug("RoleModelHibImpl method search ended");
		
		return list;
	}

    /**
     * Search Role
     *
     * @return list : List of Role
     * @param dto
     *            : Search Parameters
     * @throws ApplicationException
     */
	public List search(RoleDTO dto) throws ApplicationException {
		
		return search(dto, 0, 0);
	}

    /**
     * Gets List of Role
     *
     * @return list : List of Role
     * @throws DatabaseException
     */
	public List list() throws ApplicationException {
		return list(0, 0);
	}

    /**
     * get List of Role with pagination
     *
     * @return list : List of Role
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("RoleModelHibImpl method list started");
		
		Session session = null;
		List list = null;
		
		try 
		{
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(RoleDTO.class);
			
			if(pageSize > 0)
			{
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			
			list = criteria.list();
		}
		catch (Exception e) 
		{
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in list Role" + e.getMessage());
		}
		finally
		{
			session.close();
		}
		
		log.debug("RoleModelHibImpl method list ended");
		return list;
	}

}
