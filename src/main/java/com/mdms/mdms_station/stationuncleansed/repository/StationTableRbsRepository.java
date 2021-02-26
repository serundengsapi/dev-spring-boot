package com.mdms.mdms_station.stationuncleansed.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mdms.mdms_station.stationuncleansed.model.StationTableRbs;
import com.mdms.dahsboard.model.DashBoardStationCountDivisionWiseModel;
import com.mdms.mdms_station.stationuncleansed.model.RbsPKey;
public interface StationTableRbsRepository  extends CrudRepository<StationTableRbs,RbsPKey>{
	
	@Query(value="select distinct stn_code from  mdms_station.station_table_rbs where div_ser_no=?1 except\r\n" + 
			"	select distinct station_code from mdms_station.station_uncleansed_data where cmi_status IN ('U','A')", nativeQuery = true)
	List<String>getDivisionalStnCodeCmi(int divsno);
	
	
	
	
	@Query(value="select distinct stn_code from  mdms_station.station_table_rbs where div_ser_no=?1 except\r\n" + 
			"	select distinct station_code from mdms_station.station_uncleansed_data where dti_status IN ('U','A')", nativeQuery = true)
	List<String>getDivisionalStnCodeDti(int divsno);
	
	@Query(value="select * from  mdms_station.station_table_rbs where stn_code=?1 and stn_vld_upto=("
			+ "select stn_vld_upto from mdms_station.station_table_rbs where stn_code=?1 order by stn_vld_upto DESC LIMIT 1)", nativeQuery = true)
	StationTableRbs getStationRecordRBS(String station_code);
	

	  @Query(value="SELECT division_code,count(*) as total_division_count FROM mdms_masters.m_division where zone_code=?1 group by division_code",nativeQuery=true)
	  Collection<DashBoardStationCountDivisionWiseModel> getTotalStationCountDivisionWise(String zone_code);	  
	  
	  @Query(value=" select division_code,uncleansed_count from (\r\n"
	  		+ "			  select e.zone_code,f.division_code,count(*) as uncleansed_count from (select distinct  division_code, stn_code\r\n"
	  		+ "																	  FROM mdms_station.station_table_rbs as a join mdms_masters.m_division as b\r\n"
	  		+ "	  				on a.div_ser_no= b.division_sr_no except select division_code,station_code FROM mdms_station.station_uncleansed_data) AS f\r\n"
	  		+ "					join mdms_masters.m_division as e\r\n"
	  		+ "					on f.division_code=e.division_code\r\n"
	  		+ "					where e.zone_code=?1\r\n"
	  		+ "					group by 1,2 ) as aa",nativeQuery=true)
			  Collection<DashBoardStationCountDivisionWiseModel> getUncleansedStationCountDivisionWise(String zone_code);
			  
  

}
