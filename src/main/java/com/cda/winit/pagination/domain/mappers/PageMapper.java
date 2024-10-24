package com.cda.winit.pagination.domain.mappers;

import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PageMapper<T extends PaginableEntity, R extends PaginableModel> {

    public Page<R> ToDTOPage(Page<T> pagedEntities, Function<T, R> mappingMethod) {
        List<R> content = pagedEntities.getContent().stream()
                .map(mappingMethod)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pagedEntities.getPageable(), pagedEntities.getTotalElements());
    }
}