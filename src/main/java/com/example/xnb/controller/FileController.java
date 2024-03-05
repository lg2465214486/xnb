package com.example.xnb.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.xnb.config.JsonResult;
import com.example.xnb.config.JsonResultUtil;
import com.example.xnb.service.ISystemService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Author：.
 * DATE：2023-01-2023/1/4 16:50
 * Description：<描述>
 */
@RestController
@RequestMapping("/multiFile")
public class FileController {

    @Value("${file.image}")
    private String image;
    @Value("${file.downloadPath}")
    private String downloadPath;
    @Autowired
    private ISystemService systemService;


    @PostMapping("/uploadImage")
    public JsonResult uploadImageFile(@RequestParam("type") String type, MultipartHttpServletRequest request) {
        List<String> fileName = new ArrayList<>();
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        multiFileMap.keySet().forEach((key) -> {
            multiFileMap.get(key).forEach((file) -> {
                if (StrUtil.isEmpty(file.getOriginalFilename())) return;
                long mb = file.getSize() / 1024 / 1024;
                if (mb > 20) {
                    throw new RuntimeException("图片文件最大为 20 mb");
                }
                String[] split = file.getOriginalFilename().split("\\.");
                String uuid = UUID.randomUUID().toString().replace("-","");
                String fileType = split[split.length - 1];
                String originalFilename = uuid+fileType;
                //System.out.println(fileType);
                //bmp/gif/jpg/png
                String path = image;
                String yyyyMMdd = DateUtil.format(new Date(),"yyyyMMdd");
                path += "/"+yyyyMMdd + File.separator;
                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                path += originalFilename;
                try {
                    BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
                    int b;
                    while ((b = bis.read()) != -1) {
                        bos.write(b);
                    }
                    fileName.add(downloadPath + image +"/"+ yyyyMMdd + "/" +originalFilename);
                    bos.close();
                    bis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        if ("1".equals(type)){
            systemService.editKeyValue("qrCode",fileName.get(0));
        }else if("2".equals(type)){
            systemService.editKeyValue("helpQrCode",fileName.get(0));
        }else if("3".equals(type)){
            systemService.editKeyValue("btcQrCode",fileName.get(0));
        }else if("4".equals(type)){
            systemService.editKeyValue("ethQrCode",fileName.get(0));
        }
        return JsonResultUtil.success(fileName);
    }

    @GetMapping("/download")
    public void downloadImageFile(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            //处理中文乱码
            request.setCharacterEncoding("UTF-8");
            fileName = new String(fileName.getBytes("iso8859-1"), "UTF-8");
            //url地址如果存在空格，会导致报错！  解决方法为：用+或者%20代替url参数中的空格。
            fileName = fileName.replace(" ", "%20");
            FileInputStream in;
            String[] split = fileName.split("\\.");
            String fileType = split[split.length - 1];
            if (fileType.equals("mp4")) {
                response.setContentType("video/mp4");
            }
            in = new FileInputStream(fileName);
            int i = in.available();
            byte[] data = new byte[i];
            in.read(data);
            in.close();
            outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
