package xyz.comfyz.security.core.provider;


import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.provider.token.TokenReader;
import xyz.comfyz.security.core.provider.token.TokenWriter;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : comfy
 * @date : 2018-04-03 14:19
 */
public class TokenProvider {

    private final static TreeSet<TokenReader> readers = new TreeSet<>();
    private final static Set<TokenWriter> writers = new HashSet<>();

    public static void flush(String secret) {
        if (StringUtils.hasText(secret))
            writers.forEach(tokenWriter -> tokenWriter.write(secret));
    }

    public static void remove(String secret) {
        if (StringUtils.hasText(secret))
            writers.forEach(tokenWriter -> tokenWriter.remove(secret));
    }

    public static String get() {
        AtomicReference<String> secret = new AtomicReference<>();
        readers.stream()
                .map(TokenReader::read)
                .filter(StringUtils::hasText)
                .filter(s -> writers.parallelStream().anyMatch(writer -> writer.exist(s)))
                .findFirst().ifPresent(secret::set);
        return secret.get();
    }

    public static void add(TokenReader reader) {
        readers.add(reader);
    }

    public static void add(TokenWriter writer) {
        writers.add(writer);
    }

}
