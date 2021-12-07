package com.gfes.view;

import com.gfes.entity.User;
import com.gfes.service.FileInfoService;
import com.gfes.service.GroupService;
import com.gfes.service.UserService;
import com.gfes.util.CommonFont;
import com.gfes.util.ImagePanel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

@Component
@Slf4j
@Scope("prototype")
public class IndexJFrame extends JFrame implements MouseListener, ActionListener, WindowListener, InitializingBean {

	// 定义用户对象
	private User user;
	private final UserManagementJPanel userManagementJPanel;
	private final GroupManagementJPanel groupManagementJPanel;
	private final UserService userService;
	private final GroupService groupService;
	private final FileInfoService fileInfoService;
	private final FileManagerJPanel fileManagerJPanel;
	private final LogJPanel logJPanel;
	private final ApplicationEventPublisher eventPublisher;
	private final ObjectFactory<IndexJFrame> indexJFrameFactory;

	// 定义辅助变量
	int homeSign = 0;
	int userManagementSign = 0;
	int fileManagementLabelSign = 0;
	int userHomeSign = 0;
	int logSign = 0;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义全局组件
	JPanel backgroundPanel, topPanel, topMenu, topPrompt, centerPanel;
	JTabbedPane jTabbedPane;

	JLabel home, userManagementLabel, fileManagementLabel,logLabel,userHomeLabel;

	private final LoginJFrame loginJFrame;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("初始化IndexFrame");
	}

	public void setUser(User user) {
		this.user = user;
		fileManagerJPanel.open(user);
		initBackgroundPanel();
		this.setResizable(false);
		this.setVisible(true);
	}

	public IndexJFrame(UserManagementJPanel userManagementJPanel, GroupManagementJPanel groupManagementJPanel,
					   UserService userService, GroupService groupService, FileInfoService fileInfoService,
					   FileManagerJPanel fileManagerJPanel, LogJPanel logJPanel, ApplicationEventPublisher eventPublisher, ObjectFactory<IndexJFrame> indexJFrameFactory, LoginJFrame loginJFrame) {
		this.userManagementJPanel = userManagementJPanel;
		this.groupManagementJPanel = groupManagementJPanel;
		this.userService = userService;
		this.groupService = groupService;
		this.fileInfoService = fileInfoService;
		this.fileManagerJPanel = fileManagerJPanel;
		this.logJPanel = logJPanel;
		this.eventPublisher = eventPublisher;
		this.indexJFrameFactory = indexJFrameFactory;
		this.loginJFrame = loginJFrame;

		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

		URL resource = this.getClass().getResource("/image/logo.png");
		Image imgae = Toolkit.getDefaultToolkit().getImage(resource);
		this.setIconImage(imgae);
		this.setTitle("群组文件管理系统");
		this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// 初始化背景面板
	public void initBackgroundPanel() {

		backgroundPanel = new JPanel(new BorderLayout());
		initTop();
		initCenterPanel();

		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(centerPanel, "Center");

		this.add(backgroundPanel);
	}

	// 初始化顶部顶部面板
	public void initTop() {

		initTopMenu();
		initTopPrompt();

		topPanel = new JPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(width, 40));

		topPanel.add(topMenu, "West");
		topPanel.add(topPrompt, "East");
	}

	// 初始化顶部菜单
	public void initTopMenu() {

		topMenu = new JPanel();
		topMenu.setPreferredSize(new Dimension(600, 40));
		topMenu.setOpaque(false);

		String[] nameStrings = { "首页", "用户管理", "文件管理", "文件日志", "个人中心" };

		home = CreateMenuLabel(home, nameStrings[0], "home", topMenu);
		home.setName("home");
		if (2 != this.user.getType()){
			userManagementLabel = CreateMenuLabel(userManagementLabel, nameStrings[1], "user_management", topMenu);
			userManagementLabel.setName("userManagementLabel");
		}
		fileManagementLabel = CreateMenuLabel(fileManagementLabel, nameStrings[2], "files", topMenu);
		fileManagementLabel.setName("fileManagementLabel");
		if (2 != this.user.getType()){
			logLabel = CreateMenuLabel(logLabel, nameStrings[3], "log", topMenu);
			logLabel.setName("log");
		}
		userHomeLabel = CreateMenuLabel(userHomeLabel, nameStrings[4], "userHome", topMenu);
		userHomeLabel.setName("userHome");

	}

	// 创建顶部菜单Label
	public JLabel CreateMenuLabel(JLabel jlb, String text, String name, JPanel who) {
		JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
		URL resource = this.getClass().getResource("/image/" + name + ".png");
		Icon icon = new ImageIcon(resource);
		jlb = new JLabel(icon);
		jlb.setText("<html><font color='black'>" + text + "</font>&nbsp;</html>");
		jlb.addMouseListener(this);
		jlb.setFont(CommonFont.Static);
		who.add(jlb);
		if (!"userHome".equals(name)) {
			who.add(line);
		}
		return jlb;
	}

	// 初始化顶部欢迎面板
	public void initTopPrompt() {

		Icon icon = new ImageIcon("image/user.png");
		JLabel label = new JLabel(icon);
		if (user != null) {
			label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b>" + this.user.getName()
					+ "</b></font></html>");
		} else {
			label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b></b></font></html>");
		}
		label.setFont(CommonFont.Static);
		topPrompt = new JPanel();
		topPrompt.setPreferredSize(new Dimension(180, 40));
		topPrompt.setOpaque(false);
		topPrompt.add(label);

	}

	// 初始化中心面板
	public void initCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
		creatHome();
		centerPanel.setOpaque(false);
	}

	// 初始化辅助变量
	public void initSign() {
		homeSign = 0;
		userManagementSign = 0;
		fileManagementLabelSign = 0;
		userHomeSign = 0;
		logSign = 0;
	}

	// 创建首页面板
	public void creatHome() {

		centerPanel.removeAll();
		URL resource = this.getClass().getResource("/image/home_bg.png");
		Image bgimg = Toolkit.getDefaultToolkit().getImage(resource);
		ImagePanel centerBackground = new ImagePanel(bgimg);
		centerPanel.add(centerBackground, "Center");

	}

	// 创建用户管理面板
	public void createUserManagementTab() {
		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(CommonFont.Static);

		jTabbedPane.addTab("用户管理", userManagementJPanel.backgroundPanel);
		jTabbedPane.addTab("用户群组管理", groupManagementJPanel.backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	// // 创建文件管理面板
	public void createFileManagementTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(CommonFont.Static);

		jTabbedPane.addTab("文件管理", fileManagerJPanel.backgroundPanel);

		centerPanel.add(jTabbedPane, "Center");
	}

	// 创建文件日志面板
	public void createLogTab() {
		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(CommonFont.Static);

		jTabbedPane.addTab("文件日志", logJPanel.backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	// 刷新文件管理面板
	public void refreshpurchaseSaleStockTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(CommonFont.Static);
		jTabbedPane.addTab("文件管理", fileManagerJPanel.backgroundPanel);

		centerPanel.add(jTabbedPane, "Center");
	}

	// 创建个人中心面板
	public void createUserHomeTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(CommonFont.Static);

		jTabbedPane.addTab("个人中心", new UserHomeJPanel(user, this,userService,loginJFrame, eventPublisher, indexJFrameFactory).backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == home) {
			initSign();
			homeSign = 1;
			creatHome();
			home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
			if (userManagementLabel != null){
				userManagementLabel.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
			fileManagementLabel.setText("<html><font color='black'>" + "文件管理" + "</font>&nbsp;</html>");
			if (logLabel != null){
				logLabel.setText("<html><font color='black'>" + "文件日志" + "</font>&nbsp;</html>");
			}
			userHomeLabel.setText("<html><font color='black'>" + "个人中心" + "</font>&nbsp;</html>");
		} else if (e.getSource() == userManagementLabel) {
			initSign();
			userManagementSign = 1;
			createUserManagementTab();
			if (userManagementLabel != null){
				userManagementLabel.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户管理" + "</font>&nbsp;</html>");
			}
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			fileManagementLabel.setText("<html><font color='black'>" + "文件管理" + "</font>&nbsp;</html>");
			if (logLabel != null){
				logLabel.setText("<html><font color='black'>" + "文件日志" + "</font>&nbsp;</html>");
			}
			userHomeLabel.setText("<html><font color='black'>" + "个人中心" + "</font>&nbsp;</html>");
		} else if (e.getSource() == fileManagementLabel) {
			initSign();
			fileManagementLabelSign = 1;
			createFileManagementTab();
			fileManagementLabel.setText(
					"<html><font color='#336699' style='font-weight:bold'>" + "文件管理" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			if (userManagementLabel != null){
				userManagementLabel.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
			if (logLabel != null){
				logLabel.setText("<html><font color='black'>" + "文件日志" + "</font>&nbsp;</html>");
			}
			userHomeLabel.setText("<html><font color='black'>" + "个人中心" + "</font>&nbsp;</html>");
		} else if (e.getSource() == logLabel) {
			initSign();
			logSign = 1;
			createLogTab();
			userHomeLabel
					.setText("<html><font color='black'>" + "个人中心" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			if (userManagementLabel != null){
				userManagementLabel.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
			fileManagementLabel.setText("<html><font color='black'>" + "文件管理" + "</font>&nbsp;</html>");
			if (logLabel != null){
				logLabel.setText("<html><font color='#336699' style='font-weight:bold'>" + "文件日志" + "</font>&nbsp;</html>");
			}
		}  else if (e.getSource() == userHomeLabel) {
			initSign();
			userHomeSign = 1;
			createUserHomeTab();
			userHomeLabel
					.setText("<html><font color='#336699' style='font-weight:bold'>" + "个人中心" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			if (userManagementLabel != null){
				userManagementLabel.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
			fileManagementLabel.setText("<html><font color='black'>" + "文件管理" + "</font>&nbsp;</html>");
			if (logLabel != null){
				logLabel.setText("<html><font color='black'>" + "文件日志" + "</font>&nbsp;</html>");
			}

		} else {
			System.out.println("ok");
		}

	}

	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == home) {
			home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
		} else if (e.getSource() == userManagementLabel) {
			userManagementLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			userManagementLabel.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户管理" + "</font>&nbsp;</html>");
		} else if (e.getSource() == fileManagementLabel) {
			fileManagementLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			fileManagementLabel.setText(
					"<html><font color='#336699' style='font-weight:bold'>" + "文件管理" + "</font>&nbsp;</html>");
		} else if (e.getSource() == logLabel) {
			logLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			logLabel
					.setText("<html><font color='#336699' style='font-weight:bold'>" + "文件日志" + "</font>&nbsp;</html>");
		} else if (e.getSource() == userHomeLabel) {
			userHomeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			userHomeLabel
					.setText("<html><font color='#336699' style='font-weight:bold'>" + "个人中心" + "</font>&nbsp;</html>");
		}

	}

	// 鼠标划出事件
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == home) {
			if (homeSign == 0) {
				home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == userManagementLabel) {
			if (userManagementSign == 0) {
				userManagementLabel.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == fileManagementLabel) {
			if (fileManagementLabelSign == 0) {
				fileManagementLabel.setText("<html><font color='black'>" + "文件管理" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == logLabel) {
			if (logSign == 0) {
				logLabel.setText("<html><font color='black'>" + "文件日志" + "</font>&nbsp;</html>");
			}
		}else if (e.getSource() == userHomeLabel) {
			if (userHomeSign == 0) {
				userHomeLabel.setText("<html><font color='black'>" + "个人中心" + "</font>&nbsp;</html>");
			}
		}

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

	}


	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println("open");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("closing");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("closed");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println("iconified");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println("deiconified");
	}

	@Override
	public void windowActivated(WindowEvent e) {
		System.out.println("activated");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		System.out.println("deactivated");
	}
}
