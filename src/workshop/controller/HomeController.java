package workshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class HomeController {
	
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String home( ) {

		return "home";
	}

	/**
	 * �����л�
	 */
	@RequestMapping("language")
	public ModelAndView language(HttpServletRequest request, HttpServletResponse response, @RequestParam String lng) {

		 
		return new ModelAndView("redirect:/");
	} 

}
