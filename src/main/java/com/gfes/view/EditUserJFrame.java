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

public class EditUserJFrame extends JFrame implements MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
	private JLabel usernameLabel, passwordLabel, confirmPasswordLabel, userTypeLabel;
	private JTextField nameBox;
	private JPasswordField passwordBox, confirmPasswordBox;
	private JComboBox typeBox;
	private JButton editBtn;

	private UserManagementJPanel parentPanel;
	private UserService userService;
	private User editUser;

	public EditUserJFrame(UserManagementJPanel parentPanel, UserService userService, String id) {
		this.userService = userService;
		this.parentPanel = parentPanel;
		User user = userService.query(id);
		if (user == null){
			JOptionPane.showMessageDialog(null, "未查询到用户！");
			this.setVisible(false);
			return;
		}
		this.editUser = user;
		initBackgroundPanel();
		this.add(backgroundPanel);

		this.setTitle("修改用户");
		this.setSize(600, 200);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	// 初始化背景面板
	public void initBackgroundPanel() {
		backgroundPanel = new JPanel(new BorderLayout());

		initLabelPanel();
		initContentPanel();
		initButtonPanel();

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
		contentPanel = new JPanel(new GridLayout(2, 2));

		usernameLabel = new JLabel("用户名", JLabel.CENTER);
//		passwordLabel = new JLabel("密码", JLabel.CENTER);
//		confirmPasswordLabel = new JLabel("确认密码", JLabel.CENTER);
		userTypeLabel = new JLabel("用户类型", JLabel.CENTER);
		nameBox = new JTextField(this.editUser.getName());

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
		typeBox.setSelectedIndex(editUser.getType());

		contentPanel.add(usernameLabel);
		contentPanel.add(nameBox);
//		contentPanel.add(passwordLabel);
//		contentPanel.add(passwordBox);
//		contentPanel.add(confirmPasswordLabel);
//		contentPanel.add(confirmPasswordBox);
		contentPanel.add(userTypeLabel);
		contentPanel.add(typeBox);

	}

	// 初始化按钮面板
	public void initButtonPanel() {
		buttonPanel = new JPanel();

		editBtn = new JButton("保存修改");
		editBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		editBtn.setForeground(Color.white);
		editBtn.setFont(CommonFont.Static);
		editBtn.addMouseListener(this);

		buttonPanel.add(editBtn);
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == editBtn) {

			String name = nameBox.getText().trim();
			Item selectedItem = (Item)this.typeBox.getSelectedItem();
			int type = Integer.valueOf(selectedItem.getKey());

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入用户名");
			} else {
				editUser.setName(name);
				editUser.setType(type);
				try {
					userService.saveOrUpdateUser(editUser,false);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					return;
				}
				JOptionPane.showMessageDialog(null, "修改成功");
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
