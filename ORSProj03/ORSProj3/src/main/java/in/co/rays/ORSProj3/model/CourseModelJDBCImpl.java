package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.CourseDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.JDBCDataSource;

/**
 * JDBC Implementation of CourseModel
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class CourseModelJDBCImpl implements CourseModelInt {
	
	
	private static Logger log=Logger.getLogger(CourseModelJDBCImpl.class);
	
	/**
	 * Find next PK of Course
	 * 
	 * 
	 * 
	 * @throws DatabaseException
	 */
	public Integer nextPk() throws DatabaseException 
	{
		log.debug("CourseModel method nextPK started");

		Connection conn = null;
		int pk = 0;
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_COURSE");
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) 
			{
				pk = rs.getInt(1);
			}
			
			rs.close();
		} 
		catch (Exception e) 
		{
			log.error("Exception in Database..", e);
			throw new DatabaseException("Exception : Exception in getting Pk");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("CourseModel nextPK method ended");
		
		return pk + 1;
	}


	/**
	 * Add a Course
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 * 
	 * 
	 */
	public long add(CourseDTO dto) throws ApplicationException, DuplicateRecordException 
	{
		log.debug("CourseModel method add started");
		Connection conn = null;
		int pk = 0;
		
		CourseDTO duplicateCourseName = findByName(dto.getName());
		if(duplicateCourseName!=null)
		{
			throw new DuplicateRecordException("Course Name already Exist");
		}
		try
		{
			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_COURSE VALUES(?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getDescription());
			pstmt.setString(4, dto.getDuration());
			pstmt.setString(5, dto.getCreatedBy());
			pstmt.setString(6, dto.getModifiedBy());
			pstmt.setTimestamp(7, dto.getCreatedDateTime());
			pstmt.setTimestamp(8, dto.getModifiedDateTime());
			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
			conn.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.debug("EXception in Database...", e);
			try 
			{
				conn.rollback();
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
				throw new ApplicationException("Exception : add Rollback Exception.." + ex.getMessage());
			}
			throw new ApplicationException("Exception in Course add method");
		} 
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("CourseModel method add ended");
		
		return pk;
	}

	/**
	 * Delete a Course
	 * 
	 * @param dto
	 * 
	 */
	public void delete(CourseDTO dto) throws ApplicationException 
	{
		log.debug("CourseModel method delete started");
		Connection conn = null;
		try 
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_COURSE WHERE ID=?");
			pstmt.setLong(1, dto.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} 
		catch (Exception e)
		{
			log.error("Database Exception...", e);
			try 
			{
				conn.rollback();
			} 
			catch (Exception ex)
			{
				throw new ApplicationException("Exception : Exception in Rollback Method" + ex.getMessage());
			}
			throw new ApplicationException("Exception in Delete Method");
		} 
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("CourseModel method delete ended");
	}

	/**
	 * Update a Course
	 * 
	 * @param dto
	 * @throws DuplicateRecordException 
	 * 
	 */
	public void update(CourseDTO dto) throws ApplicationException, DuplicateRecordException
	{
		log.debug("CourseModel method update started");
		
		Connection conn = null;
		
		CourseDTO dtoExist = findByName(dto.getName());
		if(dtoExist !=null && dtoExist.getId()!=dto.getId())
		{
			throw new DuplicateRecordException("Course Already Exist");
		}
		try 
		{
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_COURSE SET NAME=?,DESCRIPTION=?,DURATION=?,CREATEDBY=?,MODIFIEDBY=?,CREATEDDATETIME=?,MODIFIEDDATETIME=? WHERE ID=?");
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getDescription());
			pstmt.setString(3, dto.getDuration());
			pstmt.setString(4, dto.getCreatedBy());
			pstmt.setString(5, dto.getModifiedBy());
			pstmt.setTimestamp(6, dto.getCreatedDateTime());
			pstmt.setTimestamp(7, dto.getModifiedDateTime());
			pstmt.setLong(8, dto.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.debug("Database Exception ...", e);
			try
			{
				conn.rollback();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw new ApplicationException("Exception : Exception in Rollback.." + ex.getMessage());
			}
			throw new ApplicationException("Exception in Updating the Course Model");
		} 
		finally 
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("CourseModel method update ended");
	}
	
	/**
	 * Find Course by name
	 * 
	 * @param name
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public CourseDTO findByName(String name) throws ApplicationException 
	{
		log.debug("CourseModel method findByName started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE NAME=?");
		CourseDTO dto = null;
		Connection conn = null;
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				dto = new CourseDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setDuration(rs.getString(4));
				dto.setCreatedBy(rs.getString(5));
				dto.setModifiedBy(rs.getString(6));
				dto.setCreatedDateTime(rs.getTimestamp(7));
				dto.setModifiedDateTime(rs.getTimestamp(8));
			}
			rs.close();
		} 
		catch (Exception e) 
		{
			log.debug("Database Exception ..", e);
			throw new ApplicationException("Exception in Course Model FindByName Method ");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		
		log.debug("CourseModel method findByName ended");
		
		return dto;
	}
	
	/**
	 * Find Course by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return dto
	 * 
	 */
	public CourseDTO findByPK(long pk) throws ApplicationException 
	{
		log.debug("CourseModel method findbyPK started");
	
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE ID=?");
		
		CourseDTO dto = null;
		Connection conn = null;

		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				dto = new CourseDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setDuration(rs.getString(4));
				dto.setCreatedBy(rs.getString(5));
				dto.setModifiedBy(rs.getString(6));
				dto.setCreatedDateTime(rs.getTimestamp(7));
				dto.setModifiedDateTime(rs.getTimestamp(8));
			}
			rs.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			log.error("DatabaseException ... ", e);
			throw new ApplicationException("Exception : Exception in the findbyPk method");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel method findbyPK ended");
		
		return dto;
	}
	
	/**
	 * Search Course
	 * 
	 * @param dto
	 *            : Search Parameters
	 * 
	 */
	public List search (CourseDTO dto) throws ApplicationException
	{
	return search (dto,0,0);	
	}
	
	/**
	 * Search Course with pagination
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
	public List search(CourseDTO dto, int pageNo, int pageSize) throws ApplicationException 
	{
	
		log.debug("CourseModel method search started");
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE WHERE 1=1");
		
		if(dto!=null)
		{
			if(dto.getId()>0)
			{
				sql.append(" AND id = " + dto.getId());
			}
			if(dto.getName()!= null && dto.getName().length()>0)
			{
				sql.append(" AND Name like '" + dto.getName() +"%'");
			}
			if(dto.getDescription()!=null && dto.getName().length()>0)
			{
				sql.append(" AND Description like '" + dto.getDescription() + "%'");
			}
			if(dto.getDuration()!=null && dto.getDuration().length() >0)
			{
				sql.append(" AND Duration like '" + dto.getDuration().length() + "%'");
			}
		}
		
		if(pageSize>0){
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit " +pageNo+","+pageSize);	
		}
		
		ArrayList list = new ArrayList();
		
		Connection conn = null;
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			System.out.println(sql);
			ResultSet rs =pstmt.executeQuery();
			while(rs.next())
			{
				dto=new CourseDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDescription(rs.getString(3));				
				dto.setDuration(rs.getString(4));				
				dto.setCreatedBy(rs.getString(5));
				dto.setModifiedBy(rs.getString(6));
				dto.setCreatedDateTime(rs.getTimestamp(7));
				dto.setModifiedDateTime(rs.getTimestamp(8));
				list.add(dto);
				
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("Database Exception ,,,," , e);
			throw new ApplicationException("Exception in the Search Method" + e.getMessage());
		
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel method search ended");

		return list;
	}
	
	/**
	 * Get List of Course
	 * 
	 * @return list : List of Course
	 * 
	 */
	public List list () throws ApplicationException
	{
		return list(0,0);
	}


	/**
	 * Get List of Course with pagination
	 * 
	 * @return list : List of Course
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException 
	{
		log.debug("CourseModel method list started");
	
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_COURSE ");

		if(pageSize>0)
		{
			pageNo = (pageNo-1)*pageSize;
			sql.append(" limit "+ pageNo +" , "+pageSize);
		}
		
		ArrayList list = new ArrayList();
		Connection conn = null;
		
		try
		{
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt =conn.prepareStatement(sql.toString());
			ResultSet rs =pstmt.executeQuery();
			while (rs.next()) 
			{
				CourseDTO dto = new CourseDTO();
				dto.setId(rs.getLong(1));
				dto.setName(rs.getString(2));
				dto.setDescription(rs.getString(3));
				dto.setDuration(rs.getString(4));
				dto.setCreatedBy(rs.getString(5));
				dto.setModifiedBy(rs.getString(6));
				dto.setCreatedDateTime(rs.getTimestamp(7));
				dto.setModifiedDateTime(rs.getTimestamp(8));
				list.add(dto);
			}
			rs.close();
		}
		catch(Exception e)
		{
			log.error("Database Exception in list ...",e);
			throw new ApplicationException("Exception : Exception in CourseModel List method " + e.getMessage());		
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel method list ended");
		
		return list;
	}
	
	

}
