package org.sazonpt.service;

import org.sazonpt.model.Menu;
import org.sazonpt.repository.MenuRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MenuService {
    
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> obtenerTodosLosMenus() {
        try {
            return menuRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los menús: " + e.getMessage(), e);
        }
    }

    public Optional<Menu> obtenerMenuPorId(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return menuRepository.findById(idMenu, idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el menú: " + e.getMessage(), e);
        }
    }

    public List<Menu> obtenerMenusPorRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return menuRepository.findByRestaurante(idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los menús del restaurante: " + e.getMessage(), e);
        }
    }

    public List<Menu> obtenerMenusPorRestaurantero(int idRestaurantero) {
        try {
            return menuRepository.findByRestaurantero(idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los menús del restaurantero: " + e.getMessage(), e);
        }
    }

    public List<Menu> obtenerMenusPorEstado(String estado) {
        try {
            // Validar que el estado sea válido
            Menu.EstadoMenu.fromString(estado);
            
            return menuRepository.findByEstado(estado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de menú inválido: " + estado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los menús por estado: " + e.getMessage(), e);
        }
    }

    public Optional<Menu> obtenerMenuActivoPorRestaurante(int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return menuRepository.findMenuActivoByRestaurante(idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el menú activo del restaurante: " + e.getMessage(), e);
        }
    }

    public Menu crearMenu(Menu menu) {
        // Validaciones
        if (menu.getRuta_archivo() == null || menu.getRuta_archivo().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        if (menu.getRuta_menu() == null || menu.getRuta_menu().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del menú es obligatoria");
        }

        if (menu.getId_restaurante() <= 0) {
            throw new IllegalArgumentException("ID de restaurante inválido");
        }
        if (menu.getId_solicitud() <= 0) {
            throw new IllegalArgumentException("ID de solicitud inválido"); 
        }
        if (menu.getId_restaurantero() <= 0) {
            throw new IllegalArgumentException("ID de restaurantero inválido");
        }

        // Verificar que el restaurante existe
        try {
            if (!menuRepository.restauranteExists(menu.getId_restaurante(), 
                    menu.getId_solicitud(), menu.getId_restaurantero())) {
                throw new IllegalArgumentException("El restaurante especificado no existe");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el restaurante: " + e.getMessage(), e);
        }

        // Establecer estado por defecto si no se proporciona
        if (menu.getEstado() == null || menu.getEstado().trim().isEmpty()) {
            menu.setEstado(Menu.EstadoMenu.PENDIENTE.getValor());
        } else {
            // Validar que el estado sea válido
            try {
                Menu.EstadoMenu.fromString(menu.getEstado());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de menú inválido: " + menu.getEstado());
            }
        }

        try {
            return menuRepository.save(menu);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el menú: " + e.getMessage(), e);
        }
    }

    public boolean actualizarMenu(int idMenu, int idRestaurante, int idSolicitud, 
                                 int idRestaurantero, Menu menuActualizado) {
        try {
            // Verificar que el menú existe
            if (!menuRepository.existsById(idMenu, idRestaurante, idSolicitud, idRestaurantero)) {
                throw new IllegalArgumentException("El menú no existe");
            }

            // Validaciones
            if (menuActualizado.getRuta_archivo() == null || menuActualizado.getRuta_archivo().trim().isEmpty()) {
                throw new IllegalArgumentException("La ruta del archivo es obligatoria");
            }

            if (menuActualizado.getRuta_menu() == null || menuActualizado.getRuta_menu().trim().isEmpty()) {
                throw new IllegalArgumentException("La ruta del menú es obligatoria");
            }

            if (menuActualizado.getId_restaurante() <= 0) {
                throw new IllegalArgumentException("ID de restaurante inválido");
            }
            if (menuActualizado.getId_solicitud() <= 0) {
                throw new IllegalArgumentException("ID de solicitud inválido");
            }
            if (menuActualizado.getId_restaurantero() <= 0) {
                throw new IllegalArgumentException("ID de restaurantero inválido");
            }

            // Validar estado si se proporciona
            if (menuActualizado.getEstado() != null && !menuActualizado.getEstado().trim().isEmpty()) {
                try {
                    Menu.EstadoMenu.fromString(menuActualizado.getEstado());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Estado de menú inválido: " + menuActualizado.getEstado());
                }
            }

            // Verificar que el restaurante existe
            if (!menuRepository.restauranteExists(menuActualizado.getId_restaurante(), 
                    menuActualizado.getId_solicitud(), menuActualizado.getId_restaurantero())) {
                throw new IllegalArgumentException("El restaurante especificado no existe");
            }

            // Establecer los IDs de la clave primaria
            menuActualizado.setId_menu(idMenu);
            menuActualizado.setId_restaurante(idRestaurante);
            menuActualizado.setId_solicitud(idSolicitud);
            menuActualizado.setId_restaurantero(idRestaurantero);

            return menuRepository.update(menuActualizado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el menú: " + e.getMessage(), e);
        }
    }

    public boolean cambiarEstadoMenu(int idMenu, int idRestaurante, int idSolicitud, 
                                    int idRestaurantero, String nuevoEstado) {
        try {
            // Verificar que el menú existe
            if (!menuRepository.existsById(idMenu, idRestaurante, idSolicitud, idRestaurantero)) {
                throw new IllegalArgumentException("El menú no existe");
            }

            // Validar el estado
            try {
                Menu.EstadoMenu.fromString(nuevoEstado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de menú inválido: " + nuevoEstado);
            }

            return menuRepository.updateEstado(idMenu, idRestaurante, idSolicitud, idRestaurantero, nuevoEstado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar el estado del menú: " + e.getMessage(), e);
        }
    }

    public boolean activarMenu(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) {
        return cambiarEstadoMenu(idMenu, idRestaurante, idSolicitud, idRestaurantero, 
                                Menu.EstadoMenu.ACTIVO.getValor());
    }

    public boolean desactivarMenu(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) {
        return cambiarEstadoMenu(idMenu, idRestaurante, idSolicitud, idRestaurantero, 
                                Menu.EstadoMenu.INACTIVO.getValor());
    }

    public boolean eliminarMenu(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            if (!menuRepository.existsById(idMenu, idRestaurante, idSolicitud, idRestaurantero)) {
                throw new IllegalArgumentException("El menú no existe");
            }

            return menuRepository.delete(idMenu, idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el menú: " + e.getMessage(), e);
        }
    }

    public boolean existeMenu(int idMenu, int idRestaurante, int idSolicitud, int idRestaurantero) {
        try {
            return menuRepository.existsById(idMenu, idRestaurante, idSolicitud, idRestaurantero);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar la existencia del menú: " + e.getMessage(), e);
        }
    }

    /**
     * Método utilitario para crear un menú asociado a un restaurante específico
     */
    public Menu crearMenuParaRestaurante(String rutaArchivo, String rutaMenu, 
                                        int idRestaurante, int idSolicitud, int idRestaurantero) {
        Menu nuevoMenu = new Menu();
        nuevoMenu.setRuta_archivo(rutaArchivo);
        nuevoMenu.setRuta_menu(rutaMenu);
        nuevoMenu.setId_restaurante(idRestaurante);
        nuevoMenu.setId_solicitud(idSolicitud);
        nuevoMenu.setId_restaurantero(idRestaurantero);
        nuevoMenu.setEstado(Menu.EstadoMenu.PENDIENTE.getValor());
        
        return crearMenu(nuevoMenu);
    }

    /**
     * Obtener todos los estados de menú disponibles
     */
    public List<String> obtenerEstadosMenu() {
        return java.util.Arrays.stream(Menu.EstadoMenu.values())
                .map(Menu.EstadoMenu::getValor)
                .toList();
    }
}
