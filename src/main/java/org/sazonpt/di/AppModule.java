package org.sazonpt.di;

import org.sazonpt.controller.*;
import org.sazonpt.repository.*;
import org.sazonpt.routes.*;
import org.sazonpt.service.*;

/**
 * Módulo de inyección de dependencias
 * Siguiendo la arquitectura del template API
 */
public class AppModule {

    public static UsuarioRoutes initUser() {
        UsuarioRepository userRepo = new UsuarioRepository();
        UsuarioService userService = new UsuarioService(userRepo);
        UsuarioController userController = new UsuarioController(userService);
        return new UsuarioRoutes(userController);
    }

    public static AdminRoutes initAdmin() {
        AdminRepository adminRepo = new AdminRepository();
        AdminService adminService = new AdminService(adminRepo);
        AdminController adminController = new AdminController(adminService);
        return new AdminRoutes(adminController);
    }
    
}