package org.sazonpt;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(/*config*/)
        .get("/", ctx -> ctx.result("Test SazonPT API"))
        .start(7070);
    }
}
