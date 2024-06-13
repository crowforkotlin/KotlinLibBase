package com.crow.base.tools.extensions

import android.util.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Random
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.math.floor

fun String.base64DecodeToString(flag: Int = Base64.DEFAULT) = Base64.decode(this, flag).decodeToString()
fun String.base64EncodeToString(flag: Int = Base64.DEFAULT) = Base64.encodeToString(this.toByteArray(), flag)

fun getPrivateKey(privateKeyContent: String): PrivateKey {
    val privateKeyPEM = privateKeyContent
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\n", "")

    val keyBytes = android.util.Base64.decode(privateKeyPEM, android.util.Base64.DEFAULT)

    val keySpec = PKCS8EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("UlNB".base64DecodeToString())
    return keyFactory.generatePrivate(keySpec)
}
fun encrypt(input: String, publicKey: PublicKey): ByteArray {
    val cipher = Cipher.getInstance("UlNBL0VDQi9QS0NTMVBhZGRpbmc=".base64DecodeToString())
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    return cipher.doFinal(input.toByteArray())
}
fun decryptRSA(input: ByteArray, privateKey: PrivateKey): String {

    val cipher = Cipher.getInstance("UlNBL0VDQi9QS0NTMVBhZGRpbmc=".base64DecodeToString())
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    return String(cipher.doFinal(input))
}

fun encryptAES(input: String, key: String): String {
    val cipher = Cipher.getInstance("QUVTL0VDQi9QS0NTNVBhZGRpbmc=".base64DecodeToString())
    val secretKey = SecretKeySpec(key.toByteArray(), "QUVT".base64DecodeToString())
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    return Base64.encodeToString(cipher.doFinal(input.toByteArray()), Base64.DEFAULT)
}

fun decryptAES(input: String, key: String): String {
    val cipher = Cipher.getInstance("QUVTL0VDQi9QS0NTNVBhZGRpbmc=".base64DecodeToString())
    val secretKey = SecretKeySpec(key.toByteArray(), "QUVT".base64DecodeToString())
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    return String(cipher.doFinal(Base64.decode(input, Base64.DEFAULT)))
}