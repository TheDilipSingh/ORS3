package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.dto.SubjectDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.JDBCDataSource;

/**
 * JDBC Implementation of Subject Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class SubjectModelJDBCImpl implements SubjectModelInt {
	
	private static Logger log = Logger.getLogger(SubjectModelJDBCImpl.class);

	/**
	 * Find next PK of Subject
	 * 
	 * @throws ApplicationException
	 * 
	 * 
	 */
	public Integer nextPk() throws ApplicationException
	{
		log.debug("SubjectModel method nextPK started");
		
		Connection conn = null;
		int pk = 0;

		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM ST_SUBJECT");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				pk = rs.getInt(1);
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			log.error("database Exception ...", e);
			throw new ApplicationException("Exception in NextPk of subject Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("SubjectModel method nextPK ended");
		
		return pk + 1;
	}


	/**
	 * Add a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException 
	 * 
	 * 
	 */
	public long add(SubjectDTO dto) throws ApplicationException, DuplicateRecordException 
	{		
		log.debug("SubjectModel method add started");
		
		Connection conn = null;
		long pk = 0;

		CourseModelInt coumodel= ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto = coumodel.findByPK(dto.getCourseId());
		String courseName= coudto.getName();

		SubjectDTO DuplicateSubjectName = findByName(dto.getSubjectName());
		if(DuplicateSubjectName != null)
		{
			throw new DuplicateRecordException("Subject name Already Exsist");
		}

		try
		{
			pk = nextPk();
		
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_SUBJECT VALUES(?,?,?,?,?,?,?,?,?)");
			
			pstmt.setLong(1, pk);
			pstmt.setString(2, dto.getSubjectName());
			pstmt.setString(3, dto.getDescription());
			pstmt.setLong(4, dto.getCourseId());
			pstmt.setString(5, courseName);
			pstmt.setString(6, dto.getCreatedBy());
			pstmt.setString(7 ,dto.getModifiedBy());
			pstmt.setTimestamp(8, dto.getCreatedDateTime());
			pstmt.setTimestamp(9, dto.getModifiedDateTime());
			pstmt.executeUpdate();
			
			pstmt.close();
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
				throw new ApplicationException("Exception in the Rollback of Subject Model" + ex.getMessage());
			}

			throw new ApplicationException("Exception in Add method of Subject Model");
		} 
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("SubjectModel method add ended");
		
		return pk;

	}

	/**
	 * Delete a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 *
	 */
	public void delete(SubjectDTO dto) throws ApplicationException 
	{
		log.debug("SubjectModel method delete started");
		Connection conn = null;
		try
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_SUBJECT WHERE ID=?");
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
				throw new ApplicationException("Exception in Rollback of Delte Method of Subject Model" + ex.getMessage());
			}
			throw new ApplicationException("Exception in Delte Method of Subject Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("SubjectModel method delete ended");
	}

	/**
	 * Update a Subject
	 * 
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(SubjectDTO dto) throws ApplicationException, DuplicateRecordException 
	{
		log.debug("SubjectModel method update started");
		
		Connection conn = null;
		System.out.println("update method");

		CourseModelInt coumodel= ModelFactory.getInstance().getCourseModel();
		CourseDTO coudto = coumodel.findByPK(dto.getCourseId());
		String courseName= coudto.getName();
		
		
		SubjectDTO dtoExist = findByName(dto.getSubjectName());
		if(dtoExist != null && dto.getId() != dto.getId())
		{
			throw new DuplicateRecordException("Subject name Already Exsist");
		}
		
		try 
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_SUBJECT SET Name=?,Description=?,Course_ID=?,Course_Name=?,Created_By=?,Modified_By=?,Created_DateTime=?,Modified_DateTime=? WHERE ID=?");
			pstmt.setString(1, dto.getSubjectName());
			pstmt.setString(2, dto.getDescription());
			pstmt.setLong(3, dto.getCourseId());
			pstmt.setString(4, courseName);
			pstmt.setString(5, dto.getCreatedBy());
			pstmt.setString(6, dto.getModifiedBy());
			pstmt.setTimestamp(7, dto.getCreatedDateTime());
			pstmt.setTimestamp(8, dto.getModifiedDateTime());
			pstmt.setLong(9, dto.getId());
			pstmt.executeUpdate();
		
			conn.commit();
			pstmt.close();

		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("database Exception....", e);
			try 
			{
				conn.rollback();
			} 
			catch (Exception ex) 
			{
				e.printStackTrace();
				throw new ApplicationException("Exception in Rollback of Update Method of Subject Model" + ex.getMessage());
			}
			throw new ApplicationException("Exception in Delte Method of Subject Model");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("SubjectModel method update ended");
	}
	
	/**
	 * Find Subject by Name
	 * 
	 * @param name
	 *            : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * 
	 */
	public SubjectDTO findByName(String name) throws ApplicationException 
	{
		log.debug("SubjectModel method findByName started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE NAME=?");
		Connection conn = null;
		SubjectDTO dto = null;

		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				dto = new SubjectDTO();				
				dto.setId(rs.getLong(1));
				dto.setSubjectName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDateTime(rs.getTimestamp(8));
				dto.setModifiedDateTime(rs.getTimestamp(9));
			}
			rs.close();
		} 
		catch (Exception e)
		{
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in findByName Method of Subject Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);

		}
		
		log.debug("SubjectModel method findByName ended");
		
		return dto;
	}
	
	/**
	 * Find Subject by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public SubjectDTO findByPK(long pk) throws ApplicationException 
	{

		log.debug("SubjectModel method findBypk started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE ID=?");
		Connection conn = null;
		SubjectDTO dto = null;

		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				dto = new SubjectDTO();				
				dto.setId(rs.getLong(1));
				dto.setSubjectName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDateTime(rs.getTimestamp(8));
				dto.setModifiedDateTime(rs.getTimestamp(9));
			}
			rs.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in findByPk Method of Subject Model");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);

		}
		
		log.debug("SubjectModel method findByPk ended");
		
		return dto;
	}
	
	/**
	 * Search Subject
	 * 
	 * @param dto
	 *            : Search Parameters
	 * @throws ApplicationException 
	 * 
	 */
	public List search(SubjectDTO dto) throws ApplicationException
	{
		return search(dto,0,0);
	}
	
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
	public List search(SubjectDTO dto, int pageNo, int pageSize) throws ApplicationException 
	{
		log.debug("SubjectModel method search started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE 1=1 ");

		if(dto!= null){
			if(dto.getId() > 0)
			{
				sql.append(" AND id = " + dto.getId()); 
			}
			if(dto.getCourseId() > 0)
			{
				sql.append(" AND Course_ID = " + dto.getCourseId()); 
			}
		
		/*	if (dto.getSubjectName() != null && dto.getSubjectName().length() >0 ) {
				sql.append(" AND Name like '" +dto.getSubjectName() + "%'");
			}
			if (dto.getCourseName() !=null && dto.getCourseName().length() >0 ) {
				sql.append(" AND CourseName like '" + dto.getCourseName() + "%'");
			}
		*/	/*if (dto.getDescription() !=null && dto.getDescription().length() >0 ) {
				sql.append(" AND description like '" + dto.getDescription() + " % ");
			}*/
			
			
		}
		

		 if (pageSize > 0) 
         {
         	pageNo = (pageNo - 1) * pageSize;

         	sql.append(" Limit " + pageNo + ", " + pageSize);
         }
		
		System.out.println(sql.toString());

		Connection conn = null;
		ArrayList list = new ArrayList();
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
		
			while(rs.next())
			{
				dto = new SubjectDTO();			
				dto.setId(rs.getLong(1));
				dto.setSubjectName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDateTime(rs.getTimestamp(8));
				dto.setModifiedDateTime(rs.getTimestamp(9));
				list.add(dto);
			}
			rs.close();
			
		}
		catch (Exception e)
		{
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in search Method of Subject Model");
		} 
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("SubjectModel method search ended");		
		
		return list;
	}	
	
	/**
	 * Get List of Subject
	 * 
	 * @return list : List of Subject
	 * @throws ApplicationException 
	 * 
	 */
	public List list() throws ApplicationException
	{
		return list(0,0);
	}
	
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
	public List list(int pageNo, int pageSize) throws ApplicationException 
	{
		log.debug("SubjectModel method list started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT ");
		
		if (pageSize>0) 
		{
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit "+ pageNo+ " , " + pageSize);
		}
		
		Connection conn = null;
		ArrayList list = new ArrayList();
		try 
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				SubjectDTO dto = new SubjectDTO();				
				dto.setId(rs.getLong(1));
				dto.setSubjectName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setCourseId(rs.getLong(4));
				dto.setCourseName(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDateTime(rs.getTimestamp(8));
				dto.setModifiedDateTime(rs.getTimestamp(9));
				list.add(dto);
			}
			rs.close();
		}
		catch (Exception e) 
		{
			log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of Subject Model");
		}
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("SubjectModel method list ended");
		
		return list;
	}
	
	

}
