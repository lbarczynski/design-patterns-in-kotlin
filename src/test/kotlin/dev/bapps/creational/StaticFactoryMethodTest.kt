package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StaticFactoryMethodTest {

    @Test
    fun `GIVEN device with modem WHEN create repository THEN should initialize with CellularStateObserver`() {
        // given
        val deviceInfo = object : DeviceInfo {
            override val hasModem = true
            override val isWifiConnected = false
        }

        // when
        val repository = Repository.create(deviceInfo)

        // then
        assertNotNull(repository)
        assertIs<SqlLiteDataSource>(repository.localDataSource)
        assertIs<RestApiDataSource>(repository.remoteDataSource)
        assertIs<CellularStateObserver>(repository.networkStateObserver)
    }

    @Test
    fun `GIVEN device with wifi only WHEN create repository THEN should initialize with WifiStateObserver`() {
        // given
        val deviceInfo = object : DeviceInfo {
            override val hasModem = false
            override val isWifiConnected = true
        }

        // when
        val repository = Repository.create(deviceInfo)

        // then
        assertNotNull(repository)
        assertIs<SqlLiteDataSource>(repository.localDataSource)
        assertIs<RestApiDataSource>(repository.remoteDataSource)
        assertIs<WifiStateObserver>(repository.networkStateObserver)
    }

    @Test
    fun `GIVEN device with no connectivity WHEN create repository THEN should throw IllegalStateException`() {
        // given
        val deviceInfo = object : DeviceInfo {
            override val hasModem = false
            override val isWifiConnected = false
        }

        // when & then
        val exception = assertFailsWith<IllegalStateException> {
            Repository.create(deviceInfo)
        }
    }
}
