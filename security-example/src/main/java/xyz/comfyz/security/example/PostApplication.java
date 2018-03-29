package xyz.comfyz.security.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author comfy at 2017-12-28 10:41
 * <p>
 * The final step be called after the container has started successfully.
 * - {@link CommandLineRunner} or {@link ApplicationRunner}
 * - The Same: There is a run method, we only need to implement this method.
 * - The difference: ApplicationRunner's run method parameters is ApplicationArguments,
 * and CommandLineRunner's run method parameters is String[].
 * <p>
 * Multiple {@link CommandLineRunner} beans can be ordered using the {@link Ordered} interface or {@link Order @Order} annotation.
 */
@Component
public class PostApplication implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(PostApplication.class);

    @Autowired
    private Environment env;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("\nAccess URLs:\n***************************\n"
                + "Local: \t\thttp://127.0.0.1:{}\n" +
                "***************************", StringUtils.isEmpty(env.getProperty("server.port")) ? "8080" : env.getProperty("server.port"));
    }
}
