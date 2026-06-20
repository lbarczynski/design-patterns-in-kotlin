package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FactoryPatternTest {

    @Test
    fun `GIVEN ZigBee device id WHEN create device THEN should return proper device`() {
        val testDeviceId = "test-zigbee-device"
        val device = ZigbeeDeviceFactory().createDevice(testDeviceId)

        assertEquals(testDeviceId, device.id)
        assertTrue(device.connect())
    }

    @Test
    fun `GIVEN WiFi device id WHEN create device THEN should return proper device with valid IP`() {
        val testDeviceId = "test-wifi-device"
        val device = WiFiDeviceFactory().createDevice(testDeviceId)

        assertEquals(testDeviceId, device.id)
        assertTrue(device.connect())
    }

    @Test
    fun `GIVEN BLE device id WHEN create device THEN should return proper device with valid MAC`() {
        val testDeviceId = "test-ble-device"
        val device = BleDeviceFactory().createDevice(testDeviceId)

        assertEquals(testDeviceId, device.id)
        assertTrue(device.connect())
    }

    @Test
    fun `GIVEN SmartHub WHEN provision devices THEN should store them and connect all`() {
        val hub = SmartHub(WiFiDeviceFactory())

        hub.provisionDevice("wifi-1")
        hub.provisionDevice("wifi-2")

        assertEquals(2, hub.getDevices().size)
        assertEquals(2, hub.connectAll())
    }

    @Test
    fun `GIVEN SmartHomeCoordinator WHEN provision various devices THEN should aggregate, sort and connect correctly`() {
        val coordinator = SmartHomeCoordinator()

        coordinator.provisionWiFiDevice("b-wifi")
        coordinator.provisionZigbeeDevice("c-zigbee")
        coordinator.provisionBleDevice("a-ble")

        val allDevices = coordinator.allDevices()

        assertEquals(3, allDevices.size)
        assertEquals("a-ble", allDevices[0].id)
        assertEquals("b-wifi", allDevices[1].id)
        assertEquals("c-zigbee", allDevices[2].id)

        assertEquals(3, coordinator.connectAll())
    }
}
