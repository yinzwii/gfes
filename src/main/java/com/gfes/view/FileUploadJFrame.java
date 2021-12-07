package com.gfes.view;

import com.gfes.entity.FileInfo;
import com.gfes.entity.User;
import com.gfes.service.FileInfoService;
import com.gfes.service.MinioService;
import com.gfes.util.CommonFont;
import lombok.SneakyThrows;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class FileUploadJFrame extends JFrame implements MouseListener {

    /**
     * 定义全局组件
     */
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JButton button_add, button_exit;
    private DefaultMutableTreeNode selectedNode;
    private final FileInfoService fileInfoService;
    private final MinioService minioService;
    private User user;
    /**
     * 父面板对象
     */
    FileManagerJPanel parentPanel;

    public FileUploadJFrame(FileInfoService fileInfoService, FileManagerJPanel parentPanel,
                            DefaultMutableTreeNode selectionNode, MinioService minioService,
                            User user) {
        this.fileInfoService = fileInfoService;
        this.parentPanel = parentPanel;
        this.minioService = minioService;
        this.selectedNode = selectionNode;
        this.user = user;

        initBackgroundPanel();

        this.add(backgroundPanel);

        this.setTitle("文件上传");
        this.setSize(640, 200);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * 初始化背景面板
     */
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
    }

    /**
     * 初始化商品信息面板
     */
    public void initContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        JToolBar jToolBar = new JToolBar();
        JLabel jl = new JLabel("请选择：");
        jl.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add("North", jl);
        contentPanel.add("North", jToolBar);
        JButton uploadBtn = new JButton("选择文件");
        uploadBtn.setHorizontalAlignment(SwingConstants.CENTER);
        jToolBar.add(uploadBtn);
        contentPanel.add("North", jToolBar);
        uploadBtn.addMouseListener(new MouseAdapter() {

            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent event) {
                eventOnImport(new JButton());
            }
        });
    }

    /**
     * 初始化按钮面板
     */
    public void initButtonPanel() {
        buttonPanel = new JPanel();

        button_exit = new JButton("关闭");
        button_exit.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        button_exit.setForeground(Color.white);
        button_exit.setFont(CommonFont.Static);
        button_exit.addMouseListener(this);
        buttonPanel.add(button_exit);
    }

    /**
     * 鼠标点击事件
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == button_exit) {
            this.setVisible(false);
        }
        if (e.getSource() == button_add) {
//            parentPanel.initTablePanel();
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


    /**
     *   * 文件上传功能
     *   *
     *   * @param developer
     *   * 按钮控件名称
     *  
     */

    public void eventOnImport(JButton developer) throws Exception {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
         /** 过滤文件类型 * */
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "xml", "txt", "doc", "docx","xlsx","xls");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(developer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File[] arrfiles = chooser.getSelectedFiles();
            if (arrfiles == null || arrfiles.length == 0) {
                return;
            }
            if (arrfiles.length > 1){
                //提示
                JOptionPane.showMessageDialog(null, "暂不支持多文件上传！");
                return;
            }
            for (File f : arrfiles) {

                FileInfo fileInfo = (FileInfo)selectedNode.getUserObject();
                try {
                    FileInfo newFile = minioService.uploadFile(f, fileInfo.getId(), user,null, true);
                    // 文件树增加文件节点
                    selectedNode.add(new DefaultMutableTreeNode(newFile));
                    parentPanel.tree.updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "上传失败！异常信息：" + e.getMessage());
                    return;
                }
                JOptionPane.showMessageDialog(null, "加密上传成功！");
                DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)parentPanel.tree.getLastSelectedPathComponent();
                parentPanel.refreshData(selectionNode);
                this.setVisible(false);
            }
        }
    }


}
