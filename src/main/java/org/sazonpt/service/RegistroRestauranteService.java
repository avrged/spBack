package org.sazonpt.service;

import org.sazonpt.model.*;
import org.sazonpt.model.dto.RegistroRestauranteDTO;
import org.sazonpt.repository.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroRestauranteService {
    
    private final Solicitud_registroRepository solicitudRepository;
    private final RestauranteRepository restauranteRepository;
    private final ImagenRepository imagenRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final MenuRepository menuRepository;
    private final RestauranteroRepository restauranteroRepository;
    
    public RegistroRestauranteService(Solicitud_registroRepository solicitudRepository,
                                    RestauranteRepository restauranteRepository,
                                    ImagenRepository imagenRepository,
                                    ComprobanteRepository comprobanteRepository,
                                    MenuRepository menuRepository,
                                    RestauranteroRepository restauranteroRepository) {
        this.solicitudRepository = solicitudRepository;
        this.restauranteRepository = restauranteRepository;
        this.imagenRepository = imagenRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.menuRepository = menuRepository;
        this.restauranteroRepository = restauranteroRepository;
    }
    
    /**
     * Registra un nuevo restaurante con todos sus datos
     * Flujo: solicitud_registro -> restaurante -> imagenes -> comprobantes -> menu
     */
    public String registrarRestaurante(RegistroRestauranteDTO datos) throws SQLException {
        try {
            // 1. Validar que el restaurantero existe
            if (!restauranteroRepository.existsById(datos.getIdRestaurantero())) {
                throw new SQLException("El restaurantero especificado no existe");
            }
            
            // 2. Crear la solicitud de registro
            Solicitud_registro solicitud = new Solicitud_registro();
            solicitud.setFecha(LocalDateTime.now());
            solicitud.setEstado("pendiente");
            solicitud.setNombre_propuesto_restaurante(datos.getNombreRestaurante());
            solicitud.setCorreo(datos.getCorreoElectronico());
            solicitud.setNombre_propietario(datos.getPropietario());
            solicitud.setHorario_atencion(datos.getHorarios());
            solicitud.setId_restaurantero(datos.getIdRestaurantero());
            
            solicitud = solicitudRepository.save(solicitud);
            int idSolicitud = solicitud.getId_solicitud();
            
            // 3. Crear el restaurante (ya que la solicitud se creó automáticamente como pendiente)
            Restaurante restaurante = new Restaurante(
                datos.getNombreRestaurante(),
                datos.getHorarios(),
                datos.getNumeroCelular(),
                buildEtiquetas(datos), // Construir etiquetas basadas en datos del form
                idSolicitud,
                datos.getIdRestaurantero(),
                datos.getIdZona()
            );
            
            restaurante = restauranteRepository.save(restaurante);
            int idRestaurante = restaurante.getId_restaurante();
            
            // 4. Guardar imágenes
            List<String> imagenesCreadas = crearImagenes(datos, idRestaurante, idSolicitud, datos.getIdRestaurantero());
            
            // 5. Guardar comprobantes
            List<String> comprobantesCreados = crearComprobantes(datos, idRestaurante, idSolicitud, 
                                                               datos.getIdRestaurantero(), datos.getIdZona());
            
            // 6. Guardar menú
            String menuCreado = crearMenu(datos, idRestaurante, idSolicitud, datos.getIdRestaurantero());
            
            return String.format("Restaurante registrado exitosamente. ID: %d, Solicitud: %d. " +
                               "Imágenes: %s, Comprobantes: %s, Menú: %s", 
                               idRestaurante, idSolicitud, 
                               String.join(", ", imagenesCreadas),
                               String.join(", ", comprobantesCreados),
                               menuCreado);
                               
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el restaurante: " + e.getMessage(), e);
        }
    }
    
    private String buildEtiquetas(RegistroRestauranteDTO datos) {
        // Construir etiquetas basadas en los datos del formulario
        List<String> etiquetas = new ArrayList<>();
        
        if (datos.getNombreRestaurante() != null) {
            // Agregar palabras clave del nombre como etiquetas
            String[] palabras = datos.getNombreRestaurante().split("\\s+");
            for (String palabra : palabras) {
                if (palabra.length() > 2) {
                    etiquetas.add(palabra);
                }
            }
        }
        
        // Agregar etiquetas genéricas
        etiquetas.add("restaurante");
        etiquetas.add("comida");
        
        return String.join(", ", etiquetas);
    }
    
    private List<String> crearImagenes(RegistroRestauranteDTO datos, int idRestaurante, 
                                     int idSolicitud, int idRestaurantero) throws SQLException {
        List<String> imagenesCreadas = new ArrayList<>();
        
        // Imagen principal
        if (datos.getImagenPrincipal() != null && !datos.getImagenPrincipal().trim().isEmpty()) {
            Imagen imagenPrincipal = new Imagen(
                "/uploads/images/" + datos.getImagenPrincipal(), // Prefijo más corto
                LocalDateTime.now(),
                idRestaurante,
                idSolicitud,
                idRestaurantero
            );
            imagenRepository.save(imagenPrincipal);
            imagenesCreadas.add("Principal");
        }
        
        // Imagen secundaria
        if (datos.getImagenSecundaria() != null && !datos.getImagenSecundaria().trim().isEmpty()) {
            Imagen imagenSecundaria = new Imagen(
                "/uploads/images/" + datos.getImagenSecundaria(),
                LocalDateTime.now(),
                idRestaurante,
                idSolicitud,
                idRestaurantero
            );
            imagenRepository.save(imagenSecundaria);
            imagenesCreadas.add("Secundaria");
        }
        
        // Imagen de platillo
        if (datos.getImagenPlatillo() != null && !datos.getImagenPlatillo().trim().isEmpty()) {
            Imagen imagenPlatillo = new Imagen(
                "/uploads/images/" + datos.getImagenPlatillo(),
                LocalDateTime.now(),
                idRestaurante,
                idSolicitud,
                idRestaurantero
            );
            imagenRepository.save(imagenPlatillo);
            imagenesCreadas.add("Platillo");
        }
        
        return imagenesCreadas;
    }
    
    private List<String> crearComprobantes(RegistroRestauranteDTO datos, int idRestaurante, 
                                         int idSolicitud, int idRestaurantero, int idZona) throws SQLException {
        List<String> comprobantesCreados = new ArrayList<>();
        
        // Comprobante de domicilio
        if (datos.getComprobanteDomicilio() != null && !datos.getComprobanteDomicilio().trim().isEmpty()) {
            Comprobante comprobanteDomicilio = new Comprobante(
                "comprobante_domicilio",
                "/uploads/docs/" + datos.getComprobanteDomicilio(), // Ruta más corta
                LocalDateTime.now(),
                idRestaurante,
                idSolicitud,
                idRestaurantero,
                idZona
            );
            comprobanteRepository.save(comprobanteDomicilio);
            comprobantesCreados.add("Domicilio");
        }
        
        return comprobantesCreados;
    }
    
    private String crearMenu(RegistroRestauranteDTO datos, int idRestaurante, 
                           int idSolicitud, int idRestaurantero) throws SQLException {
        
        if (datos.getMenuRestaurante() != null && !datos.getMenuRestaurante().trim().isEmpty()) {
            Menu menu = new Menu(
                "/uploads/menus/" + datos.getMenuRestaurante(), // ruta_archivo
                "/uploads/menus/" + datos.getMenuRestaurante(), // ruta_menu (mismo archivo)
                "activo",
                idRestaurante,
                idSolicitud,
                idRestaurantero
            );
            menuRepository.save(menu);
            return "Menú creado";
        }
        
        return "Sin menú";
    }
    
    /**
     * Obtiene el estado de una solicitud de registro
     */
    public String obtenerEstadoSolicitud(int idSolicitud) throws SQLException {
        return solicitudRepository.findById(idSolicitud)
            .map(Solicitud_registro::getEstado)
            .orElse("No encontrada");
    }
    
    /**
     * Lista todas las solicitudes de un restaurantero
     */
    public List<Solicitud_registro> obtenerSolicitudesRestaurantero(int idRestaurantero) throws SQLException {
        return solicitudRepository.findByRestaurantero(idRestaurantero);
    }
}
