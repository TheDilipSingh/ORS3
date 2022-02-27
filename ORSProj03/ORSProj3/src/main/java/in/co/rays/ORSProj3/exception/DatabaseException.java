package in.co.rays.ORSProj3.exception;

/**
 * DatabaseException is propogated by DAO classes when an unhandled Database
 * exception occurred
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */

public class DatabaseException extends Exception {

	/**
     * @param msg
     *            : Error message
     */
	public DatabaseException(String msg)
	{
		super(msg);
	}
}
