package mng.qlkt.repository;

import mng.qlkt.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {


@Query(value = "SELECT e, w, p, pr FROM Entry e " +
        "LEFT JOIN FETCH e.wareHouse w " +
        "LEFT JOIN FETCH w.product p " +
        "LEFT JOIN FETCH p.producers pr " +
        "WHERE e.isActive in (0, 1) " +
        "And (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
        "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
        "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) " +
        "AND (:startDate IS NULL OR :endDate IS NULL OR e.inTime BETWEEN :startDate AND :endDate)",
        countQuery = "SELECT COUNT(DISTINCT e) FROM Entry e " +
                "LEFT JOIN e.wareHouse w " +
                "LEFT JOIN w.product p " +
                "LEFT JOIN p.producers pr " +
                "WHERE e.isActive in (0, 1) " +
                "And (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
                "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
                "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) " +
                "AND (:startDate IS NULL OR :endDate IS NULL OR e.inTime BETWEEN :startDate AND :endDate)")
    Page<Entry> getAllEntryList(Pageable pageable, String nameProduct, String idEntry, String nameProducer, Date startDate, Date endDate);

    @Query(value = "SELECT e, w, p, pr FROM Entry e " +
            "LEFT JOIN FETCH e.wareHouse w " +
            "LEFT JOIN FETCH w.product p " +
            "LEFT JOIN FETCH p.producers pr " +
            "WHERE e.isActive in (0, 1) " +
            "And (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
            "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
            "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) ",
            countQuery = "SELECT COUNT(DISTINCT e) FROM Entry e " +
                    "LEFT JOIN e.wareHouse w " +
                    "LEFT JOIN w.product p " +
                    "LEFT JOIN p.producers pr " +
                    "WHERE e.isActive in (0, 1) " +
                    "And (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
                    "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
                    "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) ")
    Page<Entry> getAllEntryListNotDate(Pageable pageable, String nameProduct, String idEntry, String nameProducer);

    @Query(value = "SELECT e, w, p, pr FROM Entry e " +
            "LEFT JOIN FETCH e.wareHouse w " +
            "LEFT JOIN FETCH w.product p " +
            "LEFT JOIN FETCH p.producers pr " +
            "WHERE e.isActive in (1) " +
            "And (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
            "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
            "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) " +
            "AND (:creator IS NULL OR e.creator = :creator OR :creator = '') " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR e.inTime BETWEEN :startDate AND :endDate)",
            countQuery = "SELECT COUNT(DISTINCT e) FROM Entry e " +
                    "LEFT JOIN e.wareHouse w " +
                    "LEFT JOIN w.product p " +
                    "LEFT JOIN p.producers pr " +
                    "WHERE e.isActive in (1) " +
                    "And (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
                    "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
                    "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) " +
                    "AND (:creator IS NULL OR e.creator = :creator OR :creator = '') " +
                    "AND (:startDate IS NULL OR :endDate IS NULL OR e.inTime BETWEEN :startDate AND :endDate)")
    Page<Entry> getAllEntrySaleList(Pageable pageable, String nameProduct, String idEntry, String nameProducer, String creator, Date startDate, Date endDate);

    @Query(value = "SELECT SUM(e.moneyTotal) AS totalMoney, e, w, p, pr FROM Entry e " +
            "LEFT JOIN FETCH e.wareHouse w " +
            "LEFT JOIN FETCH w.product p " +
            "LEFT JOIN FETCH p.producers pr " +
            "WHERE e.isActive in (1) " +
            "AND (:idEntry IS NULL OR e.idEntry LIKE %:idEntry%) " +
            "AND (:nameProduct IS NULL OR p.productName LIKE %:nameProduct%) " +
            "AND (:nameProducer IS NULL OR pr.producerName LIKE %:nameProducer%) " +
            "AND (:creator IS NULL OR e.creator = :creator OR :creator = '') " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR e.inTime BETWEEN :startDate AND :endDate)")
    Long calculateTotalMoney(String nameProduct, String idEntry, String nameProducer, String creator, Date startDate, Date endDate);
    @Modifying
    @Query("UPDATE Entry e SET e.isActive = :isActive where e.id = :id")
    void updateEntryActive(Integer isActive, Long id);
}
