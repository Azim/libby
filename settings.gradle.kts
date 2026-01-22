rootProject.name = "libby"

setOf(
    "bukkit",
    "bungee",
    "core",
    "fabric",
    "nukkit",
    "paper",
    "sponge",
    "standalone",
    "velocity",
    "hytale"
).forEach {
    subProject(it)
}

fun subProject(name: String) {
    include(":libby-$name")
    project(":libby-$name").projectDir = file(name)
}