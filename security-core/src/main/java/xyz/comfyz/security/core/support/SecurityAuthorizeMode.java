package xyz.comfyz.security.core.support;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:41 2018/3/29
 * Version:     1.0
 * Description:
 */
public enum SecurityAuthorizeMode {
    ROLE,
    AUTHENTICATED,
    NONE;

    private SecurityAuthorizeMode() {
    }
}
