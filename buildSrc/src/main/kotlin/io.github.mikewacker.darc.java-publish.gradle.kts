plugins {
    `java-library`
    `maven-publish`
    signing
}

tasks.compileJava {
    options.release = 11
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("Werror", true)
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
}

group = "io.github.mikewacker.darc"
version = "0.1.1"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = "DARC"
                description = "Dagger And Request Context (for Dropwizard)"
                url = "https://github.com/mikewacker/darc"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit"
                    }
                }
                developers {
                    developer {
                        id = "mikewacker"
                        name = "Mike Wacker"
                        email = "11431865+mikewacker@users.noreply.github.com"
                        url = "https://github.com/mikewacker"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/mikewacker/darc.git"
                    developerConnection = "scm:git:ssh://github.com:mikewacker/darc.git"
                    url = "https://github.com/mikewacker/darc/tree/main"
                }
            }
        }
    }
    repositories {
        maven {
            name = "ossrhStagingApi"
            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            credentials {
                val portalUsername: String? by project
                val portalPassword: String? by project
                username = portalUsername
                password = portalPassword
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
