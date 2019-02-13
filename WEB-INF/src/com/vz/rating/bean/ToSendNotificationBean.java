package com.vz.rating.bean;

public class ToSendNotificationBean {

	private String flatNumber;
	private int societyId;
	private int visitorId;
	private String visitorPurpose;
	private String inTime;
	private String gcmId;
	private String deviceId;
	
	
	public int getSocietyId() {
		return societyId;
	}
	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}
	public String getGcmId() {
		return gcmId;
	}
	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getFlatNumber() {
		return flatNumber;
	}
	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}
	public int getVisitorId() {
		return visitorId;
	}
	public void setVisitorId(int visitorId) {
		this.visitorId = visitorId;
	}
	public String getVisitorPurpose() {
		return visitorPurpose;
	}
	public void setVisitorPurpose(String visitorPurpose) {
		this.visitorPurpose = visitorPurpose;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

}
