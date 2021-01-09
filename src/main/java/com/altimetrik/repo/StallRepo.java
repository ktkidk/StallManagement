package com.altimetrik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.altimetrik.model.Stall;


@Repository
public interface StallRepo extends JpaRepository<Stall, Integer>{
	
	long deleteByStallName(String name);
	

}
