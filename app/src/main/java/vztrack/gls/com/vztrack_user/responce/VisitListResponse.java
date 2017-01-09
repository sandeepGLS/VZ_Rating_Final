package vztrack.gls.com.vztrack_user.responce;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.profile.Visitors;

/**
 * Created by sandeep on 13/4/16.
 */
public class VisitListResponse {

    String code;

    ArrayList<Visitors> visitors;

    public ArrayList<Visitors> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Visitors> visitors) {
        this.visitors = visitors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
