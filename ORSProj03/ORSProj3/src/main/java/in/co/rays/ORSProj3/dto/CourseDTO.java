package in.co.rays.ORSProj3.dto;

/**
 * Course JavaBean encapsulates Course attributes
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */
public class CourseDTO extends BaseDTO
{

    /**
     * Name of Course
     */
	private String name;
	
    /**
     * Description of Course
     */
	private String description;
	
    /**
     * Duration of Course
     */
	private String duration;
	
    /**
     * accessor
     */	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public String getDuration() 
	{
		return duration;
	}

	public void setDuration(String duration) 
	{
		this.duration = duration;
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
