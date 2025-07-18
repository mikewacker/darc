package io.github.mikewacker.darc.example;

/** Runs {@link ExampleApp}. */
public final class Main {

    /** Main method. */
    public static void main(String[] args) throws Exception {
        new ExampleApp().run("server");
    }

    private Main() {} // static class
}
