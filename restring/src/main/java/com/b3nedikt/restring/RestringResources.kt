package com.b3nedikt.restring

import android.content.res.Resources
import android.os.Build


/**
 * This is the wrapped resources which will be provided by Restring.
 * For getting strings and texts, it checks the strings repository first and if there's a new string
 * that will be returned, otherwise it will fallback to the original resource strings.
 */
@Suppress("DEPRECATION")
internal class RestringResources(val res: Resources,
                                 private val stringRepository: StringRepository)
    : Resources(res.assets, res.displayMetrics, res.configuration) {

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        setLocale()

        val value = getStringFromRepository(id)
        return value?.toString() ?: super.getString(id)
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String {
        setLocale()

        val value = getStringFromRepository(id)
        if (value != null) return String.format(value.toString(), *formatArgs)
        return super.getString(id, *formatArgs)
    }

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        setLocale()

        val value = getStringFromRepository(id)
        return value ?: super.getText(id)
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        setLocale()

        val value = getStringFromRepository(id)
        return value ?: super.getText(id, def)
    }

    private fun getStringFromRepository(id: Int): CharSequence? {
        val currentLocale = RestringLocale.currentLocale
        val supportedLocales = stringRepository.supportedLocales

        val resultLocale = if (supportedLocales.contains(currentLocale)) {
            currentLocale
        } else {
            supportedLocales.find { it.language == currentLocale.language }
        }

        resultLocale ?: return null

        return try {
            val stringKey = getResourceEntryName(id)
            return stringRepository.getString(resultLocale, stringKey)
        } catch (e: NotFoundException) {
            null
        }
    }

    private fun setLocale() {
        if (RestringLocale.isInitial) return

        val conf = res.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(RestringLocale.currentLocale)
        } else {
            conf.locale = RestringLocale.currentLocale
        }

        res.updateConfiguration(conf, res.displayMetrics)
    }
}
