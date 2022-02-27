package in.co.rays.ORSProj3.dto;

import java.util.Date;

/**
 * Faculty JavaBean encapsulates Faculty attributes
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */
public class FacultyDTO extends BaseDTO
{

    /**
     * First Name of Faculty
     */
	private String firstName;

    /**
     * Last Name of Faculty
     */
	private String lastName;
	
    /**
     * Gender of Faculty
     */
	private String Gender;
	
    /**
     * Login ID of Faculty
     */
	private String loginId;
	
	/**
     * Date of Joining of Faculty
     */
	private Date dateOfJoining;
	
	/**
     * Qualification of Faculty
     */
	private String qualification;
	
    /**
     * Mobile No of Faculty
     */
	private String mobileno;
	
    /**
     * CollegeID of Faculty
     */
	private long collegeId;
	
    /**
     * College Name of Faculty
     */
	private String collegeName;
	
    /**
     * CourseID of Faculty
     */
	private long courseId;
	
    /**
     * Course Name of Faculty
     */
	private String courseName;
	
    /**
     * SubjectID of Faculty
     */
	private long subjectId;
	
    /**
     * Subject Name of Faculty
     */
	private String subjectName;
	
    /**
     * accessor
     */
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public long getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
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

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	/**
     * Returns display text of list element
     * 
     * @return value
     */
	public String getValue() {
	
		return id+"";
	}

	/**
     * Returns key of list element
     * 
     * @return key
     */
	public String getKey() {

		return firstName+""+lastName;
	}
	
	
	
}
