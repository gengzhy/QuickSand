package xin.cosmos.basic.util;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

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

}
