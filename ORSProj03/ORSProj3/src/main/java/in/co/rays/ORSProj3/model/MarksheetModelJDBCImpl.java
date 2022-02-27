package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.MarksheetDTO;
import in.co.rays.ORSProj3.dto.StudentDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.util.JDBCDataSource;

/**
 * JDBC Implementation of Marksheet Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class MarksheetModelJDBCImpl implements MarksheetModelInt {

	Logger log=Logger.getLogger(MarksheetModelJDBCImpl.class);
	
	/**
	 * NextPK
	 */
	public Integer nextPK() throws DatabaseException
	{
		log.debug("Model nextPK Started");
		Connection conn=null;
		int pk=0;
		try
		{
			conn=JDBCDataSource.getConnection();
			PreparedStatement pstmt=conn.prepareStatement("SELECT MAX(id) from ST_MARKSHEET");
			ResultSet rs=pstmt.executeQuery();
			while(rs.next())
			{
				pk=rs.getInt(1);
				
			}
			rs.close();
		}
		catch (Exception e)
		{
			log.error("Database Exception",e);
			throw new DatabaseException("Exception in Marksheet getting PK");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk+1;
	}
	
    /**
     * Adds a Marksheet
     * 
     * @param DTO
     * 
     * 
     */
	public long add(MarksheetDTO DTO) throws ApplicationException, DuplicateRecordException
	{
		log.debug("Model add Started");
		Connection conn=null;
		int pk=0;
		
		StudentModelInt sModel=ModelFactory.getInstance().getStudentModel();
		StudentDTO studentDTO=sModel.findByPK(DTO.getStudentId());
		DTO.setName(studentDTO.getFirstName()+" "+studentDTO.getLastName());
		MarksheetDTO duplicateMarksheet=findByRollNo(DTO.getRollNo());
		if(duplicateMarksheet!=null)
		{
			throw new DuplicateRecordException("Roll Number already exist");
		}
		try
		{
			conn=JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			pk=nextPK();
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO ST_MARKSHEET VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, DTO.getRollNo());
            pstmt.setLong(3, DTO.getStudentId());
            pstmt.setString(4, DTO.getName());
            pstmt.setInt(5, DTO.getPhysics());
            pstmt.setInt(6, DTO.getChemistry());
            pstmt.setInt(7, DTO.getMaths());
            pstmt.setString(8, DTO.getCreatedBy());
            pstmt.setString(9, DTO.getModifiedBy());
            pstmt.setTimestamp(10, DTO.getCreatedDateTime());
            pstmt.setTimestamp(11, DTO.getModifiedDateTime());
            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Database Exception",e);
			
			try
			{
				conn.rollback();
				
			}
			catch (Exception ex)
			{
				throw new ApplicationException("add rollback exception"+ex.getMessage());
			}
			throw new ApplicationException("Exception in add Marksheet");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
			log.debug("Model add End");
			return pk;
	}
	
    /**
     * Deletes a Marksheet
     * 
     * @param DTO
     * 
     */
	public void delete(MarksheetDTO DTO) throws ApplicationException
	{
		log.debug("Model delete Started");
		Connection conn=null;
		try
		{
			conn=JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt=conn.prepareStatement("DELETE FROM ST_MARKSHEET WHERE ID=?");
			pstmt.setLong(1, DTO.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			
		}
		catch (Exception e)
		{
			log.error("Database Exception",e);
			
			try
			{
				conn.rollback();				
			}
			catch (Exception ex)
			{
				log.error(ex);
				throw new ApplicationException("Delete rollback Exception"+ex.getMessage());			
			}
			throw new ApplicationException("Exception in delete marksheet");
		}
		finally
		{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete End");
	}
	
    /**
     * Finds Marksheet by Roll No
     * 
     * @param rollNo
     *            : get parameter
     * @return DTO
     * 
     */
    public MarksheetDTO findByRollNo(String rollNo) throws ApplicationException
    {
        log.debug("Model findByRollNo Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE ROLL_NO=?");
        MarksheetDTO DTO = null;
        Connection conn = null;
        try
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                DTO = new MarksheetDTO();
                DTO.setId(rs.getLong(1));
                DTO.setRollNo(rs.getString(2));
                DTO.setStudentId(rs.getLong(3));
                DTO.setName(rs.getString(4));
                DTO.setPhysics(rs.getInt(5));
                DTO.setChemistry(rs.getInt(6));
                DTO.setMaths(rs.getInt(7));
                DTO.setCreatedBy(rs.getString(8));
                DTO.setModifiedBy(rs.getString(9));
                DTO.setCreatedDateTime(rs.getTimestamp(10));
                DTO.setModifiedDateTime(rs.getTimestamp(11));
            }
            rs.close();
        }
        catch (Exception e)
        {
            log.error("Database Exception",e);
            throw new ApplicationException("Exception in getting marksheet by roll no");
        }
        finally
        {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("Model findByRollNo End");
        return DTO;
    }
    
    /**
     * Finds Marksheet by PK
     * 
     * @param pk
     *            : get parameter
     * @return DTO
     * 
     */
    public MarksheetDTO findByPK(long pk) throws ApplicationException
    {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE ID=?");
        MarksheetDTO DTO = null;
        Connection conn = null;
        try
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                DTO = new MarksheetDTO();
                DTO.setId(rs.getLong(1));
                DTO.setRollNo(rs.getString(2));
                DTO.setStudentId(rs.getLong(3));
                DTO.setName(rs.getString(4));
                DTO.setPhysics(rs.getInt(5));
                DTO.setChemistry(rs.getInt(6));
                DTO.setMaths(rs.getInt(7));
                DTO.setCreatedBy(rs.getString(8));
                DTO.setModifiedBy(rs.getString(9));
                DTO.setCreatedDateTime(rs.getTimestamp(10));
                DTO.setModifiedDateTime(rs.getTimestamp(11));
            }
            rs.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            log.error("Database Exception",e);
            throw new ApplicationException("Exception in getting marksheet by pk");
        }
        finally
        {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByPK End");
        return DTO;
    }
    
    /**
     * Updates a Marksheet
     * 
     * @param DTO
     * 
     */
    public void update(MarksheetDTO DTO) throws ApplicationException, DuplicateRecordException
    {
    	log.debug("Model update Started");
    	Connection conn=null;
    	MarksheetDTO DTOExist=findByRollNo(DTO.getRollNo()); 
    	if(DTOExist!=null && DTOExist.getId()!=DTO.getId())
    	{
    		throw new DuplicateRecordException("Roll Number already exist");
    	}
    	
    	StudentModelInt sModel=ModelFactory.getInstance().getStudentModel();
    	StudentDTO studentDTO=sModel.findByPK(DTO.getStudentId());
    	DTO.setName(studentDTO.getFirstName()+" "+studentDTO.getLastName());
    	
    	try
    	{
    		conn=JDBCDataSource.getConnection();
    		conn.setAutoCommit(false);
    		PreparedStatement pstmt=conn.prepareStatement("UPDATE ST_MARKSHEET SET ROLL_NO=?,STUDENT_ID=?,NAME=?,PHYSICS=?,CHEMISTRY=?,MATHS=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
    		pstmt.setString(1, DTO.getRollNo());
            pstmt.setLong(2, DTO.getStudentId());
            pstmt.setString(3, DTO.getName());
            pstmt.setInt(4, DTO.getPhysics());
            pstmt.setInt(5, DTO.getChemistry());
            pstmt.setInt(6, DTO.getMaths());
            pstmt.setString(7, DTO.getCreatedBy());
            pstmt.setString(8, DTO.getModifiedBy());
            pstmt.setTimestamp(9, DTO.getCreatedDateTime());
            pstmt.setTimestamp(10, DTO.getModifiedDateTime());
            pstmt.setLong(11, DTO.getId());
            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();			
		}
    	catch (Exception e)
    	{
			log.error("Database Exception",e);
			try
			{
				conn.rollback();				
			}
			catch (Exception ex)
			{
				throw new ApplicationException("update rollback exception"+ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Marksheet");
		}
    	finally
    	{
    		JDBCDataSource.closeConnection(conn);
    	}
    	log.debug("Model update End");
    }
    
    /**
     * Searches Marksheet
     * 
     * @param DTO
     *            : Search Parameters
     * 
     */
	public List search(MarksheetDTO DTO) throws ApplicationException
	{
		return search(DTO,0,0);
	}
    
    /**
     * Searches Marksheet with pagination
     * 
     * @return list : List of Marksheets
     * @param DTO
     *            : Search Parameters
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * 
     * 
     */
    public List search(MarksheetDTO DTO,int pageNo,int pageSize) throws ApplicationException
    {
    	log.debug("Model search Started");
    	Connection conn=null;
    	ArrayList list=new ArrayList();
    	StringBuffer sql=new StringBuffer("SELECT * FROM ST_MARKSHEET WHERE 1=1 ");
    	
    	if(DTO!=null)
    	{
    		if(DTO.getId()>0)
    		{
    			sql.append("AND id="+DTO.getId());
    		}
    		if(DTO.getRollNo()!=null && DTO.getRollNo().length()>0)
    		{
    			sql.append("AND ROLL_NO LIKE '"+DTO.getRollNo()+"%'");
    		}
    		if (DTO.getStudentId() > 0) 
            {
                sql.append("AND STUDENT_ID LIKE '"+DTO.getStudentId()+"%'");
            }
    		if(DTO.getName()!=null && DTO.getName().length()>0)
    		{
    			sql.append("AND NAME LIKE '"+DTO.getName()+"%'");
    		}
            if (DTO.getPhysics()!=null && DTO.getPhysics()>0)
            {
                sql.append("AND physics="+DTO.getPhysics());
            }
            if (DTO.getChemistry()!=null && DTO.getChemistry()>0)
            {
                sql.append("AND chemistry="+DTO.getChemistry());
            }
            if (DTO.getMaths()!=null && DTO.getMaths()>0)
            {
                sql.append("AND maths="+DTO.getMaths());
            }
            
    	}
    	
    	if(pageSize>0)
    	{
    		pageNo=(pageNo-1)*pageSize;
    		sql.append("LIMIT "+pageNo+","+pageSize);
    	}
    	
    	try
    	{
    		conn=JDBCDataSource.getConnection();
    		PreparedStatement pstmt=conn.prepareStatement(sql.toString());
    		ResultSet rs=pstmt.executeQuery();
    		while(rs.next())
    		{
                DTO = new MarksheetDTO();
                DTO.setId(rs.getLong(1));
                DTO.setRollNo(rs.getString(2));
                DTO.setStudentId(rs.getLong(3));
                DTO.setName(rs.getString(4));
                DTO.setPhysics(rs.getInt(5));
                DTO.setChemistry(rs.getInt(6));
                DTO.setMaths(rs.getInt(7));
                DTO.setCreatedBy(rs.getString(8));
                DTO.setModifiedBy(rs.getString(9));
                DTO.setCreatedDateTime(rs.getTimestamp(10));
                DTO.setModifiedDateTime(rs.getTimestamp(11));
                list.add(DTO);
    		}
			rs.close();
		}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		log.error("Database Exception",e);
    		throw new ApplicationException("Update rollback exception "+e.getMessage());		
		}
    	finally
    	{
    		JDBCDataSource.closeConnection(conn);
    	}
    	log.debug("Model search End");
        return list;
    	
    }
    
    /**
     * Gets List of Marksheet
     * 
     * @return list : List of Marksheets
     * 
     */
    public List list() throws ApplicationException
    {
        return list(0, 0);
    }
     
    /**
     * get List of Marksheet with pagination
     * 
     * @return list : List of Marksheets
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * 
     */
    public List list(int pageNo, int pageSize) throws ApplicationException
    {
    	log.debug("Model list Started");
    	Connection conn=null;
    	ArrayList list=new ArrayList();
    	StringBuffer sql=new StringBuffer("SELECT * FROM ST_MARKSHEET ");
    	if(pageSize>0)
    	{
    		pageNo=(pageNo-1)*pageSize;
    		sql.append("LIMIT "+pageNo+","+pageSize);
    	}
    	
    	try
    	{
    		conn=JDBCDataSource.getConnection();
    		PreparedStatement pstmt=conn.prepareStatement(sql.toString());
    		ResultSet rs=pstmt.executeQuery();
    		while(rs.next())
    		{
    			MarksheetDTO DTO=new MarksheetDTO();
                DTO.setId(rs.getLong(1));
                DTO.setRollNo(rs.getString(2));
                DTO.setStudentId(rs.getLong(3));
                DTO.setName(rs.getString(4));
                DTO.setPhysics(rs.getInt(5));
                DTO.setChemistry(rs.getInt(6));
                DTO.setMaths(rs.getInt(7));
                DTO.setCreatedBy(rs.getString(8));
                DTO.setModifiedBy(rs.getString(9));
                DTO.setCreatedDateTime(rs.getTimestamp(10));
                DTO.setModifiedDateTime(rs.getTimestamp(11));
                list.add(DTO);
    		}
			rs.close();
		}
    	catch (Exception e)
    	{
    		log.error("Database Exception",e);
    		throw new ApplicationException("Exception in getting list of Marksheet");
		}
    	finally
    	{
    		JDBCDataSource.closeConnection(conn);			
		}
    	log.debug("Model list End");
    	return list;    	
    	
    }
    
    /**
     * get Merit List of Marksheet with pagination
     * 
     * @return list : List of Marksheets
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * 
     */
    public List getMeritList(int pageNo, int pageSize) throws ApplicationException
    {
    	log.debug("Model getMeritList Started");
    	ArrayList list=new ArrayList();
    	Connection conn=null;
    	StringBuffer sql=new StringBuffer("SELECT ID, ROLL_NO , NAME , PHYSICS , CHEMISTRY , MATHS , (PHYSICS + CHEMISTRY + MATHS) as total from ST_MARKSHEET WHERE (PHYSICS>33 AND CHEMISTRY>33 AND MATHS>33) ORDER BY TOTAL DESC");
    	if(pageSize>0)
    	{
    		pageNo=(pageNo-1)*pageSize;
    		sql.append(" LIMIT "+pageNo+","+pageSize);
    	}
    	
    	try
    	{
    		conn=JDBCDataSource.getConnection();
    		PreparedStatement pstmt=conn.prepareStatement(sql.toString());
    		ResultSet rs=pstmt.executeQuery();
    		while(rs.next())
    		{
    			 MarksheetDTO DTO = new MarksheetDTO();
                 DTO.setId(rs.getLong(1));
                 DTO.setRollNo(rs.getString(2));
                 DTO.setName(rs.getString(3));
                 DTO.setPhysics(rs.getInt(4));
                 DTO.setChemistry(rs.getInt(5));
                 DTO.setMaths(rs.getInt(6));
                 list.add(DTO);
    		}
    		rs.close();			
		}
    	catch (Exception e)
    	{
    		log.error("Database Exception",e);
    		throw new ApplicationException("Exception in getting merit list of Marksheet");		
		}
    	finally
    	{
    		JDBCDataSource.closeConnection(conn);
    	}
    	log.debug("Model getMeritList End");
    	return list;
    }
	
}
