package com.mdms.app.mgmt.controller;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdms.app.mgmt.model.GetListUserRegistrationJsonModel;
import com.mdms.app.mgmt.model.MasterUserLoginDetail;
import com.mdms.app.mgmt.model.UserProfileRegistrationDetailModel;
import com.mdms.app.mgmt.model.UserRegistrationJsonModel;
import com.mdms.app.mgmt.service.UserProfileRegistrationService;




@CrossOrigin(origins = {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
public class UserProfileRegistrationController {

	
	Logger logger=LoggerFactory.getLogger(UserProfileRegistrationController.class);
	
	@Autowired
	UserProfileRegistrationService registrationServiceObj;
	
	@RequestMapping(method=RequestMethod.POST, value="/userregistration")
	public String UserRegistration(@RequestBody UserRegistrationJsonModel userObj){
		if(userObj.getShed().equalsIgnoreCase("")) {
			userObj.setShed(null);
			
		}if(userObj.getLoco_type().equalsIgnoreCase("")) {
			userObj.setLoco_type(null);	
		}
		logger.info("Controller : UserRegistrationController || Method : UserRegistration ||user_id: "+userObj.getUser_id()
		+"||pwd"+userObj.getEmp_password()+"||shed"+ userObj.getShed() + "loco_type: "+ userObj.getLoco_type()+" ||user_id "+userObj.getUname()
		
				+"||zone : " +userObj.getZone()+"||division : " + userObj.getDivision());
		System.out.println(userObj.getDepartment());
	
		String response= registrationServiceObj.saveUserDetails(userObj);
		//code to send otp, on hold because of Api for sending otp
		
		logger.info("Controller : UserRegistrationController || Method : UserRegistration ||user_id: "+userObj.getUser_id()+ "||Response save details:"+response);

		return response;
		
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/getlistofitems")
	public GetListUserRegistrationJsonModel getListUserRegistration(){
						
		GetListUserRegistrationJsonModel response= registrationServiceObj.getListUserRegistration();
		//code to send otp, on hold because of Api for sending otp
		
		logger.info("Controller : UserRegistrationController || Method : getListUserRegistration ");

		return response;
		
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/finduserrecord")
	public String findUserRecord(@RequestParam  String user_id){
		
		
		String response= registrationServiceObj.findUserRecord(user_id);
		//code to send otp, on hold because of Api for sending otp
		
		logger.info("Controller : UserRegistrationController || Method : findUserRecord ||user_id  "+user_id + "||Find Records Response  "+ response);

		 return JSONObject.quote(response);
		
	}
	
	//fetch userdetail based on user type	
		@RequestMapping(method=RequestMethod.POST , value ="/getuserdetaildashboard")
		public List<UserProfileRegistrationDetailModel> getallusertypedetails(@RequestBody UserProfileRegistrationDetailModel objurecord ){
				return registrationServiceObj.getalluserdetail(objurecord);
		
		}
	
		//fetch userdetail based on user id	
		@RequestMapping(method=RequestMethod.POST , value ="/getusercontrl")
		public List<UserProfileRegistrationDetailModel> getuserctrl(@RequestBody UserProfileRegistrationDetailModel objuctrl ){
				return registrationServiceObj.getusercontrol(objuctrl);
		
		}
		
		
		@RequestMapping(method=RequestMethod.POST, value="/checkmstruserexist")
		public List<MasterUserLoginDetail> findMstrUserRecord(@RequestParam  String user_id){	
			List<MasterUserLoginDetail> response= registrationServiceObj.findMstrUserRecord(user_id);	
						logger.info("Controller : UserRegistrationController || Method : findMstrUserRecord ||user_id  "+user_id + "||Find Master Records Response  "+ response);

			 return response;
			
		}
		
		@RequestMapping(method=RequestMethod.POST, value="/checkregisteruserexist")
		public String findregUserRecord(@RequestParam  String user_id){	
			String response= registrationServiceObj.findUserRecorinRegistration(user_id);	
						logger.info("Controller : UserRegistrationController || Method : findregUserRecord ||user_id  "+user_id + "||Find registered user Records Response  "+ response);

			 return JSONObject.quote(response);
			
		}
		
		//update record - master password for current date
	 	@RequestMapping(method=RequestMethod.POST, value ="/updatemstrpassword")
	 	public boolean update_edraft(@RequestBody MasterUserLoginDetail obj_mstrpwd)
	 	{	 	
	 		
	 	boolean flag = registrationServiceObj.updatemstrpwd(obj_mstrpwd);
	 	return flag;
	 	}
}
