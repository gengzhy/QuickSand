package xin.cosmos.basic.ocr.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TencentOcrConfiguration {
    @Value("${ocr.tencent.secretId}")
    private String sercretId;
    @Value("${ocr.tencent.secretKey}")
    private String sercretkey;

    @Bean
    public OcrClient ocrClient() {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(sercretId, sercretkey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");// 指定接入地域域名(默认就近接入)
        // 从3.0.96版本开始, 单独设置 HTTP 代理
        // httpProfile.setProxyHost("真实代理ip");
        // httpProfile.setProxyPort(真实代理端口);
        httpProfile.setReqMethod("POST"); // get请求(默认为post请求)
        httpProfile.setConnTimeout(30); // 请求连接超时时间，单位为秒(默认60秒)
        httpProfile.setWriteTimeout(30);  // 设置写入超时时间，单位为秒(默认0秒)
        httpProfile.setReadTimeout(30);  // 设置读取超时时间，单位为秒(默认0秒)

        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        clientProfile.setSignMethod("HmacSHA256"); // 指定签名算法(默认为HmacSHA256)
        // 自3.1.80版本开始，SDK 支持打印日志。
        clientProfile.setHttpProfile(httpProfile);
        clientProfile.setDebug(true);
        // 从3.1.16版本开始，支持设置公共参数 Language, 默认不传，选择(ZH_CN or EN_US)
        clientProfile.setLanguage(Language.ZH_CN);
        // 实例化要请求产品的client对象,clientProfile是可选的
        OcrClient client = new OcrClient(cred, "ap-shanghai", clientProfile);
        return client;
    }
}
