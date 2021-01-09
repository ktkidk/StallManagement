package com.altimetrik.service;

import java.util.List;


import com.altimetrik.model.Stall;

public interface StallService {
	public void insertStall(Stall stall);
	public List<Stall> getAllStall();
	public Stall getSingleStall(int id);
	public void updateStall(Stall stall);
	public void deleteStall(Stall s);
	public long deleteStallByName(String name);

}
