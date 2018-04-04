package xyz.comfyz.security.core.support;

import org.springframework.util.StringUtils;

/**
 * @author : comfy create at 2018-04-04 11:33
 */
public class CookieConfig {
    private final String name;
    private final String domain;
    private final String path;
    private final int expiry;

    public CookieConfig() {
        this(null, null, null, null);
    }

    public CookieConfig(String domain) {
        this(null, domain, null, null);
    }

    public CookieConfig(String name, String domain, String path, Integer expiry) {
        this.name = StringUtils.hasText(name) ? name : defaultName();
        this.domain = StringUtils.hasText(domain) ? domain : defaultDomain();
        this.path = StringUtils.hasText(path) ? path : defaultPath();
        this.expiry = expiry != null && expiry > 0 ? expiry : defaultExpiry();
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public int getExpiry() {
        return expiry;
    }

    public String defaultName() {
        return "Authentication";
    }

    public String defaultDomain() {
        return "localhost";
    }

    public String defaultPath() {
        return "/";
    }

    public int defaultExpiry() {
        return 60 * 30;
    }

}
