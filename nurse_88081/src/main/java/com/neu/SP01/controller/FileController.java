package com.neu.SP01.controller;

import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/FileController")
@CrossOrigin("*")
@Slf4j
public class FileController {
    //阿里云上传文件实现
    @Autowired
    AliOSSUtils aliOSSUtils;

    @PostMapping("/upload")
    public ResponseBean uploadFile(MultipartFile file) throws Exception {
        log.info("文件上传 文件名 {}", file.getOriginalFilename());
        String url = aliOSSUtils.upload(file);
        log.info("文件上传完成 文件访问的url:{}", url);
        return new ResponseBean<>(200, "上传成功", url);
    }
}
