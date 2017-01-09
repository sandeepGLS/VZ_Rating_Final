package vztrack.gls.com.vztrack_user.beans;

/**
 * Created by sandeep on 14/3/16.
 */

public class DataObjectRating {
    private String mvisitorMobile;
    private String mvisitorPurpose;
    private String minTime;
    private String mvisitorPhoto;
    private String mvisitorName;

    public DataObjectRating(String mvisitorName, String mvisitorMobile, String mvisitorPurpose, String minTime, String mvisitorPhoto) {
        this.mvisitorName = mvisitorName;
        this.mvisitorMobile = mvisitorMobile;
        this.mvisitorPurpose = mvisitorPurpose;
        this.minTime = minTime;
        this.mvisitorPhoto = mvisitorPhoto;
    }

    public String getMvisitorMobile() {
        return mvisitorMobile;
    }

    public void setMvisitorMobile(String mvisitorMobile) {
        this.mvisitorMobile = mvisitorMobile;
    }

    public String getMvisitorPurpose() {
        return mvisitorPurpose;
    }

    public void setMvisitorPurpose(String mvisitorPurpose) {
        this.mvisitorPurpose = mvisitorPurpose;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getMvisitorPhoto() {
        return mvisitorPhoto;
    }

    public void setMvisitorPhoto(String mvisitorPhoto) {
        this.mvisitorPhoto = mvisitorPhoto;
    }

    public String getMvisitorName() {
        return mvisitorName;
    }

    public void setMvisitorName(String mvisitorName) {
        this.mvisitorName = mvisitorName;
    }
}