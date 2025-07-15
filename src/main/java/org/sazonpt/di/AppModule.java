package org.sazonpt.di;

import org.sazonpt.controller.AdminController;
import org.sazonpt.controller.UsuarioController;
import org.sazonpt.repository.AdminRepository;
import org.sazonpt.repository.UsuarioRepository;
import org.sazonpt.routes.AdminRoutes;
import org.sazonpt.routes.UsuarioRoutes;
import org.sazonpt.service.AdminService;
import org.sazonpt.service.UsuarioService;

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

}
