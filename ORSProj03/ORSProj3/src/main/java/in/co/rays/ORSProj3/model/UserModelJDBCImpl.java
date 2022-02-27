package in.co.rays.ORSProj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.exception.RecordNotFoundException;
import in.co.rays.ORSProj3.util.EmailBuilder;
import in.co.rays.ORSProj3.util.EmailMessage;
import in.co.rays.ORSProj3.util.EmailUtility;
import in.co.rays.ORSProj3.util.JDBCDataSource;

/**
 * JDBC Implementation of UserModel
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class UserModelJDBCImpl implements UserModelInt {
	
	private static Logger log=Logger.getLogger(UserModelJDBCImpl.class);
	
	
	/**
	 * Find next PK of User
	 * @return nextPK
	 * @throws DatabaseException
	 */
	public Integer nextPK() throws DatabaseException
	{
		log.debug("Model nextPK Started");
		Connection conn=null;
		int pk=0;
		
        try
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_USER");
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
	 * Add a user
	 * 
	 * @param dto
	 * @return PK
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(UserDTO dto) throws ApplicationException, DuplicateRecordException
	{
		log.debug("Model add Started");
		int pk=0;
		Connection conn=null;
		
		UserDTO existdto = findByLogin(dto.getLogin());
		
		if(existdto != null)
		{
			throw new DuplicateRecordException("Login id already exist");
		}
		
        try
        {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            conn.setAutoCommit(false); 
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ST_USER VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, pk);
            pstmt.setString(2, dto.getFirstName());
            pstmt.setString(3, dto.getLastName());
            pstmt.setString(4, dto.getLogin());
            pstmt.setString(5, dto.getPassword());
            pstmt.setDate(6, new java.sql.Date(dto.getDob().getTime()));
            pstmt.setString(7, dto.getMobileNo());
            pstmt.setLong(8, dto.getRoleId());
            pstmt.setInt(9, dto.getUnSuccessfulLogin());
            pstmt.setString(10, dto.getGender());
            pstmt.setTimestamp(11, dto.getLastLogin());
            pstmt.setString(12, dto.getLock());
            pstmt.setString(13, dto.getRegisteredIP());
            pstmt.setString(14, dto.getLastLoginIP());
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
            log.error("Database Exception..", e);
            try 
            {
                conn.rollback();
            }
            catch (Exception ex)
            {
            	e.printStackTrace();
                throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
            }
            e.printStackTrace();
            throw new ApplicationException("Exception : Exception in add User");
        }
        finally
        {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model add End");
        return pk;

	}
	
	
    /**
     * Delete a user
     * 
     * @param dto
     * @throws ApplicationException
     */
    public void delete(UserDTO dto) throws ApplicationException 
    {
        log.debug("Model delete Started");
        Connection conn = null;
        try
        {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false); 
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ST_USER WHERE ID=?");
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
            throw new ApplicationException("Exception : Exception in delete User");
        }
        finally 
        {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model delete Started");
    }
    
    
    /**
     * Find user by login
     * 
     * @param login
     * 				: get parameter
     * @return dto
     * @throws ApplicationException
     */
    public UserDTO findByLogin(String login) throws ApplicationException
    {
        log.debug("Model findByLogin Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE LOGIN=?");
        UserDTO dto = null;
        Connection conn = null;

        try 
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                dto = new UserDTO();
                dto.setId(rs.getLong(1));
                dto.setFirstName(rs.getString(2));
                dto.setLastName(rs.getString(3));
                dto.setLogin(rs.getString(4));
                dto.setPassword(rs.getString(5));
                dto.setDob(rs.getDate(6));
                dto.setMobileNo(rs.getString(7));
                dto.setRoleId(rs.getLong(8));
                dto.setUnSuccessfulLogin(rs.getInt(9));
                dto.setGender(rs.getString(10));
                dto.setLastLogin(rs.getTimestamp(11));
                dto.setLock(rs.getString(12));
                dto.setRegisteredIP(rs.getString(13));
                dto.setLastLoginIP(rs.getString(14));
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
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception : Exception in getting User by login");
        }
        finally 
        {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByLogin End");
        return dto;
    }

	
    /**
     * Find user by PK
     * 
     * @param pk
     * 			 : get parameter
     * @return dto
     * @throws ApplicationException
     */
    public UserDTO findByPK(long pk) throws ApplicationException 
    {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE ID=?");
        UserDTO dto = null;
        Connection conn = null;

        try 
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                dto = new UserDTO();
                dto.setId(rs.getLong(1));
                dto.setFirstName(rs.getString(2));
                dto.setLastName(rs.getString(3));
                dto.setLogin(rs.getString(4));
                dto.setPassword(rs.getString(5));
                dto.setDob(rs.getDate(6));
                dto.setMobileNo(rs.getString(7));
                dto.setRoleId(rs.getLong(8));
                dto.setUnSuccessfulLogin(rs.getInt(9));
                dto.setGender(rs.getString(10));
                dto.setLastLogin(rs.getTimestamp(11));
                dto.setLock(rs.getString(12));
                dto.setRegisteredIP(rs.getString(13));
                dto.setLastLoginIP(rs.getString(14));
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
     * Update a user
     * 
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public void update(UserDTO dto) throws ApplicationException, DuplicateRecordException
    {
    	log.debug("Model update Started");
    	Connection conn = null;
    	UserDTO dtoExist = findByLogin(dto.getLogin());
    	
    	if (dtoExist != null && !(dtoExist.getId() == dto.getId()))
    	{
    		throw new DuplicateRecordException("LoginId is already exist");
    	}

    	try
    	{
    		conn = JDBCDataSource.getConnection();
    		conn.setAutoCommit(false); 
    		PreparedStatement pstmt = conn.prepareStatement("UPDATE ST_USER SET FIRST_NAME=?,LAST_NAME=?,LOGIN=?,PASSWORD=?,DATE_OF_BIRTH=?,MOBILE_NO=?,ROLE_ID=?,UNSUCCESSFUL_LOGIN=?,GENDER=?,LAST_LOGIN=?,USER_LOCK=?,REGISTERED_IP=?,LAST_LOGIN_IP=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
    		pstmt.setString(1, dto.getFirstName());
    		pstmt.setString(2, dto.getLastName());
    		pstmt.setString(3, dto.getLogin());
    		pstmt.setString(4, dto.getPassword());
    		pstmt.setDate(5, new java.sql.Date(dto.getDob().getTime()));
    		pstmt.setString(6, dto.getMobileNo());
    		pstmt.setLong(7, dto.getRoleId());
    		pstmt.setInt(8, dto.getUnSuccessfulLogin());
    		pstmt.setString(9, dto.getGender());
    		pstmt.setTimestamp(10, dto.getLastLogin());
    		pstmt.setString(11, dto.getLock());
    		pstmt.setString(12, dto.getRegisteredIP());
    		pstmt.setString(13, dto.getLastLoginIP());
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
    		log.error("Database Exception..", e);
    		try 
    		{
    			conn.rollback();
    		}
    		catch (Exception ex) 
    		{
    			e.printStackTrace();
    			throw new ApplicationException("Exception : Delete rollback exception "+ ex.getMessage());
    		}
    		throw new ApplicationException("Exception in updating User ");
    	}
    	finally 
    	{
    		JDBCDataSource.closeConnection(conn);
    	}	
    	log.debug("Model update End");
    }
    
    
    /**
     * Search user
     * 
     * @param dto
     * 				: search parameter
     * @return list : List of users
     * @throws ApplicationException
     */
    public List search(UserDTO dto) throws ApplicationException 
    {
        return search(dto, 0, 0);
    }
    
    /**
     * Search user with pagination
     * 
     * @param dto
     * @param pageNo : Current Page No.
     * @param pageSize : Size of Page
     * @return list : List of users
     * @throws ApplicationException
     */
    public List search(UserDTO dto,int pageNo, int pageSize) throws ApplicationException
    {
    	log.debug("Model search Started");
    	Connection conn=null;
    	ArrayList list=new ArrayList();
    	StringBuffer sql=new StringBuffer("SELECT * FROM ST_USER WHERE 1=1 ");
    	
    	if(dto!=null)
    	{
    		if(dto.getId()>0)
    		{
    			sql.append(" AND id="+dto.getId());
    		}
    		if(dto.getFirstName()!=null && dto.getFirstName().length() > 0)
    		{
    			sql.append(" AND FIRST_NAME LIKE'"+dto.getFirstName()+"%'");
    		}
            if (dto.getLastName() != null && dto.getLastName().length() > 0) 
            {
                sql.append(" AND LAST_NAME like '" + dto.getLastName() + "%'");
            }
            if (dto.getLogin() != null && dto.getLogin().length() > 0)
            {
                sql.append(" AND LOGIN like '" + dto.getLogin() + "%'");
            }
            if (dto.getPassword() != null && dto.getPassword().length() > 0)
            {
                sql.append(" AND PASSWORD like '" + dto.getPassword() + "%'");
            }
            if (dto.getDob() != null && dto.getDob().getDate() > 0) 
            {
                sql.append(" AND DOB = " + dto.getGender());
            }
            if (dto.getMobileNo() != null && dto.getMobileNo().length() > 0)
            {
                sql.append(" AND MOBILE_NO = " + dto.getMobileNo());
            }
            if (dto.getRoleId() > 0) 
            {
                sql.append(" AND ROLE_ID = " + dto.getRoleId());
            }
            if (dto.getUnSuccessfulLogin() > 0)
            {
                sql.append(" AND UNSUCCESSFUL_LOGIN = "+ dto.getUnSuccessfulLogin());
            }
            if (dto.getGender() != null && dto.getGender().length() > 0)
            {
                sql.append(" AND GENDER like '" + dto.getGender() + "%'");
            }
            if (dto.getLastLogin() != null && dto.getLastLogin().getTime() > 0)
            {
                sql.append(" AND LAST_LOGIN = " + dto.getLastLogin());
            }
            if (dto.getRegisteredIP() != null && dto.getRegisteredIP().length() > 0)
            {
                sql.append(" AND REGISTERED_IP like '" + dto.getRegisteredIP()+ "%'");
            }
            if (dto.getLastLoginIP() != null && dto.getLastLoginIP().length() > 0)
            {
                sql.append(" AND LAST_LOGIN_IP like '" + dto.getLastLoginIP()+ "%'");
            }
    	
    	}
    	
    	if(pageSize>0)
    	{
    		pageNo=(pageNo-1)*pageSize;
    		sql.append(" LIMIT "+pageNo+","+pageSize);
    	}
    	
        try 
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                dto = new UserDTO();
                dto.setId(rs.getLong(1));
                dto.setFirstName(rs.getString(2));
                dto.setLastName(rs.getString(3));
                dto.setLogin(rs.getString(4));
                dto.setPassword(rs.getString(5));
                dto.setDob(rs.getDate(6));
                dto.setMobileNo(rs.getString(7));
                dto.setRoleId(rs.getLong(8));
                dto.setUnSuccessfulLogin(rs.getInt(9));
                dto.setGender(rs.getString(10));
                dto.setLastLogin(rs.getTimestamp(11));
                dto.setLock(rs.getString(12));
                dto.setRegisteredIP(rs.getString(13));
                dto.setLastLoginIP(rs.getString(14));
                dto.setCreatedBy(rs.getString(15));
                dto.setModifiedBy(rs.getString(16));
                dto.setCreatedDateTime(rs.getTimestamp(17));
                dto.setModifiedDateTime(rs.getTimestamp(18));
                list.add(dto);
            }
            rs.close();
        } 
        catch (Exception e)
        {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception : Exception in search user");
        } 
        finally
        {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("Model search End");
        return list;  	
    	
    }
    
    /**
     * Get list of users
     * 
     * @return : list of users
     * @throws ApplicationException
     */
    public List list() throws ApplicationException
    {
    	return list(0,0);
    }
    
    
    /**
     * Get List of User with pagination
     * 
     * @param pageNo : Current Page No.
     * @param pageSize : Size of Page
     * @return list : List of users
     * @throws ApplicationException
     */
    public List list(int pageNo,int pageSize) throws ApplicationException
    {
    	log.debug("Model list Started");
    	ArrayList list=new ArrayList();
    	Connection conn=null;
    	StringBuffer sql=new StringBuffer("SELECT * FROM ST_USER");
    	
    	if(pageSize>0)
    	{
    		pageNo=(pageNo-1)*pageSize;
    		sql.append(" Limit "+pageNo+","+pageSize);
    	}
    	
        try 
        {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getLong(1));
                dto.setFirstName(rs.getString(2));
                dto.setLastName(rs.getString(3));
                dto.setLogin(rs.getString(4));
                dto.setPassword(rs.getString(5));
                dto.setDob(rs.getDate(6));
                dto.setMobileNo(rs.getString(7));
                dto.setRoleId(rs.getLong(8));
                dto.setUnSuccessfulLogin(rs.getInt(9));
                dto.setGender(rs.getString(10));
                dto.setLastLogin(rs.getTimestamp(11));
                dto.setLock(rs.getString(12));
                dto.setRegisteredIP(rs.getString(13));
                dto.setLastLoginIP(rs.getString(14));
                dto.setCreatedBy(rs.getString(15));
                dto.setModifiedBy(rs.getString(16));
                dto.setCreatedDateTime(rs.getTimestamp(17));
                dto.setModifiedDateTime(rs.getTimestamp(18));
                list.add(dto);
            }
            rs.close();
        }
        catch (Exception e) 
        {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception : Exception in getting list of users");
        }
        finally
        {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("Model list End");
        return list; 
   	
    }
    
    /**
     * Authenticate a user
     * 
     * @param login : String login
     * @param password : String Password
     * @return dto
     * @throws ApplicationException
     */
    public UserDTO authenticate(String login,String password) throws ApplicationException
    {
    	log.debug("Model authenticate Started");
    	StringBuffer sql=new StringBuffer("SELECT * FROM ST_USER WHERE LOGIN=? AND PASSWORD=?");
    	UserDTO dto=null;
    	Connection conn=null;
    	
    	try 
    	{
    		conn=JDBCDataSource.getConnection();
    		PreparedStatement pstmt=conn.prepareStatement(sql.toString());
    		pstmt.setString(1, login);
    		pstmt.setString(2, password);
    		ResultSet rs=pstmt.executeQuery();
    		
    		while(rs.next())
    		{
    			dto=new UserDTO();
                dto.setId(rs.getLong(1));
                dto.setFirstName(rs.getString(2));
                dto.setLastName(rs.getString(3));
                dto.setLogin(rs.getString(4));
                dto.setPassword(rs.getString(5));
                dto.setDob(rs.getDate(6));
                dto.setMobileNo(rs.getString(7));
                dto.setRoleId(rs.getLong(8));
                dto.setUnSuccessfulLogin(rs.getInt(9));
                dto.setGender(rs.getString(10));
                dto.setLastLogin(rs.getTimestamp(11));
                dto.setLock(rs.getString(12));
                dto.setRegisteredIP(rs.getString(13));
                dto.setLastLoginIP(rs.getString(14));
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
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting user");
		}
    	finally
    	{
    		JDBCDataSource.closeConnection(conn);
    	}
    	log.debug("Model authenticate End");
    	return dto;   	
    }
    
    /**
	 * Lock User for certain time duration
	 * 
	 * @return boolean : true if success otherwise false
	 * @param login
	 *            : User Login
	 * @throws ApplicationException
	 * @throws RecordNotFoundException
	 *             : if user not found
	 */
    public boolean lock(String login) throws ApplicationException, RecordNotFoundException
    {
    	log.debug("Service lock Started");
    	boolean flag=false;
    	UserDTO dtoExist=null;
    	
    	try
    	{
    		dtoExist=findByLogin(login);
    		if(dtoExist!=null)
    		{
    			dtoExist.setLock(UserDTO.ACTIVE);
    			update(dtoExist);
    			flag=true;
    		}
    		else
    		{
    			throw new RecordNotFoundException("LoginId does not exist");
    		}
			
		}
    	catch (DuplicateRecordException e)
    	{
    		log.error("Application Exception..",e);
    		throw new ApplicationException("Database Exception");	
		}
    	
    	log.debug("Service lock End");
    	return flag;
    }
    

	/**
	 * Get User Roles
	 * 
	 * @return List : User Role List
	 * @param dto
	 * @throws ApplicationException
	 */
    public List getRoles(UserDTO dto) throws ApplicationException
    {
        log.debug("Model get roles Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_USER WHERE role_Id=?");
        Connection conn = null;
        ArrayList list = new ArrayList();
        try
        {

            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, dto.getRoleId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) 
            {
                dto = new UserDTO();
                dto.setId(rs.getLong(1));
                dto.setFirstName(rs.getString(2));
                dto.setLastName(rs.getString(3));
                dto.setLogin(rs.getString(4));
                dto.setPassword(rs.getString(5));
                dto.setDob(rs.getDate(6));
                dto.setMobileNo(rs.getString(7));
                dto.setRoleId(rs.getLong(8));
                dto.setUnSuccessfulLogin(rs.getInt(9));
                dto.setGender(rs.getString(10));
                dto.setLastLogin(rs.getTimestamp(11));
                dto.setLock(rs.getString(12));
                dto.setRegisteredIP(rs.getString(13));
                dto.setLastLoginIP(rs.getString(14));
                dto.setCreatedBy(rs.getString(15));
                dto.setModifiedBy(rs.getString(16));
                dto.setCreatedDateTime(rs.getTimestamp(17));
                dto.setModifiedDateTime(rs.getTimestamp(18));
                list.add(dto);
            }
            rs.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception : Exception in get roles");

        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model get roles End");
        return list;
    }

    /**
     * @param id 
     * 			: long id
     * @param oldPassword
     * 						: String oldPassword
     * @param newPassword
     * 						: String newPassword
     * @return boolean
     * @throws ApplicationException
     * @throws RecordNotFoundException
     */
    public boolean changePassword(Long id,String oldPassword, String newPassword) throws ApplicationException, RecordNotFoundException
    {
    	log.debug("model changePassword Started");
    	
    	boolean flag=false;
    	UserDTO dtoExist=findByPK(id);
    	if(dtoExist!=null && dtoExist.getPassword().equals(oldPassword))
    	{
    		dtoExist.setPassword(newPassword);    	
    	try
    	{
			update(dtoExist);
		}
    	catch (DuplicateRecordException e)
    	{
    		e.printStackTrace();
			log.error("Duplicate Record Exception..",e);
			throw new ApplicationException("LoginId already exist");
		}
    	flag =true;
    	}
    	else
        {
        	throw new RecordNotFoundException("LoginId does not exist");
        }
    	
    	HashMap<String, String> map=new HashMap<String,String>();
    	
    	map.put("login", dtoExist.getLogin());
    	map.put("password",dtoExist.getPassword());
    	map.put("firstName", dtoExist.getFirstName());
    	map.put("lastName", dtoExist.getLastName());
    	
    	String message=EmailBuilder.getChangePasswordMessage(map);
    	
    	EmailMessage msg=new EmailMessage();
    	
    	msg.setTo(dtoExist.getLogin());
    	msg.setSubject("SUNRAYS ORS Password has been changed successfully");
    	msg.setMessage(message);
    	msg.setMessageType(EmailMessage.HTML_MSG);
    	
    	EmailUtility.sendMail(msg);
    	
    	log.debug("Model changePassword End");
    	return flag;

    }
    
    
    public UserDTO updateAccess(UserDTO dto)
    {
    	return null;
    }
    
    
    /**
     * Register a user
     * 
     * @param dto
     * @return PK
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
    public long registerUser(UserDTO dto) throws ApplicationException, DuplicateRecordException
    {
    	log.debug("Model registerUser Started");
    	
    	long pk=add(dto);
    	
    	HashMap<String, String> map=new HashMap<String, String>();
    	map.put("login", dto.getLogin());
    	map.put("password", dto.getPassword());
    	
    	String message=EmailBuilder.getUserRegistrationMessage(map);
    	
    	EmailMessage msg=new EmailMessage();
    	
    	msg.setTo(dto.getLogin());
    	msg.setSubject("Registration is successful for ORS Project SunilOS");
    	msg.setMessage(message);
    	msg.setMessageType(EmailMessage.HTML_MSG);
    	
    	EmailUtility.sendMail(msg);
    	return pk;
    }
    
    
    /**
	 * Reset Password of User with auto generated Password
	 * 
	 * @return boolean : true if success otherwise false
	 * 
	 *            : User Login
	 * @throws ApplicationException
	 * @throws RecordNotFoundException
	 *             : if user not found
	 * 
	 */
    public boolean resetPassword(UserDTO dto) throws ApplicationException
    {
    	String newPassword=String.valueOf(new Date().getTime()).substring(0, 4);
    	UserDTO userData=findByPK(dto.getId());
    	userData.setPassword(newPassword);
    	
    	try
    	{
    		update(userData);
			
		}
    	catch (DuplicateRecordException e)
    	{
    		return false;		
		}
    	
    	HashMap<String, String> map=new HashMap<String, String>();
        map.put("login", dto.getLogin());
        map.put("password", dto.getPassword());
        map.put("firstName", dto.getFirstName());
        map.put("lastName", dto.getLastName());
        
        String message=EmailBuilder.getForgetPasswordMessage(map);
        
        EmailMessage msg=new EmailMessage();
        
        msg.setTo(dto.getLogin());
        msg.setSubject("Password has been reset");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);
        
        EmailUtility.sendMail(msg);
        
        return true;

    }
    
    /**
     * Send the password of User to his Email
     * 
     * @param login 
     * 				: User Login
     * @return boolean : true if success otherwise false
     * @throws ApplicationException
     * @throws RecordNotFoundException
     * 									: if user not found
     */
    public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException
    {
    	UserDTO userData=findByLogin(login);
    	boolean flag=false;
    	
    	if(userData==null)
    	{
    		throw new RecordNotFoundException("Email ID does not exist");
    	}
    	
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("login", userData.getLogin());
        map.put("password", userData.getPassword());
        map.put("firstName", userData.getFirstName());
        map.put("lastName", userData.getLastName());
        
        String message=EmailBuilder.getForgetPasswordMessage(map);
        
        EmailMessage msg=new EmailMessage();
        msg.setTo(login);
        msg.setSubject("SUNARYS ORS Password reset");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);
        
        EmailUtility.sendMail(msg);
        
        flag=true;
        
        return flag;
    	
    }
    

}
