package com.altimetrik.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altimetrik.model.Stall;
import com.altimetrik.repo.StallRepo;
@Service
public class StallServiceImpl implements StallService{
	
	@Autowired
	private StallRepo stallRepo;
	
	@Override
	@Transactional
	public void insertStall(Stall stall) {
		stallRepo.save(stall);
	}

	@Override
	@Transactional
	public List<Stall> getAllStall() {
		return stallRepo.findAll();
	}

	@Override
	@Transactional
	public Stall getSingleStall(int id) {
		return stallRepo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void updateStall(Stall stall) {
		stallRepo.saveAndFlush(stall);
	}

	@Override
	@Transactional
	public void deleteStall(Stall s) {
		stallRepo.delete(s);
	}
	
	@Override
	@Transactional
	public long deleteStallByName(String name) {
		return stallRepo.deleteByStallName(name);
	}
	
	
	
}
