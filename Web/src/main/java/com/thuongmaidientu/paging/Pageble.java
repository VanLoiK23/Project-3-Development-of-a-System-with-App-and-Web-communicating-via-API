package com.thuongmaidientu.paging;

import com.thuongmaidientu.sort.Sorter;

public interface Pageble {
	Integer getPage();
	Integer getOffset();
	Integer getLimit();
	Sorter getSorter();
}
