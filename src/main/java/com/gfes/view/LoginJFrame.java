package com.gfes.view;

import com.gfes.entity.User;
import com.gfes.event.LoginEvent;
import com.gfes.service.UserService;
import com.gfes.util.CommonFont;
import com.gfes.util.ImagePanel;
import com.gfes.util.Md5Util;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

@Component
public class LoginJFrame extends JFrame implements MouseListener, FocusListener {

	private final UserService userService;

	private final ObjectFactory<IndexJFrame> indexJFrameFactory;

	private final ApplicationEventPublisher eventPublisher;


	// 全局的位置变量，用于表示鼠标在窗口上的位置
	static Point origin = new Point();

	// 定义全局组件
	JTextField usernameBox = new JTextField(20);
	JPasswordField passwordBox = new JPasswordField(20);
	ImagePanel backgroundPanel = null;
	JButton loginBtn, resetBtn;

	public LoginJFrame(UserService userService, ObjectFactory<IndexJFrame> indexJFrameFactory, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.indexJFrameFactory = indexJFrameFactory;
		this.eventPublisher = eventPublisher;
		Class<? extends JPasswordField> boxClass = passwordBox.getClass();
		URL logoResource = boxClass.getResource("/image/login_bg.jpg");
		Image backgrounImage = Toolkit.getDefaultToolkit().getImage(logoResource);
		Image imgae = Toolkit.getDefaultToolkit().getImage(boxClass.getResource("/image/logo.png"));
		this.setIconImage(imgae);
		// 窗口背景面板
		backgroundPanel = new ImagePanel(backgrounImage);
		backgroundPanel.setLayout(null);

		usernameBox.setBounds(378, 202, 173, 30);
		usernameBox.setFont(CommonFont.Static);
		usernameBox.addFocusListener(this);
//		usernameBox.setText("用户名/账号");
//		usernameBox.setText("admin");

		passwordBox.setBounds(378, 240, 173, 30);
		passwordBox.setFont(CommonFont.Static);
		passwordBox.addFocusListener(this);
//		passwordBox.setText("密码");
//		passwordBox.setText("123456");

		loginBtn = new JButton("登录");
		loginBtn.setBounds(380, 280, 70, 27);
		loginBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		loginBtn.setForeground(Color.white);
		loginBtn.setFont(CommonFont.Static);
		loginBtn.addMouseListener(this);

		resetBtn = new JButton("重置");
		resetBtn.setBounds(480, 280, 70, 27);
		resetBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		resetBtn.setForeground(Color.white);
		resetBtn.setFont(CommonFont.Static);
		resetBtn.addMouseListener(this);

		backgroundPanel.add(usernameBox);
		backgroundPanel.add(passwordBox);
		backgroundPanel.add(loginBtn);
		backgroundPanel.add(resetBtn);

		this.add(backgroundPanel);
		this.setTitle("群组文件管理系统");
		this.setSize(830, 530);
		this.setVisible(true);
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == loginBtn) {

			if ("用户名/账号".equals(usernameBox.getText())) {
				JOptionPane.showMessageDialog(null, "用户名不能为空");
			} else if ("密码".equals(passwordBox.getText())) {
				JOptionPane.showMessageDialog(null, "用户密码不能为空");
			} else {
				try {
					User user = userService.queryUser(usernameBox.getText(), Md5Util.MD5(passwordBox.getText()));
					if (user == null) {
						JOptionPane.showMessageDialog(null, "用户名密码有误");
					} else {
						eventPublisher.publishEvent(new LoginEvent(this,user));
						this.setVisible(false);
						IndexJFrame indexJFrame = indexJFrameFactory.getObject();
						indexJFrame.setUser(user);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == resetBtn) {
			usernameBox.setText("用户名/账号");
			passwordBox.setText("密码");
			passwordBox.setEchoChar('\0');
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

	// 聚焦事件
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == usernameBox) {
			if (usernameBox.getText().equals("用户名/账号")) {
				usernameBox.setText("");
			}
		} else if (e.getSource() == passwordBox) {
			if (passwordBox.getText().equals("密码")) {
				passwordBox.setText("");
				passwordBox.setEchoChar('*');
			}
		}

	}

	// 失焦事件
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == usernameBox) {
			if (usernameBox.getText().equals("")) {
				usernameBox.setText("用户名/账号");
			}
		} else if (e.getSource() == passwordBox) {
			if (passwordBox.getText().equals("")) {
				passwordBox.setText("密码");
				passwordBox.setEchoChar('\0');
			}
		}
	}


}
