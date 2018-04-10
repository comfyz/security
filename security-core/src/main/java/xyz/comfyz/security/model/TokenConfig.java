package xyz.comfyz.security.model;

import org.springframework.util.StringUtils;

/**
 * @author : comfy create at 2018-04-04 11:33
 */
public class TokenConfig {
    private String name;
    private int expiry;
    private String domain;
    private String path;

    public TokenConfig() {
        this.name = defaultName();
        this.domain = defaultDomain();
        this.path = defaultPath();
        this.expiry = defaultExpiry();
    }

    private String defaultName() {
        return "Authentication";
    }

    private String defaultDomain() {
        return null;
    }

    private String defaultPath() {
        return "/";
    }

    private int defaultExpiry() {
        return 60 * 30;
    }

    public String getName() {
        return name;
    }

    public TokenConfig setName(String name) {
        if (StringUtils.hasText(name))
            this.name = name;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public TokenConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getPath() {
        return path;
    }

    public TokenConfig setPath(String path) {
        if (StringUtils.hasText(path))
            this.path = path;
        return this;
    }

    public int getExpiry() {
        return expiry;
    }

    public TokenConfig setExpiry(int expiry) {
        if (expiry != 0)
            this.expiry = expiry;
        return this;
    }
}
