package vztrack.gls.com.vztrack_user.utils;

/**
 * Created by sandeep on 3/3/16.
 */
public class URL {
    public static final String LOGIN = "/api/userLogin";
    public static final String NOTICES = "/api/notices";
    public static final String VISITORS = "/api/visitors?pageNo=";
    public static final String VISIT_LIST = "/api/visitorDetails?mobileNo=";
    public static final String CHANGE_PASSWORD = "/api/changePassword?newPassword=";
    public static final String LOGOUT = "/api/logout";

    // Rating Urls
    public static final String SAVE_RATINGS = "/saveRatings";
    public static final String PENDING_RATINGS = "/getPendingRatings";
    public static final String CANCLE_RATING = "/cancelRating";
    public static final String PROVIDER_LIST = "/getProviderList";
    public static final String PROVIDERS_DATA = "/getProviderData";
    public static final String PROVIDER_DETAIL_DATA = "/getProviderDetailData";
}
