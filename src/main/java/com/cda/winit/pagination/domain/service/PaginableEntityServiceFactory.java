package com.cda.winit.pagination.domain.service;

import com.cda.winit.pagination.domain.service.interfaces.IPaginableEntityServiceFactory;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntityService;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import com.cda.winit.tournament.domain.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaginableEntityServiceFactory implements IPaginableEntityServiceFactory {

    private final Map<String, PaginableEntityService<? extends PaginableEntity, ? extends PaginableModel>> paginableEntityServices;

    @Autowired
    public PaginableEntityServiceFactory(TournamentService tournamentService) {
        this.paginableEntityServices = new HashMap<>();
        paginableEntityServices.put("tournament", tournamentService);
    }

    @Override
    public PaginableEntityService<? extends PaginableEntity, ? extends PaginableModel> getPaginableEntityService(String entityName) throws Exception {
        PaginableEntityService<? extends PaginableEntity, ? extends PaginableModel> service = paginableEntityServices.get(entityName);

        if (service == null) {
            throw new IllegalArgumentException("No service found for entityName: " + entityName);
        }

        try {
            return service;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}