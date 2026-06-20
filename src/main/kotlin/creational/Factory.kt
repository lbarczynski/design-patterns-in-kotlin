package dev.bapps.creational

interface SmartDevice {
    val id: String
    fun connect(): Boolean
}

class ZigbeeDevice(override val id: String, private val nodeId: Int) : SmartDevice {
    override fun connect(): Boolean {
        println("Pairing Zigbee node #$nodeId with coordinator...")
        return nodeId in 1..255
    }
}

class WiFiDevice(override val id: String, private val ipAddress: String) : SmartDevice {
    override fun connect(): Boolean {
        println("Connecting Wi-Fi device $id via $ipAddress...")
        return ipAddress.startsWith("192.168.")
    }
}

class BleDevice(override val id: String, private val macAddress: String) : SmartDevice {
    override fun connect(): Boolean {
        println("Scanning for BLE device $id ($macAddress)...")
        return macAddress.matches(Regex("([0-9A-F]{2}:){5}[0-9A-F]{2}"))
    }
}

interface DeviceFactory<out TDevice : SmartDevice> {
    fun createDevice(id: String): TDevice
}

class ZigbeeDeviceFactory : DeviceFactory<ZigbeeDevice> {
    private var nextNodeId = 1

    override fun createDevice(id: String): ZigbeeDevice =
        ZigbeeDevice(id = id, nodeId = nextNodeId++)
}

class WiFiDeviceFactory(private val subnet: String = "192.168.1") : DeviceFactory<WiFiDevice> {
    private var nextHost = 10

    override fun createDevice(id: String): WiFiDevice =
        WiFiDevice(id = id, ipAddress = "$subnet.${nextHost++}")
}

class BleDeviceFactory : DeviceFactory<BleDevice> {
    private var deviceId = 0

    override fun createDevice(id: String): BleDevice =
        BleDevice(id = id, macAddress = generateMacAddress())

    private fun generateMacAddress(): String =
        (1..6).joinToString(":") { "%02X".format(deviceId++ % 255) }
}

class SmartHub<out TDevice : SmartDevice>(private val factory: DeviceFactory<TDevice>) {
    private val devices = mutableListOf<TDevice>()

    fun getDevices(): List<TDevice> = devices.toList()

    fun provisionDevice(id: String): TDevice {
        return factory.createDevice(id)
            .also { device -> devices += device }
    }

    fun connectAll(): Int = devices.count { it.connect() }
}

class SmartHomeCoordinator(
    private val zigbeeSmartHub: SmartHub<ZigbeeDevice> = SmartHub(ZigbeeDeviceFactory()),
    private val wifiSmartHub: SmartHub<WiFiDevice> = SmartHub(WiFiDeviceFactory()),
    private val bleDevice: SmartHub<BleDevice> = SmartHub(BleDeviceFactory())
) {
    fun allDevices(): List<SmartDevice> =
        (zigbeeSmartHub.getDevices() + wifiSmartHub.getDevices() + bleDevice.getDevices())
            .sortedBy { device -> device.id }

    fun provisionZigbeeDevice(id: String): ZigbeeDevice = zigbeeSmartHub.provisionDevice(id)
    fun provisionWiFiDevice(id: String): WiFiDevice = wifiSmartHub.provisionDevice(id)
    fun provisionBleDevice(id: String): BleDevice = bleDevice.provisionDevice(id)

    fun connectAll(): Int {
        return listOf(
            zigbeeSmartHub.connectAll(),
            wifiSmartHub.connectAll(),
            bleDevice.connectAll()
        ).sum()
    }
}
