package com.gfes.view;

import com.browser.BrowserWindow;
import com.gfes.common.CustomizedTable;
import com.gfes.common.MyDefaultTreeCellRenderer;
import com.gfes.entity.FileInfo;
import com.gfes.entity.Group;
import com.gfes.entity.User;
import com.gfes.service.*;
import com.gfes.common.BaseTableModule;
import com.gfes.util.Tools;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

@Component
public class FileManagerJPanel extends JPanel implements ActionListener,MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, tablePanel;
	BaseTableModule baseTableModule;
	JTable table;
	JScrollPane jScrollPane;
	public JTree tree;

	private final MyDefaultTreeCellRenderer treeCellRenderer;
	private final FilePermissionService filePermissionService;
	private final UserGroupService userGroupService;

	DefaultMutableTreeNode root = new DefaultMutableTreeNode("群组文件");

	//弹出式菜单
	JPopupMenu treePopup1 =new JPopupMenu();
	JPopupMenu treePopup2 =new JPopupMenu();
	JPopupMenu treePopup3 =new JPopupMenu();

	private final GroupService groupService;
	private final FileInfoService fileInfoService;
	private final MinioService minioService;
	private final BrowserWindow browser;
	private final ApplicationEventPublisher eventPublisher;
	private final UserService userService;

	private User user;

	private final String[] HEADERS = { "id", "文件名","创建时间", "操作"};

	public FileManagerJPanel(MyDefaultTreeCellRenderer treeCellRenderer, FilePermissionService filePermissionService, UserGroupService userGroupService, GroupService groupService,
							 FileInfoService fileInfoService,
							 MinioService minioService, BrowserWindow browser,
							 ApplicationEventPublisher eventPublisher, UserService userService) {
		this.treeCellRenderer = treeCellRenderer;
		this.filePermissionService = filePermissionService;
		this.userGroupService = userGroupService;
		this.groupService = groupService;
		this.fileInfoService = fileInfoService;
		this.minioService = minioService;
		this.browser = browser;
		this.eventPublisher = eventPublisher;
		this.userService = userService;

		backgroundPanel = new JPanel(new BorderLayout());

		//右键菜单初始化
		JMenuItem item=new JMenuItem("创建文件夹");
		item.setActionCommand("mkdir");
		item.addActionListener(this);
		treePopup1.add(item);

		JMenuItem delDirMenu = new JMenuItem("删除");
		delDirMenu.setActionCommand("delDir");
		delDirMenu.addActionListener(this);
		JMenuItem uploadFileMenu = new JMenuItem("上传文件");
		uploadFileMenu.setActionCommand("uploadFile");
		uploadFileMenu.addActionListener(this);
		treePopup2.add(delDirMenu);
		treePopup2.add(uploadFileMenu);

		initFileTree();
		initTablePanel();
	}

	public void open(User user) {
		this.user = user;
		initFileTree();
		initTablePanel();
	}

	private FileManagementTableCellEditor getTableButtonCellEditor(){
		return new FileManagementTableCellEditor(this,browser,
				fileInfoService, minioService,eventPublisher,user,userService, filePermissionService, userGroupService);
	}


	// 初始化顶部面板
	public void initFileTree() {
		if (this.user == null){
			return;
		}
		root = new DefaultMutableTreeNode("群组文件");
		// 获取用户组列表
		List<Group> groupList = groupService.queryMyGroupList(this.user.getId());
			groupList.forEach(item ->{
				DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(item);
				// 通过groupId获取文件夹
				try {
					List<FileInfo> fileList = fileInfoService.queryDocListByGroupId(item.getId());
					if (fileList.size() > 0) {
						fileList.forEach(f ->{
							DefaultMutableTreeNode dirNode = null;
							List<FileInfo> list = fileInfoService.queryFile();
							if (item.getId().equals(f.getGroupId()) && f.getDoc() == 1) {
								dirNode = new DefaultMutableTreeNode(f);
								// 文件节点
								for (FileInfo file : list){
									if (file.getPId().equals(f.getId())) {
										dirNode.add(new DefaultMutableTreeNode(file));
									}
								}
								groupNode.add(dirNode);
							}

						});
					}
					root.add(groupNode);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if (tree == null){
				tree = new JTree(root);
				tree.setCellRenderer(treeCellRenderer);
				tree.addMouseListener(this);
				backgroundPanel.add(tree, BorderLayout.WEST);
				tree.setSelectionPath(new TreePath(root));
			}

	}

	// 初始化数据表格面板
	public void initTablePanel() {
		if (this.user == null){
			return;
		}
		Vector<Vector> vector = new Vector<Vector>();
		try {
			vector = fileInfoService.selectAllFileInfos(user);
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
		col3.setMaxWidth(380);
		col3.setMinWidth(380);
		col3.setResizable(false);
//		table.setRowHeight(40);
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				table.setRowHeight(40);
			}
		});
		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);
		table.getColumnModel().getColumn(3).setCellEditor(getTableButtonCellEditor());
		table.getColumnModel().getColumn(3).setCellRenderer(new FileManagementTableButtonRender());
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		tablePanel.add(jScrollPane);
		table.validate();
		table.updateUI();

		backgroundPanel.add(tablePanel, BorderLayout.CENTER);
	}

	// 更新数据表格
	public void refreshTablePanel(Vector<Vector> vector) {

		backgroundPanel.remove(tablePanel);
		baseTableModule = new BaseTableModule(HEADERS, vector);
		table = new CustomizedTable(baseTableModule);
		Tools.setTableStyle(table);
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();
		dcm.getColumn(0).setPreferredWidth(10);
		dcm.getColumn(1).setPreferredWidth(20);
		dcm.getColumn(2).setPreferredWidth(10);
		TableColumn col3 = dcm.getColumn(3);
		col3.setMaxWidth(380);
		col3.setMinWidth(380);
		col3.setResizable(false);
//		table.setRowHeight(40);
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				table.setRowHeight(40);
			}
		});

		jScrollPane = new JScrollPane(table);
		Tools.setJspStyle(jScrollPane);
		table.getColumnModel().getColumn(3).setCellEditor(getTableButtonCellEditor());
		table.getColumnModel().getColumn(3).setCellRenderer(new FileManagementTableButtonRender());
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		tablePanel.add(jScrollPane);
		table.validate();
		table.updateUI();
		backgroundPanel.updateUI();

		backgroundPanel.add(tablePanel, BorderLayout.CENTER);
	}

	//刷新节点数据
	public void refreshData(DefaultMutableTreeNode selectionNode){
		int level = selectionNode.getLevel();
		// 更新table
		if (level == 0){
			Vector<Vector> fileList = fileInfoService.selectAllFileInfos(user);
			this.refreshTablePanel(fileList);
		}else if (level == 1){
			Group group = (Group)selectionNode.getUserObject();
			Vector<Vector> fileList = fileInfoService.queryGroupFiles(group.getId());
			this.refreshTablePanel(fileList);
		}else if (level == 2){
			FileInfo fileInfo = (FileInfo)selectionNode.getUserObject();
			Vector<Vector> fileList = fileInfoService.selectListByPId(fileInfo.getId());
			this.refreshTablePanel(fileList);
		}
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println(e.getButton());
		if (e.getButton()==MouseEvent.BUTTON1){
			//左键
			TreePath pathForLocation = tree.getPathForLocation(x, y);
			if (pathForLocation != null){
				tree.setSelectionPath(pathForLocation);
				DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				refreshData(selectionNode);
			}

		} else if(e.getButton()==MouseEvent.BUTTON3) {
			TreePath pathForLocation = tree.getPathForLocation(x, y);//获取右键点击所在树节点路径
			tree.setSelectionPath(pathForLocation);
			DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			int level = selectionNode.getLevel();
			// 右键菜单
			if (level == 1){
				treePopup1.show(e.getComponent(), e.getX(), e.getY());
			}else if (level == 2){
				treePopup2.show(e.getComponent(), e.getX(), e.getY());
			}

		}

	}

	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String action=e.getActionCommand();
		System.out.println(action);
		DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		int level = selectionNode.getLevel();
		//当是打开命令时，再弹出一个窗口
		if(action.equals("mkdir")) {
			Group group = (Group)selectionNode.getUserObject();
			// 添加文件夹
			new AddDirectoryJFrame(fileInfoService,this,group);
		}else if (action.equals("delDir")){
			FileInfo fileInfo = (FileInfo)selectionNode.getUserObject();
			int n = fileInfoService.deleteFileById(fileInfo.getId());
			if (n == 0){
				JOptionPane.showMessageDialog(null, "该文件夹下还有文件，无法删除");
				return;
			}
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectionNode.getParent();
			parentNode.remove(selectionNode);
			tree.updateUI();
		}else if (action.equals("uploadFile")){
			// 上传文件
			FileUploadJFrame fileUploadJFrame = new FileUploadJFrame(fileInfoService, this, selectionNode, minioService,user);
		}
	}

}
