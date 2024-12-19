package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.responses.dish_portion_cart.DishPortionCartToReview;
import com.iamnirvan.restaurant.core.models.responses.dish_portion_cart.DishPortionCartToReviewProjection;
import com.iamnirvan.restaurant.core.models.responses.metrics.DishMetrics;
import com.iamnirvan.restaurant.core.models.responses.metrics.UnitsSoldPerMonthProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END FROM Dish d WHERE d.name = :name AND d.id != :id")
    boolean existsByNameExcludingId(String name, Long id);
    @Query(value = """
        SELECT
            COALESCE(SUM(dpc.quantity), 0) as "unitsSoldToday",
            COALESCE(SUM(dpc.quantity), 0) as "unitsSoldThisQuarter",
            COALESCE(SUM(dpc.quantity * dp.price), 0) AS "dishRevenue",
            TRUNC(((SUM(dpc.quantity * dp.price) / (SELECT SUM(total) FROM food_order)) * 100)::DECIMAL, 1) AS "revenueAccountedFor"
        FROM food_order fo
        INNER JOIN cart crt ON fo.cart_id = crt.id
        INNER JOIN dish_portion_cart dpc ON crt.id = dpc.cart_id
        INNER JOIN dish_portion dp ON dpc.dish_portion_id = dp.id
        INNER JOIN dish d ON dp.dish_id = d.id
        WHERE d.id = :dishId
        """, nativeQuery = true)
    DishMetrics getDishStats(@Param("dishId") Long dishId);

    @Query(value = """
        SELECT
            TRUNC(((SUM(dpc.quantity * dp.price) / (SELECT SUM(total) FROM food_order)) * 100)::DECIMAL, 1) AS "revenueAccountedFor"
        FROM food_order fo
                 INNER JOIN cart crt ON fo.cart_id = crt.id
                 INNER JOIN dish_portion_cart dpc ON crt.id = dpc.cart_id
                 INNER JOIN dish_portion dp ON dpc.dish_portion_id = dp.id
                 INNER JOIN dish d ON dp.dish_id = d.id
        WHERE d.id = :dishId;
    """, nativeQuery = true)
    Float getRevenueAccountedFor(@Param("dishId") Long dishId);

    @Query(value = """
        SELECT
            COALESCE(SUM(dpc.quantity), 0) as "unitsSoldThisQuarter"
        FROM food_order fo
                 INNER JOIN cart crt on fo.cart_id = crt.id
                 INNER JOIN dish_portion_cart dpc on crt.id = dpc.cart_id
                 INNER JOIN dish_portion dp on dpc.dish_portion_id = dp.id
                 INNER JOIN dish d on dp.dish_id = d.id
        WHERE d.id = :dishId
            AND fo.date BETWEEN date_trunc('quarter', current_date)
            AND date_trunc('quarter', current_date) + interval '3 months' - interval '1 second';
    """, nativeQuery = true)
    Long getUnitsSoldThisQuarter(@Param("dishId") Long dishId);

    @Query(value = """
        SELECT
            COALESCE(SUM(dpc.quantity), 0)
        FROM food_order fo
        INNER JOIN cart crt ON fo.cart_id = crt.id
        INNER JOIN dish_portion_cart dpc ON crt.id = dpc.cart_id
        INNER JOIN dish_portion dp ON dpc.dish_portion_id = dp.id
        INNER JOIN dish d ON dp.dish_id = d.id
        WHERE d.id = :dishId
        AND fo.date BETWEEN CAST (:start AS TIMESTAMP WITH TIME ZONE) AND CAST (:end AS TIMESTAMP WITH TIME ZONE);
    """, nativeQuery = true)
    Long getUnitsSoldToday(@Param("dishId") Long dishId, @Param("start") String start, @Param("end") String end);

    @Query(value = """
        SELECT
            TO_CHAR(DATE_TRUNC('month', fo.date), 'Month') AS month,
            SUM(dpc.quantity) AS unitsSold
        FROM food_order fo
                 INNER JOIN cart crt ON fo.cart_id = crt.id
                 INNER JOIN dish_portion_cart dpc ON crt.id = dpc.cart_id
                 INNER JOIN dish_portion dp ON dpc.dish_portion_id = dp.id
                 INNER JOIN dish d ON dp.dish_id = d.id
        WHERE EXTRACT(YEAR FROM fo.date) = EXTRACT(YEAR FROM CURRENT_DATE)
          AND d.id = :dishId
        GROUP BY DATE_TRUNC('month', fo.date)
        ORDER BY DATE_TRUNC('month', fo.date)
        """, nativeQuery = true)
    List<UnitsSoldPerMonthProjection> getMonthlySalesForDish(@Param("dishId") Long dishId);

    @Query(value = """
        select
            d.id,
            d.name,
            d.description,
            d.image,
            d.created,
            d.updated
        from dish_portion_cart dpc
        inner join public.cart c on dpc.cart_id = c.id
        inner join public.customer c2 on c.customer_id = c2.id
        inner join dish_portion dp on dpc.dish_portion_id = dp.id
        inner join dish d on dp.dish_id = d.id
        where c2.id = :customerId and dpc.reviewed = false
    """, nativeQuery = true)
    List<Dish> getDishesToBeReviewedByCustomer(@Param("customerId") Long customerId);

    @Query(value = """
        select new com.iamnirvan.restaurant.core.models.responses.dish_portion_cart.DishPortionCartToReview(
            fo.date,
            dpc.id,
            d.id,
            d.name,
            d.image
        )
        from DishPortionCart dpc
        inner join Cart c on dpc.cart.id = c.id
        inner join Customer c2 on c.customer.id = c2.id
        inner join DishPortion dp on dpc.dishPortion.id = dp.id
        inner join Dish d on dp.dish.id = d.id
        inner join FoodOrder fo on c.id = fo.cart.id
        where c2.id = :customerId and dpc.reviewed = false
    """)
    List<DishPortionCartToReview> getDishesToBeReviewedByCustomerV2(@Param("customerId") Long customerId);

}