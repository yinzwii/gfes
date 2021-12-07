package com.gfes.view;

import cn.hutool.core.io.FileUtil;
import com.browser.BrowserWindow;
import com.gfes.constant.Operation;
import com.gfes.entity.FileInfo;
import com.gfes.entity.FilePermission;
import com.gfes.entity.Log;
import com.gfes.entity.User;
import com.gfes.event.EditFileEvent;
import com.gfes.event.LogEvent;
import com.gfes.service.*;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EventObject;
import java.util.Vector;

public class FileManagementTableCellEditor extends DefaultCellEditor implements ActionListener, MouseListener {

    //表格按钮
    private JPanel buttonJPanel;
    private JButton previewBtn, downloadBtn, editBtn, delBtn, permitBtn;
    JLabel confirmPasswordLabel;
    private FileManagerJPanel parent;
    private BrowserWindow browser;
    private FileInfoService fileInfoService;
    private MinioService minioService;
    private ApplicationEventPublisher eventPublisher;
    private User user;
    private UserService userService;
    private final FilePermissionService filePermissionService;
    private final UserGroupService userGroupService;
    public FileManagementTableCellEditor(FileManagerJPanel parent, BrowserWindow browser,
                                         FileInfoService fileInfoService, MinioService minioService,
                                         ApplicationEventPublisher eventPublisher, User user,
                                         UserService userService, FilePermissionService filePermissionService,
                                         UserGroupService userGroupService) {
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());
        this.filePermissionService = filePermissionService;
        this.userGroupService = userGroupService;
        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);
        this.parent = parent;
        this.browser = browser;
        this.fileInfoService = fileInfoService;
        this.minioService = minioService;
        this.eventPublisher = eventPublisher;
        this.user = user;
        this.userService = userService;
    }

    public void initTableBtn() {
        //表格按钮
        buttonJPanel = new JPanel();
        URL resource = this.getClass().getResource("/image/preview.png");
        Icon previewIcon = new ImageIcon(resource);
        previewBtn = new JButton("预览", previewIcon);
        previewBtn.setBorder(null);
        resource = this.getClass().getResource("/image/download.png");
        Icon downloadIcon = new ImageIcon(resource);
        downloadBtn = new JButton("下载", downloadIcon);
        resource = this.getClass().getResource("/image/edit_file.png");
        Icon editIcon = new ImageIcon(resource);
        editBtn = new JButton("编辑", editIcon);
        resource = this.getClass().getResource("/image/delete_file.png");
        Icon delIcon = new ImageIcon(resource);
        delBtn = new JButton("删除", delIcon);
        resource = this.getClass().getResource("/image/permissions.png");
        Icon permitIcon = new ImageIcon(resource);
        permitBtn = new JButton("权限", permitIcon);
        previewBtn.setBounds(2, 3, 76, 30);
        downloadBtn.setBounds(70, 3, 76, 30);
        editBtn.setBounds(138, 3, 76, 30);
        delBtn.setBounds(206, 3, 76, 30);
        permitBtn.setBounds(274, 3, 76, 30);
        downloadBtn.addActionListener(this);
        previewBtn.addActionListener(this);
        editBtn.addActionListener(this);
        delBtn.addActionListener(this);
        permitBtn.addActionListener(this);
        buttonJPanel.add(previewBtn);
        buttonJPanel.add(downloadBtn);
        buttonJPanel.add(editBtn);
        buttonJPanel.add(delBtn);
        buttonJPanel.add(permitBtn);
        buttonJPanel.setLayout(null);
        super.editorComponent = buttonJPanel;
        buttonJPanel.setOpaque(false);
        buttonJPanel.updateUI();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        System.out.println("getTableCellEditorComponent:" + isSelected + row + "-" + column);
        initTableBtn();
        return this.buttonJPanel;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }



    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int selectedRow = parent.table.getSelectedRow();
        if (source == previewBtn) {
            if (selectedRow > -1) {
                // 查询当前登录用户权限
                // 通过文件id获取文件创建人
                FileInfo file = fileInfoService.selectFileInfoById( (String) parent.baseTableModule.getValueAt(selectedRow, 0));
                // 判断当前登录用户是否为创建者
                if (file.getCreatorId().equals(user.getId())) {
                    String fileName = (String) parent.baseTableModule.getValueAt(selectedRow, 1);
                    String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                    FileInfo fileInfo = fileInfoService.queryFile(id);
                    browser.open(fileName, fileInfo.getPreviewPath(), true);
                    Log log = Log.builder()
                            .fileId(id)
                            .fileName(fileName)
                            .operation(Operation.PREVIEW)
                            .userId(user.getId())
                            .username(user.getName())
                            .build();
                    eventPublisher.publishEvent(new LogEvent(this, log));
                } else {
                    // 判断该登录用户是否拥有权限
                    FilePermission filePermission1 = filePermissionService.selectFilePermissionByUserIdAndFileId(user.getId(), (String) parent.baseTableModule.getValueAt(selectedRow, 0));
                    if (filePermission1 != null) {
                        if (filePermission1.getAccessCode().toString().equals("1")) {
                            String fileName = (String) parent.baseTableModule.getValueAt(selectedRow, 1);
                            String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                            FileInfo fileInfo = fileInfoService.queryFile(id);
                            browser.open(fileName, fileInfo.getPreviewPath(), true);
                            Log log = Log.builder()
                                    .fileId(id)
                                    .fileName(fileName)
                                    .operation(Operation.PREVIEW)
                                    .userId(user.getId())
                                    .username(user.getName())
                                    .build();
                            eventPublisher.publishEvent(new LogEvent(this, log));
                        } else {
                            JOptionPane.showMessageDialog(null, "权限不足");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "权限不足");
                    }
                }
            }
        } else if (source == downloadBtn) {
            if (selectedRow > -1) {
                // 判断当前登录用户是否为创建者
                FileInfo file = fileInfoService.selectFileInfoById( (String) parent.baseTableModule.getValueAt(selectedRow, 0));
                if (file.getCreatorId().equals(user.getId())) {
                    String fileName = (String) parent.baseTableModule.getValueAt(selectedRow, 1);
                    String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                    String url = minioService.downloadFiles(id);
                    browser.open(fileName, url, false);
                    Log log = Log.builder()
                            .fileId(id)
                            .fileName(fileName)
                            .operation(Operation.DOWNLOAD)
                            .userId(user.getId())
                            .username(user.getName())
                            .build();
                    eventPublisher.publishEvent(new LogEvent(this, log));
                }else {
                    // 判断该登录用户是否拥有权限
                    FilePermission filePermission1 = filePermissionService.selectFilePermissionByUserIdAndFileId(user.getId(),(String) parent.baseTableModule.getValueAt(selectedRow, 0));
                    if (filePermission1 != null) {
                        String fileName = (String) parent.baseTableModule.getValueAt(selectedRow, 1);
                        String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                        String url = minioService.downloadFiles(id);
                        browser.open(fileName, url, false);
                        Log log = Log.builder()
                                .fileId(id)
                                .fileName(fileName)
                                .operation(Operation.DOWNLOAD)
                                .userId(user.getId())
                                .username(user.getName())
                                .build();
                        eventPublisher.publishEvent(new LogEvent(this, log));
                    } else {
                        JOptionPane.showMessageDialog(null, "权限不足");
                    }
                }
            }
        } else if (source == editBtn) {
            if (selectedRow > -1) {
                // 判断该登录用户是否拥有权限
                String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                FilePermission filePermission1 = filePermissionService.selectFilePermissionByUserIdAndFileId(user.getId(), id);
                String fileDir = getHomePath() + File.separator;
                Desktop desktop = Desktop.getDesktop();
                String url = minioService.downloadFiles(id);
                FileInfo fileInfo = fileInfoService.selectFileInfoById(id);
                // 判断当前登录用户是否为创建者
                if (fileInfo.getCreatorId().equals(user.getId()) ||
                        (filePermission1 != null && (filePermission1.getAccessCode() == 2))) {
                    File file = saveUrlAs(url, fileDir, fileInfo, "GET");
                    Log log = Log.builder()
                            .fileId(id)
                            .fileName(fileInfo.getFileName())
                            .operation(Operation.OPEN)
                            .userId(user.getId())
                            .username(user.getName())
                            .build();
                    eventPublisher.publishEvent(new LogEvent(this, log));
                    desktop.open(file);
                    EditFileEvent event = new EditFileEvent(this, fileInfo);
                    event.setFileManagerJPanel(parent);
                    eventPublisher.publishEvent(event);
                } else {
                    JOptionPane.showMessageDialog(null, "权限不足");
                }
            }
        } else if (source == delBtn) {
            if (selectedRow > -1) {
                // 判断当前登录用户是否为创建者
                FileInfo fileTemp = fileInfoService.selectFileInfoById( (String) parent.baseTableModule.getValueAt(selectedRow, 0));
                if (fileTemp.getCreatorId().equals(user.getId())) {
                    String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                    String fileName = (String) parent.baseTableModule.getValueAt(selectedRow, 1);
                    fileInfoService.deleteFileById(id);
                    //更新文件树
                    DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) parent.tree.getLastSelectedPathComponent();
                    if (selectionNode != null && selectionNode.getLevel() == 2) {
                        int childCount = selectionNode.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode) selectionNode.getChildAt(i);
                            if (child.getUserObject().toString().equals(fileName)) {
                                selectionNode.remove(child);
                                parent.tree.updateUI();
                                break;
                            }
                        }
                        //更新列表
                        parent.refreshData(selectionNode);
                    }

                    if (selectionNode != null && selectionNode.getLevel() == 3) {
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectionNode.getParent();
                        int childCount = parentNode.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                            if (child.getUserObject().toString().equals(fileName)) {
                                parentNode.remove(child);
                                parent.tree.updateUI();
                                break;
                            }
                        }
                        //更新列表
                        parent.refreshData(parentNode);
                    }

                    //记录日志
                    Log log = Log.builder()
                            .fileId(id)
                            .fileName(fileName)
                            .operation(Operation.OPEN)
                            .userId(user.getId())
                            .username(user.getName())
                            .build();
                    eventPublisher.publishEvent(new LogEvent(this, log));
                    JOptionPane.showMessageDialog(null, "删除成功");
                    //刷新数据
                    Vector<Vector> fileList = fileInfoService.selectAllFileInfos(user);
                    parent.refreshTablePanel(fileList);
                } else {
                    JOptionPane.showMessageDialog(null, "权限不足");
                }
            }
        }else if (source == permitBtn){
            if (selectedRow > -1){
                // 判断当前登录用户是否为创建者
                FileInfo fileTemp = fileInfoService.selectFileInfoById( (String) parent.baseTableModule.getValueAt(selectedRow, 0));
                if (fileTemp.getCreatorId().equals(user.getId())) {
                    String id = (String) parent.baseTableModule.getValueAt(selectedRow, 0);
                    new FileReaderAndWritePermission(user,confirmPasswordLabel, filePermissionService, null, userService, userGroupService, fileInfoService, id);
                } else {
                    JOptionPane.showMessageDialog(null, "权限不足");
                }
            }
        }

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

    /**
     * @从制定URL下载文件并保存到指定目录
     * @param filePath 文件将要保存的目录
     * @param method 请求方法，包括POST和GET
     * @param url 请求的路径
     * @return
     */

    public File saveUrlAs(String url,String filePath,FileInfo fileInfo ,String method){
        boolean protect = StringUtils.hasText(fileInfo.getSecretKey());
        //创建不同的文件夹目录
        File file=new File(filePath);
        //判断文件夹是否存在
        if (!file.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath + fileInfo.getId() + "." + FileUtil.getSuffix(fileInfo.getFileName()));
            BufferedOutputStream bos = null;
            if (protect){
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                User user = userService.queryById(fileInfo.getCreatorId());
                if (user == null){
                    throw new Exception("文件信息损坏");
                }
                String secret = user.getSecret();
                if(!StringUtils.hasText(secret)){
                    throw new Exception("发生未知异常，请联系系统管理员");
                }
                byte[] secretBytes = secret.getBytes();
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretBytes, "AES"),new IvParameterSpec(MinioService.AES_IV.getBytes()));
                CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOut, cipher);
                bos = new BufferedOutputStream(cipherOutputStream);
            }else {
                bos = new BufferedOutputStream(fileOut);
            }

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return new File(filePath + fileInfo.getId() + "." + FileUtil.getSuffix(fileInfo.getFileName()));

    }

    public static String getHomePath() {
        String userDir = System.getenv("KKFILEVIEW_BIN_FOLDER");
        if (userDir == null) {
            userDir = System.getProperty("user.dir");
        }
        if (userDir.endsWith("bin")) {
            userDir = userDir.substring(0, userDir.length() - 4);
        } else {
            String separator = File.separator;
            userDir = userDir + separator + "file";
        }
        return userDir;
    }


    /**
     * 删除目录本身以及目录下的所有文件及文件夹
     *
     * @param pathName 目录名
     * @return
     */
    public boolean deleteDiretory(String pathName) {
        boolean flag = false;
        //根据路径创建文件对象
        File directory = new File(pathName);
        //如果路径是一个目录且不为空时，删除目录
        if (directory.isDirectory() && directory.exists()) {
            //获取目录下的所有的目录和文件，放入数组files中
            File[] files = directory.listFiles();
            //遍历目录下的所有的文件和目录
            for (int i = 0; i < files.length; i++) {
                //如果目录下是文件时，调用deleteFiles（）方法，删除单个文件
                if (files[i].isFile()) {
                    flag = deleteFiles(files[i].getAbsolutePath());
                }//如果目录下是目录时，调用自身deleteDirectory()，形成递归调用
                else {
                    flag = deleteDiretory(files[i].getAbsolutePath());
                }
            }

            //删除目录本身，如果想要保留目录只删除文件，此句可以不要
            flag = directory.delete();
        }
        //删除成功时返回true，失败时返回false
        return flag;
    }


    /**
     * 删除单个文件
     *
     * @param pathName 删除文件路径名
     * @return
     */
    public boolean deleteFiles(String pathName) {
        boolean flag = false;
        //根据路径创建文件对象
        File file = new File(pathName);
        //路径是个文件且不为空时删除文件
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        //删除失败时，返回false
        return flag;
    }
}
