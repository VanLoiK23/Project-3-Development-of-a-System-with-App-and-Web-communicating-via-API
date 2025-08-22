package com.thuongmaidientu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thuongmaidientu.dao.INewDAO;
import com.thuongmaidientu.mapper.NewMapper;
import com.thuongmaidientu.model.NewModel;

@Repository
public class NewDAO extends AbstractDAO<NewModel> implements INewDAO {
	
	
	@Override
	public List<NewModel> findAll() {
		StringBuilder sql = new StringBuilder("SELECT * FROM news");
		
		return query(sql.toString(), new NewMapper());
		
	}

	
	
}
