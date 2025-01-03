package org.fantasymaps.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.fantasymaps.backend.dtos.*;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.fantasymaps.backend.services.BundleService;
import org.fantasymaps.backend.services.MapService;
import org.fantasymaps.backend.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fantasymaps.backend.config.AppConfig.pageSize;

@CrossOrigin
@RestController
public class MapController {
    private final MapService mapService;
    private final ModelMapper modelMapper;
    private final MapRepository mapRepository;
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final BundleService bundleService;


    @Autowired
    public MapController(MapService mapService, ModelMapper modelMapper, MapRepository mapRepository, UserService userService,
                         ObjectMapper objectMapper, BundleService bundleService) {
        this.mapService = mapService;
        this.modelMapper = modelMapper;
        this.mapRepository = mapRepository;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.bundleService = bundleService;
    }

    @DeleteMapping("/map/{id}")
    public ResponseEntity<Void> deleteMap(@PathVariable int id, HttpSession session) throws IOException {
        UserDto user = (UserDto) session.getAttribute("user");
        bundleService.deleteMapFromBundles(id);
        mapService.deleteMap(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/map", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MapDto> uploadMap(@RequestPart("file") MultipartFile map, @RequestPart("map") CreateMapDto createMapDto, HttpSession session) throws IOException {
        UserDto user = (UserDto) session.getAttribute("user");
        createMapDto.setFile(map);
        return ResponseEntity.ok(
                modelMapper.map(mapRepository.findById(mapService.saveMap(createMapDto, user.getId())), MapDto.class)
        );
    }

    @GetMapping("/map/{id}")
    public ResponseEntity<MapDetailsDto> getMap(@PathVariable int id, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null)
            return ResponseEntity.ok(mapService.getMapDetails(id));
        else
            return ResponseEntity.ok(mapService.getMapDetails(id, user.getId()));
    }

    @GetMapping("/maps")
    public ResponseEntity<Set<MapDto>> getMaps(@RequestParam long page, @RequestParam String tags, HttpSession session) throws JsonProcessingException {
        List<TagDto> filterTags = objectMapper.readValue(tags, objectMapper.getTypeFactory().constructCollectionType(List.class, TagDto.class));

        UserDto user = (UserDto) session.getAttribute("user");
        Set<MapDto> maps;

        if (user == null || !user.getRole().equals(Role.CUSTOMER))
            maps = mapService.getMapsReduced(page, pageSize, filterTags);
        else
            maps = mapService.getMaps(user.getId(), page, pageSize, filterTags);
        return ResponseEntity.ok(maps);
    }

    @GetMapping("/maps/favorite")
    public ResponseEntity<Set<MapDto>> getFavoriteMaps(@RequestParam long page, @RequestParam String tags, HttpSession session) throws JsonProcessingException {
        UserDto user = (UserDto) session.getAttribute("user");
        List<TagDto> filterTags = objectMapper.readValue(tags, objectMapper.getTypeFactory().constructCollectionType(List.class, TagDto.class));
        return ResponseEntity.ok(mapService.getFavoriteMaps(user.getId(), page, pageSize, filterTags));
    }

    @GetMapping("/maps/bundled/{mapId}")
    public ResponseEntity<Set<MapDto>> getBundledMaps(@PathVariable int mapId, @RequestParam long page) {
        return ResponseEntity.ok(mapService.getBundledMaps(mapId, page, pageSize));
    }

    @GetMapping("/maps/manage/creator/{id}")
    public ResponseEntity<Set<ManageMapItemDto>> getManageMapsByCreator(@PathVariable int id, @RequestParam long page) {
        return ResponseEntity.ok(mapService.getManageMapsByCreator(id, page, pageSize));
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
}