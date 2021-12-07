package com.gfes.view;

import com.gfes.Application;
import com.gfes.entity.User;
import com.gfes.event.LoginEvent;
import com.gfes.service.UserService;
import com.gfes.util.CommonFont;
import com.gfes.util.ImagePanel;
import com.gfes.util.Md5Util;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * 个人中心功能面板
 */
public class UserHomeJPanel implements MouseListener{

	// 定义全局组件
	ImagePanel backgroundPanel;
	JPanel contentPanel, labelPanel, textPanel, buttonPanel;
	JTextField username = new JTextField(10);
	JPasswordField password = new JPasswordField(10);
	JTextField identify = new JTextField(10);
	JButton button_modify, button_save,button_logout;

	private LoginJFrame loginJFrame;
	private UserService userService;
	private final ApplicationEventPublisher eventPublisher;
	private final ObjectFactory<IndexJFrame> indexJFrameFactory;

	// 定义大小变量
	int width;
	int height;

	// 定义用户对象
	User user = null;
	JFrame jframe = null;

	public UserHomeJPanel(User user, JFrame jframe, UserService userService, LoginJFrame loginJFrame, ApplicationEventPublisher eventPublisher, ObjectFactory<IndexJFrame> indexJFrameFactory) {
		this.user = user;
		this.jframe = jframe;
		this.userService = userService;
		this.loginJFrame = loginJFrame;
		this.eventPublisher = eventPublisher;
		this.indexJFrameFactory = indexJFrameFactory;
		URL resource = this.getClass().getResource("/image/userbackground.jpg");
		Image bgimg = Toolkit.getDefaultToolkit().getImage(resource);
		backgroundPanel = new ImagePanel(bgimg);
		// 获取背景面板大小
		this.width = backgroundPanel.getWidth();
		this.height = backgroundPanel.getHeight();

		initContentPanel();
	}

	public void initContentPanel() {

		backgroundPanel.removeAll();

		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout(0, 30));
		contentPanel.setOpaque(false);
		labelPanel = new JPanel();
		labelPanel.setOpaque(false);
		textPanel = new JPanel(new GridLayout(3, 2, 0, 20));
		textPanel.setOpaque(false);
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);

		JLabel label = new JLabel();
		label.setText("<html><h2 style='text-align:center;'>个人信息</h2></html>");
		label.setFont(CommonFont.Static);

		JLabel label_username = new JLabel("用户名:", JLabel.CENTER);
		label_username.setFont(CommonFont.Static);
		JLabel label_password = new JLabel("密码:", JLabel.CENTER);
		label_password.setFont(CommonFont.Static);
		JLabel label_identify = new JLabel("角色:", JLabel.CENTER);
		label_identify.setFont(CommonFont.Static);

		if (user != null) {
			username.setText(user.getName());
			password.setText(user.getPassword());
			identify.setText(UserManagementJPanel.USER_TYPE_MAP.get(user.getType()));
		}

		username.setFont(CommonFont.Static);
		username.setEditable(false);
		password.setFont(CommonFont.Static);
		password.setEditable(false);
		identify.setFont(CommonFont.Static);
		identify.setEditable(false);

		button_modify = new JButton("修改信息");
		button_modify.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_modify.setForeground(Color.white);
		button_modify.setFont(CommonFont.Static);
		button_modify.addMouseListener(this);

		button_logout = new JButton("退出登录");
		button_logout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_logout.setForeground(Color.white);
		button_logout.setFont(CommonFont.Static);
		button_logout.addMouseListener(this);

		labelPanel.add(label);

		textPanel.add(label_username);
		textPanel.add(username);
		textPanel.add(label_password);
		textPanel.add(password);
		textPanel.add(label_identify);
		textPanel.add(identify);

		buttonPanel.add(button_modify);
		buttonPanel.add(button_logout);

		contentPanel.add(labelPanel, BorderLayout.NORTH);
		contentPanel.add(textPanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		backgroundPanel.add(contentPanel, BorderLayout.CENTER);

	}

	public void modifyUserContentPanel() {

		username.setEditable(true);
		password.setEditable(true);

		button_save = new JButton("保存修改");
		button_save.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		button_save.setForeground(Color.white);
		button_save.setFont(CommonFont.Static);
		button_save.addMouseListener(this);

		buttonPanel.removeAll();
		buttonPanel.add(button_save);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_modify) {
			String input_password = (String) JOptionPane.showInputDialog(null, "请输入原始密码", "用户验证",
					JOptionPane.PLAIN_MESSAGE);
			if (input_password.trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "原始密码不能为空");
			} else {
				if (user != null) {
					if (!Md5Util.MD5(input_password).equals(user.getPassword())) {
						JOptionPane.showMessageDialog(null, "原始密码有误");
					} else {
						JOptionPane.showMessageDialog(null, "验证通过，请您修改信息");
						modifyUserContentPanel();
					}
				} else {
					JOptionPane.showMessageDialog(null, "登录超时，请您重新登录");
					jframe.setVisible(false);
					Application.main(null);
				}
			}
		}
		if (e.getSource() == button_save) {
			String string_username = username.getText().trim();
			String string_password = password.getText().trim();
			if (string_username.isEmpty()) {
				JOptionPane.showMessageDialog(null, "用户名不能为空");
			} else if (string_password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "用户密码不能为空");
			} else if(string_password.equals(user.getPassword())){
				JOptionPane.showMessageDialog(null, "用户信息修改成功");
				return;
			} else {
				String params[] = { username.getText(), password.getText(), user.getId() };
				User user = userService.queryById(this.user.getId());
				user.setPassword(password.getText());
				userService.saveOrUpdateUser(user,true);

				JOptionPane.showMessageDialog(null, "用户信息修改成功,请您重新登陆");
				jframe.setVisible(false);
				loginJFrame.setVisible(true);
			}

		}
		if (e.getSource() == button_logout) {
			// 关闭主面板
			jframe.setVisible(false);
			JOptionPane.showMessageDialog(null, "退出登录");
			eventPublisher.publishEvent(new LoginEvent(this , new User()));
			new LoginJFrame(userService, indexJFrameFactory, eventPublisher);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

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

}
