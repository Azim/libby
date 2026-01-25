# Libby (Azim)

<details><summary>Changes in this fork</summary>

### 2.0.1
* Adds hytale support

### 2.0.0
* indev version from upstream
* switched to using gradle
* some other changes i found no changelog for :)

</details>

# Libby (AlessioDP)

## [Changes in this fork](./CHANGELOG.md)

# Libby

A runtime dependency management library for plugins running in Java-based Minecraft
server platforms.

Libraries can be downloaded from Maven repositories (or direct URLs) into a plugin's data
folder, relocated and then loaded into the plugin's classpath at runtime.

### Why use runtime dependency management?

Due to file size constraints on plugin hosting services like SpigotMC, some plugins with
bundled dependencies become too large to be uploaded.

Using runtime dependency management, dependencies are downloaded and cached by the server
and don't need to be bundled with the plugin, which significantly reduces the size of the
plugin jar.

A smaller plugin jar also means shorter download times and less network strain for authors
who self-host their plugins on servers with limited bandwidth.

### Usage

<details><summary>Maven</summary>

Firstly, add the maven artifact to your `pom.xml`

```xml
<!-- HytaleModding Snapshots Repository -->
<repository>
  <id>hytalemodding-snapshots</id>
  <name>HytaleModding</name>
  <url>https://maven.hytalemodding.dev/snapshots</url>
</repository>

<dependency>
    <groupId>com.alessiodp.libby</groupId>
    <artifactId>libby-bukkit</artifactId> <!-- Replace bukkit if you're using another platform -->
    <version>2.0.1-SNAPSHOT</version>
</dependency>
```

Remember to **always** relocate Libby to avoid conflicts

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.6.1</version>
    <executions>
        <execution>
            <id>shade</id>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>com.alessiodp.libby</pattern>
                        <shadedPattern>YOURPACKAGE.libs.com.alessiodp.libby</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </execution>
    </executions>
</plugin>
```
---
</details>


<details><summary>Gradle</summary>

Firstly, add the maven artifact to your `build.gradle.kts`

```kts
repositories {
    maven {
        name = "hytalemodding-snapshots"
        url = uri("https://maven.hytalemodding.dev/snapshots")
    }
}

dependencies {
	//Replace bukkit if you're using another platform
    implementation("com.alessiodp.libby:libby-bukkit:2.0.1-SNAPSHOT")
}
```

Remember to **always** relocate Libby to avoid conflicts

```kts
tasks.shadowJar {
    relocate(
        "com.alessiodp.libby",
        "YOURPACKAGE.libs.com.alessiodp.libby"
    )
}
```
---
</details>


Then, create a new LibraryManager instance

```java
// Create a library manager for a Bukkit/Spigot plugin
BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(plugin);

// Create a library manager for a Bungee plugin
BungeeLibraryManager bungeeLibraryManager = new BungeeLibraryManager(plugin);

// Also Nukkit, Sponge, and Velocity are supported
```

Create a Library instance with the library builder

```java
Library lib = Library.builder()
    .groupId("your{}dependency{}groupId") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
    .artifactId("artifactId")
    .version("version")
     // The following are optional

     // Sets an id for the library
    .id("my-lib")
     // Relocation is applied to the downloaded jar before loading it
    .relocate("package{}to{}relocate", "the{}relocated{}package") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
     // The library is loaded into an IsolatedClassLoader, which is in common between every library with the same id
    .isolatedLoad(true)
    .classifier("customClassifier")
    .checksum("Base64-encoded SHA-256 checksum")
    .build();
```

Finally, add Maven Central (or other repositories) to the library manager and download your library. To do this,
you can use the `LibraryManager#loadLibrary(Library libraryToLoad)` method, which automatically downloads and then loads the provided library.

```java
libraryManager.addMavenCentral();
libraryManager.loadLibrary(lib);
```

<details><summary>Complete code</summary>

```java
BukkitLibraryManager libraryManager = new BukkitLibraryManager(plugin);

Library lib = Library.builder()
    .groupId("your{}dependency{}groupId") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
    .artifactId("artifactId")
    .version("version")
     // The following are optional

     // Sets an id for the library
    .id("my-lib")
     // Relocation is applied to the downloaded jar before loading it
    .relocate("package{}to{}relocate", "the{}relocated{}package") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
     // The library is loaded into an IsolatedClassLoader, which is in common between every library with the same id
    .isolatedLoad(true)
    .classifier("customClassifier")
    .checksum("Base64-encoded SHA-256 checksum")
    .build();

libraryManager.addMavenCentral();
libraryManager.loadLibrary(lib);
```

</details>

## Credits

Special thanks to:

* [Luck](https://github.com/lucko) for [LuckPerms](https://github.com/lucko/LuckPerms)
  and its dependency management system which was the original inspiration for this project
  and another thanks for [jar-relocator](https://github.com/lucko/jar-relocator) which is
  used by Libby to perform jar relocations.
* [Glare](https://github.com/darbyjack) for convincing me (Byteflux) that I should publish this
  library instead of letting it sit around collecting dust :)
