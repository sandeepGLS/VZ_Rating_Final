package vztrack.gls.com.vztrack_user.beans;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectVisitors_Filterd {
    private String mFirst_Name;
    private String mLast_Name;
    private String mMobile;
    private String mPhoto;
    private String mTime;
    private String mPurpose;
    private String mFrom;
    private String mTomeet;
    private String mDocUrl;


    public DataObjectVisitors_Filterd(String mFirst_Name, String mLast_Name, String mMobile, String mPhoto, String mTime, String mPurpose, String mFrom, String mTomeet, String mDocUrl) {
        this.mFirst_Name = mFirst_Name;
        this.mLast_Name = mLast_Name;
        this.mMobile = mMobile;
        this.mPhoto = mPhoto;
        this.mTime = mTime;
        this.mPurpose = mPurpose;
        this.mFrom = mFrom;
        this.mTomeet = mTomeet;
        this.mDocUrl = mDocUrl;
    }



    public String getmLast_Name() {
        return mLast_Name;
    }

    public void setmLast_Name(String mLast_Name) {
        this.mLast_Name = mLast_Name;
    }

    public String getmFirst_Name() {
        return mFirst_Name;
    }

    public void setmFirst_Name(String mFirst_Name) {
        this.mFirst_Name = mFirst_Name;
    }

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmPurpose() {
        return mPurpose;
    }

    public void setmPurpose(String mPurpose) {
        this.mPurpose = mPurpose;
    }

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmTomeet() {
        return mTomeet;
    }

    public void setmTomeet(String mTomeet) {
        this.mTomeet = mTomeet;
    }

    public String getmDocUrl() {
        return mDocUrl;
    }

    public void setmDocUrl(String mDocUrl) {
        this.mDocUrl = mDocUrl;
    }
}