plugins {
    application
    id("io.github.mikewacker.darc.java-conventions")
}

dependencies {
    annotationProcessor(libs.dagger.compiler)

    implementation(project(":darc"))
    implementation(libs.dagger.dagger)
    implementation(libs.dropwizard.core)
    implementation(libs.jakartaInject.api)
    implementation(libs.jaxRs.api)
}

application {
    mainClass = "io.github.mikewacker.darc.example.Main"
}
