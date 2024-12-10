package org.fantasymaps.backend.controllers;

import org.fantasymaps.backend.dtos.TagDto;
import org.fantasymaps.backend.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@CrossOrigin
@RestController
public class TagController {
    private final MapService mapService;

    @Autowired
    public TagController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/tags")
    public ResponseEntity<Set<TagDto>> getTags() {
        return ResponseEntity.ok(mapService.getTags());

    }
}