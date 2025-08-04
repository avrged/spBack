package org.sazonpt.di;

import org.sazonpt.controller.AdministradorController;
import org.sazonpt.controller.ComprobanteController;
import org.sazonpt.controller.DescargaController;
import org.sazonpt.controller.FileUploadController;
import org.sazonpt.controller.ImagenController;
import org.sazonpt.controller.MenuController;
import org.sazonpt.controller.RegistroRestauranteController;
import org.sazonpt.controller.RestauranteController;
import org.sazonpt.controller.RestauranteroController;
import org.sazonpt.controller.Revision_solicitudController;
import org.sazonpt.controller.Solicitud_registroController;
import org.sazonpt.controller.UsuarioController;
import org.sazonpt.controller.Solicitud_registroEstadoController;
import org.sazonpt.controller.ZonaController;
import org.sazonpt.repository.AdministradorRepository;
import org.sazonpt.repository.ComprobanteRepository;
import org.sazonpt.repository.DescargaRepository;
import org.sazonpt.repository.ImagenRepository;
import org.sazonpt.repository.MenuRepository;
import org.sazonpt.repository.RestauranteRepository;
import org.sazonpt.repository.RestauranteroRepository;
import org.sazonpt.repository.Revision_solicitudRepository;
import org.sazonpt.repository.Solicitud_registroRepository;
import org.sazonpt.repository.UsuarioRepository;
import org.sazonpt.repository.ZonaRepository;
import org.sazonpt.routes.AdministradorRoutes;
import org.sazonpt.routes.ComprobanteRoutes;
import org.sazonpt.routes.DescargaRoutes;
import org.sazonpt.routes.FileUploadRoutes;
import org.sazonpt.routes.ImagenRoutes;
import org.sazonpt.routes.MenuRoutes;
import org.sazonpt.routes.RegistroRestauranteRoutes;
import org.sazonpt.routes.RestauranteRoutes;
import org.sazonpt.routes.RestauranteroRoutes;
import org.sazonpt.routes.Revision_solicitudRoutes;
import org.sazonpt.routes.Solicitud_registroRoutes;
import org.sazonpt.routes.UsuarioRoutes;
import org.sazonpt.routes.ZonaRoutes;
import org.sazonpt.service.AdministradorService;
import org.sazonpt.service.ComprobanteService;
import org.sazonpt.service.DescargaService;
import org.sazonpt.service.ImagenService;
import org.sazonpt.service.MenuService;
import org.sazonpt.service.RegistroRestauranteService;
import org.sazonpt.service.RestauranteService;
import org.sazonpt.service.RestauranteroService;
import org.sazonpt.service.Revision_solicitudService;
import org.sazonpt.service.Solicitud_registroService;
import org.sazonpt.service.UsuarioService;
import org.sazonpt.service.ZonaService;

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
    
    public static RestauranteRoutes initRestaurante() {
        Solicitud_registroRepository solicitudRepository = new Solicitud_registroRepository();
        RestauranteRepository restauranteRepository = new RestauranteRepository();
        
        // Crear ZonaService para la dependencia
        ZonaRepository zonaRepository = new ZonaRepository();
        ZonaService zonaService = new ZonaService(zonaRepository);
        
        RestauranteService restauranteService = new RestauranteService(restauranteRepository, solicitudRepository, zonaService);
        RestauranteController restauranteController = new RestauranteController(restauranteService);
        return new RestauranteRoutes(restauranteController);
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
    
    public static ImagenRoutes initImagen() {
        ImagenRepository imagenRepository = new ImagenRepository();
        ImagenService imagenService = new ImagenService(imagenRepository);
        ImagenController imagenController = new ImagenController(imagenService);
        return new ImagenRoutes(imagenController);
    }
    
    public static ComprobanteRoutes initComprobante() {
        ComprobanteRepository comprobanteRepository = new ComprobanteRepository();
        ComprobanteService comprobanteService = new ComprobanteService(comprobanteRepository);
        ComprobanteController comprobanteController = new ComprobanteController(comprobanteService);
        return new ComprobanteRoutes(comprobanteController);
    }
    
    public static MenuRoutes initMenu() {
        MenuRepository menuRepository = new MenuRepository();
        MenuService menuService = new MenuService(menuRepository);
        MenuController menuController = new MenuController(menuService);
        return new MenuRoutes(menuController);
    }
    
    public static RegistroRestauranteRoutes initRegistroRestaurante() {
        // Inicializar todos los repositorios necesarios
        Solicitud_registroRepository solicitudRepository = new Solicitud_registroRepository();
        RestauranteRepository restauranteRepository = new RestauranteRepository();
        ImagenRepository imagenRepository = new ImagenRepository();
        ComprobanteRepository comprobanteRepository = new ComprobanteRepository();
        MenuRepository menuRepository = new MenuRepository();
        RestauranteroRepository restauranteroRepository = new RestauranteroRepository();
        
        // Crear el servicio con todas las dependencias
        RegistroRestauranteService registroService = new RegistroRestauranteService(
            solicitudRepository,
            restauranteRepository,
            imagenRepository,
            comprobanteRepository,
            menuRepository,
            restauranteroRepository
        );
        
        // Crear el controlador
        RegistroRestauranteController registroController = new RegistroRestauranteController(registroService);
        
        return new RegistroRestauranteRoutes(registroController);
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
    
    public static FileUploadRoutes initFileUpload() {
        FileUploadController fileUploadController = new FileUploadController();
        return new FileUploadRoutes(fileUploadController);
    }
    
    public static DescargaRoutes initDescarga() {
        DescargaRepository descargaRepository = new DescargaRepository();
        DescargaService descargaService = new DescargaService(descargaRepository);
        DescargaController descargaController = new DescargaController(descargaService);
        return new DescargaRoutes(descargaController);
    }
    
    // Método para obtener solo el controller de descarga si se necesita
    public static DescargaController getDescargaController() {
        DescargaRepository descargaRepository = new DescargaRepository();
        DescargaService descargaService = new DescargaService(descargaRepository);
        return new DescargaController(descargaService);
    }
    
    // Método para obtener solo el service de descarga si se necesita
    public static DescargaService getDescargaService() {
        DescargaRepository descargaRepository = new DescargaRepository();
        return new DescargaService(descargaRepository);
    }
    
    public static ZonaRoutes initZona() {
        ZonaRepository zonaRepository = new ZonaRepository();
        ZonaService zonaService = new ZonaService(zonaRepository);
        ZonaController zonaController = new ZonaController(zonaService);
        return new ZonaRoutes(zonaController);
    }
    
    // Método para obtener solo el controller de zona si se necesita
    public static ZonaController getZonaController() {
        ZonaRepository zonaRepository = new ZonaRepository();
        ZonaService zonaService = new ZonaService(zonaRepository);
        return new ZonaController(zonaService);
    }
    
    // Método para obtener solo el service de zona si se necesita
    public static ZonaService getZonaService() {
        ZonaRepository zonaRepository = new ZonaRepository();
        return new ZonaService(zonaRepository);
    }
}