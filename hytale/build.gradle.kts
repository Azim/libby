repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    api(project(":libby-core"))

    compileOnly("com.hypixel.hytale:Server:2026.01.22-6f8bdbdc4")
}
