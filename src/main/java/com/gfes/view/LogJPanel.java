package com.gfes.view;

import cn.hutool.core.date.DateUtil;
import com.gfes.common.MyTableModule;
import com.gfes.common.Pageable;
import com.gfes.entity.Log;
import com.gfes.service.LogService;
import com.gfes.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LogJPanel implements MouseListener, Pageable {

	// 定义全局组件
	JPanel backgroundPanel, topPanel, toolPanel, tablePanel;
	private MyTableModule tableModel;
	JTable table;
	JScrollPane jScrollPane;
	JButton search_button;
	JTextField jtf;

	private final LogService logService;
	private int pageSize = 10;
	private int currentPage = 0;
	private int totalPage = 0;
	private long totalElements = 0;
	private Page<Log> page = null;

	private final String HEADERS[] = { "文件", "操作人", "操作", "操作时间"};

	public LogJPanel(LogService logService) {
		this.logService = logService;
		Page<Log> groups = logService.queryPage(PageRequest.of(this.currentPage, this.pageSize,
				Sort.by(Sort.Direction.DESC,"createTime")));
		this.page = groups;
		backgroundPanel = new JPanel(new BorderLayout());
		initTopPanel();
		initTablePanel();
		initPaginationPanel();
	}

	// 初始化顶部面板
	public void initTopPanel() {

		topPanel = new JPanel(new BorderLayout());

		initToolPanel();
		backgroundPanel.add(topPanel, "North");

	}

	// 初始化工具面板
	public void initToolPanel() {

		toolPanel = new JPanel();
		search_button = new JButton("搜索");
		jtf =new JTextField(20);
//		jtf.setText("请输入关键字搜索");
		search_button.addActionListener(new PaginationActionListener());
		// 工具图标
		toolPanel.add(jtf);
		toolPanel.add(search_button);
		topPanel.add(toolPanel, "West");

	}

	// 初始化数据表格面板
	public void initTablePanel() {
		showPage(0);
	}

	public void initPaginationPanel() {
		JPanel panel = new JPanel();
		JButton button = new JButton("首页");
		button.addActionListener(new PaginationActionListener());
		button.setActionCommand("首页");
		panel.add(button);
		JButton button1 = new JButton("上一页");
		button1.addActionListener(new PaginationActionListener());
		panel.add(button1);
		JButton button2 = new JButton("下一页");
		button2.addActionListener(new PaginationActionListener());
		panel.add(button2);
		JButton button3 = new JButton("末页");
		button3.addActionListener(new PaginationActionListener());
		panel.add(button3);
		backgroundPanel.add(panel, "South");
	}



	// 更新数据表格
	public void refreshTablePanel() {
		showPage(0);
	}

	//渲染数据
	private void renderData(List<Vector<String>> data){
		if (tableModel != null){
			tableModel.refreshData(HEADERS,data);
		}else {
			tableModel = new MyTableModule(HEADERS, data);
		}
		table = new JTable(tableModel);
		table.clearSelection();
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		dcm.getColumn(0).setMinWidth(400);
		dcm.getColumn(1).setMinWidth(400);
		dcm.getColumn(2).setMinWidth(400);
		dcm.getColumn(3).setMinWidth(400);

		tableModel.fireTableDataChanged();

		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);

		tablePanel.add(jScrollPane);

		backgroundPanel.add(tablePanel, "Center");
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getSource());
	}
	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void showPage(int page) {
		Page<Log> groups = logService.queryPage(PageRequest.of(page, this.pageSize,
				Sort.by(Sort.Direction.DESC,"createTime")));
		this.currentPage = page;
		this.page = groups;
		this.totalPage = groups.getTotalPages();
		this.totalElements = groups.getTotalElements();
		List<Vector<String>> rows = groups.stream()
				.map(x -> Arrays.asList(x.getFileName(),
						x.getUsername(),
						x.getOperation(),
						DateUtil.format(x.getCreateTime(),"yyyy-MM-dd HH:mm:ss")))
				.map(x -> {
					Vector<String> vector = new Vector<>();
					vector.addAll(x);
					return vector;
				})
				.collect(Collectors.toList());
		log.info("show page {}",page);
		// 渲染数据
		renderData(rows);

	}


	public void showSearchPage(int page,String keyWords) {
		List<Log> list = logService.findByCondition(keyWords);
		Page<Log> groups = new PageImpl<Log>(list, (PageRequest.of(page, this.pageSize,
				Sort.by(Sort.Direction.DESC,"createTime"))),10);
		this.currentPage = page;
		this.page = groups;
		this.totalPage = groups.getTotalPages();
		this.totalElements = groups.getTotalElements();
		List<Vector<String>> rows = groups.stream()
				.map(x -> Arrays.asList(x.getFileName(),
						x.getUsername(),
						x.getOperation(),
						DateUtil.format(x.getCreateTime(),"yyyy-MM-dd HH:mm:ss")))
				.map(x -> {
					Vector<String> vector = new Vector<>();
					vector.addAll(x);
					return vector;
				})
				.collect(Collectors.toList());
		log.info("show page {}",page);
		// 渲染数据
		renderData(rows);

	}
	@Override
	public int getCurrentPage() {
		log.info("get current page");
		return this.currentPage;
	}

	@Override
	public int getLastPage() {
		log.info("get last page");
		if (this.totalPage == 0){
			return 0;
		}
		return this.totalPage - 1;
	}

	@Override
	public long getTotalElements() {
		log.info("get total elements");
		return this.totalElements;
	}

	@Override
	public void setCurrentPage(int page) {
		log.info("set current page");
	}

	class PaginationActionListener  implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("首页")){
				showPage(1);
			}

			if(e.getActionCommand().equals("上一页")){
				if(getCurrentPage()<=1){
					setCurrentPage(0);
				}else {
					if (!jtf.getText().isEmpty()) {
						showSearchPage(getCurrentPage()-1,jtf.getText());
					}
					showPage(getCurrentPage()-1);
				}

			}

			if(e.getActionCommand().equals("下一页")){
				if(getCurrentPage() < getLastPage()){
					showPage(getCurrentPage()+1);
				}else{
					if (!jtf.getText().isEmpty()) {
						showSearchPage(getCurrentPage()-1,jtf.getText());
					}
					showPage(getLastPage());
				}
			}

			if(e.getActionCommand().equals("末页")){
				showPage(getLastPage());
			}

			if(e.getActionCommand().equals("搜索")){
				// 回去搜索框内容
				String keywords = jtf.getText();
				// 模糊查询
				showSearchPage(1,keywords);
			}
		}
	}

}
