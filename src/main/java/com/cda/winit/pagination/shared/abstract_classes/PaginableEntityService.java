package com.cda.winit.pagination.shared.abstract_classes;

import com.cda.winit.pagination.domain.mappers.PageMapper;
import com.cda.winit.pagination.shared.interfaces.IPaginableEntityRepository;
import com.cda.winit.pagination.shared.interfaces.IPaginableEntityToModelMappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public abstract class PaginableEntityService<T extends PaginableEntity, R extends PaginableModel> {

    private final IPaginableEntityRepository<T> repository;
    private final PageMapper<T, R> pageMapper;
    private final IPaginableEntityToModelMappers<T, R> paginableEntityMapper;

    protected PaginableEntityService(IPaginableEntityRepository<T> repository, PageMapper<T, R> pageMapper, IPaginableEntityToModelMappers<T, R> paginableEntityMapper) {
        this.repository = repository;
        this.pageMapper = pageMapper;
        this.paginableEntityMapper = paginableEntityMapper;
    }

    public Page<R> getAllPaginated(Pageable pageable) {
        Page<T> pagedEntities = this.repository.findAll(pageable);

        if (!pagedEntities.isEmpty()) {
            Function<T, R> mappingFunction = paginableEntityMapper::ToDto;
            return pageMapper.ToDTOPage(pagedEntities, mappingFunction);
        }

        return Page.empty();
    }
}