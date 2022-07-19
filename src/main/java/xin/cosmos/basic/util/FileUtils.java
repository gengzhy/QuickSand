package xin.cosmos.basic.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;

/**
 * 文件处理工具
 */
public final class FileUtils {
    /**
     * 读取classpath下的文件数据
     *
     * @param fileClassPath classpath下的文件
     * @return
     */
    public static InputStream readFileStreamWithClassPath(String fileClassPath) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileClassPath);
    }

    /**
     * 读取classpath下的文件数据
     *
     * @param fileClassPath classpath下的文件
     * @return
     */
    public static File readFileWithClassPath(String fileClassPath) throws IOException {
        return new ClassPathResource(fileClassPath).getFile();
    }

    /**
     * 读取classpath下的文件数据
     *
     * @param absoluteFilePath 文件绝对路径
     * @return
     */
    public static InputStream readFile(String absoluteFilePath) throws IOException {
        return Files.newInputStream(Paths.get(absoluteFilePath));
    }

    /**
     * 复制文件到目标路径
     *
     * @param sourceFilePath
     * @param targetFilePath
     * @throws IOException
     */
    public static void copy(String sourceFilePath, String targetFilePath) throws IOException {
        Files.copy(Paths.get(sourceFilePath), Paths.get(targetFilePath));
    }

    /**
     * MultipartFile转File
     * <p>
     * 选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
     *
     * @param multipartFile
     * @return
     */
    public static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        InputStream ins;
        ins = file.getInputStream();
        toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        inputStreamToFile(ins, toFile);
        ins.close();
        return toFile;
    }

    /**
     * 获取流文件
     * @param ins
     * @param file
     */
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static boolean deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            return del.delete();
        }
        return false;
    }

}
