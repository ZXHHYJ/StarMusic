package studio.mandysa.music.logic.model;

import mandysax.anna2.annotation.Value;

/**
 * @author Huang hao
 */
public final class UserModel {
    @Value("userId")
    public String userId;

    @Value("nickname")
    public String nickname;

    @Value("avatarUrl")
    public String avatarUrl;

    @Value("signature")
    public String signature;

    @Value("createTime")
    public String createTime;
}
