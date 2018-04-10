package xyz.comfyz.security.example.bo;


import xyz.comfyz.security.model.Authority;
import xyz.comfyz.security.support.AntPathRequestMatcher;

/**
 * @author : comfy create at 2018-04-09 15:32
 */
public class Auth extends Authority {

    private final int dataRegion;

    public Auth(AntPathRequestMatcher requestMatcher, int dataRegion) {
        super(requestMatcher);
        this.dataRegion = dataRegion;
    }

    public int getDataRegion() {
        return dataRegion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Auth))
            return false;

        if (obj == this)
            return true;

        return this.getRequestMatcher().equals(((Auth) obj).getRequestMatcher())
                && this.getDataRegion() == (((Auth) obj).getDataRegion());
    }
}
