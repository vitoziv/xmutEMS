package com.vito.xmutems.domain;

import org.json.JSONObject;

public class Exam {
	private String courseName;
	private String stuName;
	private String examDate;
	private String location;
	private String sitNumber;
	private String school;
	public Exam(JSONObject jsonObject) {
		courseName = jsonObject.optString("courseName");
		stuName = jsonObject.optString("stuName");
		examDate = jsonObject.optString("examDate");
		location = jsonObject.optString("location");
		sitNumber = jsonObject.optString("sitNumber");
		school = jsonObject.optString("school");
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getExamDate() {
		return examDate;
	}
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSitNumber() {
		return sitNumber;
	}
	public void setSitNumber(String sitNumber) {
		this.sitNumber = sitNumber;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	
}
