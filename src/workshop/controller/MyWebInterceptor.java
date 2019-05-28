package workshop.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import workshop.service.StaticService;
import workshop.util.DateUtil;

 

public class MyWebInterceptor extends HandlerInterceptorAdapter {


	/**
	 * ��Controller����ǰ��������
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if(new Date().getTime()> DateUtil.parse("2019-08-16").getTime()){
			throw new Exception("�Բ���ϵͳ��Ч���Ѿ�����������");
		}
		try {
			// ִ����Щ����

			if (modelAndView != null) {

				String servletPath = request.getServletPath();
				if (servletPath.contains("/user")) {
				    
					modelAndView.addObject("deptList", StaticService.getDeptlist());
					modelAndView.addObject("roleList", StaticService.getRoleLst());
				}

				if (servletPath.contains("/home") || servletPath.equals("/")) {
					modelAndView.addObject("active_menu", "1000-");
					modelAndView.addObject("CurrentTitle", "����");
				}
				
				if (servletPath.contains("/product/list")) {
					modelAndView.addObject("active_menu", "2000-2001");
					modelAndView.addObject("CurrentTitle", "��Ʒ����");
				}
				if (servletPath.contains("/finish_statistic/list")) {
					modelAndView.addObject("active_menu", "2000-2002");
					modelAndView.addObject("CurrentTitle", "�Ǽ�����");
				}
				if (servletPath.contains("/goods_in_out/list")) {
					modelAndView.addObject("active_menu", "2000-2003");
					modelAndView.addObject("CurrentTitle", "��Ʒ�����");
				}
				
				if (servletPath.contains("/user/list")) {
					modelAndView.addObject("active_menu", "3000-3001");
					modelAndView.addObject("CurrentTitle", "Ա���б�");
				}
				if (servletPath.contains("/attendance/list")) {
					modelAndView.addObject("active_menu", "3000-3002");
					modelAndView.addObject("CurrentTitle", "Ա������");
				}

				if (servletPath.contains("/salary/list")) {
					modelAndView.addObject("active_menu", "3000-3003");
					modelAndView.addObject("CurrentTitle", "Ա��н��");
				}

				if (servletPath.contains("/reward/list")) {
					modelAndView.addObject("active_menu", "3000-3004");
					modelAndView.addObject("CurrentTitle", "Ա������");
				}

				//����4000-
				if (servletPath.contains("/report/statistic")) {
					modelAndView.addObject("active_menu", "4000-4001");
					modelAndView.addObject("CurrentTitle", "�Ǽ�ͳ��");
				}
				if (servletPath.contains("/report/goods_in_out")) {
					modelAndView.addObject("active_menu", "4000-4002");
					modelAndView.addObject("CurrentTitle", "�����ͳ��");
				}
				if (servletPath.contains("/report/salary")) {
					modelAndView.addObject("active_menu", "4000-4003");
					modelAndView.addObject("CurrentTitle", "н�ʷ���ͳ��");
				}

				//����
				if (servletPath.contains("/system/list")) {
					modelAndView.addObject("active_menu", "5000-5001");
					modelAndView.addObject("CurrentTitle", "ϵͳ����");
				}
				
				if (servletPath.contains("/user/modifyPwd")) {
					modelAndView.addObject("active_menu", "5000-5002");
					modelAndView.addObject("CurrentTitle", "�޸�����");
				}

				 
			}
		} catch (Exception ex) {

		}
	}

	/**
	 * ��Controller�������������
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
