package workshop.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import workshop.myimplclass.Md5PasswordEncoder;
import workshop.service.WebUserService;

//此类将会启用web安全检查
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;
	
	@Autowired
	WebUserService webUserService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/*auth.jdbcAuthentication().dataSource(datasource)
				.usersByUsernameQuery("select phone as username ,user_pwd as password,true from pt_user "
						+ "where phone=? and state=0")
				.authoritiesByUsernameQuery(
						"select phone as username,role_code from pt_user " + "where phone=? and state=0")
				.passwordEncoder(new Md5PasswordEncoder());*/
		
		auth.userDetailsService(webUserService).passwordEncoder(new Md5PasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").and().rememberMe().tokenValiditySeconds(30 * 24 * 3600).key("user_key")
				.and().authorizeRequests().antMatchers("/").authenticated().antMatchers("/home**").authenticated()
				.antMatchers("/product/**").authenticated().
				 antMatchers("/product/add**").access("hasAnyRole('ADMIN','HrManager','HrAssistant')").
				 antMatchers("/product/edit**").access("hasAnyRole('ADMIN','HrManager','HrAssistant')").
				 antMatchers("/product/delete**").access("hasAnyRole('ADMIN','HrManager','HrAssistant')").
				 
				 antMatchers("/attendance/**").authenticated().
				 antMatchers("/attendance/add**").access("hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')").
				 antMatchers("/attendance/edit**").access("hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')").
				 antMatchers("/attendance/delete**").access("hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')").
				 
				 antMatchers("/finish_statistic/**").authenticated().
				 antMatchers("/finish_statistic/add**").access("hasAnyRole('FactoryManager','ProductManager','WorkshopManager')").
				 antMatchers("/finish_statistic/edit**").access("hasAnyRole('FactoryManager','ProductManager','WorkshopManager')").
				 antMatchers("/finish_statistic/delete**").access("hasAnyRole('FactoryManager','ProductManager','WorkshopManager')").
				 
				 antMatchers("/goods_in_out/**").authenticated().
				 antMatchers("/goods_in_out/add**").access("hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')").
				 antMatchers("/goods_in_out/edit**").access("hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')").
				 antMatchers("/goods_in_out/delete**").access("hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')").
				 
				 antMatchers("/user/**").authenticated().
				 antMatchers("/user/add**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/user/edit**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/user/delete**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/user/leave**").access("hasAnyRole('HrManager','HrAssistant')").
				 
				 antMatchers("/salary/**").authenticated().
				 antMatchers("/salary/add**").access("hasAnyRole('HrManager','HrAssistant')").
				 
				 antMatchers("/reward/**").authenticated().
				 antMatchers("/reward/add**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/reward/edit**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/reward/delete**").access("hasAnyRole('HrManager','HrAssistant')").

				 antMatchers("/report/**").authenticated().
				 
				 antMatchers("/system/**").authenticated().
				 antMatchers("/system/add**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/system/edit**").access("hasAnyRole('HrManager','HrAssistant')").
				 antMatchers("/system/delete**").access("hasAnyRole('HrManager','HrAssistant')").
				 
				 anyRequest().permitAll().and().
				 csrf().disable();
	}
}
