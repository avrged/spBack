package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7070);

        app.get("/", ctx -> ctx.result("SazonPT API - Sistema de Catálogo de Restaurantes"));
        
    }
}