package com.gfes.view;

import com.gfes.common.BaseTableModule;
import com.gfes.common.CustomizedTable;
import com.gfes.entity.FileInfo;
import com.gfes.entity.User;
import com.gfes.entity.UserGroup;
import com.gfes.service.FileInfoService;
import com.gfes.service.FilePermissionService;
import com.gfes.service.UserGroupService;
import com.gfes.service.UserService;
import com.gfes.util.CommonFont;
import com.gfes.util.Tools;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.context.ApplicationEventPublisher;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AddFilePermissionJFrame extends JFrame implements MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel, tablePanel;
	JLabel nameLabel;
	JTextField name;
	JButton addBtn;
	BaseTableModule baseTableModule;
	JTable table;
	JScrollPane jScrollPane;
	private ApplicationEventPublisher eventPublisher;
	private final UserService userService;
	private final String[] HEADERS = { "用户id", "用户名","用户组名","操作"};
	private FilePermissionService permissionService;
	private User user;
	String id;
	private final UserGroupService userGroupService;
	private final FileInfoService fileInfoService;
	private final FilePermissionService filePermissionService;


	// 父面板对象
	private FileReaderAndWritePermission parentPanel;

	public AddFilePermissionJFrame(ApplicationEventPublisher eventPublisher, UserService userService, FileReaderAndWritePermission parentPanel,
								   FilePermissionService permissionService,
								   User user, String id, UserGroupService userGroupService, FileInfoService fileInfoService, FilePermissionService filePermissionService) {
		this.eventPublisher = eventPublisher;
		this.userService = userService;
		this.parentPanel = parentPanel;
		this.id = id;
		this.permissionService = permissionService;
		this.user = user;
		this.userGroupService = userGroupService;
		this.fileInfoService = fileInfoService;
		this.filePermissionService = filePermissionService;
		initBackgroundPanel();

		this.add(backgroundPanel);

		this.setTitle("添加文件访问权限");
		this.setSize(1200, 800);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	// 初始化背景面板
	public void initBackgroundPanel() {
		backgroundPanel = new JPanel(new BorderLayout());

		initContentPanel();
		initButtonPanel();
		initLabelPanel();

		backgroundPanel.add(labelPanel, "North");
		backgroundPanel.add(contentPanel, "Center");
		backgroundPanel.add(buttonPanel, "South");
	}

	// 初始化label面板
	public void initLabelPanel() {

		labelPanel = new JPanel();

		JLabel title = new JLabel();
		title.setFont(CommonFont.Static);

		labelPanel.add(title);
	}

	// 初始化群组信息面板
	public void initContentPanel() {
		contentPanel = new JPanel(new BorderLayout());
		Vector<Vector> vector = new Vector<Vector>();
		try {
			// 通过用户id获取用户群组
			List<UserGroup>  allList = new ArrayList<>();
			FileInfo fileInfo = fileInfoService.selectFileInfoById(this.id);
			List<UserGroup> list =  userGroupService.selectGroupIdByUserId(fileInfo.getCreatorId());
			list.forEach(item ->{
				List<UserGroup>  groupList = userGroupService.selectGroupUserByGroupId(item.getGroupId());
				allList.addAll(groupList);
			});
			List<Object[]>  vectorTemp = new ArrayList<>();
			allList.forEach(item ->{
				List<Object[]> list1 = userGroupService.selectListByUserIdAndGroupId(item.getUserId(),item.getGroupId());
				vectorTemp.addAll(list1);
			});
			vector =  userGroupService.converVector(vectorTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		baseTableModule = new BaseTableModule(HEADERS, vector);
		table = new CustomizedTable(baseTableModule);
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();
		dcm.getColumn(0).setPreferredWidth(10);
		dcm.getColumn(1).setPreferredWidth(20);
		dcm.getColumn(2).setPreferredWidth(10);
		TableColumn col3 = dcm.getColumn(3);
		col3.setMaxWidth(180);
		col3.setMinWidth(180);

		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				table.setRowHeight(45);
			}
		});
		col3.setResizable(false);
		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);
		table.getColumnModel().getColumn(3).setCellEditor(getTableButtonCellEditor());
		table.getColumnModel().getColumn(3).setCellRenderer(new JComboBoxJFrame(id, user, filePermissionService));
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		tablePanel.add(jScrollPane);
		table.validate();
		table.updateUI();
		contentPanel.add(tablePanel, BorderLayout.CENTER);
	}

	private JComboBoxEditJFrame getTableButtonCellEditor(){

		return new JComboBoxEditJFrame(this,
				fileInfoService,eventPublisher,userService, filePermissionService,this.id);
	}

	// 初始化按钮面板
	public void initButtonPanel() {
		buttonPanel = new JPanel();
		addBtn = new JButton("保存");
		addBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		addBtn.setForeground(Color.white);
		addBtn.setFont(CommonFont.Static);
		addBtn.addMouseListener(this);
		buttonPanel.add(addBtn);
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == addBtn) {
			// 隐藏当前页面
			this.setVisible(false);
			// 刷新父页面
			Vector<Vector> vector = userService.selectListByFileId(this.id);
			parentPanel.refreshTablePanel(vector);
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
