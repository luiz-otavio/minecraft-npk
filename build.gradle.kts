import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.20"

  id("maven-publish")

  id("java-library")
}

subprojects {
  for (pluginName in arrayOf("kotlin", "java-library", "maven-publish")) {
    plugins.apply(pluginName)
  }

  kotlin {
    jvmToolchain(8)
  }

  java {
    withJavadocJar()
    withSourcesJar()
  }

  tasks.withType(KotlinCompile::class.java) {
    kotlinOptions {
      jvmTarget = "1.8"
      apiVersion = "1.8"
    }

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_1_8)
      apiVersion.set(KotlinVersion.KOTLIN_1_9)
    }
  }

  dependencies {
    compileOnly(rootProject.libs.lombok)
    annotationProcessor(rootProject.libs.lombok)

    testCompileOnly(rootProject.libs.lombok)
    testImplementation(rootProject.libs.lombok)

    api(rootProject.libs.jetbrains.annotations)
  }

  configurations {
    runtimeClasspath.configure { forEach { if (it.isDirectory) it else zipTree(it) } }
  }

  publishing {
    publications {
      create("Jitpack", MavenPublication::class.java) {
        groupId = "io.github.luizotavio.npk"
        artifactId = project.name
        version = "2.0.0-SNAPSHOT"

        from(components["java"])

        versionMapping {
          usage("kotlin") {
            fromResolutionOf("kotlin")
          }

          usage("java-api") {
            fromResolutionResult()
          }
        }

        pom {
          developers {
            developer {
              name = "Wizard"
              url = "https://github.com/luiz-otavio/minecraft-npk"
            }
          }

          licenses {
            license {
              name = "GNU GENERAL PUBLIC LICENSE"
              url = "https://www.gnu.org/licenses/gpl-3.0.html"
            }
          }
        }
      }
    }
  }
}
