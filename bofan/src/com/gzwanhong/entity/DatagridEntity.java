package com.gzwanhong.entity;

import java.util.ArrayList;
import java.util.List;

public class DatagridEntity {
	public DatagridEntity() {
	}

	/**
	 * @param dataList
	 *            数据集合
	 * @param total
	 *            总行数
	 * @param page
	 *            页数
	 */
	public DatagridEntity(List<?> dataList, Integer total, Integer page) {
		this.rows = dataList;
		this.total = total;
		this.page = page;
	}

	public DatagridEntity(List<?> dataList, Integer total) {
		this.rows = dataList;
		this.total = total;
	}

	private boolean result = true;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * 数据的总数
	 */
	private Integer total = 0;

	/**
	 * 数据集合
	 */
	private List<?> rows = new ArrayList<Object>();

	/**
	 * 页脚汇总信息
	 */
	private List<?> footer = new ArrayList<Object>();

	/**
	 * 分页信息
	 */
	private Integer page = 0;

	public List<?> getFooter() {
		return footer;
	}

	public void setFooter(List<?> footer) {
		this.footer = footer;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
