package com.gfes.view;


import com.gfes.entity.Group;
import com.gfes.service.GroupService;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class GroupMembersJFrame extends JFrame implements MouseListener, ListSelectionListener {

	// Variables declaration - do not modify
	private JButton searchBtn1;
	private JButton searchBtn2;
	private JButton addBtn;
	private JButton deleteBtn;
	private JButton addAllBtn;
	private JButton deleteAllBtn;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JList jList1;
	private JList jList2;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane3;
	private JTextField jTextField1;
	private JTextField jTextField2;
	// End of variables declaration

	private GroupService groupService;
	private String groupId;
	private Group groupItem;

	private Vector<String> notJoinedUsers = new Vector();
	private Vector<String> joinedUsers = new Vector();

	/**
	 * Creates new form GroupMembersJFrame
	 */
	public GroupMembersJFrame(GroupService groupService,String groupId) {
		this.groupId = groupId;
		this.groupService = groupService;
		Group group = groupService.query(groupId);
		this.groupItem = group;

		//查询组内外用户
		List<String> members = groupService.queryMembers(groupId);
		List<String> nomembers = groupService.queryNonmembers(groupId);
		this.notJoinedUsers.clear();
		this.notJoinedUsers.addAll(nomembers);
		this.joinedUsers.clear();
		this.joinedUsers.addAll(members);

		initComponents();
		this.setTitle("群组成员管理");
		this.setResizable(false);
		this.setSize(745, 600);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		addBtn = new JButton();
		deleteBtn = new JButton();
		addAllBtn = new JButton();
		deleteAllBtn = new JButton();
		jScrollPane1 = new JScrollPane();
		jList1 = new JList(this.notJoinedUsers);
		jTextField1 = new JTextField();
		jTextField2 = new JTextField();
		searchBtn1 = new JButton();
		jScrollPane3 = new JScrollPane();
		jList2 = new JList(this.joinedUsers);
		searchBtn2 = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setText(this.groupItem.getName() + "群组成员管理");

		jLabel2.setText(this.groupItem.getName());

		jLabel3.setText("用户");

		addBtn.setText("添加");

		deleteBtn.setText("删除");

		addAllBtn.setText("全部添加");

		deleteAllBtn.setText("全部删除");
		addBtn.addMouseListener(this);
		deleteBtn.addMouseListener(this);
		addAllBtn.addMouseListener(this);
		deleteAllBtn.addMouseListener(this);


		jScrollPane1.setViewportView(jList1);
		jList1.addListSelectionListener(this);

		jTextField1.setText("");
		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		jTextField2.setText("");

		searchBtn1.setText("搜索");
		searchBtn1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jScrollPane3.setViewportView(jList2);
		jList2.getModel().addListDataListener(new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent e) {
				System.out.println("add");
			}
			@Override
			public void intervalRemoved(ListDataEvent e) {
				System.out.println("Removed");
			}
			@Override
			public void contentsChanged(ListDataEvent e) {
				System.out.println("Changed");
			}
		});
		jList2.addListSelectionListener(this);

		searchBtn2.setText("搜索");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGap(53, 53, 53)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(searchBtn1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(jScrollPane3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(searchBtn2, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
								.addGap(63, 63, 63))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
										.addGap(257, 263, Short.MAX_VALUE)
										.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE)
										.addGap(246, 246, 246))
								.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
										.addGap(132, 132, 132)
										.addComponent(jLabel3)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel2)
										.addGap(164, 164, 164))
								.addGroup(layout.createSequentialGroup()
										.addGap(323, 323, 323)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addComponent(addAllBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(addBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(deleteBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(deleteAllBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap(89, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(searchBtn2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
												.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(searchBtn1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
												.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 389, GroupLayout.PREFERRED_SIZE)
										.addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 389, GroupLayout.PREFERRED_SIZE))
								.addGap(43, 43, 43))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
										.addGap(140, 140, 140)
										.addComponent(addBtn, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
										.addGap(39, 39, 39)
										.addComponent(deleteBtn)
										.addGap(35, 35, 35)
										.addComponent(addAllBtn)
										.addGap(39, 39, 39)
										.addComponent(deleteAllBtn)
										.addGap(125, 125, 125)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel2)
												.addComponent(jLabel3))
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		);

		pack();
	}// </editor-fold>

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}
	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Object source = e.getSource();
		//添加
		if (source == addBtn){
			int selectedIndex = jList1.getSelectedIndex();
			if (selectedIndex < 0){
				JOptionPane.showMessageDialog(null, "请选择用户");
				return;
			}
			String selectedItem = (String) jList1.getModel().getElementAt(selectedIndex);
			groupService.addMembers(Arrays.asList(selectedItem),groupId);
			this.notJoinedUsers.remove(selectedItem);
			this.jList1.setListData(this.notJoinedUsers);
			this.joinedUsers.add(selectedItem);
			this.jList2.setListData(this.joinedUsers);

			jList2.clearSelection();
			jList1.clearSelection();
		} else if (source == addAllBtn){
			//存库
			groupService.addMembers(this.notJoinedUsers,groupId);
			this.joinedUsers.addAll(this.notJoinedUsers);
			this.notJoinedUsers.clear();
			this.jList2.setListData(this.joinedUsers);
			this.jList1.setListData(this.notJoinedUsers);
		} else if (source == deleteBtn){
			int selectedIndex = jList2.getSelectedIndex();
			if (selectedIndex < 0){
				JOptionPane.showMessageDialog(null, "请选择成员");
				return;
			}
			String selectedItem = (String) jList2.getModel().getElementAt(selectedIndex);
			groupService.deleteMembers(Arrays.asList(selectedItem),groupId);
			this.joinedUsers.remove(selectedItem);
			this.jList2.setListData(this.joinedUsers);
			this.notJoinedUsers.add(selectedItem);
			this.jList1.setListData(this.notJoinedUsers);

		} else if (source == deleteAllBtn){
			groupService.deleteMembers(this.joinedUsers,groupId);
			this.notJoinedUsers.addAll(this.joinedUsers);
			this.joinedUsers.clear();
			this.jList2.setListData(this.joinedUsers);
			this.jList1.setListData(this.notJoinedUsers);

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	// selection listener
	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object source = e.getSource();
		if (source == jList1 && e.getValueIsAdjusting()){
			jList2.clearSelection();
		}else if (source == jList2 && e.getValueIsAdjusting()){
			jList1.clearSelection();
		}
	}
}
