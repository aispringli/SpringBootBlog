package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author SpringLee
 * @Description //TODO
 * @Date 2019/4/18 17:11 
 * @Param  * @param null
 * @return 
 **/
@RestController()
@RequestMapping("FileUpload")
public class FileUploadController {

    class Result{
        private int errno;

        private String[] data;

        private String[] name;
        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public String[] getData() {
            return data;
        }

        public void setData(String[] data) {
            this.data = data;
        }
        public Result(){

        }
        public Result(int errno){
            this.errno=errno;
        }

        public String[] getName() {
            return name;
        }

        public void setName(String[] name) {
            this.name = name;
        }
    }
    private String rootPath ="";

    private Logger logger = LoggerFactory.getLogger(getClass());

    public FileUploadController(){
        try {
            logger.info("原始路径: "+ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1));
            rootPath =new String(ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1).getBytes("gbk"),"utf-8");
            logger.info("UTF8路径: "+rootPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("UploadLogoImg")
    public ResponseResult UploadLogoImg(@RequestParam(value = "file") MultipartFile file){
        if (file.isEmpty()) {
            return new ResponseResult(1,"文件为空");
        }
        if(file.getSize()>1*1024*1024)return new ResponseResult(2,"文件大小不得超过1MB");
        String filePath = "/static/file/logo/"; // 上传后的路径
        try {
            saveFile(file,filePath);
            return new ResponseResult(0,true,saveFile(file,filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseResult(-1,"上传失败");
    }

    @RequestMapping("UploadBlogImg")
    public Result UploadBlogImg(@RequestParam("files") MultipartFile[] files){
        if (files==null)return new Result(1);
        String filePath = "/static/file/images/"; // 上传后的路径
        Result result=new Result(0);
        String[] dates=new String[files.length];
        for(int i=0;i<files.length;i++){
            if(files[i]==null)continue;
            if(files[i].getSize()>1*1024*1024)new Result(2);
            try {
                dates[i]=saveFile(files[i],filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(3);
            }
        }
        result.setData(dates);
        return result;
    }

    @RequestMapping("UploadBlogFile")
    public Result UploadBlogFile(@RequestParam("files") MultipartFile[] files){
        if (files==null)return new Result(1);
        String filePath = "/enclosure/"; // 上传后的路径
        Result result=new Result(0);
        String[] dates=new String[files.length];
        String[] names=new String[files.length];
        for(int i=0;i<files.length;i++){
            if(files[i]==null)continue;
            if(files[i].getSize()>100*1024*1024)new Result(2);
            try {
                String fileName=files[i].getOriginalFilename();
                names[i]=fileName.replace(fileName.substring(fileName.lastIndexOf(".")),"");
                dates[i]=saveFile(files[i],filePath).replace(filePath,"");
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(3);
            }
        }
        result.setData(dates);
        result.setName(names);
        return result;
    }


    /**
     * 文件保存方法
     * @param file
     * @param filePath
     * @throws IOException
     * @throws IllegalStateException
     */
    private String saveFile(MultipartFile file,String filePath) throws IllegalStateException, IOException {
        // 获取上传的文件名称，并结合存放路径，构建新的文件名称
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        fileName = UUID.randomUUID()+df.format(new Date()) + suffixName; // 新文件名
        fileName=filePath+fileName;

        File dest = new File(rootPath+fileName );
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 将上传文件保存到目标文件目录
        //file.transferTo(new File(dest.getAbsolutePath()));
        byte[] bytes = file.getBytes();
        Path path = Paths.get(dest.getAbsolutePath());
        Files.write(path, bytes);
        return fileName;
    }
}
