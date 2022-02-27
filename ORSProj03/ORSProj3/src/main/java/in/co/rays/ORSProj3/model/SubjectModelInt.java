package in.co.rays.ORSProj3.model;

import java.util.List;

import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;

/**
 * Data Access Subject object of Subject
 * 
 * @author Dilip Singh
 * @version 1.0 Copyright (c) SunilOS
 */
public interface SubjectModelInt {

	/**
	 * Add a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException 
	 * 
	 * 
	 */
	public long add(SubjectDTO dto) throws ApplicationException, DuplicateRecordException;

	/**
	 * Delete a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 *
	 */
	public void delete(SubjectDTO dto) throws ApplicationException;

	/**
	 * Update a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(SubjectDTO dto) throws ApplicationException, DuplicateRecordException; 
	
	/**
	 * Find Subject by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * 
	 */
	public SubjectDTO findByName(String name) throws ApplicationException; 
	
	/**
	 * Find Subject by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public SubjectDTO findByPK(long pk) throws ApplicationException; 
	
	/**
	 * Search Subject
	 * 
	 * @param dto
	 *            : Search Parameters
	 * @throws ApplicationException 
	 * 
	 */
	public List search(SubjectDTO dto) throws ApplicationException;
	
	
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
	public List search(SubjectDTO dto, int pageNo, int pageSize) throws ApplicationException;	
	
	
	/**
	 * Get List of Subject
	 * 
	 * @return list : List of Subject
	 * @throws ApplicationException 
	 * 
	 */
	public List list() throws ApplicationException;
	
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
	public List list(int pageNo, int pageSize) throws ApplicationException;
}
