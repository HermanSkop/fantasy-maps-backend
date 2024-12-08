package org.fantasymaps.backend.services;

import org.fantasymaps.backend.dtos.CreateBundleDto;
import org.fantasymaps.backend.dtos.ManageBundleItemDto;
import org.fantasymaps.backend.model.product.Bundle;
import org.fantasymaps.backend.model.product.Map;
import org.fantasymaps.backend.repositories.product.BundleRepository;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BundleService {
    private final BundleRepository bundleRepository;
    private final MapRepository mapRepository;
    private final ModelMapper modelMapper;

    public BundleService(BundleRepository bundleRepository, ModelMapper modelMapper, MapRepository mapRepository) {
        this.bundleRepository = bundleRepository;
        this.modelMapper = modelMapper;
        this.mapRepository = mapRepository;
    }


    public Set<ManageBundleItemDto> getManageBundlesByCreator(int creatorId, long page, long pageSize) {
        if (page < 0 || pageSize < 0) throw new IllegalArgumentException("Page and page size must be positive");
        return bundleRepository.findByCreatorId(creatorId)
                .stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .map(bundle -> {
                    ManageBundleItemDto bundleDto = modelMapper.map(bundle, ManageBundleItemDto.class);
                    bundleDto.setCoverMapsUrls(bundle.getMaps().stream().limit(5).map(Map::getUrl).collect(Collectors.toSet()));
                    return bundleDto;
                })
                .collect(Collectors.toSet());
    }

    public int createBundle(CreateBundleDto bundleDto) {
        if (bundleDto.getMaps()==null || bundleDto.getMaps().isEmpty())
            throw new IllegalArgumentException("Bundle must contain at least one map");
        bundleDto.getMaps().forEach(mapId -> {
            if (mapId == null) throw new IllegalArgumentException("Map id cannot be null");
        });
        if (bundleDto.getCreatorId() == null) throw new IllegalArgumentException("Creator id cannot be null");

        Set<Integer> maps = mapRepository.findAllByCreatorId(bundleDto.getCreatorId()).stream().map(Map::getId).collect(Collectors.toSet());
        if (!maps.containsAll(bundleDto.getMaps())) throw new IllegalArgumentException("Invalid map id is provided");

        Bundle bundle = modelMapper.map(bundleDto, Bundle.class);
        bundle.setMaps(mapRepository.findAllByIdIn(bundleDto.getMaps()));
        bundle.setDateCreated(LocalDate.now());

        return bundleRepository.save(bundle).getId();
    }

    public void deleteBundle(int bundleId, int creatorId) {
        Bundle bundle = bundleRepository.findById(bundleId).orElseThrow();
        if (bundle.getCreator().getId() != creatorId) throw new IllegalArgumentException("Unauthorized");
        bundleRepository.delete(bundle);
    }
}
