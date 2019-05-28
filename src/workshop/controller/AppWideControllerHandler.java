package workshop.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import workshop.model.Product;
import workshop.model.PtDept;
import workshop.model.PtUser;
import workshop.service.StaticService;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;
import workshop.util.TxtLogger.LogTye;

@ControllerAdvice
public class AppWideControllerHandler extends BaseController {

	/*
	 * // ҳ�����ֱ������
	 * 
	 * @ModelAttribute(value = "ctx") public String
	 * setContextPath(HttpServletRequest request) { return
	 * request.getContextPath(); }
	 */
	// ҳ�����ֱ������
	@ModelAttribute(value = "mydept")
	public List<PtDept> setMydept(@AuthenticationPrincipal PtUser user1, HttpServletRequest request) {
		if (user1 != null) {
			String upDownId=user1.getUpDownId();
			if(user1.getRoleName().contains("����")){
				upDownId="";
			}
			return StaticService.getDeptlist(upDownId);
		} else
			return null;
	}
	
	// ҳ�����ֱ������
		@ModelAttribute(value = "productlist")
		public List<Product> setProductlist(@AuthenticationPrincipal PtUser user1, HttpServletRequest request) {
			if (user1 != null) {
				
				return productMapper.selectAll();
			} else
				return null;
		}

	// ҳ�����ֱ������
	@ModelAttribute(value = "usermode")
	public PtUser setUserMode(@AuthenticationPrincipal PtUser user, HttpServletRequest request) {
		 
		if (user != null) {
			// ��һ�ε�¼�����һ�µ�¼ʱ��
			if (request.getSession(true).getAttribute("update_login_time") == null) {
				user.setLoginTime(new Date());
				ptUserMapper.updateByPrimaryKey(user);
				request.getSession(true).setAttribute("update_login_time", 1);
			} else {
				 
			}

			// ��¼�û�����
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("UserId:" + user.getUsername() + " ");
			sBuilder.append("ServletPath:" + request.getServletPath() + " ");
			sBuilder.append("QueryString:" + request.getQueryString() + " ");
			sBuilder.append("RequestURL:" + request.getRequestURL().toString() + " ");
			sBuilder.append("RemoteAddr:" + request.getRemoteAddr() + " ");
			sBuilder.append("RemoteHost:" + request.getRemoteHost() + " ");
			TxtLogger.log(sBuilder.toString(), LogTye.INFO, LogFileCreateType.OneFileAnHour, "USER_OPER");

		} else {
			// û�е�¼����ʱ��Ҫ������µ�¼ʱ���־
			request.getSession(true).removeAttribute("update_login_time");
		}

		return user;
	}

	@ExceptionHandler(RuntimeException.class)
	public String handleIOException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			String errorCode = UUID.randomUUID().toString();
			request.setAttribute("error_msg", "Ŷ��������˼�������������ˣ�������:" + errorCode);
			ex.printStackTrace();
			// ��¼��־
			StringBuffer sb = new StringBuffer();
			for (StackTraceElement element : ex.getStackTrace()) {
				sb.append(element.toString() + "\r\n");
			}
			TxtLogger.log(errorCode + " error-" + ex.toString() + " " + sb.toString(), LogTye.ERROR,
					LogFileCreateType.OneFileAnHour, "");
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		return "errorpages/500";
	}

}
