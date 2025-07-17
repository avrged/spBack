package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Menu;
import org.sazonpt.repository.MenuRepository;

public class MenuService {
    private final MenuRepository menuRepo;

    public MenuService(MenuRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    public List<Menu> getAllMenus() throws SQLException {
        return menuRepo.findAll();
    }

    public Menu getById(int idMenu) throws SQLException {
        return menuRepo.findById(idMenu);
    }

    public List<Menu> getMenusByRestaurant(int codigoRestaurante) throws SQLException {
        return menuRepo.findByRestaurant(codigoRestaurante);
    }

    public void createMenu(Menu menu) throws SQLException {
        if (menu.getRuta_archivo() == null || menu.getRuta_archivo().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        if (menu.getCodigo_restaurante() <= 0) {
            throw new IllegalArgumentException("Código de restaurante inválido");
        }

        menuRepo.save(menu);
    }

    public void updateMenu(Menu menu) throws SQLException {
        if (menuRepo.findById(menu.getIdMenu()) == null) {
            throw new IllegalArgumentException("No existe un menú con este ID");
        }

        if (menu.getRuta_archivo() == null || menu.getRuta_archivo().trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo es obligatoria");
        }

        menuRepo.update(menu);
    }

    public void deleteMenu(int idMenu) throws SQLException {
        if (menuRepo.findById(idMenu) == null) {
            throw new IllegalArgumentException("No existe un menú con este ID");
        }
        
        menuRepo.delete(idMenu);
    }
}
