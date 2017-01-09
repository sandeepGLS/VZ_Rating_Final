package vztrack.gls.com.vztrack_user.responce;

import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.Socity;
import vztrack.gls.com.vztrack_user.profile.User;

/**
 * Created by sandeep on 17/3/16.
 */
public class LoginResponse {
    String message;
    String code;
    Socity socity;
    FamilyBean family;

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

    public Socity getSocity() {
        return socity;
    }

    public void setSocity(Socity socity) {
        this.socity = socity;
    }

    public FamilyBean getFamily() {
        return family;
    }

    public void setFamily(FamilyBean family) {
        this.family = family;
    }
}
