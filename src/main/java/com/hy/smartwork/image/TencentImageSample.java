package com.hy.smartwork.image;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TextDetection;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author hy
 */
public class TencentImageSample {
    public static void main(String[] args) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("XXX", "YYY");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();

            // 需要把图片转成base64的字符串
            req.setImageBase64(fileToBase64("D://dbf641496539.jpg"));
            // 返回的resp是一个GeneralAccurateOCRResponse的实例，与请求对象对应
            GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);
            // 输出json格式的字符串回包
//            System.out.println(GeneralAccurateOCRResponse.toJsonString(resp));
            for (TextDetection textDec: resp.getTextDetections()) {
                System.out.println(textDec.getDetectedText());
            }

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    private static String fileToBase64(String filePath) {
        String base64String = "";
        try {
            // 读取文件内容
            File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] fileBytes = new byte[(int) file.length()];
            inputStream.read(fileBytes);
            inputStream.close();

            // 将文件内容转换为Base64编码
            byte[] base64Bytes = Base64.encodeBase64(fileBytes);
            base64String = new String(base64Bytes);

            // 打印Base64编码字符串
            System.out.println("Base64编码字符串:\n" + base64String);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64String;
    }
}
