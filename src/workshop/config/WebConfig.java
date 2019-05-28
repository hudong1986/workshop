package workshop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WEB������������
 * 
 * @author huchengdong
 *
 */
@ImportResource("/WEB-INF/spring_web.xml")
@Configuration
@EnableWebMvc
@ComponentScan({ "workshop.controller", "workshop.controller" })
public class WebConfig extends WebMvcConfigurerAdapter {


	/*@Bean
	public MultipartResolver multipartResolver(){
		return new StandardServletMultipartResolver();
	}*/
}
