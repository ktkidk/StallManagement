package com.altimetrik.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.altimetrik.model.Stall;
import com.altimetrik.service.StallService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StallController {
	@Autowired
	private StallService stallService;
	
	@PostMapping("/insertStall")
	public @ResponseBody Map<String,Object> insertStall(@RequestBody Stall stall)
	{
		Map<String,Object> msg = new HashMap<String,Object>();
		/*
		 * msg.put("Status", 7874); msg.put("Error_Message","Insertion is not allowed!");
		 * return msg;
		 *
		 *
		 * if(stall.getStallName()== null || stall.getStallType()==null ||
		 * stall.getStallOwner()==null ||stall.getStallFare()==null ) {
		 * msg.put("Status", 7828); msg.put("Error_Message",
		 * "All fields are mandatory!"); return msg; }
		 */

		msg.putAll(validateFields(stall,msg));
		if(msg.isEmpty()) {	
			stallService.insertStall(stall);			
			msg.put("Status", 200);
			msg.put("Success Message", "Stall Data Inserted Successfully!");
		}
		System.out.println(msg);
		return msg;
		
	}
		
		
	
	  @GetMapping("/getAllStallNames") 
	  public @ResponseBody List<String> getAllStallNames() 
	  {	  	  
		  return stallService.getAllStall().stream().map(stall->stall.getStallName()).collect(Collectors.toList());
	  }
	  
	  @GetMapping("/getAllStalls") 
	  public @ResponseBody List<Stall> getAllStallDetails() 
	  {	  	  
		  return stallService.getAllStall();
	  }
	    
	
	  @GetMapping("/getSingleStall/{id}") 
	  public @ResponseBody Map<String,Object> getStall(@PathVariable String id) 
	  { 
		  Map<String,Object> msg = new HashMap<String,Object>();
		  if(!isNumeric(id)) {
			  msg.put("Status", 8800);
			  msg.put("Error_Message", "Please Provide Valid Stall_Id! Id must be a Numeric!");
			  return msg;
		  }
		  
		  Stall s = stallService.getSingleStall(Integer.valueOf(id));
			if(s==null) {			  
				  msg.put("Status", 4400);
				  msg.put("Error_Message", "Invalid Stall_Id! No Stall Exists with ID: "+id);
				  return msg;
			  }else {
				  msg.put("Stall ID", s.getStallId());
				  msg.put("Stall Name", s.getStallName());
				  msg.put("Stall Type", s.getStallType());
				  msg.put("Stall Owner", s.getStallOwner());
				  msg.put("Stall Fare", s.getStallFare());
			  }
		  return msg;
	  }
	  @GetMapping("/getStallByName/{name}") 
	  public @ResponseBody Stall getStallByName(@PathVariable String name) 
	  { 
		  Map<String,Object> msg = new HashMap<String,Object>();
		  if(!stallService.getAllStall().stream().map(sta->sta.getStallName()).collect(Collectors.toList()).contains(name)) {
				 
				msg.put("Status", 4911);
				  msg.put("Error_Message", "Invalid Stall_Name! Please Provide Proper Stall_Name!");
			
		  }
		  int id=stallService.getAllStall().stream().filter(s->s.getStallName().equals(name)).mapToInt(s->s.getStallId()).sum();
		  
		  return stallService.getSingleStall(id);
		  
	  }
	  
	 
	  
	  @PutMapping("/updateStallData") 
	  public @ResponseBody Map<String,Object> updateStallData(@RequestBody Stall stall) {
		  Map<String,Object> msg = new HashMap<String,Object>();
		  if(!stallService.getAllStall().stream().map(sta->sta.getStallName()).collect(Collectors.toList()).contains(stall.getStallName())) {
			 
					msg.put("Status", 4911);
					  msg.put("Error_Message", "Invalid Stall_Name! Please Provide Proper Stall_Name!");
					  return msg;
		  }
			  stall.setStallId(stallService.getAllStall().stream().filter(sta->sta.getStallName().equals(stall.getStallName())).mapToInt(sta->sta.getStallId()).sum());
			 
			 autoFill(stall);
				
				msg = validateFields(stall,msg);
				if(msg.isEmpty()) {
					stallService.updateStall(stall);
					msg.put("Status", 200);
					msg.put("Success Message", "Stall Details Updated Successfully!");
				}
			
		  
		  return msg;
	  };
	
	  @PutMapping("/updateStall") 
	  public @ResponseBody Map<String,Object> updateStall(@RequestBody Stall stall) 
	  { 
			Map<String,Object> msg = new HashMap<String,Object>();
					
			if(stall.getStallId()==null) {
				msg.put("Status", 4700);
				  msg.put("Error_Message", "Invalid Stall_Id! Please Provide Proper Stall_Id!");
				  return msg;
			}
			
			Stall s = stallService.getSingleStall(stall.getStallId());
			
			if(s==null) {			  
				msg.put("Status", 4400);
				  msg.put("Error_Message", "Invalid Stall_Id! No Stall Exists with ID: "+stall.getStallId());
				  return msg;
			  }
			
			autoFill(stall);
			 
			if(!s.getStallName().equals(stall.getStallName())) {
				msg.put("Status", 3411);
				msg.put("Error_Message", "Stall_Name Field cannot be changed!");
				return msg;
			}
			
			msg = validateFields(stall,msg);
			if(msg.isEmpty()) {
				stallService.updateStall(stall);
				msg.put("Status", 200);
				msg.put("Success Message", "Stall Details Updated Successfully!");
			}
			return msg;
	  
	  }
	  
	  @DeleteMapping("/deleteStallByName/{name}")
	  @ResponseBody Map<String,Object> deleteStallByName(@PathVariable String name){
		  Map<String,Object> msg = new HashMap<String,Object>();
		  if(stallService.deleteStallByName(name)>0) {
			  msg.put("Status", 200);
			  msg.put("Success Message", "Stall Details Deleted Successfully!");
			  return msg;
		  }
		  msg.put("Status", 4411);
		  msg.put("Error_Message", "Invalid Stall_Name! No Stall Exists with Name: "+name);
		  return msg;
	  }
	  
	  @DeleteMapping("/deleteStall/{id}") 
	  public @ResponseBody Map<String,Object> deleteStall(@PathVariable String id) 
	  {   
		  Map<String,Object> msg = new HashMap<String,Object>();
		  if(!isNumeric(id)) {
			  msg.put("Status", 8800);
			  msg.put("Error_Message", "Please Provide Valid Stall_Id! Id must be a Numeric!");
			  return msg;
		  }
		  Stall s =stallService.getSingleStall(Integer.valueOf(id));
		  if(s==null) {			  
			  msg.put("Status", 4400);
			  msg.put("Error_Message", "Invalid Stall_Id! No Stall Exists with ID: "+id);
			  return msg;
		  }
		  stallService.deleteStall(s);
			msg.put("Status", 200);
			msg.put("Success Message", "Stall Details Deleted Successfully!");
		  return msg;
	  }	  
	  
	  private static boolean isNumeric(String strNum) {
		  if (strNum == null) {
		        return false;
		    }
		    try {
		        Double d = Double.parseDouble(strNum);
		    } catch (Exception nfe) {
		        return false;
		    }
		    return true;
		}
	  
	  private static boolean containsNumeric(String str){
		  
		  for (int i = 0; i < str.length(); i++) { 
			
	            if (Character.isDigit(str.charAt(i))) { 
	                return true; 
	            } 	      
	        } 
	        return false; 		 
	  }
	  
	  private static boolean containsSpecialChar(String str) {
		  List<Character> specialCharacterSet = new ArrayList<Character>(Arrays.asList('~','`','!','@','#','$','%','^','&','*','(',')','_','-','+','=','{','}','[',']','|','\\',':',';','<','>','?','/',',','.'));
		  for (int i = 0; i < str.length(); i++) { 
			  Character ch=str.charAt(i);
			  System.out.println(ch);
			  if(specialCharacterSet.contains(ch))
				  return true;
			
	        } 
		  return false; 		
	  }
	  
	  private void autoFill(Stall stall) {
		  Stall s = stallService.getSingleStall(stall.getStallId());
		  if(stall.getStallName()== null )
			{
				stall.setStallName(s.getStallName());	
			}
			if(stall.getStallType()==null) {
				stall.setStallType(s.getStallType());
			}
			if( stall.getStallOwner()==null )
			{
				stall.setStallOwner(s.getStallOwner());
			}
			
			if(stall.getStallFare()==null && !stall.getStatus()) 
			{ 
				  stall.setStallFare(s.getStallFare()); 
			}
	  }
	  
	  private static Map<String,Object> validateFields(Stall stall, Map<String,Object> msg) {
		  	
		  if(stall.getStallName()==null ) {					// NULL FIELD VALIDATION
				
				msg.put("Status", 7511);
				msg.put("Error_Message", "Stall_Name Field cannot be null!");
				return msg;
			}
			if(stall.getStallType()==null ) {
				
				msg.put("Status", 7512);
				msg.put("Error_Message", "Stall_Type Field cannot be null!");
				return msg;
			}
			if( stall.getStallOwner()==null  ) {
				
				msg.put("Status", 7513);
				msg.put("Error_Message", "Stall_Owner Field cannot be null!");
				return msg;
			}
			if(stall.getStallFare()==null && !stall.getStatus()) {
				msg.put("Status", 7514);
				msg.put("Error_Message", "Stall_Fare Field cannot be null!");
				return msg;
			}
		  
		  
		  if(isNumeric(stall.getStallName()))				// COMPLETE NUMERIC VALIDATION FOR STALL_NAME
			{
				msg.put("Status", 3711);
				msg.put("Error_Message", "Please enter a valid  Stall_Name! Complete Numeric is Not Allowed!");
				return msg;
			}
		  
		  
		  if(stall.getStallName().isBlank()) {				// EMPTY VALIDATION
				msg.put("Status", 4011);
				msg.put("Error_Message", "Stall_Name Field cannot be blank! Please Enter Valid Stall_Name!"); 	
				return msg;
			}
		  if(stall.getStallType().isBlank()) {
				msg.put("Status", 4012);
				msg.put("Error_Message", "Stall_Type Field cannot be blank! Please Enter Valid Stall_Type!"); 	
				return msg;
			}
			if(stall.getStallOwner().isBlank()) {
				msg.put("Status", 4013);
				msg.put("Error_Message", "Stall_Owner Field cannot be blank! Please Enter Valid Stall_Owner_Name!");
				return msg;
			}
			
			
			if(containsNumeric(stall.getStallType()))				// PARTIAL-COMPLETE NUMERIC VALIDATION
			{
				msg.put("Status", 4512);
				msg.put("Error_Message", "Please enter a valid Stall_Type! Numeric Values are Not Allowed!");
				return msg;
			}
				
			if(containsNumeric(stall.getStallOwner()))
			{
				msg.put("Status", 4513);
				msg.put("Error_Message", "Please enter a valid Stall_Owner_Name! Numeric Values are Not Allowed!");
				return msg;
			}
			
			
			if(containsSpecialChar(stall.getStallType())){				// SPECIAL CHARACTER VALIDATION
				msg.put("Status", 3012);
				msg.put("Error_Message", "Please enter a valid Stall_Type! Special Characters are Not Allowed!");
				return msg;
			}
			if(containsSpecialChar(stall.getStallOwner()))
			{
				msg.put("Status", 3013);
				msg.put("Error_Message", "Please enter a valid Stall_Owner_Name! Special Characters are Not Allowed!");
				return msg;
			}
			
			
			try {
				stall.getStallFare().isNaN();			         	// DOUBLE VALUE VALIDATION
			}catch(Exception e) {
				msg.put("Status", 5014);
				msg.put("Error_Message", "Stall_Fare Field can be of type double only!");
				return msg;
			}
			return msg;
	  }
}
