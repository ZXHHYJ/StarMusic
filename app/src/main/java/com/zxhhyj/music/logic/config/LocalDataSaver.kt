@file:Suppress("UNCHECKED_CAST", "UNCHECKED_CAST", "UNCHECKED_CAST")

package com.zxhhyj.music.logic.config

import com.funny.data_saver.core.DataSaverInterface
import com.zxhhyj.music.MainApplication
import io.fastkv.FastKV

val DataSaverUtils = object : DataSaverInterface() {

    private val kv = FastKV.Builder(MainApplication.context, "fastkv").build()

    private fun notifyExternalDataChanged(key: String, value: Any?) {
        if (senseExternalDataChange) externalDataChangedFlow?.tryEmit(key to value)
    }

    override fun <T> saveData(key: String, data: T) {
        if (data == null) {
            remove(key)
            notifyExternalDataChanged(key, null)
            return
        }
        with(kv) {
            when (data) {
                is Long -> putLong(key, data)
                is Int -> putInt(key, data)
                is String -> putString(key, data)
                is Boolean -> putBoolean(key, data)
                is Float -> putFloat(key, data)
                is Double -> putDouble(key, data)
                else -> throw IllegalArgumentException("Unable to save $data, this type(${data!!::class.java}) cannot be saved to FastKV, call [registerTypeConverters] to support it.")
            }
            notifyExternalDataChanged(key, data)
        }
    }

    override fun <T> readData(key: String, default: T): T = with(kv) {
        val res: Any = when (default) {
            is Long -> getLong(key, default)
            is Int -> getInt(key, default)
            is String -> getString(key, default)!!
            is Boolean -> getBoolean(key, default)
            is Float -> getFloat(key, default)
            is Double -> getDouble(key, default)
            else -> throw IllegalArgumentException("Unable to read $default, this type(${default!!::class.java}) cannot be read from FastKV, call [registerTypeConverters] to support it.")
        }
        return@with res as T
    }

    override fun remove(key: String) {
        kv.remove(key)
        notifyExternalDataChanged(key, null)
    }

    override fun contains(key: String) = kv.contains(key)
}