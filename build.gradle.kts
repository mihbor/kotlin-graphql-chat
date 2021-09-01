import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

val kotlinVersion = "1.5.0"
val serializationVersion = "1.2.0"
val preKotlinVersion = "pre.156-kotlin-$kotlinVersion"
val ktorVersion = "1.6.0"

plugins {
    val kotlinVersion = "1.5.0"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version "1.4.0"
    id("org.springframework.boot") version "2.5.3"
    kotlin("plugin.spring") version kotlinVersion apply false
    application
}
apply(plugin = "io.spring.dependency-management")

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
        withJava()
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            runTask {
                val contentDir = File(projectDir, "src/jvmMain/resources").absolutePath
                devServer = KotlinWebpackConfig.DevServer(contentBase = listOf(contentDir), open=false)
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinVersion")
                implementation("com.expediagroup:graphql-kotlin-spring-server:4.2.0")
                implementation("org.springframework.boot:spring-boot-starter-validation:2.5.3")
                implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.3")
                implementation("com.auth0:java-jwt:3.18.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains:kotlin-react:17.0.2-$preKotlinVersion")
                implementation("org.jetbrains:kotlin-react-dom:17.0.2-$preKotlinVersion")
                implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-$preKotlinVersion")
                implementation("org.jetbrains:kotlin-styled:5.3.0-$preKotlinVersion")

                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")

                implementation(npm("react-apollo", "3.1.5"))
                implementation(npm("apollo-boost", "0.4.9"))
                implementation(npm("graphql", "15.5.1"))
                implementation(npm("jsonwebtoken", "8.5.1"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

application {
    mainClass.set("chat.ApplicationKt")
}

val isDevelopment = System.getenv().get("io.ktor.development") == "true"
val webpackTaskName = "jsBrowser${if(isDevelopment) "Development" else "Production"}Webpack"
val webpackTask = tasks.getByName<KotlinWebpack>(webpackTaskName).apply {
    outputFileName = "js.js"
}

tasks {
    getByName<Jar>("jvmJar") {
        dependsOn(webpackTask)
        from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) {
            into("static")
        }
        if (isDevelopment) {
            from(File(webpackTask.destinationDirectory, webpackTask.outputFileName + ".map")) {
                into("static")
            }
        }
    }

    getByName<JavaExec>("run") {
        dependsOn(getByName<Jar>("jvmJar"))
        classpath(getByName<Jar>("jvmJar"))
    }
// workaround for https://youtrack.jetbrains.com/issue/KT-46978 / https://youtrack.jetbrains.com/issue/KT-46165
// until 1.5.30 is released
    named<Copy>("jvmProcessResources") {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }
}
