package com.thuongmaidientu.dao;

import java.util.List;

import com.thuongmaidientu.model.NewModel;
import com.thuongmaidientu.paging.Pageble;

public interface INewDAO extends GenericDAO<NewModel> {
	
	List<NewModel> findAll();
}
