package dev.gengy.schematics.spigot.v1_21_R1

import dev.gengy.schematics.spigot.NMSVersion
import net.minecraft.core.BlockPosition
import org.bukkit.World
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld

class NMSHandler : NMSVersion {
    override fun setBlock(bWorld: World, x: Int, y: Int, z: Int, block: String) {
        val world = (bWorld as CraftWorld).handle
        val chunk = world.getChunkIfLoaded(x shr 4, z shr 4)
        val blockPos = BlockPosition(x, y, z)
        TODO("Not yet implemented")
    }
}