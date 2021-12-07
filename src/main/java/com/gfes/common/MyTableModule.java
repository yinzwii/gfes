package com.gfes.common;

/**
 *
 * 说明:自定义TabelModel工具类
 *
 *
 * */

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Vector;

public class MyTableModule extends AbstractTableModel {

	List<Vector<String>> rows;// 定义行
	Vector<String> colums;// 定义列

	public MyTableModule(String[] params, List<Vector<String>> vector) {
		// 初始化列
		this.colums = new Vector<String>();
		for (String colum : params) {
			colums.add(colum);
		}

		// 初始化行
		this.rows = vector;

	}

	public void refreshData(String[] params, List<Vector<String>> vector) {
		this.rows.clear();
		this.colums.clear();
		for (String colum : params) {
			colums.add(colum);
		}

		this.rows.addAll(vector);

	}

	@Override
	public String getColumnName(int column) {
		return this.colums.get(column);
	}

	@Override
	public int getColumnCount() {
		return this.colums.size();
	}

	@Override
	public int getRowCount() {
		return this.rows.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return (rows.get(rowIndex)).get(columnIndex);
	}

}
