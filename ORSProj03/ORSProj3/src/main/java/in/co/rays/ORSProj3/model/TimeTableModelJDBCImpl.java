package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.dto.TimeTableDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.JDBCDataSource;

/**
 * JDBC Implementation of Time Table Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class TimeTableModelJDBCImpl implements TimeTableModelInt {
	
	private static Logger log=Logger.getLogger(TimeTableModelJDBCImpl.class);
	
	/**
	 * Find next PK of TIMETABLE
	 * 
	 * @throws ApplicationException
	 * 
	 * 
	 */
	public Integer nextPk() throws ApplicationException
	{
		log.debug("TimeTableModel method nextPk started ");
		Connection conn = null;
		int pk = 0;

		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM ST_TIMETABLE");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				pk = rs.getInt(1);
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error("database Exception ...", e);
			throw new ApplicationException("Exception in NextPk of TIMETABLE Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimeTableModel method nextPk ended ");
		return pk + 1;
	}
	
	/**
	 * Add a TIMETABLE
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * 
	 * 
	 */
	public Long add(TimeTableDTO dto) throws ApplicationException, DuplicateRecordException 
	{
		log.debug("TimeTableModel method add started");
		Connection conn = null;
		long pk = 0;
		
		CourseModelInt coumodel = ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto = coumodel.findByPK(dto.getCourseId());
		String courseName = coudto.getName();
		
		SubjectModelInt  smodel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO sdto = smodel.findByPK(dto.getSubjectId());
		String subjectName = sdto.getSubjectName();

		TimeTableDTO dto11 = checkBycds(dto.getCourseId(), dto.getSemester(),  new java.sql.Date(dto.getExamDate().getTime()));
		TimeTableDTO dto12 = checkBycss(dto.getCourseId(), dto.getSubjectId(), dto.getSemester());
		
		if(dto11 != null || dto12 != null)
		 { 
			 throw new DuplicateRecordException("TimeTable Already Exsist"); 
			 
		 }
	
		try
		{
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_TIMETABLE VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setLong(2, dto.getSubjectId());
			pstmt.setString(3, subjectName);			
			pstmt.setLong(4, dto.getCourseId());
			pstmt.setString(5, courseName);
			pstmt.setString(6, dto.getSemester());
			pstmt.setDate(7, new java.sql.Date(dto.getExamDate().getTime()));
			//	pstmt.setDate(8, dto.getExamDate().getTime());
			pstmt.setString(8, dto.getExamTime());
			pstmt.setString(9, dto.getCreatedBy());
			pstmt.setString(10, dto.getModifiedBy());
			pstmt.setTimestamp(11, dto.getCreatedDateTime());
			pstmt.setTimestamp(12, dto.getModifiedDateTime());
			pstmt.executeUpdate();
			
			conn.commit();
			pstmt.close();
		}
		catch (Exception e) 
		{		
			e.printStackTrace();
			log.error("database Exception ...", e);
			try
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				e.printStackTrace();
				throw new ApplicationException("Exception in the Rollback of TIMETABLE Model" + ex.getMessage());
			}
			throw new ApplicationException("Exception in Add method of TIMETABLE Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("TimeTableModel method add ended");
		
		return pk;
		
	}
	
	/**
	 * Delete a TimeTable
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * 
	 */
	public void delete(TimeTableDTO dto) throws ApplicationException 
	{
		log.debug("TimeTableModel method delete started");
		Connection conn = null;
		try 
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_TIMETABLE WHERE ID=?");
			pstmt.setLong(1, dto.getId());
			pstmt.executeUpdate();
			conn.commit();
		} 
		catch (Exception e) 
		{
			log.error("database Exception ...", e);
			try
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				throw new ApplicationException("Exception in Rollback of Delete Method of TimeTableModel" + ex.getMessage());
			}
			throw new ApplicationException("Exception in Delte Method of TIMETABLE Model");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimeTableModel method delete ended");
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

		log.debug("TimeTableModel method delete started");
		Connection conn = null;

		CourseModelInt coumodel = ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto = coumodel.findByPK(dto.getCourseId());
		String courseName = coudto.getName();
		
		SubjectModelInt  smodel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO sdto = smodel.findByPK(dto.getSubjectId());
		String subjectName = sdto.getSubjectName();

		
		TimeTableDTO dto11 = checkBycds(dto.getCourseId(), dto.getSemester(),  new java.sql.Date(dto.getExamDate().getTime()));
		TimeTableDTO dto12 = checkBycss(dto.getCourseId(), dto.getSubjectId(), dto.getSemester());
		 if(dto11 != null || dto12 != null)
		 { 
			 throw new DuplicateRecordException("TimeTable Already Exsist"); 			 
		 }		
		try
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_TIMETABLE SET SUBJECT_ID=? , SUBJECT_NAME =? ,COURSE_ID=?,COURSE_NAME=?, SEMESTER=?,EXAM_DATE=?,EXAM_TIME=? ,CREATED_BY=? , MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			pstmt.setLong(1, dto.getSubjectId());
			pstmt.setString(2, subjectName);			
			pstmt.setLong(3, dto.getCourseId());
			pstmt.setString(4, courseName);
			pstmt.setString(5, dto.getSemester());
			pstmt.setDate(6, new java.sql.Date(dto.getExamDate().getTime()));
			pstmt.setString(7, dto.getExamTime());
			pstmt.setString(8, dto.getCreatedBy());
			pstmt.setString(9, dto.getModifiedBy());
			pstmt.setTimestamp(10, dto.getCreatedDateTime());
			pstmt.setTimestamp(11, dto.getModifiedDateTime());
			pstmt.setLong(12, dto.getId());
			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		}
		catch (Exception e)
		{
			log.error("database Exception....", e);
			try 
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				throw new ApplicationException("Exception in Rollback of Update Method of TimeTable Model" + ex.getMessage());
			}
			throw new ApplicationException("Exception in update Method of TimeTable Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimeTable Model Update method End");
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
	public TimeTableDTO findByName(String name) throws ApplicationException
	{
		log.debug("TimeTable Model Update method started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE Subject_Name = ?");
		Connection conn = null;
		TimeTableDTO dto = null;

		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				dto = new TimeTableDTO();
				dto.setId(rs.getLong(1));
				dto.setSubjectId(rs.getLong(2));
				dto.setSubjectName(rs.getString(3));
				//dto.setDescription(rs.getString(4));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setSemester(rs.getString(6));
				dto.setExamDate(rs.getDate(7));
				dto.setExamTime(rs.getString(8));
				dto.setCreatedBy(rs.getString(9));
				dto.setModifiedBy(rs.getString(10));
				dto.setCreatedDateTime(rs.getTimestamp(11));
				dto.setModifiedDateTime(rs.getTimestamp(12));
			}
			rs.close();
		}
		catch (Exception e) 
		{
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in findByName Method of TimeTable Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimeTable Model Update method ended");
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
	public TimeTableDTO findByPk(long pk) throws ApplicationException
	{
		log.debug("TimeTableModel method findBypk started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE ID=?");
		Connection conn = null;
		TimeTableDTO dto = null;

		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				dto = new TimeTableDTO();
				dto.setId(rs.getLong(1));
				dto.setSubjectId(rs.getLong(2));
				dto.setSubjectName(rs.getString(3));				
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setSemester(rs.getString(6));
				dto.setExamDate(rs.getDate(7));
				dto.setExamTime(rs.getString(8));
				dto.setCreatedBy(rs.getString(9));
				dto.setModifiedBy(rs.getString(10));
				dto.setCreatedDateTime(rs.getTimestamp(11));
				dto.setModifiedDateTime(rs.getTimestamp(12));
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in findByPk Method of TimeTable Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("TimeTableModel method findByPk ended");
		
		return dto;
	}
	
	/**
	 * Search TimeTable
	 * 
	 * @param dto
	 *            : Search Parameters
	 * @throws ApplicationException
	 * 
	 */
	public List search(TimeTableDTO dto) throws ApplicationException 
	{
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
	public List search(TimeTableDTO dto, int pageNo, int pageSize) throws ApplicationException 
	{
		log.debug("TimeTableModel method search started");

		Connection conn = null;
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE 1=1");
				
		if (dto != null)
		{
	/*		if (dto.getId() > 0)
	 * 		{
				sql.append(" AND id = " + dto.getId());
			}
	*/		
			if(dto.getCourseId() > 0)
			{
				sql.append(" AND Course_ID = " + dto.getCourseId()); 
			}
			if(dto.getSubjectId() > 0)
			{
				sql.append(" AND Subject_ID = " + dto.getSubjectId()); 
			}
			if (dto.getExamDate() !=null && dto.getExamDate().getTime() >0 )
			{				
							
 				System.out.println("===============...>>>>"+dto.getExamDate());
				Date d = new Date(dto.getExamDate().getTime());
				sql.append(" AND Exam_Date = '" + d + "'");
 				System.out.println("sql statement ==="+d);
			}
			
		/*	if (dto.getCourseName() !=null && dto.getCourseName().length() >0 ) {
				sql.append(" AND CourseName like '" + dto.getCourseName() + "%'");
			}
			
			if (dto.getSubjectName() !=null && dto.getSubjectName().length() >0 ) {
				sql.append(" AND SubjectName like '" + dto.getSubjectName() + "%'");
			}
		*/	
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + " , " + pageSize);
		}

		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				dto = new TimeTableDTO();
				dto.setId(rs.getLong(1));
				dto.setSubjectId(rs.getLong(2));
				dto.setSubjectName(rs.getString(3));				
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setSemester(rs.getString(6));
				dto.setExamDate(rs.getDate(7));
				dto.setExamTime(rs.getString(8));
				dto.setCreatedBy(rs.getString(9));
				dto.setModifiedBy(rs.getString(10));
				dto.setCreatedDateTime(rs.getTimestamp(11));
				dto.setModifiedDateTime(rs.getTimestamp(12));
				list.add(dto);
			}
			rs.close();
		} 
		catch (Exception e)
		{		
			e.printStackTrace();
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in search Method of TimeTable Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("TimeTableModel method search ended");
		
		return list;
	}
	
	/**
	 * Get List of TimeTable
	 * 
	 * @return list : List of TimeTable
	 * @throws ApplicationException
	 * 
	 */
	public List list() throws ApplicationException 
	{
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
	public List list(int pageNo, int pageSize) throws ApplicationException
	{
		log.debug("TimeTableModel method list started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE ");

		if (pageSize > 0) 
		{
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + " , " + pageSize);
		}


		Connection conn = null;
		ArrayList list = new ArrayList();
		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{

				TimeTableDTO dto = new TimeTableDTO();
				dto.setId(rs.getLong(1));
				dto.setSubjectId(rs.getLong(2));
				dto.setSubjectName(rs.getString(3));
			//	dto.setDescription(rs.getString(4));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setSemester(rs.getString(6));
				dto.setExamDate(rs.getDate(7));
				dto.setExamTime(rs.getString(8));
				dto.setCreatedBy(rs.getString(9));
				dto.setModifiedBy(rs.getString(10));
				dto.setCreatedDateTime(rs.getTimestamp(11));
				dto.setModifiedDateTime(rs.getTimestamp(12));
				list.add(dto);
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimeTableModel method list ended");
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
    public  TimeTableDTO checkBycss(long CourseId , long SubjectId , String semester) throws ApplicationException
    {
    	log.debug("TimeTableModel method checkBycss started");

    	Connection conn = null ; 
    	TimeTableDTO dto=null;

    	StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE Course_ID=? AND Subject_ID=? AND Semester=?");
    	
    	try 
    	{
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setLong(2, SubjectId);
			ps.setString(3, semester);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				dto = new TimeTableDTO();
				dto.setId(rs.getLong(1));
				dto.setSubjectId(rs.getInt(2));
				dto.setSubjectName(rs.getString(3));
//				dto.setDescription(rs.getString(4));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setSemester(rs.getString(6));
				dto.setExamDate(rs.getDate(7));
				dto.setExamTime(rs.getString(8));
				dto.setCreatedBy(rs.getString(9));
				dto.setModifiedBy(rs.getString(10));
				dto.setCreatedDateTime(rs.getTimestamp(11));
				dto.setModifiedDateTime(rs.getTimestamp(12));
			}
			rs.close();
		}
    	catch (Exception e) 
    	{		
    		e.printStackTrace();
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in checkBycss Method of timetable Model");
		}
    	finally 
    	{
			JDBCDataSource.closeConnection(conn);
		}
    	
    	log.debug("TimeTableModel method checkBycss ended");

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
    public TimeTableDTO checkBycds(long CourseId, String Semester ,Date ExamDate) throws ApplicationException
    {
    	log.debug("TimeTableModel method checkBycds started");

    	StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE Course_ID=? AND Semester=? AND Exam_Date=?");
        	
     	Connection conn = null;
    	TimeTableDTO dto=null;

    	try 
    	{
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setString(2, Semester);
			ps.setDate(3, (java.sql.Date)ExamDate);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				dto = new TimeTableDTO();
				dto.setId(rs.getLong(1));
				dto.setSubjectId(rs.getInt(2));
				dto.setSubjectName(rs.getString(3));
//				dto.setDescription(rs.getString(4));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setSemester(rs.getString(6));
				dto.setExamDate(rs.getDate(7));
				dto.setExamTime(rs.getString(8));			
				dto.setCreatedBy(rs.getString(9));
				dto.setModifiedBy(rs.getString(10));
				dto.setCreatedDateTime(rs.getTimestamp(11));
				dto.setModifiedDateTime(rs.getTimestamp(12));
			}
			rs.close();
		}
    	catch (Exception e) 
    	{		
    		e.printStackTrace();
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		} 
    	finally 
    	{
			JDBCDataSource.closeConnection(conn);
		}
		
    	log.debug("TimeTableModel method ceckBycds ended");

		return dto;
    }

	

}
