package com.hy.smartwork.file;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileDemo {

    private static Logger logger = LoggerFactory.getLogger(FileDemo.class);

    /**
     * 生产图片上传URL
     */
    private static String URL = "https://test.bankneo.co.id/console/private/image/upload";

    /**
     * 用户登录token
     * 经常要变化
     */
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJCWUIiLCJzdWIiOiIyIiwiaWF0IjoxNjUzNTMzODg5LCJleHAiOjE2NTQxMzg2ODl9.R-84kSfDLNFQVHxi07c0ZSjOLDeepbrKZhelkxLMsfQ";

    /**
     * 扫描文件
     *
     * @param path
     */
    public static void scanFile(String path) {
        File dir = new File(path);
        String[] names = dir.list();
        for (String name : names) {
            //把路径下的内容放到names里
            System.out.println(name);
            uploadFileByHttpClient(path, name);
        }
    }

    /**
     * 通过httpClient上传文件
     *
     * @param fileName
     * @param path
     * @return 外部域名的下载url
     */
    public static String uploadFileByHttpClient(String path, String fileName) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(URL);
            // HttpMultipartMode.RFC6532参数的设定是为避免文件名为中文时乱码
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            // 头部放文件上传的head可自定义
            httpPost.addHeader("accessToken", ACCESS_TOKEN);
            httpPost.addHeader("appId", "100001");
            // 上传文件的路径
            File file = new File(path + fileName);
            builder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, fileName);
            builder.addTextBody("bizType", "1");
            // 其余参数，可自定义
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            // 接收调用外部接口返回的内容
            HttpEntity responseEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回的内容都在content中
                InputStream content = responseEntity.getContent();
                // 定义BufferedReader输入流来读取URL的响应
                BufferedReader in = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                if (StringUtils.isNotBlank(result)) {
                    // 解析响应报文，提取url
                    FileResponse fr = JSONObject.parseObject(result, FileResponse.class);
                    System.out.println(fr.getData().getUrl());
                } else {
                    logger.error("上传文件失败：返回result为null");
                }
            }
        } catch (Exception e) {
            logger.error("上传文件失败：", e);
        } finally {//处理结束后关闭httpclient的链接
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void main(String[] args) {
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题一\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题二\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题三\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题四\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题五\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题六\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题七\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题八\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\红包主题_修改稿\\红包主题\\主题九\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包-封面\\1\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包-封面\\2\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包-封面\\3\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包-封面\\4\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包-封面\\5\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包-封面\\6\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\斋月红包上线准备\\讨红包\\图替换\\");
//        scanFile("C:\\Users\\huangying1\\Desktop\\群聊广场图标\\");
    }
}


