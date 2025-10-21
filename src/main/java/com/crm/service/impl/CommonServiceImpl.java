package com.crm.service.impl;

import com.aliyun.oss.OSSClient;
import com.crm.service.CommonService;
import com.crm.vo.FileUrlVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private OSSClient ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Override
    public FileUrlVO upload(MultipartFile multipartFile){
        String fileUrl = "";

        // 获取文件原名称
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 新文件名称
        String newFileName = UUID.randomUUID() + fileType;

        // 获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 文件上传到OSS
        ossClient.putObject(bucketName, newFileName, inputStream);
        fileUrl = "https://" + bucketName + "." + ossClient.getEndpoint().getHost() + "/" + newFileName;

        return new FileUrlVO(fileUrl);
    }
}
