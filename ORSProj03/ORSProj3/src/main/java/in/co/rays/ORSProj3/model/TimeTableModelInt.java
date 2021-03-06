package in.co.rays.ORSProj3.model;

import java.util.Date;
import java.util.List;

import in.co.rays.ORSProj3.dto.TimeTableDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;

/**
 * Data Access Object of Time Table
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public interface TimeTableModelInt {
	
	/**
	 * Add a TIMETABLE
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * 
	 * 
	 */
	public Long add(TimeTableDTO dto) throws ApplicationException, DuplicateRecordException; 
	
	/**
	 * Delete a TimeTable
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * 
	 */
	public void delete(TimeTableDTO dto) throws ApplicationException; 
	
	/**
	 * Update a TIMETABLE
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * 
	 */
	public void update(TimeTableDTO dto) throws ApplicationException, DuplicateRecordException;
	
	/**
	 * Find TimeTable by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * 
	 */
	public TimeTableDTO findByName(String name) throws ApplicationException;
		
	/**
	 * Find TimeTable by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public TimeTableDTO findByPk(long pk) throws ApplicationException;
	
	/**
	 * Search TimeTable
	 * 
	 * @param dto
	 *            : Search Parameters
	 * @throws ApplicationException
	 * 
	 */
	public List search(TimeTableDTO dto) throws ApplicationException;
	
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
	public List search(TimeTableDTO dto, int pageNo, int pageSize) throws ApplicationException; 
	
	/**
	 * Get List of TimeTable
	 * 
	 * @return list : List of TimeTable
	 * @throws ApplicationException
	 * 
	 */
	public List list() throws ApplicationException;
	
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
	public List list(int pageNo, int pageSize) throws ApplicationException;
	
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
    public TimeTableDTO checkBycss(long CourseId , long SubjectId , String semester) throws ApplicationException;
    

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
    public TimeTableDTO checkBycds(long CourseId, String Semester ,Date ExamDate) throws ApplicationException;

}
