package in.co.rays.ORSProj3.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.ORSProj3.dto.CollegeDTO;
import in.co.rays.ORSProj3.dto.UserDTO;
import in.co.rays.ORSProj3.exception.ApplicationException;
import in.co.rays.ORSProj3.exception.DatabaseException;
import in.co.rays.ORSProj3.exception.DuplicateRecordException;
import in.co.rays.ORSProj3.exception.RecordNotFoundException;
import in.co.rays.ORSProj3.util.EmailBuilder;
import in.co.rays.ORSProj3.util.EmailMessage;
import in.co.rays.ORSProj3.util.EmailUtility;
import in.co.rays.ORSProj3.util.HibDataSource;

/**
 * Hibernate Implementation of User Model
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 */
public class UserModelHibImpl implements UserModelInt {

	Logger log = Logger.getLogger(UserModelHibImpl.class);

    /**
     * Add a user
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : throws when user already exists
     */
	public long add(UserDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("UserModelHibImpl method add started");
		long pk = 0;
		System.out.println("login"+dto.getLogin());
		UserDTO duplicateUser = findByLogin(dto.getLogin());

		if (duplicateUser != null) {
			throw new DuplicateRecordException("LoginId already exist");
		}

		Session session = HibDataSource.getSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			session.save(dto);
			pk = dto.getId();
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("Database Exception..", e);

			if (transaction != null) {
				transaction.rollback();
			}
			throw new ApplicationException("Exception : Exception in add User" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method add ended");

		return pk;
	}

    /**
     * Delete a user
     *
     * @param dto
     * @throws ApplicationException
     */
	public void delete(UserDTO dto) throws ApplicationException {

		log.debug("UserModelHibImpl method delete started");

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibDataSource.getSession();
			transaction = session.beginTransaction();

			session.delete(dto);

			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("Database Exception..", e);

			if (transaction != null) {
				transaction.rollback();
			}

			throw new ApplicationException("Exception in delete User" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method delete ended");
	}

    /**
     * Find user by login
     *
     * @param login
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
	public UserDTO findByLogin(String login) throws ApplicationException {

		log.debug("UserModelHibImpl method findByLogin started");

		Session session = null;
		UserDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);
			criteria.add(Restrictions.eq("login", login));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (UserDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting User by login" + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method findByLogin ended");

		return dto;
	}

    /**
     * Find user by PK
     *
     * @param pk
     *            : get parameter
     * @return dto
     * @throws ApplicationException
     */
	public UserDTO findByPK(long pk) throws ApplicationException {

		log.debug("UserModelHibImpl method findByPK started");
		Session session = null;
		UserDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (UserDTO) session.get(UserDTO.class, pk);
		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in getting User by PK" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method findByPK ended");

		return dto;
	}

    /**
     * Update a User
     *
     * @param dto
     * @throws ApplicationException
     * @throws DuplicateRecordException
     *             : if updated user record is already exist
     */
	public void update(UserDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("UserModelHibImpl method update started");

		Session session = null;
		Transaction transaction = null;

		UserDTO dtoExist = findByLogin(dto.getLogin());

		if (dtoExist != null && dtoExist.getId() != dto.getId()) {
			throw new DuplicateRecordException("LoginId already exist");
		}

		try {
			session = HibDataSource.getSession();
			transaction = session.beginTransaction();
			session.update(dto);
			transaction.commit();
		} catch (HibernateException e) {
			log.error("Database Exception..", e);
			e.printStackTrace();

			if (transaction != null) {
				transaction.rollback();
			}
			throw new ApplicationException("Exception in updating User" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method update ended");

	}

    /**
     * Search Users
     *
     * @return list : List of Users
     * @param dto
     *            : Search Parameters
     * @throws ApplicationException
     */
	public List search(UserDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

    /**
     * Search Users with pagination
     *
     * @return list : List of Users
     * @param dto
     *            : Search Parameters
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List search(UserDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("UserModelHibImpl method search started");

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);

			if (dto.getId() > 0) {
				criteria.add(Restrictions.eq("id", dto.getId()));
			}
			if (dto.getFirstName() != null && dto.getFirstName().length() > 0) {
				criteria.add(Restrictions.like("firstName", dto.getFirstName() + "%"));
			}
			if (dto.getLastName() != null && dto.getLastName().length() > 0) {
				criteria.add(Restrictions.like("lastName", dto.getLastName() + "%"));
			}
			if (dto.getDob() != null && dto.getDob().getDate() > 0) {
				criteria.add(Restrictions.eq("dob", dto.getDob()));
			}
			if (dto.getLogin() != null && dto.getLogin().length() > 0) {
				criteria.add(Restrictions.like("login", dto.getLogin() + "%"));
			}
			if (dto.getMobileNo() != null && dto.getMobileNo().length() > 0) {
				criteria.add(Restrictions.like("mobileNo", dto.getMobileNo() + "%"));
			}
			if (dto.getRoleId() > 0) {
				criteria.add(Restrictions.eq("roleId", dto.getRoleId()));
			}

			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in search User" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method search ended");

		return list;
	}

    /**
     * Get List of Users
     *
     * @return list : List of Users
     * @throws DatabaseException
     */
	public List list() throws ApplicationException {

		return list(0, 0);
	}

    /**
     * Get List of Users with pagination
     *
     * @return list : List of Users
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws ApplicationException
     */
	public List list(int pageNo, int pageSize) throws ApplicationException {

		log.debug("UserModelHibImpl method list started");

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);

				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in User List");
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method list ended");
		return list;
	}

    /**
     * Change Password By pk
     *
     * @param pk
     *            ,oldPassword,newPassword : get parameter
     * @return dto
     * @throws ApplicationException
     * @throws RecordNotFoundException
     */
	public boolean changePassword(Long id, String oldPassword, String newPassword)
			throws RecordNotFoundException, ApplicationException {

		log.debug("UserModelHibImpl method chanagePassword started");

		boolean flag = false;
		UserDTO dtoExist = findByPK(id);
		if (dtoExist != null && dtoExist.getPassword().equals(oldPassword)) {
			dtoExist.setPassword(newPassword);
			try
			{
				update(dtoExist);
			}
			catch (DuplicateRecordException e)
			{
				e.printStackTrace();
				log.error("Duplicate Record Exception..", e);
				throw new ApplicationException("LoginId already exist");
			}
			flag = true;
		} 
		else
		{
			throw new RecordNotFoundException("LoginId does not exist");
		}

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("login", dtoExist.getLogin());
		map.put("password", dtoExist.getPassword());
		map.put("firstName", dtoExist.getFirstName());
		map.put("lastName", dtoExist.getLastName());

		String message = EmailBuilder.getChangePasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dtoExist.getLogin());
		msg.setSubject("SUNRAYS ORS Password has been changed successfully");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		log.debug("UserModelHibImpl method chanagePassword ended");

		return flag;
	}

	/**
     * User Authentication
     *
     * @return dto : Contains User's information
     * @param login
     *            : User Login
     * @param password
     *            : User Password
     * @throws ApplicationException
     */
	public UserDTO authenticate(String login, String password) throws ApplicationException {

		log.debug("UserModelHibImpl method authenticate started");

		Session session = null;
		UserDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);

			criteria.add(Restrictions.eq("login", login));
			criteria.add(Restrictions.eq("password", password));

			List list = criteria.list();

			if (list.size() > 0) {
				dto = (UserDTO) list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in authenticate user" + e.getMessage());
		} finally {
			session.close();
		}

		log.debug("UserModelHibImpl method authenticate ended");
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
	public boolean lock(String login) throws RecordNotFoundException, ApplicationException {

		log.debug("UserModelHibImpl method lock started");

		boolean flag = false;
		UserDTO dto = null;

		try {
			dto = findByLogin(login);

			if (dto != null) 
			{
				dto.setLock(UserDTO.ACTIVE);
			} 
			else
			{
				throw new RecordNotFoundException("LoginId does not exist");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error("Application Exception..", e);
			throw new ApplicationException("Exception in lock user" + e.getMessage());
		}

		log.debug("UserModelHibImpl method lock ended");

		return flag;
	}

    /**
     * Get User Roles
     *
     * @return List : User Role List
     * @param dto
     * @throws ApplicationException
     */
	public List getRoles(UserDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Update User access
     *
     * @return dto
     * @param dto
     * @throws ApplicationException
     */
	public UserDTO updateAccess(UserDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Register a User
     *
     * @param dto
     * @return
     * @throws ApplicationException
     * @throws DuplicateRecordException
     */
	public long registerUser(UserDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("UserModelHibImpl method registerUser started");

		long pk = add(dto);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", dto.getLogin());
		map.put("password", dto.getPassword());

		String message = EmailBuilder.getUserRegistrationMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dto.getLogin());
		msg.setSubject("Registration is successful for ORS Project SUNRAYS Technologies");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		log.debug("UserModelHibImpl method registerUser ended");

		return pk;

	}

    /**
	 * Reset Password of User with auto generated Password
	 * 
	 * @return boolean : true if success otherwise false
	 * 
	 *            : User Login
	 * @throws ApplicationException
	 * 
	 */
	public boolean resetPassword(UserDTO dto) throws ApplicationException {

		log.debug("UserModelHibImpl method resetPassword started");

		String newPassword = String.valueOf(new Date().getTime()).substring(0, 4);
		boolean flag = false;
		UserDTO userData = findByPK(dto.getId());
		userData.setPassword(newPassword);

		try 
		{
			update(userData);
		}
		catch (DuplicateRecordException e) 
		{
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception in resetPassword User");
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", dto.getLogin());
		map.put("password", dto.getPassword());

		String message = EmailBuilder.getForgetPasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dto.getLogin());
		msg.setSubject("Password has been reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		flag =true;

		EmailUtility.sendMail(msg);

		log.debug("UserModelHibImpl method resetPassword ended");
		return flag;
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
	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {

		log.debug("UserModelHibImpl method forgetPassword started");

		UserDTO userData = findByLogin(login);

		boolean flag = false;

		if (userData == null) {
			throw new RecordNotFoundException("LoginId does not exist.");

		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLogin());
		map.put("password", userData.getPassword());
		map.put("firstName", userData.getFirstName());
		map.put("lastName", userData.getLastName());

		String message = EmailBuilder.getForgetPasswordMessage(map);

		EmailMessage msg = new EmailMessage();
		msg.setTo(login);
		msg.setSubject("SUNARYS ORS Password reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		EmailUtility.sendMail(msg);
		flag = true;

		log.debug("UserModelHibImpl method forgetPassword ended");

		return flag;

	}

}
