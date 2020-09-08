package com.taotao.search.bean;

import java.util.List;

public class SearchResult {

	public Long total;
	
	public Long getTotal() {
		return total;
	}

	public SearchResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchResult(Long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public List<?> rows;
}
