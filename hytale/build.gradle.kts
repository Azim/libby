repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    api(project(":libby-core"))

    compileOnly("com.hypixel.hytale:Server:+")
}
