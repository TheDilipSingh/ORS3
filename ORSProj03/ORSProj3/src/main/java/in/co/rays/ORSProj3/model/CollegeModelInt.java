package in.co.rays.ORSProj3.model;

import java.util.List;

import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;

/**
 * Data Access Object of College
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public interface CollegeModelInt {
	
	
	/**
	 * Add a College
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(CollegeDTO dto) throws ApplicationException, DuplicateRecordException;
	
	/**
	 * Delete a College
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(CollegeDTO dto) throws ApplicationException;
	
	/**
	 * Find College by Name
	 * @param name
	 * @return
	 * @throws ApplicationException
	 */
	public CollegeDTO findByName(String name) throws ApplicationException;
	
	/**
	 * Find College by PK
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public CollegeDTO findByPK(long pk) throws ApplicationException;
	
	/**
	 * Update a College
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(CollegeDTO dto) throws ApplicationException, DuplicateRecordException;
	
	/**
	 * Search College
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CollegeDTO dto) throws ApplicationException;
	
	/**
	 * Search College with Pagination
	 * @param dto
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List search(CollegeDTO dto, int pageNo, int pageSize) throws ApplicationException;
	
	/**
	 * Get List of College
	 * @return
	 * @throws ApplicationException
	 */
	public List list() throws ApplicationException;
	
	/**
	 * Get List of College with Pagination
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException;

}
