package org.fantasymaps.backend.controllers;

import jakarta.servlet.http.HttpSession;
import org.fantasymaps.backend.dtos.MapDto;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.fantasymaps.backend.services.MapService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@CrossOrigin
@RestController
public class MapController {
    private final MapService mapService;
    private final ModelMapper modelMapper;
    private final MapRepository mapRepository;
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

    @Autowired
    public MapController(MapService mapService, ModelMapper modelMapper, MapRepository mapRepository) {
        this.mapService = mapService;
        this.modelMapper = modelMapper;
        this.mapRepository = mapRepository;
    }

    @PostMapping("/map")
    public ResponseEntity<MapDto> uploadMap(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        UserDto user = (UserDto) session.getAttribute("user");
        return ResponseEntity.ok(modelMapper.map(
                mapRepository.findById(
                        mapService.saveMap(file, user.getId())
                ), MapDto.class)
        );
    }

    @GetMapping("/maps")
    public ResponseEntity<Set<MapDto>> getMaps(@RequestParam long page) {
        Set<MapDto> maps = mapService.getMaps(page, 20);
        return ResponseEntity.ok(maps);
    }

    @PostMapping("/maps")
    public ResponseEntity<MapDto> createMaps(@RequestParam("files") MultipartFile[] files, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Arrays.stream(files).forEach(file -> {
            try {
                modelMapper.map(
                        mapRepository.findById(
                                mapService.saveMap(file, user.getId())
                        ), MapDto.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok().build();
    }
}