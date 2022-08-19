package xin.cosmos.basic.ocr.baidu.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件读取工具类
 */
public class FileUtils {

    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流  
        FileInputStream fis = new FileInputStream(filePath);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数  
        int hasRead = 0;
        while ((hasRead = fis.read(bbuf)) > 0) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileAsBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
        return readFileAsBytes(file);
    }

    /**
     * 根据文件读取byte[] 数组
     */
    public static byte[] readFileAsBytes(File file) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
            BufferedInputStream in = null;
            in = new BufferedInputStream(new FileInputStream(file));
            short bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len1;
            while ((len1 = in.read(buffer, 0, bufSize)) != -1) {
                bos.write(buffer, 0, len1);
            }
            return bos.toByteArray();
        }
    }

    /**
     * 根据文件读取byte[] 数组
     */
    public static byte[] readFileAsBytes(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            throw new FileNotFoundException("multipartFile is must be not null.");
        }
        return multipartFile.getBytes();
    }

    /**
     * MultipartFile转File
     * <p>
     * 选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
     *
     * @param multipartFile
     * @return
     */
    public static File transferToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        File file = File.createTempFile(filename[0], filename[1]);
        multipartFile.transferTo(file);
        file.deleteOnExit();
        return file;
    }
}
