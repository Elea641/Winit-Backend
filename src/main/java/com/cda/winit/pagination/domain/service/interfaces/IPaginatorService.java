package com.cda.winit.pagination.domain.service.interfaces;

import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IPaginatorService<T extends PaginableModel> {
    Optional<Page<? extends PaginableModel>> getPaginatedEntity(String entity, int pageIndex, int pageSize) throws Exception;
}