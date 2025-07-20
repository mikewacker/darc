plugins {
    id("buildlogic.java-publish")
    id("buildlogic.java-conventions")
}

dependencies {
    annotationProcessor(libs.dagger.compiler)

    api(libs.jaxRs.api)
    implementation(libs.dagger.dagger)
    implementation(libs.dropwizard.core)
    implementation(libs.jakartaInject.api)

    testAnnotationProcessor(libs.dagger.compiler)

    testImplementation(libs.dropwizard.testing)
    testImplementation(libs.okhttp.okhttp)
}
