package vztrack.gls.com.vztrack_user.beans;

public class UserBean {
	String user_name;
	String user_password;
	String user_dev_id;
	String user_gcm_id;
	String device_os;


	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getUser_dev_id() {
		return user_dev_id;
	}

	public void setUser_dev_id(String user_dev_id) {
		this.user_dev_id = user_dev_id;
	}

	public String getUser_gcm_id() {
		return user_gcm_id;
	}

	public void setUser_gcm_id(String user_gcm_id) {
		this.user_gcm_id = user_gcm_id;
	}

	public String getDevice_os() {
		return device_os;
	}

	public void setDevice_os(String device_os) {
		this.device_os = device_os;
	}
}
