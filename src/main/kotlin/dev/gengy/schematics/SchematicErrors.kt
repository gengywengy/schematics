package dev.gengy.schematics

sealed class SchematicError() : Exception() {
    class InvalidSchematicException() : SchematicError()
    class InvalidSchematicVersionException() : SchematicError()
}