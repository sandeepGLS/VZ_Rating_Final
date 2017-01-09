package vztrack.gls.com.vztrack_user.beans;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectNotices {
    private String mTextTitle;
    private String mTextdescription;
    private String mTextDateStart;
    private String mTextDateEnd;
    private String mTextURL;


    public DataObjectNotices(String mTextTitle, String mTextdescription,String mTextStartDate, String mTextEndDate, String mTextURL) {
        this.mTextTitle = mTextTitle;
        this.mTextdescription = mTextdescription;
        this.mTextDateStart = mTextStartDate;
        this.mTextURL = mTextURL;
        this.mTextDateEnd = mTextEndDate;
    }

    public String getmTextDateEnd() {
        return mTextDateEnd;
    }

    public void setmTextDateEnd(String mTextDateEnd) {
        this.mTextDateEnd = mTextDateEnd;
    }

    public String getmTextTitle() {
        return mTextTitle;
    }

    public void setmTextTitle(String mTextTitle) {
        this.mTextTitle = mTextTitle;
    }

    public String getmTextdescription() {
        return mTextdescription;
    }

    public void setmTextdescription(String mTextdescription) {
        this.mTextdescription = mTextdescription;
    }

    public String getmTextDateStart() {
        return mTextDateStart;
    }

    public void setmTextDateStart(String mTextDateStart) {
        this.mTextDateStart = mTextDateStart;
    }

    public String getmTextURL() {
        return mTextURL;
    }

    public void setmTextURL(String mTextURL) {
        this.mTextURL = mTextURL;
    }
}