package vztrack.gls.com.vztrack_user.responce;

import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.Socity;

/**
 * Created by sandeep on 17/3/16.
 */
public class LogoutResponse {
    String message;
    String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
