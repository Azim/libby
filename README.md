# Libby (fork by Azim)

A runtime dependency management library for plugins running in Java-based Minecraft
server platforms.

Libraries can be downloaded from Maven repositories (or direct URLs) into a plugin's data
folder, relocated and then loaded into the plugin's classpath at runtime.

### About this fork

Roses are red,<br>
Violets are blue,<br>
[Fatjars are bad](https://fatjar.net/),<br>
So i made this for you.

This fork is meant to be used in pair with [AzimDP](https://github.com/Azim/AzimDP) maven plugin (dont worry i am already working on gradle version too)<br>
Instead of manually adding all your transitive dependencies one-by-one, you can now add AzimDP plugin instead and let it generate the list of dependencies to download for you!<br>

### How to set it up

Firstly, you need to add this library as a dependency to your `pom.xml`.<br>
To do that, you need to add jitpack to your list of repositories

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
And then add this library to your dependencies (replace bukkit with one of the other artifacts if you are using something else)

```xml
<dependency>
  <groupId>com.github.Azim.libby</groupId>
  <artifactId>libby-bukkit</artifactId>
  <version>-SNAPSHOT</version>
</dependency>
```

Next, add the AzimDP plugin to your `pom.xml`.<br>
Adjust [the example](https://github.com/Azim/AzimDP/blob/master/README.md#azimdp) to your needs by specifying the libraries you want to exclude (for example spigot)<br>
Make sure to exclude libby too, since this one you will need to shade using maven `shade` plugin.
<details><summary>Resulting pom.xml may look simular to this</summary>

```xml
<plugins>
  <plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
      <source>1.8</source>
      <target>1.8</target>
    </configuration>
  </plugin>
  <plugin>
    <groupId>com.github.Azim</groupId>
    <artifactId>azimdp-maven-plugin</artifactId>
    <version>1.0.1</version>
    <configuration>
      <ignoreVersions>true</ignoreVersions>
      <beautify>true</beautify>
      <path>AzimDP.json</path>
      <excludes>
        <exclude>org.spigotmc:spigot-api:whateva</exclude>
        <exclude>com.github.Azim.libby:libby-bukkit:whateva</exclude>
      </excludes>
    </configuration>
    <executions>
      <execution>
        <goals>
          <goal>generate-dependency-list</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
      <dependencyReducedPomLocation>${project.build.directory}/dependency-reduced-pom.xml</dependencyReducedPomLocation>
    </configuration>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>shade</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
</plugins>
```
</details>

**You are almost done!**<br>
All that is left now, is to load (download) our dependencies on startup:

```java
BukkitLibraryManager manager = new BukkitLibraryManager(this); //depends on the server core you are using
manager.addMavenCentral(); //there are also methods for other repositories
manager.fromGeneratedResource(this.getResource("AzimDP.json")).forEach(library->{
  try {
    manager.loadLibrary(library);
  }catch(RuntimeException e) { // in case some of the libraries cant be found or dont have .jar file or etc
    getLogger().info("Skipping download of\""+library+"\", it either doesnt exist or has no .jar file");
  }
});
```


### Why use runtime dependency management?

Due to file size constraints on plugin hosting services like SpigotMC, some plugins with
bundled dependencies become too large to be uploaded.

Using runtime dependency management, dependencies are downloaded and cached by the server
and don't need to be bundled with the plugin, which significantly reduces the size of the
plugin jar.

A smaller plugin jar also means shorter download times and less network strain for authors
who self-host their plugins on servers with limited bandwidth.

## Credits

Special thanks to:

* [Byteflux](https://github.com/Byteflux) for making that library in the first place
* [Vampire](https://github.com/Vampire) for moral support
* [Luck](https://github.com/lucko) for [LuckPerms](https://github.com/lucko/LuckPerms)
  and its dependency management system which was the original inspiration for this project
  and another thanks for [jar-relocator](https://github.com/lucko/jar-relocator) which is
  used by Libby to perform jar relocations.
* [Glare](https://github.com/darbyjack) for convincing Byteflux that he/she should publish this
  library instead of letting it sit around collecting dust :)
