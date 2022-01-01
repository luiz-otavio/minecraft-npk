# mc-npk
[![](https://jitpack.io/v/luiz-otavio/mc-npk.svg)](https://jitpack.io/#luiz-otavio/mc-npk)

Easy to use, fast and efficient library to make non-playable-characters (NPC) inside a minecraft server.

## Compatible with
- 1.8.1-SNAPSHOT (1_8_R1);
- 1.8.8-SNAPSHOT (1_8_R3);
- 1.12.2-SNAPSHOT (1_12_R1).

## Requirements
- Java 8;
- Gradle 7.1 or higher.

## Installation
Gradle DSL:
```groovy
repositories() {
    maven {
        name = "jitpack"
        url = 'https://jitpack.io'
    }
}   

dependencies() {
    implementation 'com.github.luiz-otavio.mc-npk:${MINECRAFT_VERSION}:${PROJECT_VERSION}'
}
```

Kotlin DSL:
```kotlin

repositories() {
    maven("https://jitpack.io")
}

dependencies() {
    implementation("com.github.luiz-otavio.mc-npk:${MINECRAFT_VERSION}:${PROJECT_VERSION}")
}
```

Maven:
```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <name>jitpack</name>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.luiz-otavio.mc-npk</groupId>
        <artifactId>${MINECRAFT_VERSION}</artifactId>
        <version>${PROJECT_VERSION}</version>
    </dependency>
</dependencies>
```


## How to use
```kotlin
val plugin = ... // Your plugin instance
val location = ... // Location of your NPC

val npkFramework = NPKFramework(plugin)

val npc = npkFramework.createNPC(
    "npc_name", // Name of your NPC
    location, // Location of your NPC 
    Skin(...) // Your skin
) {
    // Touching Handler DSL:
    
    println("${it.player.name} has touched me!")
}
```

## License
This project is licensed under the GNU General Public License v3.0.

## Contributing
Please feel free to open an issue or create a pull request.

