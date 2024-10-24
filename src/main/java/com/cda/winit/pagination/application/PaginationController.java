package com.cda.winit.pagination.application;

import com.cda.winit.pagination.domain.service.interfaces.IPaginatorService;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/pagination")
@RequiredArgsConstructor
public class PaginationController {

    @Autowired
    private IPaginatorService paginatorService;

    @GetMapping(value = "/")
    public ResponseEntity<?> getPaginatedEntity(
            @RequestParam String entityName,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Optional<Page<PaginableEntity>> entitiesPaginated = paginatorService.getPaginatedEntity(entityName, pageIndex, pageSize);
            return ResponseEntity.ok(entitiesPaginated.get().getContent());
        } catch (Exception ex) {
            return ResponseEntity.ok(ex);
        }
    }
}