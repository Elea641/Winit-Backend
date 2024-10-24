package com.cda.winit.pagination.shared.interfaces;

import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import org.springframework.stereotype.Component;

@Component
public interface IPaginableEntityToModelMappers<T extends PaginableEntity, R extends PaginableModel> {
    R ToDto(T entity);
}