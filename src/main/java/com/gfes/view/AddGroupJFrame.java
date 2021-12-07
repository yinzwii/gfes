package com.gfes.view;

import com.gfes.entity.Group;
import com.gfes.service.GroupService;
import com.gfes.util.CommonFont;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AddGroupJFrame extends JFrame implements MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
	JLabel nameLabel;
	JTextField name;
	JButton addBtn;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	private GroupService groupService;

	// 父面板对象
	private GroupManagementJPanel parentPanel;

	public AddGroupJFrame(GroupManagementJPanel parentPanel, GroupService groupService) {
		this.parentPanel = parentPanel;
		this.groupService = groupService;
		initBackgroundPanel();

		this.add(backgroundPanel);

		this.setTitle("添加用户组");
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

		JLabel title = new JLabel("群组信息");
		title.setFont(CommonFont.Static);

		labelPanel.add(title);
	}

	// 初始化群组信息面板
	public void initContentPanel() {
		contentPanel = new JPanel(new GridLayout(1, 2));
		nameLabel = new JLabel("名称", JLabel.CENTER);
		name = new JTextField("");
		contentPanel.add(nameLabel);
		contentPanel.add(name);
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
			String name = this.name.getText().trim();

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入群组名称");
			}else {
				Group group = Group.builder()
						.name(name)
						.build();
				try {
					groupService.add(group);
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
