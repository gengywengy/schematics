package dev.gengy.schematics

data class PasteSettings(
    var type: PasteType = PasteType.NMS
) {
    enum class PasteType {
        BUKKIT,
        NMS
    }
}
