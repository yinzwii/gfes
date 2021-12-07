package com.gfes.view;

import cn.hutool.core.date.DateUtil;
import com.gfes.common.MyTableModule;
import com.gfes.common.Pageable;
import com.gfes.entity.User;
import com.gfes.service.UserService;
import com.gfes.util.Item;
import com.gfes.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserManagementJPanel implements ActionListener, MouseListener, Pageable {

	// 定义全局组件
	public JPanel backgroundPanel, topPanel, toolPanel, searchPanel, tablePanel;
	private JComboBox userTypeSelector;
	private JTable table;
	private JScrollPane jScrollPane;
	private JLabel userSelectorLabel, addLabel, editLabel, deleteLabel;

	public static Map<Integer,String> USER_TYPE_MAP  = new HashMap<>();

	private MyTableModule tableModel;
	private final UserService userService;
	private int pageSize = 10;
	private int currentPage = 0;
	private int totalPage = 0;
	private long totalElements = 0;
	private Page<User> page = null;

	private final String HEADERS[] =  { "id", "用户名", "用户类型", "创建时间" };


	public UserManagementJPanel(UserService userService) {
		this.userService = userService;
		backgroundPanel = new JPanel(new BorderLayout());
		USER_TYPE_MAP.put(-1,"全部");
		USER_TYPE_MAP.put(0,"超级管理员");
		USER_TYPE_MAP.put(1,"管理员");
		USER_TYPE_MAP.put(2,"普通用户");
		initTopPanel();
		initTablePanel();
		initPaginationPanel();
	}

	// 初始化顶部面板
	public void initTopPanel() {
		topPanel = new JPanel(new BorderLayout());
		initToolPanel();
		initSearchPanel();
		backgroundPanel.add(topPanel, "North");

	}

	// 初始化工具面板
	public void initToolPanel() {

		toolPanel = new JPanel();
		// 工具图标
		URL resource = this.getClass().getResource("/image/add.png");
		Icon icon_add = new ImageIcon(resource);
		addLabel = new JLabel(icon_add);
		addLabel.setToolTipText("新建用户");
		addLabel.addMouseListener(this);

		resource = this.getClass().getResource("/image/edit_list.png");
		Icon icon_modify = new ImageIcon(resource);
		editLabel = new JLabel(icon_modify);
		editLabel.setToolTipText("修改用户");
		editLabel.addMouseListener(this);

		resource = this.getClass().getResource("/image/delete.png");
		Icon icon_delete = new ImageIcon(resource);
		deleteLabel = new JLabel(icon_delete);
		deleteLabel.setToolTipText("删除用户");
		deleteLabel.addMouseListener(this);

		toolPanel.add(addLabel);
		toolPanel.add(editLabel);
		toolPanel.add(deleteLabel);

		topPanel.add(toolPanel, "West");

	}

	// 初始化搜素条件面板
	public void initSearchPanel() {

		searchPanel = new JPanel();

		// 用户类型下拉框
		userTypeSelector = new JComboBox();
		Set<Integer> userTypeKeys = USER_TYPE_MAP.keySet();
		for (Integer key : userTypeKeys){
			userTypeSelector.addItem(new Item(key + "", USER_TYPE_MAP.get(key)));
		}
		userTypeSelector.setSelectedIndex(0);

		userTypeSelector.addActionListener(this);

		// 标签
		userSelectorLabel = new JLabel("用户类型");

		searchPanel.add(userSelectorLabel);
		searchPanel.add(userTypeSelector);

		topPanel.add(searchPanel, "East");

	}

	// 初始化数据表格面板
	public void initTablePanel() {
		showPage(0);
	}

	// 更新数据表格
	public void refreshTablePanel() {
		showPage(0);
	}

	// 下拉框改变事件
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == userTypeSelector) {
			refreshTablePanel();
		}

	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == addLabel) {
			new AddUserJFrame(this,userService);
		} else if (e.getSource() == editLabel) {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(null, "请选择用户");
			} else {
				String id = (String) table.getValueAt(row, 0);
				new EditUserJFrame(this, userService, id);
			}

		} else if (e.getSource() == deleteLabel) {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(null, "请选择用户");
			} else {
				String id = (String) table.getValueAt(row, 0);
				int result = JOptionPane.showConfirmDialog(null, "是否确定删除？", "用户提示", JOptionPane.YES_NO_OPTION);
				if (result == 0) {
					try {
						userService.delete(id);
						JOptionPane.showMessageDialog(null, "删除成功！");
						refreshTablePanel();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			}
		}

	}

	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == addLabel) {
			addLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else if (e.getSource() == editLabel) {
			editLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else if (e.getSource() == deleteLabel) {
			deleteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

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
		table.getSelectionModel().addListSelectionListener(new UserListSelectionListener());
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
		dcm.getColumn(0).setMinWidth(10);
		dcm.getColumn(1).setMinWidth(10);
		dcm.getColumn(2).setMinWidth(10);
		dcm.getColumn(3).setMinWidth(10);

		tableModel.fireTableDataChanged();

		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);

		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);

		tablePanel.add(jScrollPane);

		backgroundPanel.add(tablePanel, "Center");
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

	@Override
	public void showPage(int page) {
		Item selectedItem = (Item)userTypeSelector.getSelectedItem();
		Page<User> users = userService.queryPage(PageRequest.of(0, this.pageSize),
				Integer.valueOf(selectedItem.getKey()));
		this.currentPage = 0;
		this.page = users;
		this.totalPage = users.getTotalPages();
		this.totalElements = users.getTotalElements();
		List<Vector<String>> rows = users.stream()
				.map(x -> Arrays.asList(x.getId(),
						x.getName(),
						USER_TYPE_MAP.get(x.getType()),
						DateUtil.format(x.getCreateTime(),"yyyy-MM-DD HH:mm:ss")))
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
					showPage(getCurrentPage()-1);
				}

			}

			if(e.getActionCommand().equals("下一页")){
				if(getCurrentPage() < getLastPage()){
					showPage(getCurrentPage()+1);
				}else{
					showPage(getLastPage());
				}
			}

			if(e.getActionCommand().equals("末页")){
				showPage(getLastPage());
			}
		}
	}

	class UserListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Object source = e.getSource();
			if (source instanceof DefaultListSelectionModel){
				DefaultListSelectionModel selectionModel = (DefaultListSelectionModel)source;
				int selectedRowIdx = selectionModel.getAnchorSelectionIndex();
				if (selectedRowIdx > -1){
					table.setRowSelectionInterval(selectedRowIdx, selectedRowIdx);
				}
			}

		}
	}
}
