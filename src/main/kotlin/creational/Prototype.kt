package dev.bapps.creational

/**
 * Please note:
 *      Kotlin Data Classes generate a "copy" method by default.
 *      This implementation was intentional to make the code clearer
 *      for the reader. For more complex objects, you will need to
 *      implement this pattern anyway, as Kotlin's "copy" method
 *      does not perform a deep copy.
 */
class VirtualMachineConfiguration(
    val os: String,
    val arch: String,
    val cpuCores: Int,
    val ram: Int,
    val hdd: Int,
    val preInstalledPackages: List<String>
) {
    fun clone(
        os: String = this.os,
        arch: String = this.arch,
        cpuCores: Int = this.cpuCores,
        ram: Int = this.ram,
        hdd: Int = this.hdd,
        preInstalledPackages: List<String> = this.preInstalledPackages.toList()
    ): VirtualMachineConfiguration {
        return VirtualMachineConfiguration(
            os = os,
            arch = arch,
            cpuCores = cpuCores,
            ram = ram,
            hdd = hdd,
            preInstalledPackages = preInstalledPackages
        )
    }
}

object VirtualMachinesTemplates {
    val GenericVM = VirtualMachineConfiguration(
        os = "Debian 12",
        arch = "amd64",
        cpuCores = 2,
        ram = 4096,
        hdd = 256,
        preInstalledPackages = listOf("htop", "cockpit", "micro")
    )

    val MiniPC = GenericVM.clone(
        cpuCores = 4,
        ram = 8192,
        hdd = 1024
    )

    val SFF = GenericVM.clone(
        cpuCores = 16,
        ram = 65536,
        hdd = 2048,
        preInstalledPackages = listOf("htop", "cockpit", "micro", "docker")
    )

    val RaspberryPi4 = VirtualMachineConfiguration(
        os = "Raspbian",
        arch = "armv8",
        cpuCores = 4,
        ram = 4096,
        hdd = 128,
        preInstalledPackages = emptyList()
    )
}