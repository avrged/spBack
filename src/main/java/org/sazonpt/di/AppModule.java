package org.sazonpt.di;

import org.sazonpt.controller.AdministradorController;
import org.sazonpt.controller.RestauranteroController;
import org.sazonpt.controller.Revision_solicitudController;
import org.sazonpt.controller.Solicitud_registroController;
import org.sazonpt.controller.UsuarioController;
import org.sazonpt.repository.AdministradorRepository;
import org.sazonpt.repository.RestauranteroRepository;
import org.sazonpt.repository.Revision_solicitudRepository;
import org.sazonpt.repository.Solicitud_registroRepository;
import org.sazonpt.repository.UsuarioRepository;
import org.sazonpt.routes.AdministradorRoutes;
import org.sazonpt.routes.RestauranteroRoutes;
import org.sazonpt.routes.Revision_solicitudRoutes;
import org.sazonpt.routes.Solicitud_registroRoutes;
import org.sazonpt.routes.UsuarioRoutes;
import org.sazonpt.service.AdministradorService;
import org.sazonpt.service.RestauranteroService;
import org.sazonpt.service.Revision_solicitudService;
import org.sazonpt.service.Solicitud_registroService;
import org.sazonpt.service.UsuarioService;

public class AppModule {
    
    public static UsuarioRoutes initUsuario() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        return new UsuarioRoutes(usuarioController);
    }
    
    public static AdministradorRoutes initAdministrador() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        AdministradorRepository administradorRepository = new AdministradorRepository();
        AdministradorService administradorService = new AdministradorService(administradorRepository, usuarioRepository);
        AdministradorController administradorController = new AdministradorController(administradorService);
        return new AdministradorRoutes(administradorController);
    }
    
    public static RestauranteroRoutes initRestaurantero() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        RestauranteroRepository restauranteroRepository = new RestauranteroRepository();
        RestauranteroService restauranteroService = new RestauranteroService(restauranteroRepository, usuarioRepository);
        RestauranteroController restauranteroController = new RestauranteroController(restauranteroService);
        return new RestauranteroRoutes(restauranteroController);
    }
    
    public static Solicitud_registroRoutes initSolicitudRegistro() {
        Solicitud_registroRepository solicitudRepository = new Solicitud_registroRepository();
        Solicitud_registroService solicitudService = new Solicitud_registroService(solicitudRepository);
        Solicitud_registroController solicitudController = new Solicitud_registroController(solicitudService);
        return new Solicitud_registroRoutes(solicitudController);
    }
    
    public static Revision_solicitudRoutes initRevisionSolicitud() {
        Revision_solicitudRepository revisionRepository = new Revision_solicitudRepository();
        Revision_solicitudService revisionService = new Revision_solicitudService(revisionRepository);
        Revision_solicitudController revisionController = new Revision_solicitudController(revisionService);
        return new Revision_solicitudRoutes(revisionController);
    }
    
    // Método para obtener solo el controller de usuario si se necesita
    public static UsuarioController getUsuarioController() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        return new UsuarioController(usuarioService);
    }
    
    // Método para obtener solo el controller de administrador si se necesita
    public static AdministradorController getAdministradorController() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        AdministradorRepository administradorRepository = new AdministradorRepository();
        AdministradorService administradorService = new AdministradorService(administradorRepository, usuarioRepository);
        return new AdministradorController(administradorService);
    }
    
    // Método para obtener solo el controller de restaurantero si se necesita
    public static RestauranteroController getRestauranteroController() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        RestauranteroRepository restauranteroRepository = new RestauranteroRepository();
        RestauranteroService restauranteroService = new RestauranteroService(restauranteroRepository, usuarioRepository);
        return new RestauranteroController(restauranteroService);
    }
    
    // Método para obtener solo el service de usuario si se necesita
    public static UsuarioService getUsuarioService() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        return new UsuarioService(usuarioRepository);
    }
    
    // Método para obtener solo el service de administrador si se necesita
    public static AdministradorService getAdministradorService() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        AdministradorRepository administradorRepository = new AdministradorRepository();
        return new AdministradorService(administradorRepository, usuarioRepository);
    }
    
    // Método para obtener solo el service de restaurantero si se necesita
    public static RestauranteroService getRestauranteroService() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        RestauranteroRepository restauranteroRepository = new RestauranteroRepository();
        return new RestauranteroService(restauranteroRepository, usuarioRepository);
    }
}