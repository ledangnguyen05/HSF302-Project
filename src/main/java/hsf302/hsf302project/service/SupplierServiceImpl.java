package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.SupplierEntity;
import hsf302.hsf302project.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    SupplierRepository supplierRepository;

    @Override
    public List<SupplierEntity> getAll() {
        return supplierRepository.findAll();
    }

    @Override
    public boolean addSupplier(SupplierEntity supplier) {
        try{
            supplierRepository.save(supplier);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean update(int id, SupplierEntity supplier) {
        try{
            if(supplierRepository.findById(id).isPresent()) {
                supplierRepository.save(supplier);
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try{
            supplierRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public SupplierEntity findById(int id) {
        return supplierRepository.findById(id).orElse(null);
    }
}
