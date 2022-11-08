package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value

/**
 * @author 黄浩
 */
class UserBean {
    @Value("userId")
    lateinit var userId: String

    @Value("nickname")
    lateinit var nickname: String

    @Value("avatarUrl")
    lateinit var avatarUrl: String

    @Value("signature")
    lateinit var signature: String

    @Value("createTime")
    lateinit var createTime: String
}