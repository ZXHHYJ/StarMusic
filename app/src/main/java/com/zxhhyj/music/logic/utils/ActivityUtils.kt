package com.zxhhyj.music.logic.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * 该对象提供了一些常用的帮助方法，用于执行与 Web 和邮件相关的常见活动。
 */
object ActivityUtils {

    /**
     * 在 Web 浏览器中打开指定的 URL。
     * @param context 调用该方法的上下文
     * @param url 要在 Web 浏览器中打开的 URL
     */
    fun openUrl(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            val activity = context as? Activity
            activity?.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    /**
     * 打开邮件客户端以发送邮件。
     * @param context 调用该方法的上下文
     * @param mail 要发送邮件的地址
     */
    fun openMail(context: Context, mail: String) {
        try {
            val uri = Uri.parse("mailto:$mail")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            val activity = context as? Activity
            activity?.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    /**
     * 发起添加QQ群流程
     * @param context 调用该方法的上下文
     * @param key key 由官网生成的key
     */
    fun openQQGroup(context: Context, key: String) {
        try {
            val intent = Intent()
            intent.data =
                Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key")
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

}