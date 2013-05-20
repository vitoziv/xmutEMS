package com.vito.xmutems.domain;

import org.json.JSONObject;

public class Course {
	private String courseName;
	private String detail;
	private String teacher;
	private String room;
	private String dayOfWeek;
	private String lessonsBegin;
	private String lessonsEnd;
	
	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Course(String courseName, String detail, String teacher,
			String room, String dayOfWeek, String lessonsBegin,
			String lessonsEnd) {
		super();
		this.courseName = courseName;
		this.detail = detail;
		this.teacher = teacher;
		this.room = room;
		this.dayOfWeek = dayOfWeek;
		this.lessonsBegin = lessonsBegin;
		this.lessonsEnd = lessonsEnd;
	}
	
	//解析json数据
	public Course(JSONObject jsonNotification) {
		courseName = jsonNotification.optString("courseName");
		detail = jsonNotification.optString("detail");
		teacher = jsonNotification.optString("teacher");
		room = jsonNotification.optString("room");
		dayOfWeek = jsonNotification.optString("dayOfWeek");
		lessonsBegin = jsonNotification.optString("lessonsBegin");
		lessonsEnd = jsonNotification.optString("lessonsEnd");
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getLessonsBegin() {
		return lessonsBegin;
	}
	public void setLessonsBegin(String lessonsBegin) {
		this.lessonsBegin = lessonsBegin;
	}
	public String getLessonsEnd() {
		return lessonsEnd;
	}
	public void setLessonsEnd(String lessonsEnd) {
		this.lessonsEnd = lessonsEnd;
	}
	
}
