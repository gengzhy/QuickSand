package xin.cosmos.basic.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 服务器文件下载工具类
 */
@Slf4j
public class RemoteFileUtil {
    /**
     * 从服务器URL下载文件到本地
     * 连接超时时间为6秒，文件读取超时时间为6秒
     *
     * @param url              服务器文件URL地址
     * @param absoluteFilePath 下载本地路径
     */
    public static void downloadFileFromUrl(String url, String absoluteFilePath) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(absoluteFilePath), 6000, 6000);
        } catch (Exception ex) {
            log.error("文件下载失败", ex);
        }
    }

    /**
     * 下载远程文件并保存到本地
     *
     * @param remoteFilePath 远程文件路径
     * @param localFilePath  本地文件路径
     */
    public static void downloadFile(String remoteFilePath, String localFilePath) {
        URL urlFile;
        HttpURLConnection httpUrl;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        try {
            urlFile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection) urlFile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载远程文件并转换为bytes数组
     *
     * @param remoteFilePath 远程文件路径
     */
    public static byte[] readRemoteFileToBytes(String remoteFilePath) {
        URL urlFile;
        HttpURLConnection httpUrl;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            urlFile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection) urlFile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new ByteArrayOutputStream(bis.available());
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len;
            while ((len = bis.read(buffer, 0, bufSize)) > 0) {
                bos.write(buffer, 0, len);
            }
            httpUrl.disconnect();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将文件字节写入到本地磁盘文件
     *
     * @param fileBytes  文件字节
     * @param targetPath 本地路径
     */
    public static void writeFile(byte[] fileBytes, String targetPath) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetPath))) {
            bos.write(fileBytes, 0, fileBytes.length);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
