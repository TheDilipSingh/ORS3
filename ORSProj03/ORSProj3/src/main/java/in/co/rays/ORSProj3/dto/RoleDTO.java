package in.co.rays.ORSProj3.dto;

/**
 * Role JavaBean encapsulates Role attributes
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */

public class RoleDTO extends BaseDTO {

    /**
     * Predefined Role constants
     */
    public static final int ADMIN = 1;
    public static final int STUDENT = 2;
    public static final int COLLEGE = 3;
    public static final int FACULTY = 4;
    public static final int KIOSK = 5;
	
    /**
     * Role Name
     */
	private String name;
	
    /**
     * Role Description
     */
	private String description;
	
    /**
     * accessor
     */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
     * Returns key of list element
     * 
     * @return key
     */
	public String getKey()
	{
		return id+"";
	}
	
	/**
     * Returns display text of list element
     * 
     * @return value
     */
	public String getValue()
	{
		return name;
	}
	
}
