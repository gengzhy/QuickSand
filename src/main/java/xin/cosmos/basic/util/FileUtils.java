package xin.cosmos.basic.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * 修改文件的最后修改时间
     *
     * @param absoluteFilePath 文件路径
     * @param date 最后修改时间
     * @return
     */
    public static boolean modifyFileDate(String absoluteFilePath, LocalDateTime date) {
        File file = new File(absoluteFilePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    modifyFileDate(f.getAbsolutePath(), date);
                }
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
        return file.setLastModified(calendar.getTimeInMillis());
    }

    /**
     * 修改文件的最后修改时间
     *
     * @param absoluteFilePath 文件路径
     * @param diffDays 距离文件最后修改时间的差的天数（整数表示最后修改日期往后延，负数表示最后修改时间往前移）
     * @return
     */
    public static boolean modifyFileDate(String absoluteFilePath, int diffDays) {
        File file = new File(absoluteFilePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    modifyFileDate(f.getAbsolutePath(), diffDays);
                }
            }
        }
        long diffMillis = file.lastModified() + 24L * 60 * 60 * 1000 * diffDays;
        return file.setLastModified(diffMillis);
    }

}
