package in.co.rays.ORSProj3.dto;

/**
 * Subject JavaBean encapsulates Subject attributes
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */
public class SubjectDTO extends BaseDTO
{
	/**
     * Name of Subject
     */
	private String subjectName;
	
	/**
     * Description of Subject
     */
	private String description;
	
    /**
     * CourseId of Subject
     */
	private long courseId;
	
    /**
     * Course Name of Subject
     */
	private String courseName;
	
    /**
     * accessor
     */
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
     * Returns key of list element
     * 
     * @return key
     */
	public String getKey() {

		return id+"";
	}

	/**
     * Returns display text of list element
     * 
     * @return value
     */
	public String getValue() {
		
		return subjectName;
	}

	
	
}
