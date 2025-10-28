package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer> {
    List<SupplierEntity>findBySupplierNameContainingIgnoreCase(String supplierName);
    List<SupplierEntity>findByContactNameContainingIgnoreCase(String contactName);
    List<SupplierEntity> findByPhone(String phone);
    Optional<SupplierEntity>findBySupplierNameIgnoreCase(String supplierName);
}
