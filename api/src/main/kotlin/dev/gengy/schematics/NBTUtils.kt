package dev.gengy.schematics

import dev.gengy.schematics.world.BlockPos
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag

fun CompoundBinaryTag.getBlockPos(key: String): BlockPos {
    val xyz = getIntArray(key)
    return BlockPos(xyz[0], xyz[1], xyz[2])
}

fun CompoundBinaryTag.getStringList(key: String): List<String> {
    val list = getList(key)
    val final = mutableListOf<String>()
    list.forEach {
        if (it.type() != BinaryTagTypes.STRING) return@forEach
        final.add((it as StringBinaryTag).value())
    }

    return final
}