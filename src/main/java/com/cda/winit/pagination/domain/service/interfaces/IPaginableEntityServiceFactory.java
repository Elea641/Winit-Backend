package com.cda.winit.pagination.domain.service.interfaces;

import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntityService;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;

public interface IPaginableEntityServiceFactory {

    PaginableEntityService<? extends PaginableEntity, ? extends PaginableModel> getPaginableEntityService(String entityName) throws Exception;
}