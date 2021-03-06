package in.co.rays.ORSProj3.model;

import java.util.List;

import in.co.rays.ORSProj3.dto.FacultyDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;

/**
 * Data Access Object of Faculty
 *
 * @author Dilip Singh
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
public interface FacultyModelInt {

	/**
	 * Add a Faculty
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 *
	 * 
	 */
	public long add(FacultyDTO dto) throws ApplicationException, DuplicateRecordException;

	/**
	 * Delete a Faculty
	 * 
	 * @param dto
	 * 
	 */
	public void delete(FacultyDTO dto) throws ApplicationException;	

	/**
	 * Update a Faculty
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(FacultyDTO dto) throws ApplicationException, DuplicateRecordException;
	
	/**
	 * Find Faculty by Email
	 * 
	 * @param EmailId
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public FacultyDTO findByEmail(String EmailId) throws ApplicationException;
		
	/**
	 * Find Faculty by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public FacultyDTO findByPK(long pk) throws ApplicationException;
	
	/**
	 * Search Faculty
	 * 
	 * @param dto
	 *            : Search Parameters
	 * 
	 */
	public List search(FacultyDTO dto) throws ApplicationException;
	
	
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
	public List search(FacultyDTO dto, int pageNo, int pageSize) throws ApplicationException;
	
	/**
	 * Get List of Faculty
	 * 
	 * @return list : List of Faculty
	 * 
	 */
	public List list() throws ApplicationException;
	
	
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
	public List list(int pageNo, int pageSize) throws ApplicationException;

	
}
