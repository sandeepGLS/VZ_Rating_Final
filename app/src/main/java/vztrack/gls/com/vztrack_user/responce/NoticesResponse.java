package vztrack.gls.com.vztrack_user.responce;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.Socity;

/**
 * Created by sandeep on 17/3/16.
 */
public class NoticesResponse {
    String message;
    String code;
    ArrayList<Notices> notices;

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

    public ArrayList<Notices> getNotices() {
        return notices;
    }

    public void setNotices(ArrayList<Notices> notices) {
        this.notices = notices;
    }
}
