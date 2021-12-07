package com.gfes.event;

import cn.hutool.core.io.FileUtil;
import com.gfes.constant.Operation;
import com.gfes.entity.FileInfo;
import com.gfes.entity.Log;
import com.gfes.entity.User;
import com.gfes.repository.LogRepository;
import com.gfes.repository.UserRepository;
import com.gfes.service.MinioService;
import com.gfes.util.RSAUtil;
import com.gfes.util.RandomStringUtil;
import com.gfes.util.UuidUtil;
import com.gfes.view.FileManagerJPanel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class ApplicationEventListener {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final MinioService minioService;
    private final ApplicationEventPublisher eventPublisher;

    public ApplicationEventListener(UserRepository userRepository, LogRepository logRepository,
                                    MinioService minioService, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.minioService = minioService;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @EventListener
    public void onLoginEvent(LoginEvent event){
        User user = event.getPayload();
        // 为用户分配密钥
        if (StringUtils.hasText(user.getSecret())){
            return;
        }else {
            String secret = RandomStringUtil.randomLetterString(16);
            user.setSecret(secret);
            Map<String, String> keys = RSAUtil.generateKeyStrings();
            user.setPublicKey(keys.get("puk"));
            user.setPrivateKey(keys.get("prk"));
            userRepository.save(user);
        }


    }

    @Async
    @EventListener
    public void onLogEvent(LogEvent event){
        Log log = event.getPayload();
        log.setId(UuidUtil.idNoline());
        log.setCreateTime(new Date());
        logRepository.save(log);
    }

    @Async
    @EventListener
    @SneakyThrows
    public void onEditFileEvent(EditFileEvent event){
        FileInfo fileInfo = event.getPayload();
        User user = userRepository.findById(fileInfo.getCreatorId()).get();
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
        String localPath = userDir + File.separator + fileInfo.getId() + "." + FileUtil.getSuffix(fileInfo.getFileName());
        File file = new File(localPath);
        Thread.sleep(5_000);
        File file1 = new File(localPath + ".temp");
        while (true){
            file.renameTo(file1);
            if (file.exists()){
                System.out.println("文件占用中:" + localPath + ",原文件：" + fileInfo.getFileName());
                file1.delete();
                Thread.sleep(5_000);
            }else {
                file1.renameTo(file);
                break;
            }
        }
        System.out.println("文件解除占用:" + fileInfo.getFileName());
        //文件重新加密上传
        FileInfo newFile = minioService.uploadFile(file, fileInfo.getPId(), user,fileInfo, true);
        //刷新本地文件列表
        FileManagerJPanel fileManagerJPanel = event.getFileManagerJPanel();

        DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)fileManagerJPanel.tree.getLastSelectedPathComponent();
        if (selectionNode != null){
            fileManagerJPanel.refreshData(selectionNode);
        }
        //删除本地副本
        file.delete();
        Log log = Log.builder()
                .username(user.getName())
                .userId(user.getId())
                .operation(Operation.CLOSE)
                .fileName(fileInfo.getFileName())
                .fileId(fileInfo.getId())
                .createTime(new Date())
                .id(UuidUtil.idNoline())
                .build();
        eventPublisher.publishEvent(new LogEvent(this,log));
        ApplicationEventListener.log.info("文件保存成功,{}",newFile.getPreviewPath());

    }


}
