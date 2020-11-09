package com.mdms.app.mgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mdms.PasswordSecurityConfiguration;
import com.mdms.app.mgmt.model.UserLoginDetailModel;
import com.mdms.app.mgmt.repository.UserLoginDetailRepository;


@Service
public class UserLoginService {
	
	
	 Logger logger=LoggerFactory.getLogger(UserLoginService.class);
	 
	 
	 @Autowired
	 UserLoginDetailRepository loginDetailObj;

	 private final PasswordEncoder passwordEncoder = new PasswordSecurityConfiguration().passwordEncoder();
public String verifyLogin(String user_id,String pwd) {
	
	String response = "failed";
	try {
	
	

	
			logger.info("Service : UserLoginService || Method : verifyLogin ||");
			
			
			List<UserLoginDetailModel> list  =	loginDetailObj.getDeatils(user_id);

			
			
			if(list.size()>0) {
				
				
				boolean result=passwordEncoder.matches(pwd,list.get(0).getEmp_password());
			if(result) {
				response="success";	
			}else {
				response="Wrong Password";	
			}
			
			}else {
				response="This UserId is not registered.Please Registered First.";
			}
}catch(Exception ex) {
	
	logger.info("Service : UserProfileRegistrationService || Method : saveUserDetails ||Exception pwd encryption" + ex.getMessage());

//	System.out.print(ex.getMessage());
}
	return response;
}



}
