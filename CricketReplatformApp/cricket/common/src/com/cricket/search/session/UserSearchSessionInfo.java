package com.cricket.search.session;

import java.util.List;
import java.util.Map;

import com.cricket.search.records.CatLinkVO;

import atg.nucleus.GenericService;

public class UserSearchSessionInfo extends GenericService {
	
	private String searchQuery;
	
	private int totalCount;
	
	private Map<String, CatLinkVO> catLinkMap;
	
	private List<String> keyOrder;
	
	private String genTextBoxSearchQuery;
	
	private Integer itemsPerPage;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public Map<String, CatLinkVO> getCatLinkMap() {
		return catLinkMap;
	}

	public void setCatLinkMap(Map<String, CatLinkVO> catLinkMap) {
		this.catLinkMap = catLinkMap;
	}

	public List<String> getKeyOrder() {
		return keyOrder;
	}

	public void setKeyOrder(List<String> keyOrder) {
		this.keyOrder = keyOrder;
	}

	public String getGenTextBoxSearchQuery() {
		return genTextBoxSearchQuery;
	}

	public void setGenTextBoxSearchQuery(String genTextBoxSearchQuery) {
		this.genTextBoxSearchQuery = genTextBoxSearchQuery;
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
}
