package mng.qlkt.repository;

import mng.qlkt.model.WareHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {

    @Query(value = "SELECT w, p, pr FROM WareHouse w LEFT JOIN FETCH w.product p LEFT JOIN FETCH p.producers pr where w.is_active IN (:isActive)" +
            "And (:productName IS NULL OR p.productName like %:productName%) " +
            "And (:productId IS NULL OR p.productId like %:productId%) ",
            countQuery = "SELECT COUNT(DISTINCT w) FROM WareHouse w LEFT JOIN w.product p LEFT JOIN p.producers pr where w.is_active IN (:isActive)"+
                    "And (:productName IS NULL OR p.productName like %:productName%) " +
                    "And (:productId IS NULL OR p.productId like %:productId%) " +
                    "ORDER BY w.manufactureDate DESC"
    )
    Page<WareHouse> getAllWareHouseList(Pageable pageable, String productName, String productId, List<Integer> isActive);

    @Query("SELECT w FROM WareHouse w left join Warehouse_product wp ON wp.warehouser_id = w.id LEFT JOIN Product p ON wp.product_id = p.id where w.is_active = 1")
    List<WareHouse> getWareHouse();

    @Modifying
    @Query("UPDATE WareHouse w SET w.quantity = :quantity where w.id = :id")
    void updateExportWareHouse(Integer quantity, Long id);


    @Modifying
    @Query("UPDATE WareHouse w SET w.quantity = :quantity, w.is_active = 2 where w.id = :id")
    void updateExportWareHouseifClean(Integer quantity, Long id);

    @Modifying
    @Query("UPDATE WareHouse w SET w.is_active = :isActive where w.id = :id")
    void updateEntryWareHouse(Integer isActive, Long id);

    @Modifying
    @Query("UPDATE WareHouse w " +
            "SET w.statusDate = 1L " +
            "WHERE w.manufactureDate BETWEEN CURRENT_DATE AND :fiveDaysLater")
    void updateStatusDateForExpiredWarehouses( Date fiveDaysLater);
}
