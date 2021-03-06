package xyz.comfyz.security.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.model.UserDetalis;
import xyz.comfyz.security.util.EncryptUtil;

/**
 * @author : comfy create at 2018-04-03 19:27
 */
public class SecretUtils {

    private static final String MD5_SALT = "$1$Comfy";
    private static final String SECRET_SPLIT = "@";


    public static String encrypt(UserDetalis userDetalis) {
        StringBuilder body = new StringBuilder(JSONObject.toJSONString(userDetalis));
        return EncryptUtil.aesEncrypt(body.append(SECRET_SPLIT).append(EncryptUtil.md5(MD5_SALT, body.toString())).toString());
    }

    public static UserDetalis decrypt(String secret) {
        if (StringUtils.hasText(secret)) {
            String val = EncryptUtil.aesDecrypt(secret);
            final int splitIndex = val.lastIndexOf(SECRET_SPLIT);
            if (splitIndex >= 0 && splitIndex < val.length() - 1) {
                String md5_var = val.substring(splitIndex + 1, val.length());
                String body = val.substring(0, splitIndex);
                if (EncryptUtil.md5(MD5_SALT, (body + SECRET_SPLIT)).equals(md5_var))
                    return JSON.parseObject(body, UserDetalis.class);
            }
        }
        return null;
    }
}
