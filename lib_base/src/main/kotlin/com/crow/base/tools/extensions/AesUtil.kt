package com.crow.base.tools.extensions

import android.text.TextUtils
import android.util.Base64
import java.io.UnsupportedEncodingException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AesUtil {
    //加密算法
    private const val algorithm = "AES"

    //模式
    private const val transformation = "AES/ECB/PKCS5Padding"

    //字符集
    private const val charset = "UTF-8"

    /**
     * 加密后转为Base64编码
     *
     * @param key  秘钥 16位
     * @param data 原文
     * @return Base64密文
     */
    fun encryptToBase64(key: String, data: String): String {
        try {
            if (!TextUtils.isEmpty(data)) {
                val valueByte = encrypt(
                    data.toByteArray(charset(charset)), key.toByteArray(
                        charset(
                            charset
                        )
                    )
                )
                return encodeBase64(valueByte)
            }
            return ""
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * 解密Base64编码的密文
     *
     * @param key  秘钥 16位
     * @param data Base64密文
     * @return 原文
     */
    fun decryptFromBase64(key: String, data: String): String? {
        try {
            return decrypt(decodeBase64(data), key.toByteArray(charset(charset)))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 编码为Base64
     *
     * @param input 原文
     * @return 编码后密文
     */
    fun encodeBase64(input: ByteArray?): String {
        return Base64.encodeToString(input, Base64.NO_WRAP)
    }

    /**
     * Base64解码
     *
     * @param input Base64文本
     * @return 原文
     */
    fun decodeBase64(input: String): ByteArray? {
        try {
            return Base64.decode(input.toByteArray(charset(charset)), Base64.NO_WRAP)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 加密
     *
     * @param data 加密byte数组
     * @param key  秘钥byte数组
     * @return 密文byte数组
     */
    fun encrypt(data: ByteArray?, key: ByteArray?): ByteArray? {
        try {
            val keySpec: SecretKeySpec = SecretKeySpec(key, algorithm)
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            return cipher.doFinal(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 解密
     *
     * @param data 密文byte数组
     * @param key  秘钥byte数组
     * @return 原文
     */
    fun decrypt(data: ByteArray?, key: ByteArray?): String? {
        try {
            val keySpec: SecretKeySpec = SecretKeySpec(key, algorithm)
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val result: ByteArray = cipher.doFinal(data)
            return String(result, charset(charset))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}