package com.vz.rating.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.opensymphony.xwork2.ActionInvocation;
import com.vz.rating.dao.AuditDumpDao;

public class DemoCustomInterceptor implements com.opensymphony.xwork2.interceptor.Interceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String ip_address;
		if (request.getHeader("x-forwarded-for")!=null)
			ip_address= request.getHeader("x-forwarded-for").split(",")[0];
		else
			ip_address = request.getRemoteAddr();
		
		String url = request.getServletPath();
		if(request!=null){
			AuditDumpDao objAuditDumpDao = new AuditDumpDao();
			objAuditDumpDao.insertData(ip_address,url,request.getParameter("data"));
			objAuditDumpDao.closeAll();
		}
		
		return invocation.invoke();
		
	}

	
	
}
