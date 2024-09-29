package org.fantasymaps.backend.controllers;

import jakarta.servlet.http.HttpSession;
import org.fantasymaps.backend.dtos.MapDto;
import org.fantasymaps.backend.dtos.Role;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.fantasymaps.backend.services.MapService;
import org.fantasymaps.backend.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
public class MapController {
    private final MapService mapService;
    private final ModelMapper modelMapper;
    private final MapRepository mapRepository;
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);
    private final UserService userService;

    @Autowired
    public MapController(MapService mapService, ModelMapper modelMapper, MapRepository mapRepository, UserService userService) {
        this.mapService = mapService;
        this.modelMapper = modelMapper;
        this.mapRepository = mapRepository;
        this.userService = userService;
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
    public ResponseEntity<Set<MapDto>> getMaps(@RequestParam long page, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Set<MapDto> maps;
        if (user == null || !user.getRole().equals(Role.CUSTOMER))
            maps = mapService.getMaps(page, 20);
        else
            maps = mapService.getMaps(page, 20, user.getId());
        return ResponseEntity.ok(maps);
    }

    @PostMapping("/map/{id}/favorite")
    public ResponseEntity<Void> favoriteMap(@PathVariable int id, @RequestBody Map<String, Boolean> body, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        Boolean isFavorite = body.get("favorite");
        if (isFavorite == null)
            throw new IllegalArgumentException("Favorite status not provided");

        userService.favoriteMap(id, user.getId(), isFavorite);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/map/{id}")
    public ResponseEntity<MapDto> getMap(@PathVariable int id) {
        return ResponseEntity.ok(modelMapper.map(
                mapRepository.findById(id).orElseThrow(() -> new RuntimeException("Map not found")), MapDto.class)
        );
    }
}