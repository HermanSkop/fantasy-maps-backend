package org.fantasymaps.backend.controllers;

import jakarta.servlet.http.HttpSession;
import org.fantasymaps.backend.dtos.CreateBundleDto;
import org.fantasymaps.backend.dtos.ManageBundleItemDto;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.services.BundleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.logging.Logger;

import static org.fantasymaps.backend.config.AppConfig.pageSize;

@CrossOrigin
@RestController
public class BundleController {

    private final BundleService bundleService;
    private final Logger logger = Logger.getLogger(BundleController.class.getName());
    private final HttpSession session;

    @Autowired
    public BundleController(BundleService bundleService, HttpSession session) {
        this.bundleService = bundleService;
        this.session = session;
    }

    @GetMapping("/bundles/manage/creator/{id}")
    public ResponseEntity<Set<ManageBundleItemDto>> getManageBundlesByCreator(@PathVariable int id, @RequestParam long page) {
        logger.info(bundleService.getManageBundlesByCreator(id, page, pageSize).toString());
        return ResponseEntity.ok(bundleService.getManageBundlesByCreator(id, page, pageSize));
    }

    @PostMapping("/bundle")
    public ResponseEntity<ManageBundleItemDto> createBundle(@RequestBody CreateBundleDto bundleDto) {
        UserDto user = (UserDto) session.getAttribute("user");
        logger.info("Maps: " + bundleDto.getMaps().size());
        try {
            bundleDto.setCreatorId(user.getId());
            return ResponseEntity.ok(bundleService.createBundle(bundleDto));
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
