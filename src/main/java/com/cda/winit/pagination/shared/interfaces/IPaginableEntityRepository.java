package com.cda.winit.pagination.shared.interfaces;

import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface IPaginableEntityRepository<T extends PaginableEntity> {
    Page<T> findAll(Pageable pageable);
}