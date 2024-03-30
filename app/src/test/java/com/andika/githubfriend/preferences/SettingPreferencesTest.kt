package com.andika.githubfriend.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class SettingPreferencesTest {

    private lateinit var settingPreferences: SettingPreferences
    private val dataStore = mock(DataStore::class.java) as DataStore<Preferences>
    private val themeKey = booleanPreferencesKey("theme_setting")

    @BeforeEach
    fun setup() {
        settingPreferences = SettingPreferences.getInstance(dataStore)
    }


    @Test
    fun `getThemeSetting returns false when no value is set`(): Unit = runTest {
        val preferences = mock(Preferences::class.java)
        `when`(preferences[themeKey]).thenReturn(null)
        `when`(dataStore.data).thenReturn(flow { emit(preferences) })

        val themeSetting = settingPreferences.getThemeSetting().first()

        assertFalse(themeSetting)
    }

    @Test
    fun `getThemeSetting returns true when value is set to true`(): Unit = runTest {
        val preferences = mock(Preferences::class.java)
        `when`(preferences[themeKey]).thenReturn(true)
        `when`(dataStore.data).thenReturn(flow { emit(preferences) })

        val themeSetting = settingPreferences.getThemeSetting().first()

        assertTrue(themeSetting)
    }

    @Test
    fun `getThemeSetting returns false when value is set to false`(): Unit = runTest {
        val preferences = mock(Preferences::class.java)
        `when`(preferences[themeKey]).thenReturn(false)
        `when`(dataStore.data).thenReturn(flow { emit(preferences) })

        val themeSetting = settingPreferences.getThemeSetting().first()

        assertFalse(themeSetting)
    }
}