package com.gfes.view;

import com.gfes.common.BaseTableModule;
import com.gfes.common.CustomizedTable;
import com.gfes.entity.User;
import com.gfes.service.FileInfoService;
import com.gfes.service.FilePermissionService;
import com.gfes.service.UserGroupService;
import com.gfes.service.UserService;
import com.gfes.util.CommonFont;
import com.gfes.util.Tools;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.context.ApplicationEventPublisher;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Vector;

public class FileReaderAndWritePermission extends JFrame  implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel, tablePanel;
    private JLabel passwordLabel, confirmPasswordLabel, userTypeLabel;
    private JTextField nameBox;
    private JPasswordField passwordBox, confirmPasswordBox;
    private JComboBox typeBox;
    private JButton addBtn;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    private JComboBox sexBox;
    JLabel addLabel, editLabel, deleteLabel, membersLabel;
    private FilePermissionService permissionService;
    private User user;
    private final FilePermissionService filePermissionService;
    private ApplicationEventPublisher eventPublisher;
    private UserService userService;
    private final UserGroupService userGroupService;
    private final FileInfoService fileInfoService;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 父面板对象
    private final FileManagementTableCellEditor parentPanel;
    private final String[] HEADERS = { "文件名", "用户名","读写权限","创建人"};
    String id;

    public FileReaderAndWritePermission(User user,JLabel confirmPasswordLabel, FilePermissionService filePermissionService, FileManagementTableCellEditor parentPanel, UserService userService, UserGroupService userGroupService, FileInfoService fileInfoService, String id) {
        this.confirmPasswordLabel = confirmPasswordLabel;
        this.permissionService = permissionService;
        this.filePermissionService = filePermissionService;
        this.userGroupService = userGroupService;
        this.fileInfoService = fileInfoService;
        this.user = user;
        this.passwordLabel = passwordLabel;
        this.parentPanel = parentPanel;
        this.userService = userService;
        this.id = id;
        this.passwordBox = new JPasswordField(20);
        this.confirmPasswordBox = new JPasswordField(20);


        initBackgroundPanel();
        this.add(backgroundPanel);
        this.setTitle("添加文件访问权限");
        this.setSize(1200, 800);
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

    /**
     * 初始化label面板
     */
    public void initLabelPanel() {

        labelPanel = new JPanel();
        JLabel title = new JLabel("添加权限");
        title.setFont(CommonFont.Static);
        initToolPanel();
        labelPanel.add(title);
    }

    // 初始化工具面板
    public void initToolPanel() {

        labelPanel = new JPanel();
        // 工具图标
        URL resource = this.getClass().getResource("/image/add.png");
        Icon icon_add = new ImageIcon(resource);
        addLabel = new JLabel(icon_add);
        addLabel.setToolTipText("添加权限");
        addLabel.addMouseListener(this);
        labelPanel.add(addLabel);

    }

    // 初始化商品信息面板
    public void initContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        Vector<Vector> vector = new Vector<Vector>();
        try {
            // 通过文件id获取用户权限列表
            vector = userService.selectListByFileId(this.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseTableModule = new BaseTableModule(HEADERS, vector);
        table = new CustomizedTable(baseTableModule);
        Tools.setTableStyle(table);
        DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();
        dcm.getColumn(0).setPreferredWidth(10);
        dcm.getColumn(1).setPreferredWidth(10);
        dcm.getColumn(2).setPreferredWidth(20);
        dcm.getColumn(3).setPreferredWidth(20);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        table.validate();
        table.updateUI();
        backgroundPanel.updateUI();
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(jScrollPane);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
    }


    // 更新数据表格
    public void refreshTablePanel(Vector<Vector> vector) {

        contentPanel.remove(tablePanel);
        baseTableModule = new BaseTableModule(HEADERS, vector);
        table = new CustomizedTable(baseTableModule);
        Tools.setTableStyle(table);
        DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();
        dcm.getColumn(0).setPreferredWidth(10);
        dcm.getColumn(1).setPreferredWidth(10);
        dcm.getColumn(2).setPreferredWidth(20);
        dcm.getColumn(3).setPreferredWidth(20);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(jScrollPane);

        table.validate();
        table.updateUI();
        backgroundPanel.updateUI();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
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
        new AddFilePermissionJFrame(eventPublisher, userService, this,permissionService,user,id, userGroupService, fileInfoService, filePermissionService);
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
