plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/com.diogonunes/JColor
    implementation("com.diogonunes:JColor:5.5.1")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
    // https://mvnrepository.com/artifact/jakarta.websocket/jakarta.websocket-api
    compileOnly("jakarta.websocket:jakarta.websocket-api:2.2.0-M1")
    // https://mvnrepository.com/artifact/jakarta.websocket/jakarta.websocket-client-api
    implementation("jakarta.websocket:jakarta.websocket-client-api:2.2.0-M1")
    // https://mvnrepository.com/artifact/org.eclipse.jetty.websocket/javax-websocket-server-impl
    implementation("org.eclipse.jetty.websocket:javax-websocket-server-impl:9.4.54.v20240208")
    // https://mvnrepository.com/artifact/org.glassfish.tyrus.bundles/tyrus-standalone-client
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:2.1.5")
    implementation("org.glassfish.tyrus:tyrus-server:1.17")
    // https://mvnrepository.com/artifact/org.glassfish.tyrus/tyrus-container-grizzly-server
    implementation("org.glassfish.tyrus:tyrus-container-grizzly-server:2.1.5")

}
