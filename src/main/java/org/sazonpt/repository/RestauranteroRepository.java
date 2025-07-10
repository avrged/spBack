package org.sazonpt.repository;

import org.sazonpt.model.Restaurantero;

public interface RestauranteroRepository {
    void AddRestaurantero(Restaurantero ro);
    boolean FindRestaurantero(int id);
    boolean DeleteRestaurantero(int id);
    Restaurantero UpdateRestaurantero(Restaurantero ro);
    void ListAllRestauranteros();
}
