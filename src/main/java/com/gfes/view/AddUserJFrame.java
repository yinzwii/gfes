package com.gfes.view;

import com.gfes.entity.User;
import com.gfes.service.UserService;
import com.gfes.util.CommonFont;
import com.gfes.util.Item;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddUserJFrame extends JFrame implements MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
	private JLabel usernameLabel, passwordLabel, confirmPasswordLabel, userTypeLabel;
	private JTextField nameBox;
	private JPasswordField passwordBox, confirmPasswordBox;
	private JComboBox typeBox;
	private JButton addBtn;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 父面板对象
	private final UserManagementJPanel parentPanel;
	private final UserService userService;

	public AddUserJFrame(UserManagementJPanel parentPanel, UserService userService) {
		this.parentPanel = parentPanel;
		this.userService = userService;
		this.passwordBox = new JPasswordField(20);
		this.confirmPasswordBox = new JPasswordField(20);


		initBackgroundPanel();
		this.add(backgroundPanel);
		this.setTitle("添加用户");
		this.setSize(600, 200);
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

		JLabel title = new JLabel("用户信息");
		title.setFont(CommonFont.Static);

		labelPanel.add(title);
	}

	// 初始化商品信息面板
	public void initContentPanel() {
		contentPanel = new JPanel(new GridLayout(4, 2));

		usernameLabel = new JLabel("用户名", JLabel.CENTER);
		passwordLabel = new JLabel("密码", JLabel.CENTER);
		confirmPasswordLabel = new JLabel("确认密码", JLabel.CENTER);
		userTypeLabel = new JLabel("用户类型", JLabel.CENTER);

		nameBox = new JTextField("");

		// 用户类型下拉框
		typeBox = new JComboBox();
		Set<Integer> userTypeKeySet = UserManagementJPanel.USER_TYPE_MAP.keySet();
		List<Integer> userTypeKeyList = userTypeKeySet.stream().collect(Collectors.toList());
		for (int i = 0; i < userTypeKeyList.size(); i++) {
			String id = userTypeKeyList.get(i) + "";
			String typeName = UserManagementJPanel.USER_TYPE_MAP.get(userTypeKeyList.get(i));
			if (!"-1".equals(id)){
				typeBox.addItem(new Item(id, typeName));
			}
		}
		typeBox.setSelectedIndex(2);

		contentPanel.add(usernameLabel);
		contentPanel.add(nameBox);
		contentPanel.add(passwordLabel);
		contentPanel.add(passwordBox);
		contentPanel.add(confirmPasswordLabel);
		contentPanel.add(confirmPasswordBox);
		contentPanel.add(userTypeLabel);
		contentPanel.add(typeBox);

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

			String name = nameBox.getText().trim();
			String password = passwordBox.getText().trim();
			String confirmPassword = confirmPasswordBox.getText().trim();

			Item selectedItem = (Item)this.typeBox.getSelectedItem();
			int type = Integer.valueOf(selectedItem.getKey());

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入用户名");
			} else if (password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入密码");
			} else if (confirmPassword.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入确认密码");
			} else if (!confirmPassword.equals(password)) {
				JOptionPane.showMessageDialog(null, "两次输入密码不一致，请重新输入");
			} else {
				User user = User.builder()
						.name(name)
						.password(password)
						.type(type)
						.build();
				try {
					userService.add(user);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					return;
				}
				JOptionPane.showMessageDialog(null, "添加成功");
				this.setVisible(false);
				parentPanel.refreshTablePanel();
			}
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
