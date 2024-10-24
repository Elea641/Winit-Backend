package com.cda.winit.pagination.domain.service;

import com.cda.winit.pagination.domain.service.interfaces.IPaginableEntityServiceFactory;
import com.cda.winit.pagination.domain.service.interfaces.IPaginatorService;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntityService;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaginatorService<T extends PaginableEntity, R extends PaginableModel> implements IPaginatorService<R> {

    private final IPaginableEntityServiceFactory paginableEntityServiceFactory;

    @Override
    public Optional<Page<? extends PaginableModel>> getPaginatedEntity(String entityName, int pageIndex, int pageSize) throws Exception {
        PaginableEntityService<? extends PaginableEntity, ? extends PaginableModel> paginableEntityService = paginableEntityServiceFactory.getPaginableEntityService(entityName);

        if (paginableEntityService != null) {
            return Optional.of(paginableEntityService.getAllPaginated(PageRequest.of(pageIndex, pageSize)));
        }
        return Optional.empty();
    }
}