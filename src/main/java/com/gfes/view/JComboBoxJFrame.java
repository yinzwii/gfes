package com.gfes.view;

import com.gfes.entity.FilePermission;
import com.gfes.entity.User;
import com.gfes.service.FilePermissionService;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JComboBoxJFrame implements TableCellRenderer {


    private JPanel jPanel;
    private JComboBox comboBox;
    JScrollPane jScrollPane;
    JTextField textField;
    private User user;
    String fileId;
    private final FilePermissionService filePermissionService;

    public JComboBoxJFrame(String id,User user,FilePermissionService filePermissionService){
        this.fileId = id;
        this.user = user;
        this.filePermissionService = filePermissionService;
        initTableBtn();
    }

    private void initTableBtn() {
        jPanel = new JPanel();
//        FilePermission filePermission = filePermissionService.selectFilePermissionByUserIdAndFileId(user.getId(),fileId);
//        if (filePermission != null) {
//           textField = new JTextField(filePermission.getAccessCode() == 1 ? "读" : (filePermission.getAccessCode() == 2 ? "写" :"读与写"));
//        }else {
//            textField = new JTextField("请选择权限");
//        }
        comboBox = new JComboBox();
        comboBox.addItem("--请选择--");
        comboBox.addItem("读");
        comboBox.addItem("写");
//        comboBox.addItem("读与写");
        jScrollPane = new JScrollPane(comboBox);
//        jPanel.add(textField);
        jPanel.add(jScrollPane);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return  this.jPanel;
    }
}
