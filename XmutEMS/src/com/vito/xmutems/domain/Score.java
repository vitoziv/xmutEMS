package com.vito.xmutems.domain;

import org.json.JSONObject;

public class Score {
	private String year;
	private String semester;
	private String name;
	private String type;
	private String score;
	private String credits;
	private String gradePoint;
	
	private String admissionForm;//准考证

	
	public Score(String year, String semester, String name, String type,
			String score, String credits, String gradePoint,
			String admissionForm) {
		super();
		this.year = year;
		this.semester = semester;
		this.name = name;
		this.type = type;
		this.score = score;
		this.credits = credits;
		this.gradePoint = gradePoint;
		this.admissionForm = admissionForm;
	}
	
	//解析json数据
	public Score(JSONObject jsonNotification) {
		year = jsonNotification.optString("year");
		semester = jsonNotification.optString("semester");
		name = jsonNotification.optString("name");
		type = jsonNotification.optString("type");
		score = jsonNotification.optString("score");
		credits = jsonNotification.optString("credits");
		gradePoint = jsonNotification.optString("gradePoint");
		admissionForm = jsonNotification.optString("admissionForm");
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCredits() {
		return credits;
	}
	public void setCredits(String credits) {
		this.credits = credits;
	}
	public String getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(String gradePoint) {
		this.gradePoint = gradePoint;
	}
	public String getAdmissionForm() {
		return admissionForm;
	}
	public void setAdmissionForm(String admissionForm) {
		this.admissionForm = admissionForm;
	}
	
}
