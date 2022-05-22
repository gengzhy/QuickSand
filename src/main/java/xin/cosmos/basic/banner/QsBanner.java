package xin.cosmos.basic.banner;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import xin.cosmos.basic.helper.ContextHolder;

import javax.validation.constraints.NotNull;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 自定义banner
 *
 * @author geng
 */
@Component
public class QsBanner implements CommandLineRunner, ApplicationContextAware {
    private static final Random RANDOM = new SecureRandom();
    private static final PrintStream PRINTER = System.out;
    private ApplicationContext applicationContext;

    private static final String[] BANNER = {
            "  ██████╗ ██╗   ██╗██╗ ██████╗██╗  ██╗███████╗ █████╗ ███╗   ██╗██████╗ ",
            " ██╔═══██╗██║   ██║██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗████╗  ██║██╔══██╗",
            " ██║   ██║██║   ██║██║██║     █████╔╝ ███████╗███████║██╔██╗ ██║██║  ██║",
            " ██║▄▄ ██║██║   ██║██║██║     ██╔═██╗ ╚════██║██╔══██║██║╚██╗██║██║  ██║",
            " ╚██████╔╝╚██████╔╝██║╚██████╗██║  ██╗███████║██║  ██║██║ ╚████║██████╔╝",
            "  ╚══▀▀═╝  ╚═════╝ ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝ ",
            " (C)Author($user)   Jdk(v$jdk)   Supported by:Spring Boot(v$version)"};

    @Override
    public void run(String... args) {
        AnsiColor color = randomColor();
        for (int i = 0; i < BANNER.length; i++) {
            if (i == BANNER.length - 1) {
                color = AnsiColor.GREEN;
                BANNER[i] = BANNER[i].replace("$user", System.getProperty("user.name"));
                BANNER[i] = BANNER[i].replace("$jdk", System.getProperty("java.version"));
                BANNER[i] = BANNER[i].replace("$version", SpringBootVersion.getVersion());
            }
            PRINTER.println(AnsiOutput.toString(color, BANNER[i]));
        }
    }

    private AnsiColor randomColor() {
        AnsiColor[] colors = AnsiColor.values();
        return colors[RANDOM.nextInt(colors.length)];
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ContextHolder.initApplicationContext(applicationContext);
    }
}
