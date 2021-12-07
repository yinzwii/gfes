package com.gfes.view;

import com.gfes.entity.Group;
import com.gfes.service.GroupService;
import com.gfes.util.CommonFont;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditGroupJFrame extends JFrame implements MouseListener {

	// 定义全局组件
	private JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
	private JLabel nameLabel;
	private JTextField name;
	private JButton editBtn;

	private GroupManagementJPanel parentPanel;
	private GroupService groupService;
	private Group group;


	public EditGroupJFrame(GroupManagementJPanel parentPanel, String itemId, GroupService groupService) {
		this.parentPanel = parentPanel;
		this.groupService = groupService;
		Group group = groupService.query(itemId);
		this.group = group;
		initBackgroundPanel();

		this.add(backgroundPanel);

		this.setTitle("修改用户组");
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

		JLabel title = new JLabel("群组信息");
		title.setFont(CommonFont.Static);

		labelPanel.add(title);
	}

	// 初始化商品信息面板
	public void initContentPanel() {
		contentPanel = new JPanel(new GridLayout(1, 2));
		nameLabel = new JLabel("群组名称", JLabel.CENTER);
		name = new JTextField(this.group.getName());
		contentPanel.add(nameLabel);
		contentPanel.add(name);

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

			String name = this.name.getText().trim();

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(null, "请输入群组名称");
			} else {
				group.setName(name);
				try {
					groupService.update(group.getId(),group.getName());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					return;
				}
				JOptionPane.showMessageDialog(null, "修改成功!");
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
