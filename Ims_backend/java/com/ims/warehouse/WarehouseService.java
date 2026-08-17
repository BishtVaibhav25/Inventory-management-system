package com.ims.warehouse;

import com.ims.common.DuplicateResourceException;
import com.ims.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<Warehouse> getAll() {
        return warehouseRepository.findAll();
    }

    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + id));
    }

    public Warehouse create(Warehouse warehouse) {
        if (warehouseRepository.existsByName(warehouse.getName())) {
            throw new DuplicateResourceException(
                    "Warehouse with name '" + warehouse.getName() + "' already exists");
        }
        warehouse.setActive(true);
        return warehouseRepository.save(warehouse);
    }
}