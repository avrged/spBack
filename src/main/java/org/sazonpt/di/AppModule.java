package org.sazonpt.di;

import org.sazonpt.controller.*;
import org.sazonpt.repository.*;
import org.sazonpt.routes.*;
import org.sazonpt.service.*;

public class AppModule {

    public static AdminRoutes initAdmin() {
        AdminRepository adminRepo = new AdminRepository();
        AdminService adminService = new AdminService(adminRepo);
        AdminController adminController = new AdminController(adminService);
        return new AdminRoutes(adminController);
    }

    public static UsuarioRoutes initUser() {
        UsuarioRepository userRepo = new UsuarioRepository();
        UsuarioService userService = new UsuarioService(userRepo);
        UsuarioController userController = new UsuarioController(userService);
        return new UsuarioRoutes(userController);
    }

    public static RestauranteroRoutes initRestaurantero() {
        RestauranteroRepository restauranteroRepo = new RestauranteroRepository();
        RestauranteroService restauranteroService = new RestauranteroService(restauranteroRepo);
        RestauranteroController restauranteroController = new RestauranteroController(restauranteroService);
        return new RestauranteroRoutes(restauranteroController);
    }

    // Nuevas rutas implementadas
    public static MenuRoutes initMenu() {
        MenuRepository menuRepo = new MenuRepository();
        MenuService menuService = new MenuService(menuRepo);
        MenuController menuController = new MenuController(menuService);
        return new MenuRoutes(menuController);
    }

    public static ZonaRoutes initZona() {
        ZonaRepository zonaRepo = new ZonaRepository();
        ZonaService zonaService = new ZonaService(zonaRepo);
        ZonaController zonaController = new ZonaController(zonaService);
        return new ZonaRoutes(zonaController);
    }

    public static RestauranteRoutes initRestaurante() {
        RestauranteRepository restauranteRepo = new RestauranteRepository();
        RestauranteService restauranteService = new RestauranteService(restauranteRepo);
        RestauranteController restauranteController = new RestauranteController(restauranteService);
        return new RestauranteRoutes(restauranteController);
    }

    public static ImagenRoutes initImagen() {
        ImagenRepository imagenRepo = new ImagenRepository();
        ImagenService imagenService = new ImagenService(imagenRepo);
        ImagenController imagenController = new ImagenController(imagenService);
        return new ImagenRoutes(imagenController);
    }

    public static DescargaRoutes initDescarga() {
        DescargaRepository descargaRepo = new DescargaRepository();
        DescargaService descargaService = new DescargaService(descargaRepo);
        DescargaController descargaController = new DescargaController(descargaService);
        return new DescargaRoutes(descargaController);
    }

    public static ComprobanteRoutes initComprobante() {
        ComprobanteRepository comprobanteRepo = new ComprobanteRepository();
        ComprobanteService comprobanteService = new ComprobanteService(comprobanteRepo);
        ComprobanteController comprobanteController = new ComprobanteController(comprobanteService);
        return new ComprobanteRoutes(comprobanteController);
    }

    public static Solicitud_registroRoutes initSolicitudRegistro() {
        Solicitud_registroRepository solicitudRepo = new Solicitud_registroRepository();
        Solicitud_registroService solicitudService = new Solicitud_registroService(solicitudRepo);
        Solicitud_registroController solicitudController = new Solicitud_registroController(solicitudService);
        return new Solicitud_registroRoutes(solicitudController);
    }

    public static Adquirir_membresiaRoutes initAdquirirMembresia() {
        Adquirir_membresiaRepository membresiaRepo = new Adquirir_membresiaRepository();
        Adquirir_membresiaService membresiaService = new Adquirir_membresiaService(membresiaRepo);
        Adquirir_membresiaController membresiaController = new Adquirir_membresiaController(membresiaService);
        return new Adquirir_membresiaRoutes(membresiaController);
    }

    public static Revision_soliRoutes initRevisionSolicitud() {
        Revision_soliRepository revisionRepo = new Revision_soliRepository();
        Revision_soliService revisionService = new Revision_soliService(revisionRepo);
        Revision_solicitudController revisionController = new Revision_solicitudController(revisionService);
        return new Revision_soliRoutes(revisionController);
    }

}
