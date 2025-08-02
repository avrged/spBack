package org.sazonpt.di;

import org.sazonpt.controller.*;
import org.sazonpt.repository.*;
import org.sazonpt.routes.*;
import org.sazonpt.service.*;

public class AppModule {

    public static UsuarioRoutes initUser() {
        UsuarioRepository userRepo = new UsuarioRepository();
        RestauranteroRepository restauranteroRepo = new RestauranteroRepository();
        UsuarioService userService = new UsuarioService(userRepo, restauranteroRepo);
        UsuarioController userController = new UsuarioController(userService);
        return new UsuarioRoutes(userController);
    }

    public static AdminRoutes initAdmin() {
        AdminRepository adminRepo = new AdminRepository();
        AdminService adminService = new AdminService(adminRepo);
        AdminController adminController = new AdminController(adminService);
        return new AdminRoutes(adminController);
    }
    
    public static RestauranteRoutes initRestaurante() {
        RestauranteRepository restauranteRepo = new RestauranteRepository();
        RestauranteService restauranteService = new RestauranteService(restauranteRepo);
        RestauranteController restauranteController = new RestauranteController(restauranteService);
        return new RestauranteRoutes(restauranteController);
    }
    
    public static Solicitud_registroRoutes initSolicitudRegistro() {
        Solicitud_registroRepository solicitudRepo = new Solicitud_registroRepository();
        RestauranteRepository restauranteRepo = new RestauranteRepository();
        Solicitud_registroService solicitudService = new Solicitud_registroService(solicitudRepo, restauranteRepo);
        Solicitud_registroController solicitudController = new Solicitud_registroController(solicitudService);
        return new Solicitud_registroRoutes(solicitudController);
    }
    
    public static Imagen_restauranteRoutes initImagenRestaurante() {
        Imagen_restauranteRepository imagenRepo = new Imagen_restauranteRepository();
        Imagen_restauranteService imagenService = new Imagen_restauranteService(imagenRepo);
        Imagen_restauranteController imagenController = new Imagen_restauranteController(imagenService);
        return new Imagen_restauranteRoutes(imagenController);
    }
}