package com.mdms.dashboard.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mdms.dahsboard.model.DashBoardCoachCountDepoWiseModel;
import com.mdms.dahsboard.model.DashBoardLocoCountShedWiseModel;
import com.mdms.dahsboard.model.DashBoardStationCountDivisionWiseModel;
import com.mdms.dahsboard.model.DashboardLocoModel;
import com.mdms.dahsboard.model.DashboardStationModel;
import com.mdms.dashboard.repository.StationDashboardRepo;
import com.mdms.loco.locouncleansed.repository.LocoApprovedDataRepository;
import com.mdms.loco.locouncleansed.repository.LocoDataFoisRepository;
import com.mdms.loco.locouncleansed.repository.LocoUncleansedDataElectricRepository;
import com.mdms.mdms_coach.coachuncleansed.repository.CoachCMMDataRepository;
import com.mdms.mdms_coach.coachuncleansed.repository.CoachCleansedDataRepository;
import com.mdms.mdms_coach.coachuncleansed.repository.CoachUncleansedDataRepository;
import com.mdms.mdms_masters.model.MDivision;
import com.mdms.mdms_station.stationuncleansed.repository.StationTableRbsRepository;
import com.mdms.mdms_station.stationuncleansed.repository.StationUncleansedDataRepository;




@Service
public class StationDashboardService {
	 Logger logger=LoggerFactory.getLogger(StationDashboardService.class);
	 
//	 @Autowired
//		private StationDashboardRepo stationRepositoryObj;
//	 
	 @Autowired
		StationUncleansedDataRepository stn_unclsnd_repo;
		
		@Autowired
		StationTableRbsRepository stn_tbl_rbs_repo;
		
		@Autowired
		LocoDataFoisRepository loco_tbl_fois_repo;
		
		@Autowired
		LocoUncleansedDataElectricRepository loco_tbl_repo;
		
		@Autowired
		LocoApprovedDataRepository  loco_tbl_approve;
		MDivision objmdiv;
		@Autowired
		CoachCMMDataRepository coach_cmm_repo;
		
		@Autowired
		CoachUncleansedDataRepository coach_unclean_repo;
		
		@Autowired
		CoachCleansedDataRepository coach_clean_repo;

		@Autowired
		private JdbcTemplate jdbcTemplate;
		
		public HashMap<String, Integer> getStationStats() {
			logger.info("Service : StationDashboardService || Method: getStationStats");

	 HashMap<String, Integer> map = new HashMap<>();
			 
	 try {
			 final String noOfStations = "SELECT count(*) FROM mdms_station.station_table_rbs";
			    final int totalStations = (int)jdbcTemplate.queryForObject(noOfStations,Integer.class);
	
	 
	final String cleansedStations = "select count(*) from mdms_station.station_uncleansed_data";
	final int noOfCleansedStations = (int)jdbcTemplate.queryForObject(cleansedStations, Integer.class);
	
	final String uncleansedStations = "select count(*) from(\r\n" + 
			"	Select stn_code FROM mdms_station.station_table_rbs \r\n" + 
			"	except select station_code FROM mdms_station.station_uncleansed_data) AS uncleansed";
	final int noOfUncleansedStations = (int)jdbcTemplate.queryForObject(uncleansedStations, Integer.class);
	
	//Integer noOfUncleansedStations=totalStations-noOfCleansedStations;
	
	final String unapprovedStations = "select count(*) from mdms_station.station_uncleansed_data where cmi_status='0' OR dti_status='0'";
	final int waitingForApproval = (int)jdbcTemplate.queryForObject(unapprovedStations,Integer.class);
	
			    map.put("noOfUncleansedStations", noOfUncleansedStations);
			    map.put("noOfCleansedStations", noOfCleansedStations);
			    map.put("totalStations", totalStations);
			    map.put("waitingForApproval", waitingForApproval);
			    return map;
		}
		  catch(Exception e) {
		    	logger.error("Service : StationDashboardService || Method: getStationStats|| Exception : " +e.getMessage());
				e.getMessage();
				return null;
		    }
		}
		
		public HashMap<String, Integer> getDivisionWiseStationStats(String  divcode) {
			logger.info("Service : StationDashboardService || Method: getDivisionWiseStationStats");
			 String dc= "MAS";	
	 HashMap<String, Integer> map = new HashMap<>();			 
	 try {
		
			 final String noOfStations = "SELECT count(*) FROM mdms_station.station_table_rbs as a\r\n" + 
			 		"		join mdms_masters.m_division AS b on a.div_ser_no=b.division_sr_no\r\n" + 
			 		"		where b.division_code=dc";
			    final int totalStations = (int)jdbcTemplate.queryForObject(noOfStations,Integer.class);
	
	 
	final String cleansedStations = "select count(*) from mdms_station.station_uncleansed_data where division_code=?1";
	final int noOfCleansedStations = (int)jdbcTemplate.queryForObject(cleansedStations, Integer.class);
	
	final String uncleansedStations = "select count(*) from(\r\n" + 
			"	Select stn_code FROM mdms_station.station_table_rbs \r\n" + 
			"	except select station_code FROM mdms_station.station_uncleansed_data) AS uncleansed where division_code=?1 ";
	final int noOfUncleansedStations = (int)jdbcTemplate.queryForObject(uncleansedStations, Integer.class);
	
	//Integer noOfUncleansedStations=totalStations-noOfCleansedStations;
	
	final String unapprovedStations = "select count(*) from mdms_station.station_uncleansed_data where division_code=?1 AND(cmi_status='0' OR dti_status='0')";
	final int waitingForApproval = (int)jdbcTemplate.queryForObject(unapprovedStations,Integer.class);	
			    map.put("noOfUncleansedStations", noOfUncleansedStations);
			    map.put("noOfCleansedStations", noOfCleansedStations);
			    map.put("totalStations", totalStations);
			    map.put("waitingForApproval", waitingForApproval);
			    return map;
		}
		  catch(Exception e) {
		    	logger.error("Service : StationDashboardService || Method: getDivisionWiseStationStats|| Exception : " +e.getMessage());
				e.getMessage();
				return null;
		    }
		}

			
		 int uncleansedFlag=0;
			
		 
			
		 
		 DashBoardStationCountDivisionWiseModel obj;
		
		//find  station details
		
		public List<DashboardStationModel> getStationCountDivisionWise(DashboardStationModel objzone_code) {
			String zone_code =objzone_code.getZone_code();
			List<DashboardStationModel> list= new ArrayList<DashboardStationModel>();			
			Collection<DashBoardStationCountDivisionWiseModel> totalCountList= stn_tbl_rbs_repo.getTotalStationCountDivisionWise(zone_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountDivisionWise || getTotalStationCountDivisionWise Query list return : "+totalCountList.size());
				if(totalCountList.size()>0) {
				totalCountList.forEach(DashBoardStationCountDivisionWiseModel -> setTotalDivision(DashBoardStationCountDivisionWiseModel,list));

				}	
				
				Collection<DashBoardStationCountDivisionWiseModel> uncleansedCountList= stn_tbl_rbs_repo.getUncleansedStationCountDivisionWise(zone_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountDivisionWise || getUncleansedStationCountDivisionWise Query list return : "+uncleansedCountList.size());

				uncleansedCountList.forEach(DashBoardStationCountDivisionWiseModel -> callTotal(DashBoardStationCountDivisionWiseModel,list));
				
				
				Collection<DashBoardStationCountDivisionWiseModel> pendingApprovalCountList= stn_unclsnd_repo.getPendingApprovalStationCountDivisionWise(zone_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountDivisionWise || getPendingApprovalStationCountDivisionWise Query list return : "+pendingApprovalCountList.size());
		
				pendingApprovalCountList.forEach(DashBoardStationCountDivisionWiseModel -> callPendingApproval(DashBoardStationCountDivisionWiseModel,list));

				
				Collection<DashBoardStationCountDivisionWiseModel> cleansedCountList= stn_unclsnd_repo.getTotalCleansedStationCountDivisionWise(zone_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountDivisionWise || getTotalCleansedStationCountDivisionWise Query list return : "+cleansedCountList.size());			
				cleansedCountList.forEach(DashBoardStationCountDivisionWiseModel -> callCleansedCount(DashBoardStationCountDivisionWiseModel,list));			
			
				
				Collection<DashBoardStationCountDivisionWiseModel> draftCountList= stn_unclsnd_repo.getTotalDraftForwardApprovalStationCountDivisionWise(zone_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountDivisionWise || getTotalDraftForwardApprovalStationCountDivisionWise Query list return : "+draftCountList.size());			
				draftCountList.forEach(DashBoardStationCountDivisionWiseModel -> callDraftCount(DashBoardStationCountDivisionWiseModel,list));	
				
				
				
				
				
				return list;
			
		}


	

		private void callTotal(DashBoardStationCountDivisionWiseModel uncleansedObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			try {		
				uncleansedFlag=0;
				list.forEach(totalobj -> callTotalSub(uncleansedObj,totalobj));	
				if(uncleansedFlag==0){
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(uncleansedObj.getzone_code());
					obj.setDivision_code(uncleansedObj.getdivision_code());
					obj.setUncleansed_count(uncleansedObj.getuncleansed_count());
			//		list.add(obj);	
				}
					}catch (Exception e) {
				// TODO: Handle Exception
				e.getMessage();		}
		}
		private void callTotalSub(DashBoardStationCountDivisionWiseModel uncleansedObj,DashboardStationModel totalobj) {
			
			try {
			if(uncleansedObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())) {
				uncleansedFlag++;
				totalobj.setUncleansed_count(uncleansedObj.getuncleansed_count());
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		

		
		private void callPendingApproval(DashBoardStationCountDivisionWiseModel pendingApprovObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			try {
			
				uncleansedFlag=0;
				list.forEach(totalobj -> callPendingApprovalSub(pendingApprovObj,totalobj));	
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(pendingApprovObj.getzone_code());					
					obj.setDivision_code(pendingApprovObj.getdivision_code());
					obj.setPending_approval(pendingApprovObj.getpending_approval());
			//		list.add(obj);	
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callPendingApprovalSub(DashBoardStationCountDivisionWiseModel pendingApprovObj,DashboardStationModel totalobj) {
			
			try {
			if(pendingApprovObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())) {
				uncleansedFlag++;
				totalobj.setPending_approval(pendingApprovObj.getpending_approval());		
		
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		
		private void callCleansedCount(DashBoardStationCountDivisionWiseModel cleansedObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			
			try {
				uncleansedFlag=0;
				list.forEach(totalobj -> callCleansedCountSub(cleansedObj,totalobj));

		
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(cleansedObj.getzone_code());
					obj.setDivision_code(cleansedObj.getdivision_code());
					obj.setCleansed_count(cleansedObj.getcleansed_count());
				//	list.add(obj);		
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callCleansedCountSub(DashBoardStationCountDivisionWiseModel cleansedObj,DashboardStationModel totalobj) {
			try {
			if(cleansedObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())){
				uncleansedFlag++;
				totalobj.setCleansed_count(cleansedObj.getcleansed_count());	
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		private void callDraftCount(DashBoardStationCountDivisionWiseModel draftObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			
			try {
				uncleansedFlag=0;
				list.forEach(totalobj -> callDraftCountSub(draftObj,totalobj));

		
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(draftObj.getzone_code());
					obj.setDivision_code(draftObj.getdivision_code());
					obj.setDraft_forward_approval_count(draftObj.getdraft_forward_approval_count());
				//	list.add(obj);		
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callDraftCountSub(DashBoardStationCountDivisionWiseModel draftObj,DashboardStationModel totalobj) {
			try {
			if(draftObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())){
				uncleansedFlag++;
				totalobj.setDraft_forward_approval_count(draftObj.getdraft_forward_approval_count());	
		//		System.out.println("draft add in list divcode"+ totalobj.getDivision_code()+"|| AND Draft count: "+totalobj.getDraft_forward_approval_count());
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		
		private void setTotalDivision(DashBoardStationCountDivisionWiseModel DashBoardStationCountDivisionWiseModel,Collection<DashboardStationModel> list) {
		DashboardStationModel obj =new DashboardStationModel();	
//		obj.setZone_code(DashBoardStationCountDivisionWiseModel.getzone_code());
			
		obj.setDivision_code(DashBoardStationCountDivisionWiseModel.getdivision_code());

//		obj.setTotal_division_count(DashBoardStationCountDivisionWiseModel.gettotal_division_count());			

		obj.setTotal_division_count(DashBoardStationCountDivisionWiseModel.gettotal_division_count());			

		list.add(obj);
		
		}
		


		
		
		
	//Shilpi 04-03-2021
		
		public List<DashboardStationModel> getStationCountSingleDivisionWise(DashboardStationModel objdivision_code) {
			String division_code =objdivision_code.getDivision_code();
			List<DashboardStationModel> list= new ArrayList<DashboardStationModel>();		
			Collection<DashBoardStationCountDivisionWiseModel> totalCountLists= stn_tbl_rbs_repo.getTotalStationCountSingleDivisionWise(division_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountSingleDivisionWise || getTotalStationCountSingleDivisionWise Query list return : "+totalCountLists);
				if(totalCountLists.size()>0) {
				totalCountLists.forEach(DashBoardStationCountDivisionWiseModel -> setTotalDivision(DashBoardStationCountDivisionWiseModel,list));

			}	
				
				Collection<DashBoardStationCountDivisionWiseModel> uncleansedCountLists= stn_tbl_rbs_repo.getUncleansedStationCountsingledivision(division_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountSingleDivisionWise || getUncleansedStationCountsingledivision Query list return : "+uncleansedCountLists.size());

				uncleansedCountLists.forEach(DashBoardStationCountDivisionWiseModel -> callTotal(DashBoardStationCountDivisionWiseModel,list));
				
				
				Collection<DashBoardStationCountDivisionWiseModel> pendingApprovalCountLists= stn_unclsnd_repo.getPendingApprovalStationCountSingleDivisionWise(division_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountSingleDivisionWise || getPendingApprovalStationCountSingleDivisionWise Query list return : "+pendingApprovalCountLists.size());
		
				pendingApprovalCountLists.forEach(DashBoardStationCountDivisionWiseModel -> callPendingApproval(DashBoardStationCountDivisionWiseModel,list));

				
				Collection<DashBoardStationCountDivisionWiseModel> cleansedCountLists= stn_unclsnd_repo.getTotalCleansedStationCountSingleDivisionWise(division_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountSingleDivisionWise || getTotalCleansedStationCountSingleDivisionWise Query list return : "+cleansedCountLists.size());			
				cleansedCountLists.forEach(DashBoardStationCountDivisionWiseModel -> callCleansedCount(DashBoardStationCountDivisionWiseModel,list));			
			
				
				Collection<DashBoardStationCountDivisionWiseModel> draftCountLists= stn_unclsnd_repo.getTotalDraftForwardApprovalStationCountSingleDivisionWise(division_code);
				logger.info("Service : DashBoardStationService || Method: getStationCountSingleDivisionWise || getTotalDraftForwardApprovalStationCountSingleDivisionWise Query list return : "+draftCountLists.size());			
				draftCountLists.forEach(DashBoardStationCountDivisionWiseModel -> callDraftCount(DashBoardStationCountDivisionWiseModel,list));	
				
				
				
				
				
				return list;
			
		}
			//end changes
		
		private void callTotaldivisionwise(DashBoardStationCountDivisionWiseModel uncleansedObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			try {		
				uncleansedFlag=0;
				list.forEach(totalobj -> callTotalSub(uncleansedObj,totalobj));	
				if(uncleansedFlag==0){
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(uncleansedObj.getzone_code());
					obj.setDivision_code(uncleansedObj.getdivision_code());
					obj.setUncleansed_count(uncleansedObj.getuncleansed_count());
			//		list.add(obj);	
				}
					}catch (Exception e) {
				// TODO: Handle Exception
				e.getMessage();		}
		}
		private void callTotalSubdivisionwise(DashBoardStationCountDivisionWiseModel uncleansedObj,DashboardStationModel totalobj) {
			
			try {
			if(uncleansedObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())) {
				uncleansedFlag++;
				totalobj.setUncleansed_count(uncleansedObj.getuncleansed_count());
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		

		
		private void callPendingApprovaldivisionwise(DashBoardStationCountDivisionWiseModel pendingApprovObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			try {
			
				uncleansedFlag=0;
				list.forEach(totalobj -> callPendingApprovalSub(pendingApprovObj,totalobj));	
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(pendingApprovObj.getzone_code());					
					obj.setDivision_code(pendingApprovObj.getdivision_code());
					obj.setPending_approval(pendingApprovObj.getpending_approval());
			//		list.add(obj);	
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callPendingApprovalSubdivisionwise(DashBoardStationCountDivisionWiseModel pendingApprovObj,DashboardStationModel totalobj) {
			
			try {
			if(pendingApprovObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())) {
				uncleansedFlag++;
				totalobj.setPending_approval(pendingApprovObj.getpending_approval());		
		
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		
		private void callCleansedCountdivisionwise(DashBoardStationCountDivisionWiseModel cleansedObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			
			try {
				uncleansedFlag=0;
				list.forEach(totalobj -> callCleansedCountSub(cleansedObj,totalobj));

		
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(cleansedObj.getzone_code());
					obj.setDivision_code(cleansedObj.getdivision_code());
					obj.setCleansed_count(cleansedObj.getcleansed_count());
				//	list.add(obj);		
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callCleansedCountSubdivisionwise(DashBoardStationCountDivisionWiseModel cleansedObj,DashboardStationModel totalobj) {
			try {
			if(cleansedObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())){
				uncleansedFlag++;
				totalobj.setCleansed_count(cleansedObj.getcleansed_count());	
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		private void callDraftCountdivisionwise(DashBoardStationCountDivisionWiseModel draftObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			
			try {
				uncleansedFlag=0;
				list.forEach(totalobj -> callDraftCountSub(draftObj,totalobj));

		
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(draftObj.getzone_code());
					obj.setDivision_code(draftObj.getdivision_code());
					obj.setDraft_forward_approval_count(draftObj.getdraft_forward_approval_count());
				//	list.add(obj);		
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callDraftCountSubdivisionwise(DashBoardStationCountDivisionWiseModel draftObj,DashboardStationModel totalobj) {
			try {
			if(draftObj.getdivision_code().equalsIgnoreCase(totalobj.getDivision_code())){
				uncleansedFlag++;
				totalobj.setDraft_forward_approval_count(draftObj.getdraft_forward_approval_count());	
		//		System.out.println("draft add in list divcode"+ totalobj.getDivision_code()+"|| AND Draft count: "+totalobj.getDraft_forward_approval_count());
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		
		private void setTotalDivisiondivisionwise(DashBoardStationCountDivisionWiseModel DashBoardStationCountDivisionWiseModel,Collection<DashboardStationModel> list) {
		DashboardStationModel obj =new DashboardStationModel();	
//		obj.setZone_code(DashBoardStationCountDivisionWiseModel.getzone_code());
			
		obj.setDivision_code(DashBoardStationCountDivisionWiseModel.getdivision_code());
		obj.setTotal_division_count(DashBoardStationCountDivisionWiseModel.gettotal_division_count());			
		list.add(obj);
		
		}
		
		





// Shilpi 09-03-2021



		public List<DashboardStationModel> getLocoCountSingleShedWise(DashboardStationModel objshedid) {
			String shedid =objshedid.getLoco_Owningshed();
			
			//String shedid =objshedid.getElec_locoOwningShed();
			List<DashboardStationModel> list= new ArrayList<DashboardStationModel>();		
			Collection<DashBoardLocoCountShedWiseModel> totalCountLists= loco_tbl_fois_repo.getLocoSingleShed(shedid);
				logger.info("Service : DashBoardStationService || Method: getLocoSingleShed || getLocoSingleShed Query list return : "+totalCountLists);
				if(totalCountLists.size()>0) {
				totalCountLists.forEach(DashBoardLocoCountShedWiseModel -> setTotalShedwise(DashBoardLocoCountShedWiseModel,list));

			}	
				
				Collection<DashBoardLocoCountShedWiseModel> uncleansedCountLists= loco_tbl_fois_repo.getUncleansedLocoSingleShed(shedid);
				logger.info("Service : DashBoardStationService || Method: getUncleansedLocoSingleShed || getUncleansedLocoSingleShed Query list return : "+uncleansedCountLists.size());

				uncleansedCountLists.forEach(DashBoardLocoCountShedWiseModel -> callTotalShedwise(DashBoardLocoCountShedWiseModel,list));
				
				
					
				Collection<DashBoardLocoCountShedWiseModel> pendingApprovalCountLists= loco_tbl_repo.getLocoPendingSingleshed(shedid);
				logger.info("Service : DashBoardStationService || Method: getLocoPendingSingleshed || getLocoPendingSingleshed Query list return : "+pendingApprovalCountLists.size());
		
				pendingApprovalCountLists.forEach(DashBoardLocoCountShedWiseModel -> callPendingApprovalShedwise(DashBoardLocoCountShedWiseModel,list));

		
				
				Collection<DashBoardLocoCountShedWiseModel> cleansedCountLists= loco_tbl_approve.getLocoApprovedSingleshed(shedid);
				logger.info("Service : DashBoardStationService || Method: getLocoApprovalSingleshed || getLocoApprovalSingleshed Query list return : "+cleansedCountLists.size());			
				cleansedCountLists.forEach(DashBoardLocoCountShedWiseModel -> callCleansedCountShedwise(DashBoardLocoCountShedWiseModel,list));			
			
				
				Collection<DashBoardLocoCountShedWiseModel> draftCountLists= loco_tbl_repo.getDraftLocoApprovalSingleshed(shedid);
				logger.info("Service : DashBoardStationService || Method: getDraftLocoApprovalSingleshed || getDraftLocoApprovalSingleshed Query list return : "+draftCountLists.size());			
				draftCountLists.forEach(DashBoardLocoCountShedWiseModel -> callDraftCountShedwise(DashBoardLocoCountShedWiseModel,list));	
				
				
				
				
				
				return list;
			
		}
			//end changes
		
		private void callTotalShedwise(DashBoardLocoCountShedWiseModel uncleansedObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			try {		
				uncleansedFlag=0;
				list.forEach(totalobj -> callTotalSubShedwise(uncleansedObj,totalobj));	
				if(uncleansedFlag==0){
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(uncleansedObj.getzone_code());
					obj.setLoco_Owningshed(uncleansedObj.getLoco_Owningshed());
					obj.setUncleansed_count(uncleansedObj.getuncleansed_count());
			//		list.add(obj);	
				}
					}catch (Exception e) {
				// TODO: Handle Exception
				e.getMessage();		}
		}
		private void callTotalSubShedwise(DashBoardLocoCountShedWiseModel uncleansedObj,DashboardStationModel totalobj) {
			
			try {
			if(uncleansedObj.getLoco_Owningshed().equalsIgnoreCase(totalobj.getLoco_Owningshed())) {
				uncleansedFlag++;
				totalobj.setUncleansed_count(uncleansedObj.getuncleansed_count());
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		

		
		private void callPendingApprovalShedwise(DashBoardLocoCountShedWiseModel pendingApprovObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			try {
			
				uncleansedFlag=0;
				list.forEach(totalobj -> callPendingApprovalSubShedwise(pendingApprovObj,totalobj));	
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(pendingApprovObj.getzone_code());					
					obj.setLoco_Owningshed(pendingApprovObj.getLoco_Owningshed());
					obj.setPending_approval(pendingApprovObj.getpending_approval());
			//		list.add(obj);	
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callPendingApprovalSubShedwise(DashBoardLocoCountShedWiseModel pendingApprovObj,DashboardStationModel totalobj) {
			
			try {
			if(pendingApprovObj.getLoco_Owningshed().equalsIgnoreCase(totalobj.getLoco_Owningshed())) {
				uncleansedFlag++;
				totalobj.setPending_approval(pendingApprovObj.getpending_approval());		
		
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		
		private void callCleansedCountShedwise(DashBoardLocoCountShedWiseModel cleansedObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			
			try {
				uncleansedFlag=0;
				list.forEach(totalobj -> callCleansedCountSubShedwise(cleansedObj,totalobj));

		
				if(uncleansedFlag==0) {
					DashboardStationModel obj = new DashboardStationModel();
//					obj.setZone_code(cleansedObj.getzone_code());
					obj.setLoco_Owningshed(cleansedObj.getLoco_Owningshed());
					obj.setCleansed_count(cleansedObj.getcleansed_count());
				//	list.add(obj);		
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callCleansedCountSubShedwise(DashBoardLocoCountShedWiseModel cleansedObj,DashboardStationModel totalobj) {
			try {
			if(cleansedObj.getLoco_Owningshed().equalsIgnoreCase(totalobj.getLoco_Owningshed())){
				uncleansedFlag++;
				totalobj.setCleansed_count(cleansedObj.getcleansed_count());	
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		private void callDraftCountShedwise(DashBoardLocoCountShedWiseModel draftObj,Collection< DashboardStationModel>list) {
			// TODO Auto-generated method stub
			
			try {
				uncleansedFlag=0;
				list.forEach(totalobj -> callDraftCountShedwise(draftObj,totalobj));

		
				if(uncleansedFlag==0) {
					DashboardLocoModel obj = new DashboardLocoModel();
//					obj.setZone_code(draftObj.getzone_code());
					obj.setLoco_Owningshed(draftObj.getLoco_Owningshed());
//					obj.setElec_locoOwningShed(draftObj.getelec_locoOwningShed());
					obj.setDraft_forward_approval_count(draftObj.getDraft_forward_approval_count());
				//	list.add(obj);		
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			
		}
		private void callDraftCountShedwise(DashBoardLocoCountShedWiseModel draftObj,DashboardStationModel totalobj) {
			try {
			if(draftObj.getLoco_Owningshed().equalsIgnoreCase(totalobj.getLoco_Owningshed())){
				uncleansedFlag++;
				totalobj.setDraft_forward_approval_count(draftObj.getDraft_forward_approval_count());	
		//		System.out.println("draft add in list divcode"+ totalobj.getshedid()+"|| AND Draft count: "+totalobj.getDraft_forward_approval_count());
			}
			}catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
		}
		
		
		private void setTotalShedwise(DashBoardLocoCountShedWiseModel DashBoardLocoCountShedWiseModel,Collection<DashboardStationModel> list) {
			DashboardStationModel obj =new DashboardStationModel();	
//		obj.setZone_code(DashBoardStationCountDivisionWiseModel());			
		obj.setLoco_Owningshed(DashBoardLocoCountShedWiseModel.getLoco_Owningshed());
		obj.setTotal_loco_count(DashBoardLocoCountShedWiseModel.getTotal_loco_count());			
		list.add(obj);
		
		}
		
		
		// Shilpi 16-03-2021



				public List<DashboardStationModel> getCoachCountSingleDepotWise(DashboardStationModel objcoachid) {
					String owning_depot =objcoachid.getOwning_depot();
					
					
					
					List<DashboardStationModel> list= new ArrayList<DashboardStationModel>();		
					Collection<DashBoardCoachCountDepoWiseModel> totalCountLists= coach_cmm_repo.getCoachSingleDepo(owning_depot);
						logger.info("Service : DashBoardStationService || Method: getCaochSingleDepo || getCaochSingleDepo Query list return : "+totalCountLists);
						if(totalCountLists.size()>0) {
						totalCountLists.forEach(DashBoardCoachCountDepoWiseModel -> setTotalShedwise(DashBoardCoachCountDepoWiseModel,list));

					}	
						
						Collection<DashBoardCoachCountDepoWiseModel> uncleansedCountLists= coach_cmm_repo.getUncleansedCoachSingleDepo(owning_depot);
						logger.info("Service : DashBoardStationService || Method: getUncleansedCoachSingleDepo || getUncleansedCoachSingleDepo Query list return : "+uncleansedCountLists.size());

						uncleansedCountLists.forEach(DashBoardCoachCountDepoWiseModel -> callTotalShedwise(DashBoardCoachCountDepoWiseModel,list));
						
						
							
						Collection<DashBoardCoachCountDepoWiseModel> pendingApprovalCountLists= coach_unclean_repo.getCoachPendingSingledepo(owning_depot);
						logger.info("Service : DashBoardStationService || Method: getCoachPendingSingledepo || getCoachPendingSingledepo Query list return : "+pendingApprovalCountLists.size());
				
						pendingApprovalCountLists.forEach(DashBoardCoachCountDepoWiseModel -> callPendingApprovalShedwise(DashBoardCoachCountDepoWiseModel,list));

				
						
						Collection<DashBoardCoachCountDepoWiseModel> cleansedCountLists= coach_clean_repo.getCoachApprovedSingleDepo(owning_depot);
						logger.info("Service : DashBoardStationService || Method: getCoachApprovedSingleDepo || getCoachApprovedSingleDepo Query list return : "+cleansedCountLists.size());			
						cleansedCountLists.forEach(DashBoardCoachCountDepoWiseModel -> callCleansedCountShedwise(DashBoardCoachCountDepoWiseModel,list));			
					
						
						Collection<DashBoardCoachCountDepoWiseModel> draftCountLists= coach_unclean_repo.getDraftCoachApprovalSingledepo(owning_depot);
						logger.info("Service : DashBoardStationService || Method: getDraftCoachApprovalSingledepo || getDraftCoachApprovalSingledepo Query list return : "+draftCountLists.size());			
						draftCountLists.forEach(DashBoardCoachCountDepoWiseModel -> callDraftCountShedwise(DashBoardCoachCountDepoWiseModel,list));	
						
						
						
						
						
						return list;
					
				}
					//end changes
				
				private void callTotalShedwise(DashBoardCoachCountDepoWiseModel uncleansedObj,Collection< DashboardStationModel>list) {
					// TODO Auto-generated method stub
					try {		
						uncleansedFlag=0;
						list.forEach(totalobj -> callTotalSubShedwise(uncleansedObj,totalobj));	
						if(uncleansedFlag==0){
							DashboardStationModel obj = new DashboardStationModel();
//							obj.setZone_code(uncleansedObj.getzone_code());
							obj.setOwning_depot(uncleansedObj.getOwning_depot());
							obj.setUncleansed_count(uncleansedObj.getuncleansed_count());
					//		list.add(obj);	
						}
							}catch (Exception e) {
						// TODO: Handle Exception
						e.getMessage();		}
				}
				private void callTotalSubShedwise(DashBoardCoachCountDepoWiseModel uncleansedObj,DashboardStationModel totalobj) {
					
					try {
					if(uncleansedObj.getOwning_depot().equalsIgnoreCase(totalobj.getOwning_depot())) {
						uncleansedFlag++;
						totalobj.setUncleansed_count(uncleansedObj.getuncleansed_count());
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				

				
				private void callPendingApprovalShedwise(DashBoardCoachCountDepoWiseModel pendingApprovObj,Collection< DashboardStationModel>list) {
					// TODO Auto-generated method stub
					try {
					
						uncleansedFlag=0;
						list.forEach(totalobj -> callPendingApprovalSubShedwise(pendingApprovObj,totalobj));	
						if(uncleansedFlag==0) {
							DashboardStationModel obj = new DashboardStationModel();
//							obj.setZone_code(pendingApprovObj.getzone_code());					
							obj.setOwning_depot(pendingApprovObj.getOwning_depot());
							obj.setPending_approval(pendingApprovObj.getpending_approval());
					//		list.add(obj);	
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
				}
				private void callPendingApprovalSubShedwise(DashBoardCoachCountDepoWiseModel pendingApprovObj,DashboardStationModel totalobj) {
					
					try {
					if(pendingApprovObj.getOwning_depot().equalsIgnoreCase(totalobj.getOwning_depot())) {
						uncleansedFlag++;
						totalobj.setPending_approval(pendingApprovObj.getpending_approval());		
				
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				
				
				private void callCleansedCountShedwise(DashBoardCoachCountDepoWiseModel cleansedObj,Collection< DashboardStationModel>list) {
					// TODO Auto-generated method stub
					
					try {
						uncleansedFlag=0;
						list.forEach(totalobj -> callCleansedCountSubShedwise(cleansedObj,totalobj));

				
						if(uncleansedFlag==0) {
							DashboardStationModel obj = new DashboardStationModel();
//							obj.setZone_code(cleansedObj.getzone_code());
							obj.setOwning_depot(cleansedObj.getOwning_depot());
							obj.setCleansed_count(cleansedObj.getcleansed_count());
						//	list.add(obj);		
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
				}
				private void callCleansedCountSubShedwise(DashBoardCoachCountDepoWiseModel cleansedObj,DashboardStationModel totalobj) {
					try {
					if(cleansedObj.getOwning_depot().equalsIgnoreCase(totalobj.getOwning_depot())){
						uncleansedFlag++;
						totalobj.setCleansed_count(cleansedObj.getcleansed_count());	
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				
				private void callDraftCountShedwise(DashBoardCoachCountDepoWiseModel draftObj,Collection< DashboardStationModel>list) {
					// TODO Auto-generated method stub
					
					try {
						uncleansedFlag=0;
						list.forEach(totalobj -> callDraftCountShedwise(draftObj,totalobj));

				
						if(uncleansedFlag==0) {
							DashboardStationModel obj = new DashboardStationModel();
//							obj.setZone_code(draftObj.getzone_code());
							obj.setOwning_depot(draftObj.getOwning_depot());
//							obj.setElec_locoOwningShed(draftObj.getelec_locoOwningShed());
							obj.setDraft_forward_approval_count(draftObj.getdraft_forward_approval_count());
						//	list.add(obj);		
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
				}
				private void callDraftCountShedwise(DashBoardCoachCountDepoWiseModel draftObj,DashboardStationModel totalobj) {
					try {
					if(draftObj.getOwning_depot().equalsIgnoreCase(totalobj.getOwning_depot())){
						uncleansedFlag++;
						totalobj.setDraft_forward_approval_count(draftObj.getdraft_forward_approval_count());	
				//		System.out.println("draft add in list divcode"+ totalobj.getowning_depot()+"|| AND Draft count: "+totalobj.getDraft_forward_approval_count());
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				
				
				private void setTotalShedwise(DashBoardCoachCountDepoWiseModel DashBoardCoachCountDepoWiseModel,Collection<DashboardStationModel> list) {
				DashboardStationModel obj =new DashboardStationModel();	
//				obj.setZone_code(DashBoardStationCountDivisionWiseModel.getzone_code());
					
				obj.setOwning_depot(DashBoardCoachCountDepoWiseModel.getOwning_depot());
				obj.setTotal_depo_count(DashBoardCoachCountDepoWiseModel.getTotal_depo_count());			
				list.add(obj);
				
				}
				
				
// Shilpi 19-03-2021
				
				public List<DashboardLocoModel> getLocoCountZoneWise(DashboardLocoModel objzone_code) {
					String loco_owning_zone_code =objzone_code.getLoco_owning_zone_code();					
					List<DashboardLocoModel> list= new ArrayList<DashboardLocoModel>();		
					Collection<DashBoardLocoCountShedWiseModel> totalCountListz= loco_tbl_fois_repo.getLocoZoneShed(loco_owning_zone_code);
						logger.info("Service : DashBoardStationService || Method: getLocoZoneShed || getLocoZoneShed Query list return : "+totalCountListz);
						if(totalCountListz.size()>0) {
						totalCountListz.forEach(DashBoardLocoCountShedWiseModel -> setTotalZoneShed(DashBoardLocoCountShedWiseModel,list));

					}	
						
						Collection<DashBoardLocoCountShedWiseModel> uncleansedCountListz= loco_tbl_fois_repo.getUncleansedLocoZoneShed(loco_owning_zone_code);
						logger.info("Service : DashBoardStationService || Method: getUncleansedLocoZoneShed || getUncleansedLocoZoneShed Query list return : "+uncleansedCountListz.size());

						uncleansedCountListz.forEach(DashBoardLocoCountShedWiseModel -> callUncleansedLocoZoneShed(DashBoardLocoCountShedWiseModel,list));
						
						
							
						Collection<DashBoardLocoCountShedWiseModel> pendingApprovalCountListz= loco_tbl_repo.getLocoPendingZoneshed(loco_owning_zone_code);
						logger.info("Service : DashBoardStationService || Method: getLocoPendingZoneshed || getLocoPendingZoneshed Query list return : "+pendingApprovalCountListz.size());
				
						pendingApprovalCountListz.forEach(DashBoardLocoCountShedWiseModel -> callLocoPendingZoneshed(DashBoardLocoCountShedWiseModel,list));

				
						
						Collection<DashBoardLocoCountShedWiseModel> cleansedCountListz= loco_tbl_approve.getLocoApprovedZoneShed(loco_owning_zone_code);
						logger.info("Service : DashBoardStationService || Method: getLocoApprovedZoneShed || getLocoApprovedZoneShed Query list return : "+cleansedCountListz.size());			
						cleansedCountListz.forEach(DashBoardLocoCountShedWiseModel -> callApprovedZoneShed(DashBoardLocoCountShedWiseModel,list));			
					
						
						Collection<DashBoardLocoCountShedWiseModel> draftCountListz= loco_tbl_repo.getDraftLocoApprovalZoneshed(loco_owning_zone_code);
						logger.info("Service : DashBoardStationService || Method: getDraftLocoApprovalZoneshed || getDraftLocoApprovalZoneshed Query list return : "+draftCountListz.size());			
						draftCountListz.forEach(DashBoardLocoCountShedWiseModel -> callDraftLocoApprovalZone(DashBoardLocoCountShedWiseModel,list));	
						
						
						
						
						
						return list;
					
				}
					//end changes
				
				private void callUncleansedLocoZoneShed(DashBoardLocoCountShedWiseModel uncleansedObj,Collection< DashboardLocoModel>list) {
					// TODO Auto-generated method stub
					try {		
						uncleansedFlag=0;
						list.forEach(totalobj -> callTotalSubShedwise1(uncleansedObj,totalobj));	
						if(uncleansedFlag==0){
							DashboardStationModel obj = new DashboardStationModel();
							obj.setLoco_owning_zone_code(uncleansedObj.getLoco_owning_zone_code());
							obj.setLoco_Owningshed(uncleansedObj.getLoco_Owningshed());
							obj.setUncleansed_count(uncleansedObj.getuncleansed_count());
					//		list.add(obj);	
						}
							}catch (Exception e) {
						// TODO: Handle Exception
						e.getMessage();		}
				}
				private void callTotalSubShedwise1(DashBoardLocoCountShedWiseModel uncleansedObj,DashboardLocoModel totalobj) {
					
					try {
					if(uncleansedObj.getLoco_owning_zone_code().equalsIgnoreCase(totalobj.getLoco_owning_zone_code())) {
						uncleansedFlag++;
						totalobj.setUncleansed_count(uncleansedObj.getuncleansed_count());
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				

				
				private void callLocoPendingZoneshed(DashBoardLocoCountShedWiseModel pendingApprovObj,Collection< DashboardLocoModel>list) {
					// TODO Auto-generated method stub
					try {
					
						uncleansedFlag=0;
						list.forEach(totalobj -> callPendingApprovalSubShedwise1(pendingApprovObj,totalobj));	
						if(uncleansedFlag==0) {
							DashboardLocoModel obj = new DashboardLocoModel();
							obj.setLoco_owning_zone_code(pendingApprovObj.getLoco_owning_zone_code());					
							obj.setLoco_Owningshed(pendingApprovObj.getLoco_Owningshed());
							obj.setPending_approval(pendingApprovObj.getpending_approval());
					//		list.add(obj);	
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
				}
				private void callPendingApprovalSubShedwise1(DashBoardLocoCountShedWiseModel pendingApprovObj,DashboardLocoModel totalobj) {
					
					try {
					if(pendingApprovObj.getLoco_owning_zone_code().equalsIgnoreCase(totalobj.getLoco_owning_zone_code())) {
						uncleansedFlag++;
						totalobj.setPending_approval(pendingApprovObj.getpending_approval());		
				
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				
				
				private void callApprovedZoneShed(DashBoardLocoCountShedWiseModel cleansedObj,Collection< DashboardLocoModel>list) {
					// TODO Auto-generated method stub
					
					try {
						uncleansedFlag=0;
						list.forEach(totalobj -> callCleansedCountSubShedwise1(cleansedObj,totalobj));

				
						if(uncleansedFlag==0) {
							DashboardLocoModel obj = new DashboardLocoModel();
							obj.setLoco_owning_zone_code(cleansedObj.getLoco_owning_zone_code());
							obj.setLoco_Owningshed(cleansedObj.getLoco_Owningshed());
							obj.setCleansed_count(cleansedObj.getcleansed_count());
						//	list.add(obj);		
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
				}
				private void callCleansedCountSubShedwise1(DashBoardLocoCountShedWiseModel cleansedObj,DashboardLocoModel totalobj) {
					try {
					if(cleansedObj.getLoco_owning_zone_code().equalsIgnoreCase(totalobj.getLoco_owning_zone_code())){
						uncleansedFlag++;
						totalobj.setCleansed_count(cleansedObj.getcleansed_count());	
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				
				private void callDraftLocoApprovalZone(DashBoardLocoCountShedWiseModel draftObj,Collection< DashboardLocoModel>list) {
					// TODO Auto-generated method stub
					
					try {
						uncleansedFlag=0;
						list.forEach(totalobj -> callDraftCountShedwise1(draftObj,totalobj));

				
						if(uncleansedFlag==0) {
							DashboardLocoModel obj = new DashboardLocoModel();
							obj.setLoco_owning_zone_code(draftObj.getLoco_owning_zone_code());
							obj.setLoco_Owningshed(draftObj.getLoco_Owningshed());
//							obj.setElec_locoOwningShed(draftObj.getelec_locoOwningShed());
							obj.setDraft_forward_approval_count(draftObj.getDraft_forward_approval_count());
						//	list.add(obj);		
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
					
				}
				private void callDraftCountShedwise1(DashBoardLocoCountShedWiseModel draftObj,DashboardLocoModel totalobj) {
					try {
					if(draftObj.getLoco_owning_zone_code().equalsIgnoreCase(totalobj.getLoco_owning_zone_code())){
						uncleansedFlag++;
						totalobj.setDraft_forward_approval_count(draftObj.getDraft_forward_approval_count());	
				//		System.out.println("draft add in list divcode"+ totalobj.getshedid()+"|| AND Draft count: "+totalobj.getDraft_forward_approval_count());
					}
					}catch (Exception e) {
						// TODO: handle exception
						e.getMessage();
					}
				}
				
				
				private void setTotalZoneShed(DashBoardLocoCountShedWiseModel DashBoardLocoCountShedWiseModel,Collection<DashboardLocoModel> list) {
					DashboardLocoModel obj =new DashboardLocoModel();	
				obj.setLoco_owning_zone_code(DashBoardLocoCountShedWiseModel.getLoco_owning_zone_code());				
				obj.setLoco_Owningshed(DashBoardLocoCountShedWiseModel.getLoco_Owningshed());
				
				obj.setTotal_loco_count(DashBoardLocoCountShedWiseModel.getTotal_loco_count());			
				list.add(obj);
				
				}
				
				
				
}

