package workshop.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import workshop.model.AjaxReturn;
import workshop.model.Pager;
import workshop.model.Product;
import workshop.model.SystemParam;

//系统参数相关
@Controller
@RequestMapping("product")
public class ProductController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		Pager pager = new Pager();
		List<Product> list = productMapper.selectAll();
		if (list != null && list.size() > 0) {
			pager.setTotalCount(list.size());
			pager.setData(list);
			model.addAttribute("pager", pager);
		}

		return "product/list";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody int edit(@RequestParam int id, @RequestParam BigDecimal out_in) {
		Product product = productMapper.selectByPrimaryKey(id);
		if (product != null) {
			product.setOutIn(out_in);
			productMapper.updateByPrimaryKey(product);
			return 1;
		}

		return 0;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody int add(@RequestParam String name, @RequestParam byte type, 
			@RequestParam(defaultValue="0") BigDecimal out_in) {
		Product product = new Product();
		product.setType(type);
		product.setProductName(name);
		product.setOutIn(out_in);
		productMapper.insert(product);
		return 1;

	}
}
