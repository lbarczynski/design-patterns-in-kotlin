package dev.bapps.creational

/**
 * Static factory method
 *
 * Source: Effective Java (Joshua Bloch) 2008
 * https://www.google.com/books/edition/Effective_Java/ka2VUBqHiWkC?hl=en&gbpv=1&pg=PA5&printsec=frontcover&dq=%22consider%20static%20factory%20methods%20instead%20of%20constructors%22
 */
interface RemoteDataSource
class RestApiDataSource : RemoteDataSource

interface LocalDataSource
class SqlLiteDataSource : LocalDataSource

interface NetworkStateObserver
class WifiStateObserver : NetworkStateObserver
class CellularStateObserver : NetworkStateObserver

interface DeviceInfo {
    val hasModem: Boolean
    val isWifiConnected: Boolean
}

class Repository(
    internal val localDataSource: LocalDataSource,
    internal val remoteDataSource: RemoteDataSource,
    internal val networkStateObserver: NetworkStateObserver
) {
    companion object {
        fun create(deviceInfo: DeviceInfo): Repository {
            return Repository(
                localDataSource = SqlLiteDataSource(),
                remoteDataSource = RestApiDataSource(),
                networkStateObserver = when {
                    deviceInfo.hasModem -> CellularStateObserver()
                    deviceInfo.isWifiConnected -> WifiStateObserver()
                    else -> throw IllegalStateException("WiFi or cellar modem is required!")
                }
            )
        }
    }
}