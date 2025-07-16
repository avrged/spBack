package org.sazonpt;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.sazonpt.di.AppModule;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7070);

        app.get("/", ctx -> ctx.result("API - Catalogo de Restaurantes"));
        
        AppModule.initAdmin().registerRoutes(app);
        AppModule.initUser().registerRoutes(app);
        AppModule.initRestaurantero().registerRoutes(app);
    }
}