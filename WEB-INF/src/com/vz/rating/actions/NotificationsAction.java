package com.vz.rating.actions;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.vz.rating.bean.InputBeanSearchProvider;
import com.vz.rating.bean.OutputBeanSearchProvider;
import com.vz.rating.bean.RatingBean;
import com.vz.rating.bean.RatingResponseBean;
import com.vz.rating.bean.ResponseBean;
import com.vz.rating.dao.NotificationsDao;

@ParentPackage("default")
public class NotificationsAction extends ActionSupport{

	/**
	 * CM
	 */
	private static final long serialVersionUID = 1L;
	private String data;
	private ArrayList<RatingBean> listRatingBeans;
	ResponseBean objResponseBean;
	private ArrayList<String> listStrings;
	OutputBeanSearchProvider objBeanSearchProvider;
	ArrayList<OutputBeanSearchProvider> listSearchProviders;

	public ArrayList<OutputBeanSearchProvider> getListSearchProviders() {
		return listSearchProviders;
	}

	public void setListSearchProviders(
			ArrayList<OutputBeanSearchProvider> listSearchProviders) {
		this.listSearchProviders = listSearchProviders;
	}

	public OutputBeanSearchProvider getObjBeanSearchProvider() {
		return objBeanSearchProvider;
	}

	public void setObjBeanSearchProvider(
			OutputBeanSearchProvider objBeanSearchProvider) {
		this.objBeanSearchProvider = objBeanSearchProvider;
	}

	public ArrayList<String> getListStrings() {
		return listStrings;
	}

	public void setListStrings(ArrayList<String> listStrings) {
		this.listStrings = listStrings;
	}

	public ResponseBean getObjResponseBean() {
		return objResponseBean;
	}

	public void setObjResponseBean(ResponseBean objResponseBean) {
		this.objResponseBean = objResponseBean;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ArrayList<RatingBean> getListRatingBeans() {
		return listRatingBeans;
	}

	public void setListRatingBeans(ArrayList<RatingBean> listRatingBeans) {
		this.listRatingBeans = listRatingBeans;
	}


	@Action(value="saveRatings",results={@Result(name="success",type="json",params={"root","objResponseBean"}),@Result(name="excep",location="/fail.jsp")},
			exceptionMappings = {
			@ExceptionMapping(exception="java.lang.Exception",result="excep")},
			interceptorRefs={@InterceptorRef(value="myStack")}
			)
	public String saveRatings(){
		data = ServletActionContext.getRequest().getParameter("data");
		System.out.println(data);
		RatingResponseBean obRatingResponseBean = new Gson().fromJson(data, RatingResponseBean.class);
		if(obRatingResponseBean!=null){
			NotificationsDao objNotificationsDao = new NotificationsDao("jdbc/vz");
			objResponseBean = objNotificationsDao.saveRatings(obRatingResponseBean);
			if(objResponseBean!=null && !objResponseBean.getCode().equals("ERROR"))
			objNotificationsDao.updateStatus(obRatingResponseBean);
			objNotificationsDao.closeAll();
		}else{
			objResponseBean = new ResponseBean();
			objResponseBean.setCode("ERROR");
			objResponseBean.setMessage("Network Problem");
		}
		return SUCCESS;
	}
	
	@Action(value="cancelRating",results={@Result(name="success",type="json",params={"root","objResponseBean"}),@Result(name="excep",location="/fail.jsp")},
			exceptionMappings = {@ExceptionMapping(exception="java.lang.Exception",result="excep")},
					interceptorRefs={@InterceptorRef(value="myStack")}
			)
	public String cancelNotification(){
		data = ServletActionContext.getRequest().getParameter("data");
		RatingResponseBean obRatingResponseBean = new Gson().fromJson(data, RatingResponseBean.class);
		if(obRatingResponseBean!=null){
			NotificationsDao objNotificationsDao = new NotificationsDao("jdbc/vz");
			objResponseBean = objNotificationsDao.cancelRating(obRatingResponseBean);
			objNotificationsDao.closeAll();
		}else{
			objResponseBean = new ResponseBean();
			objResponseBean.setCode("ERROR");
			objResponseBean.setMessage("Network Problem");
		}
		return SUCCESS;
	}
	
	@Action(value="getPendingRatings",results={@Result(name="success",type="json",params={"root","listRatingBeans"}),
			@Result(name="excep",location="/fail.jsp")},
			interceptorRefs={@InterceptorRef(value="myStack")},
			exceptionMappings = {
			@ExceptionMapping(exception="java.lang.Exception",result="excep")}
			)
	public String getPendingRatings(){
		data = ServletActionContext.getRequest().getParameter("data");
		RatingResponseBean obRatingResponseBean = new Gson().fromJson(data, RatingResponseBean.class);
		if(obRatingResponseBean!=null){
			NotificationsDao objNotificationsDao = new NotificationsDao("jdbc/vz");
			listRatingBeans = objNotificationsDao.getPendingRatings(obRatingResponseBean);
			objNotificationsDao.closeAll();
		}else{
			objResponseBean = new ResponseBean();
			objResponseBean.setCode("ERROR");
			objResponseBean.setMessage("Network Problem");
		}
		return SUCCESS;
	}

	@Action(value="getProviderList",results={@Result(name="success",type="json",params={"root","listStrings"}),@Result(name="excep",location="/fail.jsp")},
			interceptorRefs={@InterceptorRef(value="myStack")},
			exceptionMappings={@ExceptionMapping(exception="java.lang.Exception",result="excep")}
			)
	public String getProviderList(){
		float versionCode=0;
		NotificationsDao objNotificationsDao = new NotificationsDao("jdbc/vz");
		listStrings = objNotificationsDao.getProviderList();
		if(ServletActionContext.getRequest().getParameter("versionCode")!=null && ServletActionContext.getRequest().getParameter("data")!=null){
			versionCode = Float.parseFloat(ServletActionContext.getRequest().getParameter("versionCode"));
			data = ServletActionContext.getRequest().getParameter("data");
			System.out.println("Device id = "+data + "  Version Code = "+versionCode);
			objNotificationsDao.updateVersionCode(data,versionCode);
		}
		objNotificationsDao.closeAll();
		return SUCCESS;
	}
	
	@Action(value="getProviderData",results={@Result(name="success",type="json",params={"root","listSearchProviders"}),
			@Result(name="excep",location="/fail.jsp")},
			interceptorRefs={@InterceptorRef(value="myStack")},
			exceptionMappings={@ExceptionMapping(exception="java.lang.Exception",result="excep")}
			)
	public String getProviderData(){
		data = ServletActionContext.getRequest().getParameter("data");
		System.out.println(data);
		InputBeanSearchProvider bean = new Gson().fromJson(data, InputBeanSearchProvider.class);
		NotificationsDao objNotificationsDao = new NotificationsDao("jdbc/vz");
		listSearchProviders = objNotificationsDao.getProviderData(bean);
		objNotificationsDao.closeAll();
		return SUCCESS;
	}
	
	@Action(value="getProviderDetailData",results={@Result(name="success",type="json",params={"root","objBeanSearchProvider"}),
			@Result(name="excep",location="/fail.jsp")},
			interceptorRefs={@InterceptorRef(value="myStack")},
			exceptionMappings={@ExceptionMapping(exception="java.lang.Exception",result="excep")}
			)
	public String getProviderDetailData(){
		data = ServletActionContext.getRequest().getParameter("data");
		InputBeanSearchProvider bean = new Gson().fromJson(data, InputBeanSearchProvider.class);
		NotificationsDao objNotificationsDao = new NotificationsDao("jdbc/vz");
		objBeanSearchProvider = objNotificationsDao.getProviderDetailData(bean);
		objNotificationsDao.getProviderComments(bean, objBeanSearchProvider);
		objNotificationsDao.closeAll();
		return SUCCESS;
	}
	
}
