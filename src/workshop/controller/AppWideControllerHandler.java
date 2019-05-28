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
	 * // 页面可以直接运用
	 * 
	 * @ModelAttribute(value = "ctx") public String
	 * setContextPath(HttpServletRequest request) { return
	 * request.getContextPath(); }
	 */
	// 页面可以直接运用
	@ModelAttribute(value = "mydept")
	public List<PtDept> setMydept(@AuthenticationPrincipal PtUser user1, HttpServletRequest request) {
		if (user1 != null) {
			String upDownId=user1.getUpDownId();
			if(user1.getRoleName().contains("人事")){
				upDownId="";
			}
			return StaticService.getDeptlist(upDownId);
		} else
			return null;
	}
	
	// 页面可以直接运用
		@ModelAttribute(value = "productlist")
		public List<Product> setProductlist(@AuthenticationPrincipal PtUser user1, HttpServletRequest request) {
			if (user1 != null) {
				
				return productMapper.selectAll();
			} else
				return null;
		}

	// 页面可以直接运用
	@ModelAttribute(value = "usermode")
	public PtUser setUserMode(@AuthenticationPrincipal PtUser user, HttpServletRequest request) {
		 
		if (user != null) {
			// 第一次登录后更新一下登录时间
			if (request.getSession(true).getAttribute("update_login_time") == null) {
				user.setLoginTime(new Date());
				ptUserMapper.updateByPrimaryKey(user);
				request.getSession(true).setAttribute("update_login_time", 1);
			} else {
				 
			}

			// 记录用户操作
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("UserId:" + user.getUsername() + " ");
			sBuilder.append("ServletPath:" + request.getServletPath() + " ");
			sBuilder.append("QueryString:" + request.getQueryString() + " ");
			sBuilder.append("RequestURL:" + request.getRequestURL().toString() + " ");
			sBuilder.append("RemoteAddr:" + request.getRemoteAddr() + " ");
			sBuilder.append("RemoteHost:" + request.getRemoteHost() + " ");
			TxtLogger.log(sBuilder.toString(), LogTye.INFO, LogFileCreateType.OneFileAnHour, "USER_OPER");

		} else {
			// 没有登录令牌时需要清楚更新登录时间标志
			request.getSession(true).removeAttribute("update_login_time");
		}

		return user;
	}

	@ExceptionHandler(RuntimeException.class)
	public String handleIOException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			String errorCode = UUID.randomUUID().toString();
			request.setAttribute("error_msg", "哦，不好意思！服务器出错了，错误码:" + errorCode);
			ex.printStackTrace();
			// 记录日志
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
