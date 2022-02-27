package in.co.rays.ORSProj3.model;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.TimeTableDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of Time Table Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class TimeTableModelHibImpl implements TimeTableModelInt {

	Logger log = Logger.getLogger(TimeTableModelHibImpl.class);

	/**
	 * Add a TIMETABLE
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * 
	 * 
	 */
	public Long add(TimeTableDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("TimeTableModelHibImpl method add started");

		long pk = 0;

		SubjectModelInt sModel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO subjectDTO = sModel.findByPK(dto.getSubjectId());
		dto.setSubjectName(subjectDTO.getSubjectName());

		CourseModelInt cModel = ModelFactory.getInstance().getCourseModel();
		CourseDTO courseDTO = cModel.findByPK(dto.getCourseId());
		dto.setCourseName(courseDTO.getName());

		TimeTableDTO dto11 = checkBycds(dto.getCourseId(), dto.getSemester(),
				new java.sql.Date(dto.getExamDate().getTime()));
		TimeTableDTO dto12 = checkBycss(dto.getCourseId(), dto.getSubjectId(), dto.getSemester());
		if (dto11 != null || dto12 != null) {
			throw new DuplicateRecordException("TimeTable Already Exsist");
		}

		Session session = HibDataSource.getSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			pk = (Long) session.save(dto);
			transaction.commit();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw new ApplicationException("Exception in add TimeTable" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method add ended");

		return pk;
	}

	/**
	 * Delete a TimeTable
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * 
	 */
	public void delete(TimeTableDTO dto) throws ApplicationException {
		log.debug("TimeTableModelHibImpl method delete started");

		TimeTableDTO dtoExist = findByPk(dto.getId());

		if (dtoExist == null) {
			throw new ApplicationException("TimeTable does not exist");
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibDataSource.getSession();
			transaction = session.beginTransaction();
			session.delete(dto);
			transaction.commit();
		} catch (HibernateException e) {
			log.error("Database Exception..", e);

			if (transaction != null) {
				transaction.rollback();
			}
			throw new ApplicationException("Exception in delete TimeTable" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method delete ended");

	}

	/**
	 * Find TimeTable by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * 
	 */

	public TimeTableDTO findByName(String name) throws ApplicationException {
		log.debug("TimeTableModelHibImpl method findByName started");

		TimeTableDTO dto = null;
		Session session = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TimeTableDTO.class);
			criteria.add(Restrictions.eq("subject_name", name));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (TimeTableDTO) list.get(0);
			}
		} catch (Exception e) {
			log.error("Database Exeption", e);
			throw new ApplicationException("Exception in getting TimeTable by Subject_Name " + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method findByName ended");

		return dto;
	}

	/**
	 * Find TimeTable by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public TimeTableDTO findByPk(long pk) throws ApplicationException {
		log.debug("TimeTableModelHibImpl method findByPK started");

		Session session = null;
		TimeTableDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (TimeTableDTO) session.get(TimeTableDTO.class, pk);
		} catch (Exception e) {
			log.error("Database Exeption", e);

			throw new ApplicationException("Exception in getting TimeTable by pk" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method findByPK ended");
		return dto;
	}

	/**
	 * Update a TIMETABLE
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * 
	 */
	public void update(TimeTableDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("TimeTableModelHibImpl method update started");

		Session session = null;
		Transaction transaction = null;

		TimeTableDTO dto11 = checkBycds(dto.getCourseId(), dto.getSemester(),
				new java.sql.Date(dto.getExamDate().getTime()));
		TimeTableDTO dto12 = checkBycss(dto.getCourseId(), dto.getSubjectId(), dto.getSemester());
		if (dto11 != null || dto12 != null) 
		{
			throw new DuplicateRecordException("TimeTable Already Exsist");
		}

		SubjectModelInt sModel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO subjectDTO = sModel.findByPK(dto.getSubjectId());
		dto.setSubjectName(subjectDTO.getSubjectName());

		CourseModelInt cModel = ModelFactory.getInstance().getCourseModel();
		CourseDTO courseDTO = cModel.findByPK(dto.getCourseId());
		dto.setCourseName(courseDTO.getName());

		try {

			session = HibDataSource.getSession();
			transaction = session.beginTransaction();
			session.update(dto);
			transaction.commit();
		} catch (Exception e) {
			log.error("Database Exeption", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw new ApplicationException("Exception in Update TimeTable" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method update ended");

	}

	/**
	 * Search TimeTable
	 * 
	 * @param dto
	 *            : Search Parameters
	 * @throws ApplicationException
	 * 
	 */
	public List search(TimeTableDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

	/**
	 * Search TimeTable with pagination
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
	public List search(TimeTableDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("TimeTableModelHibImpl method search started");

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TimeTableDTO.class);

			if (dto != null)
			{
				if(dto.getCourseId() > 0)
				{
					criteria.add(Restrictions.eq("courseId", dto.getCourseId()));
				}
				if(dto.getSubjectId() > 0)
				{
					criteria.add(Restrictions.eq("subjectId", dto.getSubjectId())); 
				}
				if (dto.getExamDate() !=null && dto.getExamDate().getTime() > 0 )
				{				
					Date d = new Date(dto.getExamDate().getTime());
					criteria.add(Restrictions.eq("examDate", d));
				}				
			}	
			
			// if page size is greater than zero the apply pagination
			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();
		} catch (Exception e) {
			log.error("Database Exeption", e);
			e.printStackTrace();
			throw new ApplicationException("Exception in TimeTable Search " + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method search started");

		return list;
	}

	/**
	 * Get List of TimeTable
	 * 
	 * @return list : List of TimeTable
	 * @throws ApplicationException
	 * 
	 */
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of TimeTable with pagination
	 * 
	 * @return list : List of TimeTable
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws ApplicationException
	 * 
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("TimeTableModelHibImpl method list started");

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TimeTableDTO.class);

			// if page size is greater than zero then apply pagination
			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();
		} catch (Exception e) {
			log.error("Database Exeption", e);
			throw new ApplicationException("Exception in  TimeTable List" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method list ended");
		return list;
	}

	/**
	 * Search TimeTable
	 * 
	 * @return dto
	 * 
	 * @param CourseId
	 *            
	 * @param SubjectId
	 *            
	 * @param semester
	 *          
	 * @throws ApplicationException
	 * 
	 * 
	 */
	public TimeTableDTO checkBycss(long CourseId, long SubjectId, String semester) throws ApplicationException {
		log.debug("TimeTableModelHibImpl method checkBycss started");

		TimeTableDTO dto = null;
		Session session = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TimeTableDTO.class);
			criteria.add(Restrictions.eq("courseId", CourseId));
			criteria.add(Restrictions.eq("subjectId", SubjectId));
			criteria.add(Restrictions.eq("semester", semester));
		
			List list = criteria.list();
			
			if (list.size() == 1) 
			{
				dto = (TimeTableDTO) list.get(0);
			}
		} catch (Exception e) {
			log.error("Database Exeption", e);
			throw new ApplicationException("Exception in checkBycss TimeTable" + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method checkBycss ended");

		return dto;
	}

	/**
	 * Search TimeTable
	 * 
	 * @return dto
	 * 
	 * @param CourseId
	 *            
	 * @param ExamDate
	 *            
	 * @param semester
	 *          
	 * @throws ApplicationException
	 * 
	 * 
	 */
	public TimeTableDTO checkBycds(long CourseId, String Semester, Date ExamDate) throws ApplicationException {
		log.debug("TimeTableModelHibImpl method checkBycds started");

		TimeTableDTO dto = null;
		Session session = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TimeTableDTO.class);
			criteria.add(Restrictions.eq("courseId", CourseId));
			criteria.add(Restrictions.eq("examDate", ExamDate));
			criteria.add(Restrictions.eq("semester", Semester));
		
			List list = criteria.list();
			
			if (list.size() == 1) 
			{
				dto = (TimeTableDTO) list.get(0);
			}
		} catch (Exception e) {
			log.error("Database Exeption", e);
			throw new ApplicationException("Exception in checkBycds TimeTable" + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("TimeTableModelHibImpl method checkBycds ended");

		return dto;
	}

}
