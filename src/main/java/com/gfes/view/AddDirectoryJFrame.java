package com.gfes.view;

import com.gfes.entity.FileInfo;
import com.gfes.entity.Group;
import com.gfes.service.FileInfoService;
import com.gfes.service.GroupService;
import com.gfes.util.CommonFont;
import com.gfes.util.UuidUtil;
import lombok.SneakyThrows;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.UUID;

public class AddDirectoryJFrame extends JFrame implements MouseListener {

	// 定义全局组件
	JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
	JLabel label_name;
	JTextField name;
	JButton button_add, button_exit;


	private Group group;

	private final FileInfoService fileInfoService;

	// 父面板对象
	FileManagerJPanel parentPanel;

	public AddDirectoryJFrame(FileInfoService fileInfoService, FileManagerJPanel parentPanel,
							  Group group) {
		this.fileInfoService = fileInfoService;
		this.parentPanel = parentPanel;
		this.group = group;
		initBackgroundPanel();

		this.add(backgroundPanel);
		this.setTitle("添加文件夹");
		this.setSize(640, 200);
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

		JLabel title = new JLabel("文件信息");
		title.setFont(CommonFont.Static);

		labelPanel.add(title);
	}

	// 初始化商品信息面板
	public void initContentPanel() {
		contentPanel = new JPanel(new GridLayout(1, 2));
		label_name = new JLabel("文件夹名称", JLabel.CENTER);
		name = new JTextField("");
		contentPanel.add(label_name);
		contentPanel.add(name);

	}

	// 初始化按钮面板
	public void initButtonPanel() {
		buttonPanel = new JPanel();

		button_add = new JButton("保存");
		button_exit = new JButton("关闭");
		button_add.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_exit.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_exit.setForeground(Color.white);
		button_exit.setFont(CommonFont.Static);
		button_exit.addMouseListener(this);
		button_add.setForeground(Color.white);
		button_add.setFont(CommonFont.Static);
		button_add.addMouseListener(this);
		buttonPanel.add(button_add);
		buttonPanel.add(button_exit);
	}

	// 鼠标点击事件
	@SneakyThrows
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_add) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setId(UuidUtil.idNoline());
			fileInfo.setFileName(name.getText().trim());
			fileInfo.setGroupId(this.group.getId());
			fileInfo.setCreateTime(new Date());
			fileInfo.setDoc(1);
			fileInfoService.insertById(fileInfo);
			this.setVisible(false);
			DefaultMutableTreeNode selectionNode=(DefaultMutableTreeNode)parentPanel.tree.getLastSelectedPathComponent();
			selectionNode.add(new DefaultMutableTreeNode(fileInfo));
			parentPanel.tree.updateUI();
		}
		if (e.getSource() == button_exit) {
			this.setVisible(false);
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
