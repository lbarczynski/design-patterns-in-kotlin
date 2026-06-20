package dev.bapps.creational

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class PrototypePatternTest {

    @Test
    fun `GIVEN vm template WHEN clone THEN should update only specified properties`() {
        // given
        val sffVm = VirtualMachinesTemplates.SFF

        // when
        val testVm = sffVm.clone(ram = 5000)

        // then
        assertEquals(sffVm.os, testVm.os)
        assertEquals(sffVm.arch, testVm.arch)
        assertEquals(sffVm.cpuCores, testVm.cpuCores)
        assertEquals(5000, testVm.ram)
        assertEquals(sffVm.hdd, testVm.hdd)
        assertContentEquals(sffVm.preInstalledPackages, testVm.preInstalledPackages)
    }

    @Test
    fun `GIVEN vm template WHEN clone THEN should do a deep copy`() {
        // given
        val vm = VirtualMachinesTemplates.GenericVM

        // when
        val testVm = vm.clone()

        // then
        assertContentEquals(vm.preInstalledPackages, testVm.preInstalledPackages)
        assertNotSame(vm.preInstalledPackages, testVm.preInstalledPackages)
    }
}
