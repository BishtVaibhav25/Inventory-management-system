package com.ims.supplier;

import com.ims.common.DuplicateResourceException;
import com.ims.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    public Supplier getById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier not found with id: " + id));
    }

    public Supplier create(Supplier supplier) {
        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new DuplicateResourceException(
                    "Supplier with email '" + supplier.getEmail() + "' already exists");
        }
        supplier.setActive(true);
        return supplierRepository.save(supplier);
    }
}