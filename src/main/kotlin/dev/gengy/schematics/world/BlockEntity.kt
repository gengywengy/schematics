package dev.gengy.schematics.world

import net.kyori.adventure.nbt.CompoundBinaryTag

data class BlockEntity(
    val pos: BlockPos,
    val id: String,
    val data: CompoundBinaryTag
)
