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

    public static MenuRoutes initMenu() {
        MenuRepository menuRepo = new MenuRepository();
        MenuService menuService = new MenuService(menuRepo);
        MenuController menuController = new MenuController(menuService);
        return new MenuRoutes(menuController);
    }

    public static EstadisticaRoutes initEstadistica() {
        EstadisticaRepository estadisticaRepo = new EstadisticaRepository();
        EstadisticaService estadisticaService = new EstadisticaService(estadisticaRepo);
        EstadisticaController estadisticaController = new EstadisticaController(estadisticaService);
        return new EstadisticaRoutes(estadisticaController);
    }

    public static Solicitud_registroRoutes initSolicitudRegistro() {
        Solicitud_registroRepository solicitudRepo = new Solicitud_registroRepository();
        Solicitud_registroService solicitudService = new Solicitud_registroService(solicitudRepo);
        Solicitud_registroController solicitudController = new Solicitud_registroController(solicitudService);
        return new Solicitud_registroRoutes(solicitudController);
    }

}
