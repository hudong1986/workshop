package workshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import workshop.model.PtUser;
import workshop.repository.PtUserMapper;

@Service
public class WebUserService implements UserDetailsService {

	@Autowired
	PtUserMapper ptUserMapper;
	
	
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		PtUser ptUser = ptUserMapper.selectByPhone(arg0);
		if(ptUser!=null){
			
			return ptUser;
		}
		else{
			throw new UsernameNotFoundException("用户:"+arg0+"不存在！");
		}
		 
	}

}
