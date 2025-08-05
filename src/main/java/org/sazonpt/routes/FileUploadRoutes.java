package org.sazonpt.routes;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.sazonpt.controller.FileUploadController;

public class FileUploadRoutes {
    private final FileUploadController fileUploadController;
    
    public FileUploadRoutes(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }
    
    public void register(Javalin app) {

        app.post("/api/upload", fileUploadController::uploadFile);
        app.post("/api/upload/multiple", fileUploadController::uploadMultipleFiles);

        app.post("/api/upload/image", ctx -> {
            uploadWithPredefinedType(ctx, "image");
        });
        
        app.post("/api/upload/document", ctx -> {
            uploadWithPredefinedType(ctx, "document");
        });
        
        app.post("/api/upload/menu", ctx -> {
            uploadWithPredefinedType(ctx, "menu");
        });
    }
    
    private void uploadWithPredefinedType(Context ctx, String type) {
        var uploadedFile = ctx.uploadedFile("file");
        if (uploadedFile == null) {
            ctx.status(400).json(java.util.Map.of(
                "success", false, 
                "message", "No se recibió ningún archivo"
            ));
            return;
        }

        ctx.attribute("predefined-type", type);
        fileUploadController.uploadFile(ctx);
    }
}
