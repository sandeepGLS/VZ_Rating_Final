package vztrack.gls.com.vztrack_user.profile;

import java.util.ArrayList;

/**
 * Created by sandeep on 11/4/16.
 */
public class VisitorsArray {

    String message;
    String code;

    ArrayList<Visitors> visitors;

    public ArrayList<Visitors> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Visitors> visitors) {
        this.visitors = visitors;
    }

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



