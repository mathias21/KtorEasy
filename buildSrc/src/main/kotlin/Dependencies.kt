object Dependencies {

    object Versions {
        const val kotlinPluginVersion = "1.8.0"
        const val ktorPluginVersion = "2.2.3"
        const val shadowJarVersion = "5.0.0"

        const val ktorVersion = "2.2.3"
        const val h2Version = "2.1.214"
        const val koinVersion = "3.3.3"
        const val koinKtorVersion = "3.3.1"
        const val exposedVersion = "0.41.1"
        const val logbackVersion = "1.4.5"
        const val hikariVersion = "5.0.1"
        const val mysqlVersion = "8.0.32"
        const val bcryptVersion = "0.4"

        const val micrometerPrometeusVersion = "1.10.3"

        const val assertJVersion = "3.24.2"
        const val junit = "5.9.2"
        const val mockK = "1.13.4"
    }


    // Dependencies
    val ktorNetty = "io.ktor:ktor-server-netty:${Versions.ktorVersion}"
    val logback = "ch.qos.logback:logback-classic:${Versions.logbackVersion}"
    val ktorAuth = "io.ktor:ktor-server-auth:${Versions.ktorVersion}"
    val ktorLogging = "io.ktor:ktor-server-call-logging:${Versions.ktorVersion}"
    val ktorStatusPages = "io.ktor:ktor-server-status-pages:${Versions.ktorVersion}"
    val ktorContentNegotiation = "io.ktor:ktor-server-content-negotiation:${Versions.ktorVersion}"
    val ktorSerialization = "io.ktor:ktor-serialization-gson:${Versions.ktorVersion}"
    val ktorMetricsMicrometer = "io.ktor:ktor-server-metrics-micrometer:${Versions.ktorVersion}"
    val ktorJwt = "io.ktor:ktor-server-auth-jwt:${Versions.ktorVersion}"

    val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposedVersion}"
    val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposedVersion}"
    val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposedVersion}"
    val hikari = "com.zaxxer:HikariCP:${Versions.hikariVersion}"
    val h2 = "com.h2database:h2:${Versions.h2Version}"
    val mysqlConnector = "com.mysql:mysql-connector-j:${Versions.mysqlVersion}"

    val bcrypt = "org.mindrot:jbcrypt:${Versions.bcryptVersion}"

    // Koin for Kotlin
    val koin = "io.insert-koin:koin-ktor:${Versions.koinKtorVersion}"

    // Micrometer Prometeus
    val micrometerPrometeus = "io.micrometer:micrometer-registry-prometheus:${Versions.micrometerPrometeusVersion}"

    // Testing
    val koinTest = "io.insert-koin:koin-test:${Versions.koinVersion}"
    val ktorServerTest = "io.ktor:ktor-server-test-host:${Versions.ktorVersion}"
    val assertJ = "org.assertj:assertj-core:${Versions.assertJVersion}"
    val junit = "org.junit.jupiter:junit-jupiter:${Versions.junit}"
    val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
    val mockK = "io.mockk:mockk:${Versions.mockK}"
}