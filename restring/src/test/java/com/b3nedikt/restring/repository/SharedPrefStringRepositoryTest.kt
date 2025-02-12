package com.b3nedikt.restring.repository

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SharedPrefStringRepositoryTest {

    @Before
    fun setUp() {
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(locale, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(strings, newRepository.getStrings(locale))
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(locale, strings)

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        for (i in 0 until stringCount) {
            assertEquals(newRepository.getString(locale, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        val stringRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        stringRepository.setStrings(locale, strings)
        stringRepository.setString(locale, "key5", "aNewValue")

        val newRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())
        assertEquals(newRepository.getString(locale, "key5"), "aNewValue")
    }

    private fun generateStrings(count: Int): Map<String, String> {
        val strings = LinkedHashMap<String, String>()
        for (i in 0 until count) {
            strings["key$i"] = "value$i"
        }
        return strings
    }
}