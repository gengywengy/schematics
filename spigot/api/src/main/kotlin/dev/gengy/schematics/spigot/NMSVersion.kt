package dev.gengy.schematics.spigot

import org.bukkit.World

interface NMSVersion {
    fun setBlock(bWorld: World, x: Int, y: Int, z: Int, block: String)
}