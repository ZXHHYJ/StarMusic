package mandysax.anna2

/**
 * @author liuxiaoliu66
 */
enum class TYPE {
    /**
     * 以get的方式进行请求
     */
    GET,

    /**
     * 以post的方法进行请求
     */
    POST,

    /**
     * 删除
     */
    DELETE,

    /**
     * 向服务器提交数据
     */
    PUT
}