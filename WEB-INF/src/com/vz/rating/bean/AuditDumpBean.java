package com.vz.rating.bean;

public class AuditDumpBean {

	private int ipAddress;
	private String actionName;
	private String jsonParam;
	public int getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(int ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getJsonParam() {
		return jsonParam;
	}
	public void setJsonParam(String jsonParam) {
		this.jsonParam = jsonParam;
	}
	
	
}
