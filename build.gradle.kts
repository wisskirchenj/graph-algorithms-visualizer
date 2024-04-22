plugins {
    application
    id("com.github.ben-manes.versions") version("0.47.0")
    id("org.openrewrite.rewrite") version("6.1.22")
}

group = "de.cofinpro"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("de.cofinpro.visualizer.ApplicationRunner")
}

repositories {
    mavenCentral()
}
rewrite {
    activeRecipe("de.cofinpro.UpgradeDependencyVersionAll") // not working with Kotlin-DSL :-(
    //activeRecipe("org.openrewrite.java.testing.junit5.JUnit5BestPractices")
}

dependencies {
    rewrite(platform("org.openrewrite.recipe:rewrite-recipe-bom:2.1.0"))
    rewrite("org.openrewrite.recipe:rewrite-java-dependencies")
    rewrite("org.openrewrite.recipe:rewrite-testing-frameworks")

    implementation("org.apache.logging.log4j:log4j-api:3.0.0-alpha1")
    implementation("org.apache.logging.log4j:log4j-core:3.0.0-alpha1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:3.0.0-alpha1")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.4.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")

    testCompileOnly("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}