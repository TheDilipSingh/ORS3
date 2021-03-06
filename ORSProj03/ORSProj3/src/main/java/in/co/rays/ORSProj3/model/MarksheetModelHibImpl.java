package in.co.rays.ORSProj3.model;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.MarksheetDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of Marksheet Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class MarksheetModelHibImpl implements MarksheetModelInt{
	
	Logger log = Logger.getLogger(MarksheetModelHibImpl.class);

    /**
     * Adds a Marksheet
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : throws when Roll No already exists
     *
     */
	public long add(MarksheetDTO dto) throws ApplicationException, DuplicateRecordException {
		
		log.debug("MarksheetModelHibImpl method add started");
		
		long pk = 0;
		
		StudentModelInt sModel = ModelFactory.getInstance().getStudentModel();
		StudentDTO studentDTO = sModel.findByPK(dto.getStudentId());
		dto.setName(studentDTO.getFirstName() + " " + studentDTO.getLastName());

        MarksheetDTO duplicateMarksheet = findByRollNo(dto.getRollNo());

        if (duplicateMarksheet != null) 
        {
            throw new DuplicateRecordException("Roll Number already exists");
        }

        Session session = HibDataSource.getSession();
        Transaction transaction = null;
        try 
        {
            transaction = session.beginTransaction();
            pk = (Long) session.save(dto);
            transaction.commit();
        }
        catch (Exception e) 
        {
            log.error("Database Exception..", e);
            if (transaction != null)
            {
                transaction.rollback();
            }
            throw new ApplicationException("Exception in add Marksheet" + e.getMessage());
        } finally {
            session.close();
        }

        log.debug("MarksheetModelHibImpl method add ended");
        
        return pk;
	}

    /**
     * Deletes a Marksheet
     *
     * @param dto
     * @throws ApplicationException
     */
	public void delete(MarksheetDTO dto) throws ApplicationException 
	{
		log.debug("MarksheetModelHibImpl method delete started");
		
        MarksheetDTO dtoExist = findByPK(dto.getId());
       
        if (dtoExist == null) 
        {
            throw new ApplicationException("Marksheet does not exist");
        }

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
            
            if (transaction != null) 
            {
                transaction.rollback();
            }
            throw new ApplicationException("Exception in delete Marksheet" + e.getMessage());
        } 
        finally 
        {
            session.close();
        }

        log.debug("MarksheetModelHibImpl method delete ended");
		
	}

    /**
     * Finds Marksheet by Roll No
     *
     * @param rollNo
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public MarksheetDTO findByRollNo(String rollNo) throws ApplicationException
	{
		log.debug("MarksheetModelHibImpl method findByRollNo started");
		
        MarksheetDTO dto = null;
        Session session = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MarksheetDTO.class);
            criteria.add(Restrictions.eq("rollNo", rollNo));
            List list = criteria.list();
            if (list.size() == 1)
            {
                dto = (MarksheetDTO) list.get(0);
            } else
            {
                dto = null;
            }

        }
        catch (Exception e)
        {
        	e.printStackTrace();
            log.error("Database Exeption", e);
            throw new ApplicationException("Exception in getting Marksheet by Rollno " + e.getMessage());

        }
        finally
        {
            session.close();
        }
		
		log.debug("MarksheetModelHibImpl method findByRollNo ended");
		
		return dto;
	}

    /**
     * Finds Marksheet by PK
     *
     * @param pk
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public MarksheetDTO findByPK(long pk) throws ApplicationException
	{
		log.debug("MarksheetModelHibImpl method findByPK started");
	
		Session session = null;
		MarksheetDTO dto = null;
		
		try 
		{
			session = HibDataSource.getSession();
			dto = (MarksheetDTO) session.get(MarksheetDTO.class, pk);		
		}
		catch (Exception e) 
		{
			log.error("Database Exeption", e);
			
			throw new ApplicationException("Exception in getting Marksheet by pk" + e.getMessage());			
		}
		finally
		{
			session.close();
		}
		
		
		log.debug("MarksheetModelHibImpl method findByPK ended");
		return dto;
	}

    /**
     * Updates a Marksheet
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
	public void update(MarksheetDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("MarksheetModelHibImpl method update started");	
		
        Session session = null;
        Transaction transaction = null;

        MarksheetDTO dtoExist = findByRollNo(dto.getRollNo());

        // Check if updated Roll no already exist
        if (dtoExist != null && dtoExist.getId() != dto.getId())
        {
            throw new DuplicateRecordException("Roll No is already exist");
        }

        
        //get Student Name
        StudentModelInt sModel = ModelFactory.getInstance().getStudentModel();
        StudentDTO studentDTO = sModel.findByPK(dto.getStudentId());
        dto.setName(studentDTO.getFirstName() + " " + studentDTO.getLastName());

        try
        {

            session = HibDataSource.getSession();
            transaction = session.beginTransaction();
            session.update(dto);
            transaction.commit();
        }
        catch (Exception e)
        {
        	log.error("Database Exeption", e);
            if (transaction != null)
            {
                transaction.rollback();
            }
            throw new ApplicationException("Exception in Update Marksheet" + e.getMessage());
        }
        finally 
        {
            session.close();
        }
        
		log.debug("MarksheetModelHibImpl method update ended");
		
	}

	  /**
     * Searches Marksheet
     *
     * @param dto
     *            : Search Parameters
     * @throws ApplicationException
     */
	public List search(MarksheetDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

    /**
     * Searches Marksheet with pagination
     *
     * @return list : List of Marksheets
     * @param dto
     *            : Search Parameters
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     *
     * @throws ApplicationException
     */
	public List search(MarksheetDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		log.debug("MarksheetModelHibImpl method search started");
		
        Session session = null;
        List list = null;
        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MarksheetDTO.class);

            if (dto.getId() > 0)
            {
                criteria.add(Restrictions.eq("id", dto.getId()));
            }
            if (dto.getRollNo() != null && dto.getRollNo().length() > 0) 
            {
                criteria.add(Restrictions.eq("rollNo", dto.getRollNo()));
            }
            if (dto.getStudentId() > 0)
            {
                criteria.add(Restrictions.eq("studentId", dto.getStudentId()));
            }
            if (dto.getPhysics() != null && dto.getPhysics() > 0)
            {
                criteria.add(Restrictions.eq("physics", dto.getPhysics()));
            }
            if (dto.getChemistry() != null && dto.getChemistry() > 0)
            {
                criteria.add(Restrictions.eq("chemistry", dto.getChemistry()));
            }
            if (dto.getMaths() != null && dto.getMaths() > 0)
            {
                criteria.add(Restrictions.eq("maths", dto.getMaths()));
            }

            // if page size is greater than zero the apply pagination
            if (pageSize > 0) 
            {
                criteria.setFirstResult(((pageNo - 1) * pageSize));
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        }
        catch (Exception e) 
        {
        	log.error("Database Exeption", e);
        	e.printStackTrace();
            throw new ApplicationException("Exception in Marksheet Search " + e.getMessage());
        } 
        finally 
        {
            session.close();
        }
        
        log.debug("MarksheetModelHibImpl method search started");

		return list;
	}

	 /**
     * Gets List of Marksheet
     *
     * @return list : List of Marksheets
     * @throws DatabaseException
     */
	public List list() throws ApplicationException 
	{
		return list(0, 0);
	}

    /**
     * get List of Marksheet with pagination
     *
     * @return list : List of Marksheets
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		
		log.debug("MarksheetModelHibImpl method list started");
		
        Session session = null;
        List list = null;
        try 
        {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(MarksheetDTO.class);

            // if page size is greater than zero then apply pagination
            if (pageSize > 0)
            {
                pageNo = ((pageNo - 1) * pageSize);
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = criteria.list();
        }
        catch (Exception e) 
        {
        	log.error("Database Exeption", e);
            throw new ApplicationException("Exception in  Marksheet List" + e.getMessage());
        }
        finally
        {
            session.close();
        }

        log.debug("MarksheetModelHibImpl method list ended");
		return list;
	}

    /**
     * get Merit List of Marksheet with pagination
     *
     * @return list : List of Marksheets
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List getMeritList(int pageNo, int pageSize) throws ApplicationException {
		
		log.debug("MarksheetModelHibImpl method getMeritList started");
		
		Session session  = null;
		List list = null;
		try 
		{
			session = HibDataSource.getSession();
			
			StringBuffer hql = new StringBuffer("from MarksheetDTO  WHERE (PHYSICS>33 AND CHEMISTRY>33 AND MATHS>33) order by (physics + chemistry + maths) desc");
			
			Query query = session.createQuery(hql.toString());
			
			if(pageSize > 0)
			{
				query.setFirstResult(pageNo);
				query.setMaxResults(pageSize);
			}
			
			System.out.println(hql.toString());
			
			
			
			list = query.list();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			 log.error("Database Exception..", e);
	         throw new ApplicationException("Exception in  marksheet list" + e.getMessage());
	    }
		finally
		{
	            session.close();
	    }
		
		log.debug("MarksheetModelHibImpl method getMeritList ended");
		return list;
	}
	
}
