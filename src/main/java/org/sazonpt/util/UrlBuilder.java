package org.sazonpt.util;

public class UrlBuilder {
    private static final String BASE_URL = "http://52.23.26.163:7070";
    private static final String UPLOADS_PATH = "/uploads";

    public static String buildImageUrl(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        return BASE_URL + UPLOADS_PATH + "/images/" + nombreArchivo;
    }

    public static String buildDocumentUrl(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        return BASE_URL + UPLOADS_PATH + "/documents/" + nombreArchivo;
    }

    public static String buildMenuUrl(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        return BASE_URL + UPLOADS_PATH + "/menus/" + nombreArchivo;
    }

    public static String buildFileUrl(String tipoArchivo, String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        
        switch (tipoArchivo.toLowerCase()) {
            case "images":
                return buildImageUrl(nombreArchivo);
            case "documents":
                return buildDocumentUrl(nombreArchivo);
            case "menus":
                return buildMenuUrl(nombreArchivo);
            default:
                return BASE_URL + UPLOADS_PATH + "/" + tipoArchivo + "/" + nombreArchivo;
        }
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getUploadsPath() {
        return UPLOADS_PATH;
    }
}
