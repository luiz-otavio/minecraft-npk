# mc-npk
Easy to use, fast and efficient library to make non-playable-characters (NPC) inside a minecraft server.

## Compatible with
- 1.8.8-SNAPSHOT (1_8_R3 - 47)

## Requirements
- Java 8
- Gradle 7.+

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

## License
This project is licensed under the GNU General Public License v3.0.

## Contributing
Please feel free to open an issue or create a pull request.

