package vztrack.gls.com.vztrack_user.beans;

public class NotificationBean {

	private String gcmId;
	private String deviceOs;
	private String visitorName;
	private String visitorMobile;
	private String visitorPurpose;
	private String visitorPhoto;
	private String inTime;

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public String getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	public String getVisitorPhoto() {
		return visitorPhoto;
	}

	public void setVisitorPhoto(String visitorPhoto) {
		this.visitorPhoto = visitorPhoto;
	}

	public String getGcmId() {
		return gcmId;
	}
	public String getVisitorMobile() {
		return visitorMobile;
	}
	public void setVisitorMobile(String visitorMobile) {
		this.visitorMobile = visitorMobile;
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
