package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishOrderStatusRest;
import edu.hei.school.restaurant.model.DishOrderStatusHistory;
import org.springframework.stereotype.Component;

@Component
public class DishOrderStatusRestMapper {

    public DishOrderStatusRest toRest (DishOrderStatusHistory dishOrderStatusList) {
        return new DishOrderStatusRest(
                dishOrderStatusList.getStatus(),
                dishOrderStatusList.getCreationDateTime()
        );
    }
}
