package dev.bapps.creational

class VirtualMachineConfiguration(
    val os: String,
    val arch: String,
    val cpuCores: Int,
    val ram: Int,
    val disk: Hdd,
    val preInstalledPackages: MutableList<String>
) {
    fun clone(
        os: String = this.os,
        arch: String = this.arch,
        cpuCores: Int = this.cpuCores,
        ram: Int = this.ram,
        disk: Hdd = this.disk.copy(), // deep copy
        preInstalledPackages: MutableList<String> = this.preInstalledPackages.toMutableList() // deep copy
    ): VirtualMachineConfiguration {
        return VirtualMachineConfiguration(
            os = os,
            arch = arch,
            cpuCores = cpuCores,
            ram = ram,
            disk = disk,
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
        disk = Hdd(256, "SSD"),
        preInstalledPackages = mutableListOf("htop", "cockpit", "micro")
    )

    val MiniPC = GenericVM.clone(
        cpuCores = 4,
        ram = 8192,
        disk = Hdd(1024, "NVMe")
    )

    val SFF = GenericVM.clone(
        cpuCores = 16,
        ram = 65536,
        disk = Hdd(2048, "NVMe"),
        preInstalledPackages = mutableListOf("htop", "cockpit", "micro", "docker")
    )

    val RaspberryPi4 = VirtualMachineConfiguration(
        os = "Raspbian",
        arch = "armv8",
        cpuCores = 4,
        ram = 4096,
        disk = Hdd(128, "SD Card"),
        preInstalledPackages = mutableListOf()
    )
}

data class Hdd(var hddSizeMb: Int, var type: String)
