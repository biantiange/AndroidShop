package com.wishes.market.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    /**
     * 图片文件保存路径
     */
    @Value("${image.save.path}")
    private String SAVE_PATH;


    /**
     * nginx映射出来的图片url
     */
    @Value("${image.save.url}")
    private String SAVE_URL;

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        //String fileName = file.getOriginalFilename();

        String fileName = System.currentTimeMillis()+".png";
        String okPath = SAVE_PATH + fileName;
        File dest = new File(okPath);
        try {
            file.transferTo(dest);
            LOGGER.info("上传成功");

            return SAVE_URL + fileName;
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return "上传失败";
    }


}
