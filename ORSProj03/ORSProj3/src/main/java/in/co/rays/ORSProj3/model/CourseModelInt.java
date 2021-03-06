package in.co.rays.ORSProj3.model;

import java.util.List;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;


/**
 * Data Access Object of Course
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public interface CourseModelInt {

	/**
	 * Add a Course
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(CourseDTO dto) throws ApplicationException, DuplicateRecordException;

	/**
	 * Delete a Course
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(CourseDTO dto) throws ApplicationException; 

	
	/**
	 * Uodate a Course
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(CourseDTO dto) throws ApplicationException, DuplicateRecordException;
	
	/**
	 * Find a Course by Name
	 * @param name
	 * @return
	 * @throws ApplicationException
	 */
	public CourseDTO findByName(String name) throws ApplicationException;
	
	
	/**
	 * Find a Course by PK
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public CourseDTO findByPK(long pk) throws ApplicationException;
	
	/**
	 * Search Course
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 */
	public List search (CourseDTO dto) throws ApplicationException;
	
	
	/**
	 * Search Course with Pagination
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CourseDTO dto, int pageNo, int pageSize) throws ApplicationException;
	
	/**
	 * Get List of Course
	 * @return
	 * @throws ApplicationException
	 */
	public List list () throws ApplicationException;

	/**
	 * Get List of Course with Pagination
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException;
	
}
