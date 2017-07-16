package com.gzwanhong.entity;

public class PageInfo {
	private Integer rows;
	private Integer page;
	private Integer totalPage;
	private Integer total;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public int getTotalPage() {
		if (this.total % 10 == 0) {
			totalPage = this.total / 10;
		} else {
			totalPage = (this.total / 10) + 1;
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public PageInfo(Integer rows, Integer page) {
		this.rows = rows;
		this.page = page;
	}

	public Integer getStartNum() {
		return (page - 1) * rows + 1;
	}

	public Integer getEndNum() {
		return (page - 1) * rows + rows;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

}
