package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.SupplierEntity;

import java.util.List;

public interface SupplierService {
    List<SupplierEntity> getAll();
    boolean addSupplier(SupplierEntity supplier);
    boolean update(int id,SupplierEntity supplier);
    boolean delete(int id);
    SupplierEntity findById(int id);
}

