package com.hg.httpdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.httpdb.schema.ColumnType;

/**
 * 数据集
 * 
 * @author wanghg
 */
public class RowSet {
	private List<ColumnType> fields = new ArrayList<ColumnType>();
	private List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

	public List<ColumnType> getFields() {
		return fields;
	}

	public ColumnType fieldAt(int inx) {
		return (ColumnType) this.fields.get(inx);
	}

	public int fieldIndex(String name) {
		for (int i = 0; i < this.fields.size(); i++) {
			if (((ColumnType) this.fields.get(i)).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public Map<String, Object> get(int inx) {
		return this.rows.get(inx);
	}

	public void add(Map<String, Object> map) {
		this.rows.add(map);
	}

	public int size() {
		return this.rows.size();
	}

	public Object value(int row, int col) {
		if (row < this.rows.size() && col < this.fields.size()) {
			return (this.rows.get(row)).get(this.fieldAt(col).getName());
		} else {
			return null;
		}
	}
}
