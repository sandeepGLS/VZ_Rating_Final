package vztrack.gls.com.vztrack_user.beans;

/**
 * Created by sandeep on 22/12/16.
 */

public class InputBeanSearchProvider {

    private int societyId;
    private String ratingInput;
    private String serviceProvider;
    private String nearBy;
    private String visitorMobile;

    public String getVisitorMobile() {
        return visitorMobile;
    }

    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }

    public int getSocietyId() {
        return societyId;
    }
    public void setSocietyId(int societyId) {
        this.societyId = societyId;
    }
    public String getRatingInput() {
        return ratingInput;
    }
    public void setRatingInput(String ratingInput) {
        this.ratingInput = ratingInput;
    }
    public String getServiceProvider() {
        return serviceProvider;
    }
    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
    public String getNearBy() {
        return nearBy;
    }
    public void setNearBy(String nearBy) {
        this.nearBy = nearBy;
    }

}

