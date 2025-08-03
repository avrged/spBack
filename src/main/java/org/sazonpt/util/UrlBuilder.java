package org.sazonpt.util;

/**
 * Utilidad para generar URLs completas de archivos
 */
public class UrlBuilder {
    
    // Configurar la base URL del servidor
    private static final String BASE_URL = "http://localhost:7070";
    private static final String UPLOADS_PATH = "/uploads";
    
    /**
     * Genera URL completa para una imagen
     * @param nombreArchivo nombre del archivo (ej: "59b3e87a-7988-4638-b4a3-ff7602282b00.png")
     * @return URL completa (ej: "http://localhost:7070/uploads/images/59b3e87a-7988-4638-b4a3-ff7602282b00.png")
     */
    public static String buildImageUrl(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        return BASE_URL + UPLOADS_PATH + "/images/" + nombreArchivo;
    }
    
    /**
     * Genera URL completa para un documento
     * @param nombreArchivo nombre del archivo (ej: "75807c59-ba11-47eb-bd19-0514420b2aba.pdf")
     * @return URL completa (ej: "http://localhost:7070/uploads/documents/75807c59-ba11-47eb-bd19-0514420b2aba.pdf")
     */
    public static String buildDocumentUrl(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        return BASE_URL + UPLOADS_PATH + "/documents/" + nombreArchivo;
    }
    
    /**
     * Genera URL completa para un menú
     * @param nombreArchivo nombre del archivo (ej: "abc123-menu.pdf")
     * @return URL completa (ej: "http://localhost:7070/uploads/menus/abc123-menu.pdf")
     */
    public static String buildMenuUrl(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return null;
        }
        return BASE_URL + UPLOADS_PATH + "/menus/" + nombreArchivo;
    }
    
    /**
     * Genera URL genérica para cualquier tipo de archivo
     * @param tipoArchivo tipo de archivo ("images", "documents", "menus")
     * @param nombreArchivo nombre del archivo
     * @return URL completa
     */
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
    
    /**
     * Obtiene la URL base del servidor
     * @return URL base (ej: "http://localhost:7070")
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }
    
    /**
     * Obtiene la ruta de uploads
     * @return ruta de uploads (ej: "/uploads")
     */
    public static String getUploadsPath() {
        return UPLOADS_PATH;
    }
}
