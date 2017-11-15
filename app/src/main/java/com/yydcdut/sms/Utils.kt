package com.yydcdut.sms

import android.content.Context
import android.os.Environment
import java.io.*

/**
 * Created by yuyidong on 2017/11/12.
 */
object Utils {
    private val DIR = Environment.getDataDirectory()

    private val IGNORE_NUMBER = "ignore_number"
    private val IGNORE_TEXT = "ignore_text"

    private fun readStringFromFile(file: File?): String {
        if (file != null && file.exists() && file.isFile && file.length() > 0) {
            val fis = FileInputStream(file)
            val s = readStringFromInputStream(fis)
            closeStream(fis)
            return s
        }
        return ""
    }

    private fun readStringFromInputStream(inputStream: InputStream?): String {
        if (inputStream == null) return ""
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        while (true) {
            val len = inputStream.read(buffer)
            if (len != -1) bos.write(buffer, 0, len)
            else break
        }
        val s = String(bos.toByteArray())
        closeStream(bos)
        return s
    }

    private fun closeStream(closeable: Closeable?) {
        closeable?.close()
    }

    fun readIgnoreNumber(): MutableList<String> = readIgnoreFile(DIR.absolutePath + File.separator + IGNORE_NUMBER)

    fun saveIgnoreNumber(list: MutableList<String>) = saveIgnoreFile(DIR.absolutePath + File.separator + IGNORE_NUMBER, format(list))

    fun readIgnoreText(): MutableList<String> = readIgnoreFile(DIR.absolutePath + File.separator + IGNORE_TEXT)

    fun saveIgnoreText(list: MutableList<String>) = saveIgnoreFile(DIR.absolutePath + File.separator + IGNORE_TEXT, format(list))

    private fun format(list: MutableList<String>): String {
        val set: HashSet<String> = HashSet(list)
        val sb = StringBuilder()
        for (text in set) {
            sb.append(text).append(";")
        }
        return sb.toString()
    }

    private fun readIgnoreFile(path: String): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
            return list
        }
        val content = readStringFromFile(file)
        val result = content.split(";")
        list += result
        return list
    }

    private fun saveIgnoreFile(path: String, content: String) {
        val file = File(path)
        if (file.exists()) {
            file.writeText(content)
        }
    }

    fun savePhone(phone: String) =
            App.instance().getSharedPreferences("sms", Context.MODE_PRIVATE).edit().putString("phone", phone).commit()

    fun getPhone(): String =
            App.instance().getSharedPreferences("sms", Context.MODE_PRIVATE).getString("phone", "")
}