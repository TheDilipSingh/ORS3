package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.FacultyDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.JDBCDataSource;


/**
 * JDBC Implementation of FacultyModel
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class FacultyModelJDBCImpl implements FacultyModelInt {

	public static Logger log=Logger.getLogger(FacultyModelJDBCImpl.class);
	
	/**
	 * Find next PK of Faculty
	 * 
	 * 
	 */
	public Integer nextPk() throws ApplicationException 
	{
		log.debug("FacultyModel method nextPK started");
		Connection conn = null;
		int pk = 0;
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM ST_FACULTY");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				pk = rs.getInt(1);
			}
			rs.close();
		}
		catch (Exception e) 
		{
			log.error("DataBase Exception ..", e);
			throw new ApplicationException("Exception in Getting the PK");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("FacultyModel method nextPK ended");
		
		return pk + 1;
	}

	/**
	 * Add a Faculty
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 *
	 * 
	 */
	public long add(FacultyDTO dto) throws ApplicationException, DuplicateRecordException 
	{
		log.debug("FacultyModel method add started");
	
		Connection conn = null;
		int pk = 0;

		FacultyDTO duplicateFacultyName = findByEmail(dto.getLoginId());
		if (duplicateFacultyName != null) 
		{
			throw new DuplicateRecordException("Faculty name already Exist");
		}
		try 
		{
			pk = nextPk();
			
			CollegeModelInt cmodel = ModelFactory.getInstance().getCollegeModel();
			CollegeDTO cdto = cmodel.findByPK(dto.getCollegeId());
			String collegeName = cdto.getName();

			SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
			SubjectDTO sdto = smodel.findByPK(dto.getSubjectId());
			String subjectname = sdto.getSubjectName();
			    
			CourseModelInt coumodel = ModelFactory.getInstance().getCourseModel();
			CourseDTO coudto =    coumodel.findByPK(dto.getCourseId());
			String courseName= coudto.getName();
			
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_FACULTY VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			pstmt.setLong(1, pk);
			pstmt.setString(2, dto.getFirstName());
			pstmt.setString(3, dto.getLastName());
			pstmt.setString(4, dto.getGender());
			pstmt.setDate(5, new java.sql.Date(dto.getDateOfJoining().getTime()));
			pstmt.setString(6, dto.getQualification());
			pstmt.setString(7, dto.getLoginId());
			pstmt.setString(8, dto.getMobileno());
			pstmt.setLong(9, dto.getCollegeId());
			pstmt.setString(10, collegeName);
			pstmt.setLong(11, dto.getCourseId());
			pstmt.setString(12, courseName);
			pstmt.setLong(13, dto.getSubjectId());
			pstmt.setString(14, subjectname);
			pstmt.setString(15, dto.getCreatedBy());
			pstmt.setString(16, dto.getModifiedBy());
			pstmt.setTimestamp(17, dto.getCreatedDateTime());
			pstmt.setTimestamp(18, dto.getModifiedDateTime());
			
			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error("DATABASeEXCEPTION :...", e);
			try 
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				throw new ApplicationException("Exception in getting Rollback.." + ex.getMessage());
			}
			throw new ApplicationException("Exception in Faculty Model Add method");
		} 
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("FacultyModel method add ended");
		return pk;
	}

	/**
	 * Delete a Faculty
	 * 
	 * @param dto
	 * 
	 */
	public void delete(FacultyDTO dto) throws ApplicationException 
	{
		log.debug("FacultyModel method delete started");
		Connection conn = null;
		try 
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_FACULTY WHERE ID=?");
			pstmt.setLong(1, dto.getId());
			pstmt.executeUpdate();
			conn.commit();
		} 
		catch (Exception e) 
		{
			log.error("DATABASE EXCEPTION ", e);
			try
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				throw new ApplicationException("Exception in Faculty Model rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in Faculty Model Delete Method");
		} 
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("FacultyModel method delete ended");
	}	

	/**
	 * Update a Faculty
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(FacultyDTO dto) throws ApplicationException, DuplicateRecordException 
	{
		log.debug("FacultyModel method update started");
		Connection conn = null;

		CollegeModelInt cmodel = ModelFactory.getInstance().getCollegeModel();
		CollegeDTO cdto = cmodel.findByPK(dto.getCollegeId());
		String collegeName = cdto.getName();

		SubjectModelInt smodel = ModelFactory.getInstance().getSubjectModel();
		SubjectDTO sdto = smodel.findByPK(dto.getSubjectId());
		String subjectname = sdto.getSubjectName();
		    
		CourseModelInt coumodel = ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto =    coumodel.findByPK(dto.getCourseId());
		String courseName= coudto.getName();
		
		FacultyDTO dtoExist = findByEmail(dto.getFirstName());
		if (dtoExist != null && dto.getId()!= dto.getId())
		{
			throw new DuplicateRecordException("faculty already Exist");
		}
		
		
		try 
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_FACULTY SET FIRST_NAME=?, LAST_NAME=?, GENDER=?, DATE_OF_JOINING=?, QUALIFICATION=?, EMAIL_ID=?, MOBILE_NO=? , COLLEGE_ID=? , COLLEGE_NAME=?,COURSE_ID=?,COURSE_NAME=?, SUBJECT_ID=?, SUBJECT_NAME=?, CREATED_BY=? , MODIFIED_BY=? , CREATED_DATETIME=? , MODIFIED_DATETIME=? WHERE ID=? ");

			pstmt.setString(1, dto.getFirstName());
			pstmt.setString(2, dto.getLastName());
			pstmt.setString(3, dto.getGender());
			pstmt.setDate(4, new java.sql.Date(dto.getDateOfJoining().getTime()));
			pstmt.setString(5, dto.getQualification());
			pstmt.setString(6, dto.getLoginId());
			pstmt.setString(7, dto.getMobileno());
			pstmt.setLong(8, dto.getCollegeId());
			pstmt.setString(9, collegeName);
			pstmt.setLong(10, dto.getCourseId());
			pstmt.setString(11, courseName);
			pstmt.setLong(12, dto.getSubjectId());
			pstmt.setString(13, subjectname);
			pstmt.setString(14, dto.getCreatedBy());
			pstmt.setString(15, dto.getModifiedBy());
			pstmt.setTimestamp(16, dto.getCreatedDateTime());
			pstmt.setTimestamp(17, dto.getModifiedDateTime());
			pstmt.setLong(18, dto.getId());

			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("DATABASE EXCEPTION ...", e);
			try 
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				throw new ApplicationException("Exception in rollback faculty model .." + ex.getMessage());
			}
			throw new ApplicationException("Exception in faculty model Update Method..");
		} 
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Faculty Model update method End");
	}

	/**
	 * Find Faculty by Email
	 * 
	 * @param EmailId
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public FacultyDTO findByEmail(String EmailId) throws ApplicationException 
	{
		
		log.debug("FacultyModel method findByName started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE Email_Id=?");
		Connection conn = null;
		FacultyDTO dto = null;
		
		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, EmailId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				dto = new FacultyDTO();
				dto.setId(rs.getLong(1));
				dto.setFirstName(rs.getString(2));
				dto.setLastName(rs.getString(3));
				dto.setGender(rs.getString(4));
				dto.setDateOfJoining(rs.getDate(5));
				dto.setQualification(rs.getString(6));
				dto.setLoginId(rs.getString(7));
				dto.setMobileno(rs.getString(8));
				dto.setCollegeId(rs.getLong(9));
				dto.setCollegeName(rs.getString(10));
				dto.setCourseId(rs.getLong(11));
				dto.setCourseName(rs.getString(12));
				dto.setSubjectId(rs.getLong(13));
				dto.setSubjectName(rs.getString(14));
				dto.setCreatedBy(rs.getString(15));
				dto.setModifiedBy(rs.getString(16));
				dto.setCreatedDateTime(rs.getTimestamp(17));
				dto.setModifiedDateTime(rs.getTimestamp(18));
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			log.error("database exception ..." , e);
			throw new ApplicationException("Exception : Exception in faculty model in findbyName method");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Faculty Model findbyName method ended");
		
		return dto;
	}
	
	/**
	 * Find Faculty by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public FacultyDTO findByPK(long pk) throws ApplicationException {
		log.debug("FacultyModel method findByPK started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE ID=?");
		Connection conn = null;
		FacultyDTO dto = null;
		
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				dto = new FacultyDTO();
				dto.setId(rs.getLong(1));
				dto.setFirstName(rs.getString(2));
				dto.setLastName(rs.getString(3));
				dto.setGender(rs.getString(4));
				dto.setDateOfJoining(rs.getDate(5));
				dto.setQualification(rs.getString(6));
				dto.setLoginId(rs.getString(7));
				dto.setMobileno(rs.getString(8));
				dto.setCollegeId(rs.getLong(9));
				dto.setCollegeName(rs.getString(10));
				dto.setCourseId(rs.getLong(11));
				dto.setCourseName(rs.getString(12));
				dto.setSubjectId(rs.getLong(13));
				dto.setSubjectName(rs.getString(14));
				dto.setCreatedBy(rs.getString(15));
				dto.setModifiedBy(rs.getString(16));
				dto.setCreatedDateTime(rs.getTimestamp(17));
				dto.setModifiedDateTime(rs.getTimestamp(18));
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error("database exception ..." , e);
			throw new ApplicationException("Exception : Exception in findByPK in faculty model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("Faculty Model FindByPK method ended");
		
		return dto;
	}
	
	/**
	 * Search Faculty
	 * 
	 * @param dto
	 *            : Search Parameters
	 * 
	 */
	public List search(FacultyDTO dto) throws ApplicationException
	{
		return search(dto,0,0);
	}
	
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
	public List search(FacultyDTO dto, int pageNo, int pageSize) throws ApplicationException 
	{
		
		log.debug("FacultyModel method search started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY WHERE 1=1");
		if (dto!=null) 
		{
			if (dto.getId()>0) 
			{
				sql.append(" AND id = '" + dto.getId());
			}
			if (dto.getFirstName()!=null && dto.getFirstName().length()>0) 
			{
				sql.append(" AND First_Name like '" + dto.getFirstName() + "%'");
			}
			if (dto.getLastName()!=null && dto.getLastName().length()>0) 
			{
				sql.append(" AND Last_Name like '" + dto.getLastName() + "%'");
			}
			if (dto.getLoginId()!=null && dto.getLoginId().length()>0) 
			{
				sql.append(" AND Email_Id like '" + dto.getLoginId() + "%'");
			}
			if (dto.getCollegeId() > 0)
			{
				sql.append(" AND college_id like '" + dto.getCollegeId()+ "%'" );
			}
			
	/*		if (dto.getGender()!=null && dto.getGender().length()>0) {
				sql.append(" AND Gender like '" + dto.getGender() + "%'");
			}
	
			// date of birth
			
				if (dto.getFirstName()!=null && dto.getFirstName().length()>0) {
				sql.append(" AND firstname = " + dto.getFirstName() + " % ");
			}
			
			if (dto.getQualification()!=null && dto.getQualification().length()>0) {
				sql.append(" AND Qualification like '" + dto.getQualification() + "%'");
			}
			
			if (dto.getMobileno()!=null && dto.getMobileno().length()>0) {
				sql.append(" AND MobileNo like '" + dto.getMobileno() + "%'");
			}
			
			if (dto.getCollegeName()!=null && dto.getCollegeName().length()>0) {
				sql.append(" AND collegename like '" + dto.getCollegeName() + "%'");
			}
			if (dto.getCourseId() > 0) {
				sql.append(" AND courseid = '" + dto.getCourseId());
			}
			if (dto.getCourseName()!=null && dto.getCourseName().length()>0) {
				sql.append(" AND coursename like '" + dto.getCourseName() + "%'");
			}
			if (dto.getSubjectId() > 0) {
				sql.append(" AND Subjectid = '" + dto.getSubjectId());
			}
			if (dto.getSubjectName()!=null && dto.getSubjectName().length()>0) {
				sql.append(" AND subjectname like '" + dto.getSubjectName() + "%'");
			}
	*/	}
		
		if(pageSize>0)
		{
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit "+pageNo+ " , " + pageSize);
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
				dto = new FacultyDTO();
				dto.setId(rs.getLong(1));
				dto.setFirstName(rs.getString(2));
				dto.setLastName(rs.getString(3));
				dto.setGender(rs.getString(4));
				dto.setDateOfJoining(rs.getDate(5));
				dto.setQualification(rs.getString(6));
				dto.setLoginId(rs.getString(7));
				dto.setMobileno(rs.getString(8));
				dto.setCollegeId(rs.getLong(9));
				dto.setCollegeName(rs.getString(10));
				dto.setCourseId(rs.getLong(11));
				dto.setCourseName(rs.getString(12));
				dto.setSubjectId(rs.getLong(13));
				dto.setSubjectName(rs.getString(14));
				dto.setCreatedBy(rs.getString(15));
				dto.setModifiedBy(rs.getString(16));
				dto.setCreatedDateTime(rs.getTimestamp(17));
				dto.setModifiedDateTime(rs.getTimestamp(18));

				list.add(dto);
			}
			
			rs.close();			
		}
		catch(Exception e)
		{
		log.error("database Exception .. " , e);
		throw new ApplicationException("Exception : Exception in Search method of Faculty Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel method search ended");

		return list;		
	}
	
	/**
	 * Get List of Faculty
	 * 
	 * @return list : List of Faculty
	 * 
	 */
	public List list() throws ApplicationException
	{
		return list(0,0);
	}
	
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
	public List list(int pageNo, int pageSize) throws ApplicationException
	{
		log.debug("FacultyModel method list started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_FACULTY");
		Connection conn = null;
		ArrayList list = new ArrayList();

		if (pageSize>0) 
		{
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit "+ pageNo+ " , " + pageSize);
		}
		try
		{
				conn = JDBCDataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) 
				{
					FacultyDTO dto = new FacultyDTO();
					dto.setId(rs.getLong(1));
					dto.setFirstName(rs.getString(2));
					dto.setLastName(rs.getString(3));
					dto.setGender(rs.getString(4));
					dto.setDateOfJoining(rs.getDate(5));
					dto.setQualification(rs.getString(6));
					dto.setLoginId(rs.getString(7));
					dto.setMobileno(rs.getString(8));
					dto.setCollegeId(rs.getLong(9));
					dto.setCollegeName(rs.getString(10));
					dto.setCourseId(rs.getLong(11));
					dto.setCourseName(rs.getString(12));
					dto.setSubjectId(rs.getLong(13));
					dto.setSubjectName(rs.getString(14));
					dto.setCreatedBy(rs.getString(15));
					dto.setModifiedBy(rs.getString(16));
					dto.setCreatedDateTime(rs.getTimestamp(17));
					dto.setModifiedDateTime(rs.getTimestamp(18));
					list.add(dto);
				}
				rs.close();
		}
		catch(Exception e)
		{
			log.error("Database Exception ......" , e);
			throw new ApplicationException("Exception in list method of FacultyModel");
		}
		finally 
		{
		JDBCDataSource.closeConnection(conn);	
		}
		
		log.debug("FacultyModel method list ended");
		
		return list;
	}	
	

}
