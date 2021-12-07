package com.gfes.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;

public class FileManagementTableButtonRender implements TableCellRenderer{

    //表格按钮
    private JPanel buttonJPanel;
    private JButton previewBtn, downloadBtn, editBtn, delBtn, permitBtn;

    public FileManagementTableButtonRender() {
        initTableBtn();

    }

    public void initTableBtn(){
        //表格按钮
        buttonJPanel = new JPanel();
        URL resource = this.getClass().getResource("/image/preview.png");
        Icon previewIcon = new ImageIcon(resource);
        previewBtn = new JButton("预览",previewIcon);
        previewBtn.setBorder(null);
        resource = this.getClass().getResource("/image/download.png");
        Icon downloadIcon = new ImageIcon(resource);
        downloadBtn = new JButton("下载",downloadIcon);
        resource = this.getClass().getResource("/image/edit_file.png");
        Icon editIcon = new ImageIcon(resource);
        editBtn = new JButton("编辑",editIcon);
        resource = this.getClass().getResource("/image/delete_file.png");
        Icon delIcon = new ImageIcon(resource);
        delBtn = new JButton("删除",delIcon);
        resource = this.getClass().getResource("/image/permissions.png");
        Icon permitIcon = new ImageIcon(resource);
        permitBtn = new JButton("权限",permitIcon);
        previewBtn.setBounds(2, 3, 76, 30);
        downloadBtn.setBounds(70, 3, 76, 30);
        editBtn.setBounds(138, 3, 76, 30);
        delBtn.setBounds(206, 3, 76, 30);
        permitBtn.setBounds(274, 3, 76, 30);

        buttonJPanel.add(previewBtn);
        buttonJPanel.add(downloadBtn);
        buttonJPanel.add(editBtn);
        buttonJPanel.add(delBtn);
        buttonJPanel.add(permitBtn);
        buttonJPanel.setLayout(null);
        buttonJPanel.updateUI();
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        System.out.println("getTableCellRendererComponent:" + isSelected + hasFocus + "" + row + "" + column);
        return this.buttonJPanel;
    }



}
