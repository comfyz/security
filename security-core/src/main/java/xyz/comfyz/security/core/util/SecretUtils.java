package xyz.comfyz.security.core.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.model.UserDetalis;

/**
 * @author : comfy create at 2018-04-03 19:27
 */
@SuppressWarnings("")
public class SecretUtils {

    private static final String SECRET_SALT = "@*#&$^%";
    private static final String SECRET_SPLIT = ".";

    public static String encrypt(UserDetalis userDetalis) {
        StringBuilder body = new StringBuilder(EncryptUtil.aesEncrypt(JSONObject.toJSONString(userDetalis)));
        return body.append(SECRET_SPLIT).append(MD5Encoder.encode(body.append(SECRET_SALT).toString().getBytes())).toString();
    }

    public static UserDetalis decrypt(String secret) {
        if (StringUtils.hasText(secret) && secret.lastIndexOf(SECRET_SPLIT) < 0) {
            String md5_var = secret.substring(secret.lastIndexOf(SECRET_SPLIT), secret.length());
            String body = secret.substring(0, secret.lastIndexOf(md5_var));
            if (MD5Encoder.encode(body.getBytes()).equals(md5_var))
                return JSONObject.parseObject(body, UserDetalis.class);
        }
        return null;
    }
}
