package vztrack.gls.com.vztrack_user.beans;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectVisitList {
    private String mFirst_Name;
    private String mLast_Name;
    private String InTime;
    private String OutTime;
    private String mPurpose;
    private String mFrom;
    private String mTomeet;
    private String mVehicle_no;
    private String mNoOfVisitors;
    private String mWhomeToVisit;

    public DataObjectVisitList(String mFirst_Name, String mLast_Name, String InTime, String outTime ,String mPurpose, String mFrom, String mTomeet, String mVehicle_no, String mNoOfVisitors, String mWhomeToVisit) {
        this.mFirst_Name = mFirst_Name;
        this.mLast_Name = mLast_Name;
        this.InTime = InTime;
        this.OutTime = outTime;
        this.mPurpose = mPurpose;
        this.mFrom = mFrom;
        this.mTomeet = mTomeet;
        this.mVehicle_no = mVehicle_no;
        this.mNoOfVisitors = mNoOfVisitors;
        this.mWhomeToVisit = mWhomeToVisit;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getmFirst_Name() {
        return mFirst_Name;
    }

    public void setmFirst_Name(String mFirst_Name) {
        this.mFirst_Name = mFirst_Name;
    }

    public String getmLast_Name() {
        return mLast_Name;
    }

    public void setmLast_Name(String mLast_Name) {
        this.mLast_Name = mLast_Name;
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

    public String getmVehicle_no() {
        return mVehicle_no;
    }

    public void setmVehicle_no(String mVehicle_no) {
        this.mVehicle_no = mVehicle_no;
    }

    public String getmNoOfVisitors() {
        return mNoOfVisitors;
    }

    public void setmNoOfVisitors(String mNoOfVisitors) {
        this.mNoOfVisitors = mNoOfVisitors;
    }

    public String getmWhomeToVisit() {
        return mWhomeToVisit;
    }

    public void setmWhomeToVisit(String mWhomeToVisit) {
        this.mWhomeToVisit = mWhomeToVisit;
    }
}