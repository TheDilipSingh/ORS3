package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.JDBCDataSource;

/**
 * JDBC Implementation of Student Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class StudentModelJDBCImpl  implements StudentModelInt {
	
	private static Logger log=Logger.getLogger(StudentModelJDBCImpl.class);
	
    /**
     * Find next PK of Student
     * 
     * @throws DatabaseException
     */
	public int nextPK() throws DatabaseException
	{
		log.debug("Model nextPK Started");
		Connection conn=null;
		int pk=0;
		try
		{
			conn=JDBCDataSource.getConnection();
			PreparedStatement pstmt=conn.prepareStatement("SELECT MAX(id) FROM ST_STUDENT");
			ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                pk = rs.getInt(1);
            }
            rs.close();

        }
		catch (Exception e)
		{
            log.error("Database Exception..", e);
            throw new DatabaseException("Exception : Exception in getting PK");
        }
		finally
		{
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model nextPK End");
        return pk + 1;
        
	}
       
    /**
     * Add a Student
     * 
     * @param dto
     * 
     * 
     */
        public long add(StudentDTO dto) throws ApplicationException,DuplicateRecordException
        {
        	log.debug("Model add Started");
        	Connection conn = null;

    
        	CollegeModelInt cModel = ModelFactory.getInstance().getCollegeModel();
        	CollegeDTO collegeDTO = cModel.findByPK(dto.getCollegeId());
        	dto.setCollegeName(collegeDTO.getName());

        	StudentDTO duplicateName = findByEmailId(dto.getEmail());
        	int pk = 0;

        	if (duplicateName != null)
        	{
        		throw new DuplicateRecordException("Email already exists");
        	}

        	try
        	{
        		conn = JDBCDataSource.getConnection();
        		pk = nextPK();
                conn.setAutoCommit(false); 
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_STUDENT VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                pstmt.setInt(1, pk);
                pstmt.setLong(2, dto.getCollegeId());
                pstmt.setString(3, dto.getCollegeName());
                pstmt.setString(4, dto.getFirstName());
                pstmt.setString(5, dto.getLastName());
                pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
                pstmt.setString(7, dto.getMobileNo());
                pstmt.setString(8, dto.getEmail());
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
        		log.error("Database Exception..",e);
        	try 
        	{
            conn.rollback();
        	}
        	catch (Exception ex)
        	{
            throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
        	}
        	throw new ApplicationException("Exception : Exception in add Student");
        	}
        	finally
        	{
        		JDBCDataSource.closeConnection(conn);
        	}
        	log.debug("Model add End");
        	return pk;
       }
       
        /**
         * Delete a Student
         * 
         * @param dto
         * 
         */
        public void delete(StudentDTO dto) throws ApplicationException
        {
            log.debug("Model delete Started");
            Connection conn = null;
            try
            {
                conn = JDBCDataSource.getConnection();
                conn.setAutoCommit(false);
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_STUDENT WHERE ID=?");
                pstmt.setLong(1, dto.getId());
                pstmt.executeUpdate();
                conn.commit();
                pstmt.close();
            }
            catch (Exception e)
            {
                log.error("Database Exception..", e);
                try
                {
                    conn.rollback();
                }
                catch (Exception ex)
                {
                    throw new ApplicationException("Exception : Delete rollback exception "+ ex.getMessage());
                }
                throw new ApplicationException("Exception : Exception in delete Student");
            }
            finally
            {
                JDBCDataSource.closeConnection(conn);
            }
            
            log.debug("Model delete End");
        }
        
        /**
         * Find Student by Email
         * 
         * @param Email
         *            : get parameter
         * @return dto
         * 
         */
        public StudentDTO findByEmailId(String Email) throws ApplicationException
        {
            log.debug("Model findBy Email Started");
            StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE EMAIL=?");
            StudentDTO dto = null;
            Connection conn = null;
            try
            {
                conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                pstmt.setString(1, Email);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                {
                    dto = new StudentDTO();
                    dto.setId(rs.getLong(1));
                    dto.setCollegeId(rs.getLong(2));
                    dto.setCollegeName(rs.getString(3));
                    dto.setFirstName(rs.getString(4));
                    dto.setLastName(rs.getString(5));
                    dto.setDob(rs.getDate(6));
                    dto.setMobileNo(rs.getString(7));
                    dto.setEmail(rs.getString(8));
                    dto.setCreatedBy(rs.getString(9));
                    dto.setModifiedBy(rs.getString(10));
                    dto.setCreatedDateTime(rs.getTimestamp(11));
                    dto.setModifiedDateTime(rs.getTimestamp(12));
                }
                rs.close();
            }
            catch (Exception e)
            {
                log.error("Database Exception..", e);
                throw new ApplicationException("Exception : Exception in getting User by Email");
            }
            finally
            {
                JDBCDataSource.closeConnection(conn);
            }
            log.debug("Model findBy Email End");
            return dto;
        }
        
        /**
         * Find Student by PK
         * 
         * @param pk
         *            : get parameter
         * @return dto
         * 
         */
        public StudentDTO findByPK(long pk) throws ApplicationException
        {
            log.debug("Model findByPK Started");
            StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE ID=?");
            StudentDTO dto = null;
            Connection conn = null;
            try
            {
                conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                pstmt.setLong(1, pk);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                {
                    dto = new StudentDTO();
                    dto.setId(rs.getLong(1));
                    dto.setCollegeId(rs.getLong(2));
                    dto.setCollegeName(rs.getString(3));
                    dto.setFirstName(rs.getString(4));
                    dto.setLastName(rs.getString(5));
                    dto.setDob(rs.getDate(6));
                    dto.setMobileNo(rs.getString(7));
                    dto.setEmail(rs.getString(8));
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
                log.error("Database Exception..", e);
                throw new ApplicationException("Exception : Exception in getting User by pk");
            }
            finally
            {
                JDBCDataSource.closeConnection(conn);
            }
            log.debug("Model findByPK End");
            return dto;
        }
        
        /**
         * Update a Student
         * 
         * @param dto
         * 
         */
        public void update(StudentDTO dto) throws ApplicationException,DuplicateRecordException
        {
        	log.debug("Model update Started");
        	Connection conn = null;

        	StudentDTO dtoExist = findByEmailId(dto.getEmail());

    
        	if (dtoExist != null && dtoExist.getId() != dto.getId())
        	{
        		throw new DuplicateRecordException("Email Id is already exist");
        	}

        	CollegeModelInt cModel = ModelFactory.getInstance().getCollegeModel();
        	CollegeDTO collegeDTO = cModel.findByPK(dto.getCollegeId());
        	dto.setCollegeName(collegeDTO.getName());

        	try
        	{

        		conn = JDBCDataSource.getConnection();

        		conn.setAutoCommit(false);
        		PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_STUDENT SET COLLEGE_ID=?,COLLEGE_NAME=?,FIRST_NAME=?,LAST_NAME=?,DATE_OF_BIRTH=?,MOBILE_NO=?,EMAIL=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
        		pstmt.setLong(1, dto.getCollegeId());
        		pstmt.setString(2, dto.getCollegeName());
        		pstmt.setString(3, dto.getFirstName());
        		pstmt.setString(4, dto.getLastName());
        		pstmt.setDate(5, new java.sql.Date(dto.getDob().getTime()));
        		pstmt.setString(6, dto.getMobileNo());
        		pstmt.setString(7, dto.getEmail());
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
        		log.error("Database Exception..", e);
        		try
        		{
        			conn.rollback();
        		} 
        		catch (Exception ex) 
        		{
        			throw new ApplicationException(
                    "Exception : Delete rollback exception "+ ex.getMessage());
        		}
        		throw new ApplicationException("Exception in updating Student ");
        	}
        	finally 
        	{
        		JDBCDataSource.closeConnection(conn);
        	
        	}
        	log.debug("Model update End");
        }
        
        /**
         * Search Student
         * 
         * @param dto
         *            : Search Parameters
         * 
         */
        public List search(StudentDTO dto) throws ApplicationException
        {
            return search(dto, 0, 0);
        }
        
        /**
         * Search Student with pagination
         * 
         * @return list : List of Students
         * @param dto
         *            : Search Parameters
         * @param pageNo
         *            : Current Page No.
         * @param pageSize
         *            : Size of Page
         * 
         * 
         */
        public List search(StudentDTO dto, int pageNo, int pageSize)throws ApplicationException
        {
            log.debug("Model search Started");
            StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE 1=1 ");

            if (dto != null)
            {
                if (dto.getId() > 0)
                {
                    sql.append(" AND id = " + dto.getId());
                }
                if (dto.getFirstName() != null && dto.getFirstName().length() > 0)
                {
                    sql.append(" AND FIRST_NAME like '" + dto.getFirstName()+ "%'");
                }
                if (dto.getLastName() != null && dto.getLastName().length() > 0)
                {
                    sql.append(" AND LAST_NAME like '" + dto.getLastName() + "%'");
                }
                if (dto.getDob() != null && dto.getDob().getDate() > 0)
                {
                    sql.append(" AND DOB = " + dto.getDob());
                }
                if (dto.getMobileNo() != null && dto.getMobileNo().length() > 0)
                {
                    sql.append(" AND MOBILE_NO like '" + dto.getMobileNo() + "%'");
                }
                if (dto.getEmail() != null && dto.getEmail().length() > 0)
                {
                    sql.append(" AND EMAIL like '" + dto.getEmail() + "%'");
                }
                if (dto.getCollegeName() != null && dto.getCollegeName().length() > 0)
                {
                    sql.append(" AND COLLEGE_NAME = " + dto.getCollegeName());
                }
                if (dto.getCollegeId() > 0)
                {
                    sql.append(" AND COLLEGE_ID like '" + dto.getCollegeId()+ "%'");
                }

            }

                        if (pageSize > 0) 
                        {
                        	pageNo = (pageNo - 1) * pageSize;

                        	sql.append(" Limit " + pageNo + ", " + pageSize);
                        }

                        ArrayList list = new ArrayList();
                        Connection conn = null;
                        try 
                        {
                        	conn = JDBCDataSource.getConnection();
                        	PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                        	ResultSet rs = pstmt.executeQuery();
                        	while (rs.next())
                        	{
                        		dto = new StudentDTO();
                        		dto.setId(rs.getLong(1));
                        		dto.setCollegeId(rs.getLong(2));
                        		dto.setCollegeName(rs.getString(3));
                        		dto.setFirstName(rs.getString(4));
                        		dto.setLastName(rs.getString(5));
                        		dto.setDob(rs.getDate(6));
                        		dto.setMobileNo(rs.getString(7));
                        		dto.setEmail(rs.getString(8));
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
                        	log.error("Database Exception..", e);
                        	throw new ApplicationException("Exception : Exception in search Student");
                        } 
                        finally
                        {
                        	JDBCDataSource.closeConnection(conn);
                        }

                        log.debug("Model search End");
                        return list;
        	}
        
        /**
         * Get List of Student
         * 
         * @return list : List of Student
         * 
         */
        public List list() throws ApplicationException
        {
            return list(0, 0);
        }
        
        /**
         * Get List of Student with pagination
         * 
         * @return list : List of Student
         * @param pageNo
         *            : Current Page No.
         * @param pageSize
         *            : Size of Page
         * 
         */
        public List list(int pageNo, int pageSize) throws ApplicationException {
            log.debug("Model list Started");
            ArrayList list = new ArrayList();
            StringBuffer sql = new StringBuffer("select * from ST_STUDENT");
            Connection conn = null;
            if (pageSize > 0)
            {
            	pageNo = (pageNo - 1) * pageSize;
                sql.append(" limit " + pageNo + "," + pageSize);
            }

            try
            {
                conn = JDBCDataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                {
                    StudentDTO dto = new StudentDTO();
                    dto.setId(rs.getLong(1));
                    dto.setCollegeId(rs.getLong(2));
                    dto.setCollegeName(rs.getString(3));
                    dto.setFirstName(rs.getString(4));
                    dto.setLastName(rs.getString(5));
                    dto.setDob(rs.getDate(6));
                    dto.setMobileNo(rs.getString(7));
                    dto.setEmail(rs.getString(8));
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
                log.error("Database Exception..", e);
                throw new ApplicationException("Exception : Exception in getting list of Student");
            }
            finally
            {
                JDBCDataSource.closeConnection(conn);
            }

            log.debug("Model list End");
            return list;

        }


}
