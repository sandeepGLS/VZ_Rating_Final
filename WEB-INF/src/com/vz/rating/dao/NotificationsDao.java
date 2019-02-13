package com.vz.rating.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import com.vz.rating.bean.CommentsBean;
import com.vz.rating.bean.InputBeanSearchProvider;
import com.vz.rating.bean.OutputBeanSearchProvider;
import com.vz.rating.bean.RatingBean;
import com.vz.rating.bean.RatingResponseBean;
import com.vz.rating.bean.ResponseBean;
import com.vz.rating.connection.MyConnection;

public class NotificationsDao {

	Connection conn;
	PreparedStatement pstmt;
	Properties objProperties;
	
	public NotificationsDao(String contextName) {
		initalizeConnection(contextName);
	}

	
	public void initalizeConnection(String contextName){
		try {
			conn = new MyConnection().getConnection(contextName);
		} catch (Exception e) {
			System.out.println("Exception while initalizing connection in initalizeConnection() NotificationsDao");
			e.printStackTrace();
		}
	}
	
	public void closeAll(){
		
		try {
			if(pstmt!=null)
				pstmt.close();
			if(conn!=null)
				conn.close();
		} catch (Exception e) {
			System.out.println("Connections are closed in NotificationsDao Class ");
			e.printStackTrace();
		}
	}
	

	
	public ResponseBean saveRatings(RatingResponseBean bean){
		ResponseBean objResponseBean = new ResponseBean();
		try {
			pstmt = conn.prepareStatement("insert into vz_rating values(?,?,?,?,?,?,?,?,?,?,now(),?)");
			pstmt.setInt(1, bean.getSocietyId());
			pstmt.setInt(2, bean.getFamilyId());
			pstmt.setString(3, bean.getFlatNumber());
			pstmt.setString(4, bean.getVisitorName());
			pstmt.setString(5, bean.getVisitorPurpose());
			pstmt.setString(6, bean.getVisitorMobile());
			pstmt.setInt(7, bean.getQualityRating());
			pstmt.setInt(8, bean.getPriceRating());
			pstmt.setInt(9, bean.getPunctualityRating());
			pstmt.setString(10, bean.getComments());
			pstmt.setString(11, bean.getVisitorPhoto());
			pstmt.executeUpdate();
			objResponseBean.setCode("SUCCESS");
			objResponseBean.setMessage("Rating Saved Successfully.");
		} catch (Exception e) {
			objResponseBean.setCode("ERROR");
			objResponseBean.setMessage("Something Gone Wrong.");
			System.out.println("Exception in saveRatings() NotificationsDao ");
			e.printStackTrace();
		}
		return objResponseBean;
	}

	public ResponseBean cancelRating(RatingResponseBean bean) {
		ResponseBean objResponseBean = new ResponseBean();
		try {
			pstmt = conn.prepareStatement("update vz_rating_processing set response_status=? where family_id=? and society_id=? and "
					+ " visitor_mobile=? and visitor_purpose=?");
			pstmt.setString(1, "cancel");
			pstmt.setInt(2, bean.getFamilyId());
			pstmt.setInt(3, bean.getSocietyId());
			pstmt.setString(4, bean.getVisitorMobile());
			pstmt.setString(5, bean.getVisitorPurpose());
			System.out.println("cancelRating Query = "+pstmt.toString());
			pstmt.executeUpdate();
			objResponseBean.setCode("SUCCESS");
			objResponseBean.setMessage("Thanks for reply.");
		} catch (Exception e) {
			objResponseBean.setCode("ERROR");
			objResponseBean.setMessage("Something Gone Wrong.");
			System.out.println("Error in cancelRating() NotificationsDao");
			e.printStackTrace();
		}
		return objResponseBean;
	}
	
	
	public void updateStatus(RatingResponseBean bean){
		
		try {
			pstmt = conn.prepareStatement("update vz_rating_processing set response_status=? where family_id=? and society_id=? and "
					+ " visitor_mobile=? and visitor_purpose=?");
			pstmt.setString(1, "success");
			pstmt.setInt(2, bean.getFamilyId());
			pstmt.setInt(3, bean.getSocietyId());
			pstmt.setString(4, bean.getVisitorMobile());
			pstmt.setString(5, bean.getVisitorPurpose());
			System.out.println("updateStatus Query = "+pstmt.toString());
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in updateStatus() NotificationsDao");
			e.printStackTrace();
		}
	}
	
	public ArrayList<RatingBean> getPendingRatings(RatingResponseBean bean){
		ArrayList<RatingBean> lisRatingBeans = new ArrayList<RatingBean>();
		try {
			pstmt = conn.prepareStatement("select visitor_name,visitor_mobile,visitor_purpose,visitor_photo,date_format(in_time,'%d %b %Y') as in_time "
					+ " from vz_rating_processing where family_id=? and society_id=? and response_status='sent'");
			pstmt.setInt(1, bean.getFamilyId());
			pstmt.setInt(2, bean.getSocietyId());
			ResultSet rs = pstmt.executeQuery();
			System.out.println(pstmt.toString());
			RatingBean objRatingBean = null;
			
			while(rs.next()){
				objRatingBean = new RatingBean();
				objRatingBean.setVisitorName(rs.getString("visitor_name"));
				objRatingBean.setVisitorMobile(rs.getString("visitor_mobile"));
				objRatingBean.setVisitorPurpose(rs.getString("visitor_purpose"));
				objRatingBean.setVisitorPhoto(rs.getString("visitor_photo"));
				objRatingBean.setInTime(rs.getString("in_time"));
				lisRatingBeans.add(objRatingBean);
			}
		} catch (Exception e) {
			System.out.println("Exception in getPendingRatings() in NotificationsDao");
			e.printStackTrace();
		}
		return lisRatingBeans;
	}
	
	public ArrayList<String> getProviderList(){
		ArrayList<String> list = new ArrayList<String>();;
		try {
			pstmt = conn.prepareStatement("select visitor_purpose from visitor_purposes");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println("Error in getProviderList() NotificationsDao");
			e.printStackTrace();
		}
		return list;
	}


	public ArrayList<OutputBeanSearchProvider> getProviderData(InputBeanSearchProvider bean) {

		ArrayList<OutputBeanSearchProvider> listSearchProviders = new ArrayList<OutputBeanSearchProvider>();
		
		String rate = "",societyIds="";
		if(bean.getRatingInput().equals("price")){
			rate = " price_rating";
		}else if(bean.getRatingInput().equals("quality")){
			rate = " quality_rating";
		}else{
			rate = " punctuality_rating";
		}
		if(bean.getNearBy().equals("OUT_SOC")){
			ArrayList<String> list = getLatLongOfSociety(bean.getSocietyId());
			if(list!=null && list.size()>=1){
				societyIds = getSurroundingSocities(list.get(0), list.get(1));
			}
		}
		if(societyIds.equals(""))
			societyIds = bean.getSocietyId()+"";
		
//		String finalQuery1 =" select visitor_photo,visitor_name,visitor_mobile,sum(case when society_id in("+societyIds+") "
//				+ "then "+rate+" else 0 end) / sum(case when society_id in("+societyIds+") then 1 else 0 end) as societyRating, "
//				+ "sum("+rate+") / count(*) as overAllRating, sum(case when society_id="+bean.getSocietyId()+" then "+rate+" else 0 "
//				+ "end) / sum(case when society_id="+bean.getSocietyId()+" then 1 else 0 end) mySocietyRating from vz_rating where "
//				+ "visitor_purpose=? and society_id in("+societyIds+") group by 2,3  union select photo_url, "
//				+ "concat(first_name,' ',last_name),mobile_no,0.1,0.1,0.1 from vz_visitors where mobile_no not in "
//				+ "(select visitor_mobile from vz_rating where visitor_mobile=mobile_no) and visit_purpose =? and "
//				+ "socity_id in("+societyIds+") group by 2,3 order by 4 desc ";
		
		String finalQuery ="select visitor_photo,visitor_name,visitor_mobile, count(visitor_mobile) as v_count, sum(case when society_id in("+societyIds+") "
				+ "then "+rate+" else 0 end) / sum(case when society_id in("+societyIds+") then 1 else 0 end) as societyRating, "
				+ "sum("+rate+") / count(*) as overAllRating, sum(case when society_id="+bean.getSocietyId()+" then "+rate+" else 0 "
				+ "end) / sum(case when society_id="+bean.getSocietyId()+" then 1 else 0 end) mySocietyRating from vz_rating where "
				+ "visitor_purpose=? and society_id in("+societyIds+") group by 2,3 union select photo_url, "
				+ "concat(first_name,' ',last_name),mobile_no, count(mobile_no),0.1,0.1,0.1 from vz_visitors vv where mobile_no not in "
				+ "(select visitor_mobile from vz_rating vr where visitor_mobile=mobile_no and vr.visitor_purpose =  vv.visit_purpose) and visit_purpose =? and "
				+ "socity_id in("+societyIds+") and in_time > (CURDATE() - INTERVAL 60 DAY) group by 2,3 order by 5 desc";
		
		try {
			pstmt = conn.prepareStatement(finalQuery);
			pstmt.setString(1, bean.getServiceProvider());
			pstmt.setString(2, bean.getServiceProvider());
			ResultSet rs = pstmt.executeQuery();
			System.out.println(pstmt.toString());
			OutputBeanSearchProvider objSearchProvider = null;
			while(rs.next()){				
				objSearchProvider = new OutputBeanSearchProvider();
				objSearchProvider.setVisitorPhoto(rs.getString("visitor_photo"));
				objSearchProvider.setVisitorName(rs.getString("visitor_name"));
				objSearchProvider.setVisitorMobile(rs.getString("visitor_mobile"));
				objSearchProvider.setVisitCount(rs.getInt("v_count"));
				if(rs.getInt("mySocietyRating")>=1){
					objSearchProvider.setSocietyPriceRating(rs.getInt("societyRating"));
				}else{
					objSearchProvider.setSocietyPriceRating(0);
				}
				objSearchProvider.setOverAllPriceRating(rs.getInt("overAllRating"));
				if(rs.getInt("societyRating")>=1 || rs.getFloat("mySocietyRating")>=0.1)
					listSearchProviders.add(objSearchProvider);
			}
		} catch (Exception e) {
			System.out.println("Error in getProviderData() NotificationsDao");
			e.printStackTrace();
		}
		return listSearchProviders;
	}
	
		
	public ArrayList<String> getLatLongOfSociety(int societyId){
		ArrayList<String> list = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement("select latitude ,longitude from vz_socities_coordinates where society_id=?");
			pstmt.setInt(1, societyId);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getLatLongOfSociety = "+pstmt.toString());
			if(rs.next()){
				list.add(rs.getString(1));
				list.add(rs.getString(2));
			}
				
		} catch (Exception e) {
			System.out.println("Error in getLatLongOfSociety() NotificationsDao");
			e.printStackTrace();
		}
		return list;
	}
	
	public String getSurroundingSocities(String latitude,String longitude){
		String result="";
		try {
			pstmt = conn.prepareStatement("SELECT *,( 6371 * acos(cos(radians(?)) * cos(radians(latitude)) * cos(radians(longitude) "
					+ " - radians(?)) + sin(radians(?)) * sin(radians(latitude)) ) ) AS distance "
					+ " FROM `vz_socities_coordinates` HAVING distance < 2 ORDER BY distance ");
			pstmt.setString(1, latitude);
			pstmt.setString(2, longitude);
			pstmt.setString(3, latitude);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getSurroundingSocities = "+pstmt.toString());
			if(rs.next())
				result = rs.getString(1);
			while(rs.next()){
				result = result + ","+rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Error in getSurroundingSocities() NotificationsDao ");
			e.printStackTrace();
		}
		//System.out.println(result);
		return result;
	}
	
	
	public OutputBeanSearchProvider getProviderDetailData(InputBeanSearchProvider inBeanSearchProvider){
		
		OutputBeanSearchProvider bean=null;
		
		String finalQuery = "select sum(case when society_id = "+inBeanSearchProvider.getSocietyId()+" then price_rating else 0 end) / sum(case when society_id = "+inBeanSearchProvider.getSocietyId()+" then 1 else 0 end) as societyPriceRating,"
				+ " sum(case when society_id = "+inBeanSearchProvider.getSocietyId()+" then quality_rating else 0 end) / sum(case when society_id = "+inBeanSearchProvider.getSocietyId()+" then 1 else 0 end) as societyQualityRating,"
				+ " sum(case when society_id = "+inBeanSearchProvider.getSocietyId()+" then punctuality_rating else 0 end) / sum(case when society_id = "+inBeanSearchProvider.getSocietyId()+" then 1 else 0 end) as societyPunctualityRating,"
				+ " sum(price_rating) / count(*) as overAllPriceRating, sum(quality_rating) / count(*) as overAllQualityRating, "
				+ "sum(punctuality_rating) / count(*) as overAllPunctualityRating"
				+ " from vz_rating where visitor_purpose='"+inBeanSearchProvider.getServiceProvider()+"' and visitor_mobile='"+inBeanSearchProvider.getVisitorMobile()+"'"; 
		System.out.println(finalQuery);
		try {
			pstmt = conn.prepareStatement(finalQuery);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				bean = new OutputBeanSearchProvider();
				bean.setSocietyPriceRating(rs.getInt("societyPriceRating"));
				bean.setSocietyQualityRating(rs.getInt("societyQualityRating"));
				bean.setSocietyPunctualityRating(rs.getInt("societyPunctualityRating"));
				bean.setOverAllPriceRating(rs.getInt("overAllPriceRating"));
				bean.setOverAllQualityRating(rs.getInt("overAllQualityRating"));
				bean.setOverAllPunctualityRating(rs.getInt("overAllPunctualityRating"));
			}
		} catch (Exception e) {
			System.out.println("Error in getProviderDetailData() NotificationsDao ");
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public void getProviderComments(InputBeanSearchProvider objInputBeanSearchProvider,OutputBeanSearchProvider outputBeanSearchProvider){
		
		ArrayList<CommentsBean> listCommentsBeans = new ArrayList<CommentsBean>();
		String query="select SUBSTRING_INDEX(b.ownerName,' ',1) as name,date_format(a.load_date,'%d %b %Y') as ratedDate,a.comments "
				+ " from vz_rating a,vz_families b where a.family_id = b.familyId and visitor_purpose=? and visitor_mobile=?"; 
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, objInputBeanSearchProvider.getServiceProvider());
			pstmt.setString(2, objInputBeanSearchProvider.getVisitorMobile());
			ResultSet rs = pstmt.executeQuery();
			System.out.println(pstmt.toString());
			CommentsBean bean = null;
			while(rs.next()){
				if(rs.getString("comments")!=null && !rs.getString("comments").trim().equals("")){
				//	System.out.println(rs.getString("comments"));
					bean = new CommentsBean();
					bean.setName(rs.getString("name"));
					bean.setTime(rs.getString("ratedDate"));
					bean.setComment(rs.getString("comments"));
					listCommentsBeans.add(bean);
				}
			}
			//System.out.println(listCommentsBeans.size());
			outputBeanSearchProvider.setListCommentsBeans(listCommentsBeans);
		} catch (Exception e) {
			System.out.println("Error in getProviderComments() NotificationsDao ");
			e.printStackTrace();
		}
	}


	public void updateVersionCode(String data, float versionCode) {
		try {
			String query = " update vz_user_devices set version_code=? where device_id=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setFloat(1,versionCode);
			pstmt.setString(2, data);
			pstmt.executeUpdate();
			System.out.println(pstmt.toString());
		} catch (Exception e) {
			System.out.println("Error in updateVersionCode() NotificationsDao ");
			e.printStackTrace();
		}
		
	}
	
}
