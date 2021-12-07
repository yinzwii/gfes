package com.gfes.service;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gfes.config.MinioProperties;
import com.gfes.entity.FileInfo;
import com.gfes.entity.User;
import com.gfes.util.HttpIOUtil;
import com.gfes.util.MinioUtils;
import com.gfes.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.PublicKey;
import java.util.*;

@Service
@Slf4j
public class MinioService {

    private final MinioUtils minioUtils;
    private final MinioProperties minioProperties;
    private final FileInfoService fileInfoService;
    private static final String[] PREVIEW_FILE_TYPE = new String[]{".xlsx", ".xls", ".docx", ".doc"};
    public static final String AES_IV = "1234567887654321";


    public MinioService(MinioUtils minioUtils, MinioProperties minioProperties, FileInfoService fileInfoService) {
        this.minioUtils = minioUtils;
        this.minioProperties = minioProperties;
        this.fileInfoService = fileInfoService;
    }

    /**
     * 附件上传
     * @return
     */
    public FileInfo uploadFile(File file,String pId, User user,FileInfo fileInfo, boolean protect) throws Exception {
        FileItem fileItem = createFileItem(file);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        InputStream inputStream = new FileInputStream(file);
        long size = file.length();
        String fileName = fileInfo == null ? file.getName() : fileInfo.getFileName();
        String filePath = getSourcePath(fileName);
        if (fileInfo == null){
            // 验证文件名重复
            if (fileInfoService.existFile(pId,fileName)){
                throw new Exception("文件名已存在！");
            }
            fileInfo = new FileInfo();
            fileInfo.setId(UUID.randomUUID().toString());
            fileInfo.setCreateTime(new Date());
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(filePath);
            fileInfo.setPId(pId);
            fileInfo.setLock(false);
            fileInfo.setCreatorId(user.getId());
        }else {
            filePath = fileInfo.getFilePath();
        }

        if (protect){
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            String secret = user.getSecret();
            if(!StringUtils.hasText(secret)){
                throw new Exception("发生未知异常，请联系系统管理员");
            }
            byte[] secretBytes = secret.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretBytes, "AES"),new IvParameterSpec(AES_IV.getBytes()));
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            String puk = user.getPublicKey();
            PublicKey publicKey = RSAUtil.getPublicKey(Base64Utils.decode(puk.getBytes()));
            byte[] bytes = RSAUtil.RSAEncode(publicKey, secretBytes);
            fileInfo.setSecretKey(Base64Utils.encodeToString(bytes));
            try {
                minioUtils.overwritingUploadObject(minioProperties.getBucketName(), size, ContentType.MULTIPART.toString(), cipherInputStream,filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                cipherInputStream.close();
                inputStream.close();
            }
        }else {
            try {
                minioUtils.overwritingUploadObject(minioProperties.getBucketName(), size, ContentType.MULTIPART.toString(), inputStream,filePath);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        }


        fileInfoService.insertById(fileInfo);
        if (checkPreviewFileType(multipartFile)) {
            MultipartFile[] f = new MultipartFile[]{multipartFile};
            Map<String,MultipartFile[]> map = new HashMap<String,MultipartFile[]>();
            map.put(fileName,f);
            Map<String,String> params = new HashMap<>();
            String[] arr = filePath.split("\\.");
            params.put("objectName",arr[0] + ".pdf");
            // 调用第三方office预览服务,返回预览地址
            String responseStr = HttpIOUtil.sendMoreRequest("http://online-doc.xx.com/file-conversion/office-to-pdf",params,map);
            if (responseStr != "" && responseStr != null) {
                JSONObject response = JSONUtil.parseObj(responseStr);
                if (500 == Integer.valueOf(response.get("status") + "")){
                    fileInfo.setPreviewPath("#");
                }else {
                    fileInfo.setPreviewPath(response.getStr("previewUrl"));
                }
                fileInfoService.updateFile(fileInfo);
            }
        } else {
            fileInfo.setPreviewPath(filePath);
            fileInfoService.updateFile(fileInfo);
        }
        return  fileInfo;
    }

    /**
     * 创建文件item
     * @param file
     * @return
     */
    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * @Description: 校验可转换文件 <br/>
     * @Date: 2019/10/10 10:30 <br/>
     * @Version: 1.0 <br/>
     */
    public boolean checkPreviewFileType(MultipartFile uploadFile) {
        boolean isLegal = false;
        for (String type : PREVIEW_FILE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
            }
        }
        return isLegal;

    }

    /**
     * 通过路径下载文件
     * @param id
     * @return
     */
    public String downloadFiles(String id) {
        FileInfo fileInfo = fileInfoService.selectFileInfoById(id);
        return  minioUtils.presignedGetObject(minioProperties.getBucketName(),fileInfo.getFilePath(),24*3600);
    }


    /**
     * 生成临时访问链接
     *
     * @param id
     * @return
     */
    public String presignedGetObject(String id) {
        FileInfo fileInfo = this.fileInfoService.selectFileInfoById(id);
        if (fileInfo.getFilePath().equals(fileInfo.getPreviewPath())) {
            return minioUtils.presignedGetObject(minioProperties.getBucketName(), fileInfo.getPreviewPath(), 24 * 3600);
        } else {
            return minioUtils.presignedGetObject(minioProperties.getBucketNameNode(), fileInfo.getPreviewPath(), 24 * 3600);
        }
    }

    /**
     * @Description: 生成路径以及文件名 <br/>
     * @Date: 2019/10/10 10:41 <br/>
     * @Version: 1.0 <br/>
     */
    public String getSourcePath(String sourceFileName) {
        Calendar calendar = Calendar.getInstance();
        return "source/" + calendar.get(Calendar.YEAR)
                + "/" +  calendar.get(Calendar.MONTH) + "/"
                +  calendar.get(Calendar.DAY_OF_MONTH)  + "/"  +  calendar.get(Calendar.HOUR_OF_DAY) +sourceFileName;
    }
}
