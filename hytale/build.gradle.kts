repositories {
    maven("https://maven.hytale.com/release")
    maven("https://maven.hytale.com/pre-release")
}

dependencies {
    api(project(":libby-core"))

    compileOnly("com.hypixel.hytale:Server:+")
}
