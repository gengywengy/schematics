package dev.gengy.schematics

import dev.gengy.schematics.world.Block
import dev.gengy.schematics.world.BlockEntity
import dev.gengy.schematics.world.BlockPos
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import java.io.File
import java.io.FileInputStream

class Schematic(
    private val dataVersion: Int,
    val width: Short,
    val height: Short,
    val length: Short,
    val pasteOffset: BlockPos,
    val metadata: Metadata,
    val blocks: Blocks
) {
    companion object {
        fun read(file: File): Schematic {
            return read(BinaryTagIO.unlimitedReader().read(FileInputStream(file), BinaryTagIO.Compression.GZIP))
        }
        fun read(cbt: CompoundBinaryTag): Schematic {
            if (cbt.get("Schematic") != null) {
                return read(cbt.getCompound("Schematic"))
            }
            val version = getVersion(cbt)
            if (version == null) throw SchematicError.InvalidSchematicVersionException()
            return version.deserialize(cbt)
        }

        private fun getVersion(cbt: CompoundBinaryTag): SchematicVersion? {
            val version = cbt.getInt("Version")
            if (version != 3) {
                throw SchematicError.InvalidSchematicVersionException()
            }
            return SchematicVersion.V3SchematicVersion()
        }
    }

    data class Metadata(
        val name: String,
        val author: String,
        val date: Long,
        val requiredMods: List<String>
    )

    data class Blocks(
        val palette: Map<Int, String>,
        val blocks: Set<Block>,
        val blockEntities: Set<BlockEntity>
    )
}