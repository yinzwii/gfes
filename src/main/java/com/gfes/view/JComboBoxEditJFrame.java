package com.gfes.view;

import com.gfes.entity.FileInfo;
import com.gfes.entity.FilePermission;
import com.gfes.entity.User;
import com.gfes.service.FileInfoService;
import com.gfes.service.FilePermissionService;
import com.gfes.service.UserService;
import com.gfes.util.Item;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JComboBoxEditJFrame extends DefaultCellEditor implements ActionListener, MouseListener {

    //表格按钮
    private JPanel jPanel;
    private JComboBox comboBox;
    private JLabel userSelectorLabel;
    JScrollPane jScrollPane;
    String fileId;
    String userId;
    Integer accessCode;

    private FileInfoService fileInfoService;
    private ApplicationEventPublisher eventPublisher;
    private UserService userService;
    private final  AddFilePermissionJFrame permission;
    public static Map<Integer,String> FILE_PERMISSION_MAP  = new HashMap<>();
    private final FilePermissionService filePermissionService;


    public JComboBoxEditJFrame(AddFilePermissionJFrame permission1, FileInfoService fileInfoService, ApplicationEventPublisher eventPublisher,
                               UserService userService, FilePermissionService filePermissionService,String fileId) {
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());
        this.fileId = fileId;
        this.filePermissionService = filePermissionService;
        FILE_PERMISSION_MAP.put(-1,"请选择");
        FILE_PERMISSION_MAP.put(0,"可读");
        FILE_PERMISSION_MAP.put(1,"可写");
//        FILE_PERMISSION_MAP.put(2,"可读可写");
        this.permission = permission1;
        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);
        this.fileInfoService = fileInfoService;
        this.eventPublisher = eventPublisher;
        this.userService = userService;
    }

    public void initTableBtn(){
        jPanel = new JPanel();
        // 用户类型下拉框
        comboBox = new JComboBox();
        Set<Integer> userTypeKeys = FILE_PERMISSION_MAP.keySet();
        for (Integer key : userTypeKeys){
            comboBox.addItem(new Item(key + "", FILE_PERMISSION_MAP.get(key)));
        }
        comboBox.setSelectedIndex(0);

        comboBox.addActionListener(this);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedRow = permission.table.getSelectedRow();
                    String userId = (String) permission.baseTableModule.getValueAt(selectedRow, 0);

                    // 获取当前用户权限
                    FilePermission filePermission = filePermissionService.selectFilePermissionByUserIdAndFileId(userId,fileId);
                    if (filePermission != null) {
                        accessCode = filePermission.getAccessCode();
                    }

                    // 获取创建人id
                    FileInfo fileInfo = fileInfoService.selectFileInfoById(fileId);
                    // 通过userId获取用户名
                    User user = userService.queryById(userId);
                    // 判断该用户是否存在该权限
                    FilePermission filePermission1 = filePermissionService.selectFilePermissionByUserIdAndFileId(userId,fileId);
                    if (filePermission1 != null) {
                        filePermission.setAccessCode(comboBox.getSelectedIndex());
                        filePermissionService.update(filePermission);
                    } else {
                        // 保存数据
                        filePermissionService.addFilePermission(user.getName(),userId,fileId,comboBox.getSelectedIndex(),fileInfo.getCreatorId());
                    }
                }
            }
        });
        jPanel.add(comboBox);
        super.editorComponent = jPanel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        System.out.println("getTableCellEditorComponent:" + isSelected + row + "-" + column);
        initTableBtn();
        return this.jPanel;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }



    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int selectedRow = permission.table.getSelectedRow();
        String id = (String) permission.baseTableModule.getValueAt(selectedRow, 0);
        // 更新数据
        System.out.println("kkkkkkkk"+ id);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(null, "点击" + this.getClickCountToStart());
    }

    @Override
    public void mousePressed(MouseEvent e) {

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
}
