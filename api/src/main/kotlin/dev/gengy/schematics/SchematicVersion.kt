package dev.gengy.schematics

import dev.gengy.schematics.world.Block
import dev.gengy.schematics.world.BlockEntity
import dev.gengy.schematics.world.BlockPos
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import kotlin.experimental.and

sealed interface SchematicVersion {
    class V3SchematicVersion : SchematicVersion {
        override fun deserialize(cbt: CompoundBinaryTag): Schematic {
            val dataVersion = cbt.getInt("DataVersion")
            val metaData = parseMetaData(cbt.getCompound("Metadata"))
            val width = cbt.getShort("Width") // X-Axis
            val height = cbt.getShort("Height") // Y-Axis
            val length = cbt.getShort("Length") // Z-Axis
            val pasteOffset = if (cbt.get("Offset") != null) cbt.getBlockPos("Offset") else BlockPos(0, 0, 0)
            val blocks = parseBlocks(cbt.getCompound("Blocks"), width.toInt(), height.toInt(), length.toInt())
//            val entities = TODO("Parse entities")
//            val biomes = TODO("Parse biomes")
            return Schematic(
                dataVersion = dataVersion,
                metadata = metaData,
                width = width,
                height = height,
                length = length,
                pasteOffset = pasteOffset,
                blocks = blocks
            )
        }

        private fun parseMetaData(cbt: CompoundBinaryTag): Schematic.Metadata {
            return Schematic.Metadata(
                name = cbt.getString("Name"),
                author = cbt.getString("Author"),
                date = cbt.getLong("Date"),
                requiredMods = cbt.getStringList("RequiredMods"),
            )
        }
        private fun parseBlocks(cbt: CompoundBinaryTag, width: Int, height: Int, length: Int): Schematic.Blocks {
            val palette = parsePaletteData(cbt.getCompound("Palette"))
            val blockData = parseBlockData(cbt.getByteArray("Data"), palette, width, height, length)
            val blockEntities = parseBlockEntities(cbt.getList("BlockEntities"))

            return Schematic.Blocks(
                palette = palette,
                blocks = blockData,
                blockEntities = blockEntities,
            )
        }

        private fun parseBlockData(blockData: ByteArray, palette: Map<Int, String>, width: Int, height: Int, length: Int): Set<Block> {
            val blocks = mutableSetOf<Block>()

            var index = 0
            var i = 0
            var value = 0
            var varIntLength = 0
            while (i < blockData.size) {
                value = 0
                varIntLength = 0
                while (true) {
                    value = value or ((blockData[i] and 127).toInt()) shl (varIntLength++ * 7)
                    if (varIntLength > 5) {
                        throw RuntimeException("VarInt too big")
                    }

                    if ((blockData[i] and 128.toByte()).toInt() != 128) {
                        i++
                        break
                    }
                    i++
                }

                val state = palette[value]
                if (state == null) throw RuntimeException("State missing in palette")

                val y = index / (width * length);
                val z = (index % (width * length)) / width;
                val x = (index % (width * length)) % width;
                blocks.add(Block(
                    position = BlockPos(x, y, z),
                    blockState = state,
                ))
                index++
            }

            return blocks
        }

        private fun parsePaletteData(cbt: CompoundBinaryTag): Map<Int, String> {
            val palette = mutableMapOf<Int, String>()
            cbt.keySet().forEach {
                val value = cbt.getInt(it)
                palette[value] = it
            }
            return palette
        }

        private fun parseBlockEntities(cbt: ListBinaryTag): Set<BlockEntity> {
            val blockEntities = mutableSetOf<BlockEntity>()
            cbt.forEach {
                if (it !is CompoundBinaryTag) return@forEach
                val pos = it.getBlockPos("Pos")
                val id = it.getString("Id")
                val data = it.getCompound("Data")

                blockEntities.add(BlockEntity(
                    pos = BlockPos(pos.x, pos.y, pos.z),
                    id = id,
                    data = data
                ))
            }

            return blockEntities
        }
    }

    fun deserialize(cbt: CompoundBinaryTag): Schematic
}