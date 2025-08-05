package org.sazonpt.controller;

import io.javalin.http.Context;
import org.sazonpt.model.Menu;
import org.sazonpt.service.MenuService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MenuController {
    
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    public void obtenerTodosLosMenus(Context ctx) {
        try {
            List<Menu> menus = menuService.obtenerTodosLosMenus();
            ctx.json(Map.of(
                "success", true,
                "data", menus,
                "message", "Menús obtenidos correctamente"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los menús: " + e.getMessage()
            ));
        }
    }

    public void obtenerMenuPorId(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Optional<Menu> menuOpt = menuService.obtenerMenuPorId(idMenu, idRestaurante, idSolicitud, idRestaurantero);
            
            if (menuOpt.isPresent()) {
                ctx.json(Map.of(
                    "success", true,
                    "data", menuOpt.get(),
                    "message", "Menú encontrado"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Menú no encontrado"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener el menú: " + e.getMessage()
            ));
        }
    }

    public void obtenerMenusPorRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            List<Menu> menus = menuService.obtenerMenusPorRestaurante(idRestaurante, idSolicitud, idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", menus,
                "message", "Menús del restaurante obtenidos correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los menús del restaurante: " + e.getMessage()
            ));
        }
    }

    public void obtenerMenusPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            List<Menu> menus = menuService.obtenerMenusPorRestaurantero(idRestaurantero);
            
            ctx.json(Map.of(
                "success", true,
                "data", menus,
                "message", "Menús del restaurantero obtenidos correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los menús del restaurantero: " + e.getMessage()
            ));
        }
    }

    public void obtenerMenusPorEstado(Context ctx) {
        try {
            String estado = ctx.pathParam("estado");

            List<Menu> menus = menuService.obtenerMenusPorEstado(estado);
            
            ctx.json(Map.of(
                "success", true,
                "data", menus,
                "message", "Menús con estado '" + estado + "' obtenidos correctamente"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los menús por estado: " + e.getMessage()
            ));
        }
    }

    public void obtenerMenuActivoPorRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            Optional<Menu> menuOpt = menuService.obtenerMenuActivoPorRestaurante(idRestaurante, idSolicitud, idRestaurantero);
            
            if (menuOpt.isPresent()) {
                ctx.json(Map.of(
                    "success", true,
                    "data", menuOpt.get(),
                    "message", "Menú activo encontrado"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se encontró menú activo para este restaurante"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener el menú activo: " + e.getMessage()
            ));
        }
    }

    public void crearMenu(Context ctx) {
        try {
            Menu menu = ctx.bodyAsClass(Menu.class);
            Menu menuCreado = menuService.crearMenu(menu);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", menuCreado,
                "message", "Menú creado correctamente"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al crear el menú: " + e.getMessage()
            ));
        }
    }

    public void crearMenuParaRestaurante(Context ctx) {
        try {
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            String rutaArchivo = (String) requestBody.get("ruta_archivo");
            String rutaMenu = (String) requestBody.get("ruta_menu");

            if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "La ruta del archivo es obligatoria"
                ));
                return;
            }

            if (rutaMenu == null || rutaMenu.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "La ruta del menú es obligatoria"
                ));
                return;
            }

            Menu menuCreado = menuService.crearMenuParaRestaurante(rutaArchivo, rutaMenu, 
                idRestaurante, idSolicitud, idRestaurantero);
            
            ctx.status(201).json(Map.of(
                "success", true,
                "data", menuCreado,
                "message", "Menú creado para el restaurante correctamente"
            ));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al crear el menú: " + e.getMessage()
            ));
        }
    }

    public void actualizarMenu(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            Menu menuActualizado = ctx.bodyAsClass(Menu.class);
            
            boolean actualizado = menuService.actualizarMenu(idMenu, idRestaurante, 
                idSolicitud, idRestaurantero, menuActualizado);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Menú actualizado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar el menú"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar el menú: " + e.getMessage()
            ));
        }
    }

    public void cambiarEstadoMenu(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            @SuppressWarnings("unchecked")
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            String nuevoEstado = (String) requestBody.get("estado");

            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "El estado es obligatorio"
                ));
                return;
            }

            boolean actualizado = menuService.cambiarEstadoMenu(idMenu, idRestaurante, 
                idSolicitud, idRestaurantero, nuevoEstado);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Estado del menú actualizado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar el estado del menú"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al cambiar el estado del menú: " + e.getMessage()
            ));
        }
    }

    public void activarMenu(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            boolean actualizado = menuService.activarMenu(idMenu, idRestaurante, idSolicitud, idRestaurantero);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Menú activado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo activar el menú"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al activar el menú: " + e.getMessage()
            ));
        }
    }

    public void desactivarMenu(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));

            boolean actualizado = menuService.desactivarMenu(idMenu, idRestaurante, idSolicitud, idRestaurantero);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Menú desactivado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo desactivar el menú"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al desactivar el menú: " + e.getMessage()
            ));
        }
    }

    public void eliminarMenu(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            int idRestaurante = Integer.parseInt(ctx.pathParam("idRestaurante"));
            int idSolicitud = Integer.parseInt(ctx.pathParam("idSolicitud"));
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            boolean eliminado = menuService.eliminarMenu(idMenu, idRestaurante, idSolicitud, idRestaurantero);
            
            if (eliminado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Menú eliminado correctamente"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "Menú no encontrado"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "IDs inválidos"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al eliminar el menú: " + e.getMessage()
            ));
        }
    }

    public void obtenerEstadosMenu(Context ctx) {
        try {
            List<String> estados = menuService.obtenerEstadosMenu();
            ctx.json(Map.of(
                "success", true,
                "data", estados,
                "message", "Estados de menú obtenidos correctamente"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al obtener los estados de menú: " + e.getMessage()
            ));
        }
    }

    public void actualizarMenuPorRestaurantero(Context ctx) {
        try {
            int idRestaurantero = Integer.parseInt(ctx.pathParam("idRestaurantero"));
            
            String rutaArchivo = null;
            String rutaMenu = null;
            
            // Debug: Imprimir archivos recibidos
            System.out.println("=== DEBUG UPLOADED FILES ===");
            System.out.println("Número de archivos: " + ctx.uploadedFiles().size());
            for (var file : ctx.uploadedFiles()) {
                System.out.println("Archivo: " + file.filename() + ", Content-Type: " + file.contentType());
            }
            System.out.println("============================");
            
            // Verificar si hay archivos subidos
            if (!ctx.uploadedFiles().isEmpty()) {
                // Verificar que tengamos exactamente 2 archivos
                if (ctx.uploadedFiles().size() != 2) {
                    ctx.status(400).json(Map.of(
                        "success", false,
                        "message", "Debe enviar exactamente 2 archivos PDF: uno para 'ruta_archivo' y otro para 'ruta_menu'"
                    ));
                    return;
                }
                
                // Procesar archivos subidos (asumir que el primero es ruta_archivo y el segundo es ruta_menu)
                try {
                    var archivoFile = ctx.uploadedFiles().get(0);
                    var menuFile = ctx.uploadedFiles().get(1);
                    
                    rutaArchivo = guardarArchivoPDF(archivoFile);
                    rutaMenu = guardarArchivoPDF(menuFile);
                    
                    System.out.println("Primer archivo guardado en: " + rutaArchivo);
                    System.out.println("Segundo archivo guardado en: " + rutaMenu);
                } catch (Exception e) {
                    ctx.status(500).json(Map.of(
                        "success", false,
                        "message", "Error al guardar los archivos: " + e.getMessage()
                    ));
                    return;
                }
            } else {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "No se encontraron archivos. Debe enviar 2 archivos PDF"
                ));
                return;
            }
            
            // Validar que se hayan procesado los archivos
            if (rutaArchivo == null || rutaMenu == null) {
                ctx.status(400).json(Map.of(
                    "success", false,
                    "message", "Error al procesar los archivos PDF"
                ));
                return;
            }
            
            // Actualizar el menú del restaurantero
            boolean actualizado = menuService.actualizarMenuPorRestauranteroSimple(idRestaurantero, rutaArchivo, rutaMenu);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Menú del restaurantero actualizado correctamente con archivos PDF",
                    "ruta_archivo", rutaArchivo,
                    "ruta_menu", rutaMenu,
                    "id_restaurantero", idRestaurantero
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se encontró menú para este restaurantero o no se pudo actualizar"
                ));
            }
            
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de restaurantero inválido"
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar el menú: " + e.getMessage()
            ));
        }
    }

    // Método súper simplificado - solo necesita el ID del menú
    public void actualizarMenuSimplificado(Context ctx) {
        try {
            int idMenu = Integer.parseInt(ctx.pathParam("idMenu"));
            
            Menu menuActualizado = new Menu();
            String nuevaRutaMenu = null;
            String contentType = ctx.header("Content-Type");
            
            if (contentType != null && contentType.contains("multipart/form-data")) {
                if (!ctx.uploadedFiles().isEmpty()) {
                    var uploadedFile = ctx.uploadedFiles().get(0); // Tomar el primer archivo
                    
                    try {
                        nuevaRutaMenu = guardarArchivoPDF(uploadedFile);
                        menuActualizado.setRuta_archivo(nuevaRutaMenu);
                        menuActualizado.setRuta_menu(nuevaRutaMenu); // Actualizar ambos campos
                    } catch (Exception e) {
                        ctx.status(500).json(Map.of(
                            "success", false,
                            "message", "Error al guardar el archivo PDF: " + e.getMessage()
                        ));
                        return;
                    }
                } else {
                    String rutaArchivo = ctx.formParam("ruta_archivo");
                    String rutaMenu = ctx.formParam("ruta_menu");
                    String estado = ctx.formParam("estado");
                    
                    if (rutaArchivo != null && !rutaArchivo.trim().isEmpty()) {
                        menuActualizado.setRuta_archivo(rutaArchivo);
                    }
                    if (rutaMenu != null && !rutaMenu.trim().isEmpty()) {
                        menuActualizado.setRuta_menu(rutaMenu);
                    }
                    if (estado != null && !estado.trim().isEmpty()) {
                        menuActualizado.setEstado(estado);
                    }
                }
            } else {
                menuActualizado = ctx.bodyAsClass(Menu.class);
            }
            
            boolean actualizado = menuService.actualizarMenuSimplificado(idMenu, menuActualizado);
            
            if (actualizado) {
                ctx.json(Map.of(
                    "success", true,
                    "message", "Menú actualizado correctamente",
                    "nueva_ruta", nuevaRutaMenu != null ? nuevaRutaMenu : "Menú actualizado"
                ));
            } else {
                ctx.status(404).json(Map.of(
                    "success", false,
                    "message", "No se pudo actualizar el menú. Verifica que el menú exista"
                ));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", "ID de menú inválido"
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "success", false,
                "message", "Error al actualizar el menú: " + e.getMessage()
            ));
        }
    }

    private String guardarArchivoPDF(io.javalin.http.UploadedFile uploadedFile) throws Exception {
        String originalName = uploadedFile.filename();
        if (originalName == null || originalName.trim().isEmpty()) {
            throw new IllegalArgumentException("El archivo debe tener un nombre válido");
        }

        String extension = "";
        int lastDotIndex = originalName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalName.substring(lastDotIndex);
        }

        if (!extension.toLowerCase().equals(".pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF (.pdf)");
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + 
                               Math.abs(originalName.hashCode()) + extension;

        java.io.File uploadDir = new java.io.File("./uploads/menus");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        java.io.File destinationFile = new java.io.File(uploadDir, uniqueFileName);
        java.nio.file.Files.copy(uploadedFile.content(), destinationFile.toPath(), 
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return "http://52.23.26.163:7070/uploads/menus/" + uniqueFileName;
    }
}
