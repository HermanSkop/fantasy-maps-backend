package org.fantasymaps.backend.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.NonNull;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.fantasymaps.backend.dtos.*;
import org.fantasymaps.backend.model.Tag;
import org.fantasymaps.backend.model.product.Bundle;
import org.fantasymaps.backend.model.product.Map;
import org.fantasymaps.backend.model.product.MapSize;
import org.fantasymaps.backend.model.product.Product;
import org.fantasymaps.backend.model.user.Customer;
import org.fantasymaps.backend.repositories.CategoryRepository;
import org.fantasymaps.backend.repositories.TagRepository;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.fantasymaps.backend.repositories.user.CreatorRepository;
import org.fantasymaps.backend.repositories.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MapService {
    private final MapRepository mapRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final CreatorRepository creatorRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final Logger logger = Logger.getLogger(MapService.class.getName());
    private final TagRepository tagRepository;

    @Autowired
    public MapService(MapRepository mapRepository, ModelMapper modelMapper, CategoryRepository categoryRepository,
                      CreatorRepository creatorRepository, UserService userService, UserRepository userRepository,
                      TagRepository tagRepository) {
        this.mapRepository = mapRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.creatorRepository = creatorRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }


    /**
     * Save map image to Firebase Storage and store map details in database
     *
     * @param createMapDto map details
     * @return map id
     * @throws IOException if file is empty or invalid
     */
    public int saveMap(CreateMapDto createMapDto, int creatorId) throws IOException, IllegalArgumentException {
        if (createMapDto == null)
            throw new IllegalArgumentException("Map details are empty");
        else if (createMapDto.getFile() == null || createMapDto.getFile().isEmpty())
            throw new IllegalArgumentException("File is empty");
        else if (!Objects.equals(createMapDto.getFile().getContentType(), "image/png") && !Objects.equals(createMapDto.getFile().getContentType(), "image/jpeg") && !Objects.equals(createMapDto.getFile().getContentType(), "image/jpg"))
            throw new IllegalArgumentException("Invalid file type");
        if (createMapDto.getSize() != null) {
            if (createMapDto.getSize().getWidthSquares() <= 0 || createMapDto.getSize().getHeightSquares() <= 0 || createMapDto.getSize().getSquareSideLength() <= 0)
                throw new IllegalArgumentException("Invalid map size");
        }

        String mapFileName = RandomStringUtils.randomAlphanumeric(10) + createMapDto.getFile().getContentType().replace("image/", ".");
        saveMapToFirebase(createMapDto.getFile(), "original/" + mapFileName);
        saveMapToFirebase(resizeImage(createMapDto.getFile(), mapFileName), "preview/" + mapFileName);

        return mapRepository.save(
                Map.builder()
                        .name(createMapDto.getName())
                        .url(mapFileName)
                        .dateCreated(LocalDate.now())
                        .creator(creatorRepository.findById(creatorId).orElseThrow())
                        .categories(new HashSet<>(categoryRepository.findAll()))
                        .price(createMapDto.getPrice())
                        .description(createMapDto.getDescription())
                        .size(createMapDto.getSize() != null ? modelMapper.map(createMapDto.getSize(), MapSize.class) : null)
                        .tags(createMapDto.getTags().stream()
                                .map(tagDto -> tagRepository.findByName(tagDto.getName())
                                        .orElse(org.fantasymaps.backend.model.Tag.builder().name(StringUtils.capitalize(tagDto.getName())).build()))
                                .collect(Collectors.toSet()))
                        .build()
        ).getId();
    }

    private MultipartFile resizeImage(@NonNull MultipartFile multipartFile, @NonNull String fileName) {
        File file = convertMultipartFileToFile(multipartFile, fileName);

        try {
            File outputFile = new File(file.getParent(), fileName);

            double scale = multipartFile.getContentType().equals("image/png") ? 0.8 : 0.5;
            Thumbnails.of(file)
                    .scale(scale)
                    .outputFormat(multipartFile.getContentType().replace("image/", ""))
                    .toFile(outputFile);

            return convertFileToMultipartFile(outputFile, multipartFile.getContentType());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error resizing image");
        }
    }

    private MultipartFile convertFileToMultipartFile(@NonNull File file, @NonNull String fileType) throws IOException {
        return new ConvertMultipartFile(file.toPath(), fileType);
    }

    private File convertMultipartFileToFile(@NonNull MultipartFile multipartFile, @NonNull String fileName) {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting multipart file to file");
        }
        return file;
    }

    private static void saveMapToFirebase(@NonNull MultipartFile file, @NonNull String fileName) throws IOException {
        InputStream content = file.getInputStream();
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(fileName, content, file.getContentType());
        blob.getMediaLink();
    }

    public static void deleteMapFromFirebase(@NonNull String fileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(fileName);
        if (blob == null)
            throw new IllegalArgumentException("File not found");
        if(!blob.delete())
            throw new IOException("Error while deleting file");
    }


    public Set<MapDto> getMaps(int userId, long page, long size, List<TagDto> tags) {
        if (page < 0 || size < 0)
            throw new IllegalArgumentException("Invalid page or size");
        List<Tag> tagList = getActualTags(tags).orElse(new ArrayList<>());

        return mapRepository.findAll().stream()
                .filter(map -> map.getTags().containsAll(tagList))
                .skip(page * size)
                .limit(size)
                .map(map -> modelMapper.map(map, MapDto.class))
                .peek(mapDto -> mapDto.setIsFavorite(userService.isFavorite(mapDto.getId(), userId)))
                .collect(Collectors.toSet());
    }

    private Optional<List<Tag>> getActualTags(List<TagDto> tags) {
        if (tags == null)
            throw new IllegalArgumentException("Tags cannot be null");
        List<Tag> tagList = tags.stream().map(tagDto -> modelMapper.map(tagDto, Tag.class)).toList();
        if (tagList.isEmpty())
            return Optional.empty();
        return Optional.of(tagList);
    }

    public Set<MapDto> getMapsReduced(long page, long size, List<TagDto> tags) {
        if (page < 0 || size < 0)
            throw new IllegalArgumentException("Invalid page or size");
        List<Tag> tagList = getActualTags(tags).orElse(new ArrayList<>());

        return mapRepository.findAll().stream()
                .filter(map -> map.getTags().containsAll(tagList))
                .skip(page * size)
                .limit(size)
                .map(map -> modelMapper.map(map, MapDto.class))
                .collect(Collectors.toSet());
    }

    public MapDetailsDto getMapDetails(int mapId, int userId) {
        MapDetailsDto mapDetails = modelMapper.map(mapRepository.findById(mapId).orElseThrow(), MapDetailsDto.class);
        if (userService.getUserById(userId).getRole().equals(Role.CUSTOMER))
            mapDetails.setIsFavorite(userService.isFavorite(mapId, userId));
        return mapDetails;
    }

    public MapDetailsDto getMapDetails(int mapId) {
        return modelMapper.map(mapRepository.findById(mapId).orElseThrow(), MapDetailsDto.class);
    }

    public Set<ManageMapItemDto> getManageMapsByCreator(int creatorId, long page, int size) {
        if (page < 0 || size < 0)
            throw new IllegalArgumentException("Invalid page or size");
        return mapRepository.findAllByCreatorId(creatorId).stream()
                .skip(page * size)
                .limit(size)
                .map(map -> modelMapper.map(map, ManageMapItemDto.class))
                .collect(Collectors.toSet());
    }

    public void deleteMap(int mapId, int creatorId) throws IOException {
        Map map = mapRepository.findById(mapId).orElseThrow();
        if (map.getCreator().getId() != creatorId)
            throw new IllegalArgumentException("Unauthorized");
        deleteMapFromFirebase("original/" + map.getUrl());
        deleteMapFromFirebase("preview/" + map.getUrl());

        mapRepository.delete(map);
    }

    public Set<MapDto> getFavoriteMaps(int userId, long page, int size, List<TagDto> tags) {
        if (page < 0 || size < 0)
            throw new IllegalArgumentException("Invalid page or size");
        try {
            Customer customer = (Customer) userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<Tag> tagList = getActualTags(tags).orElse(new ArrayList<>());
            return mapRepository.findAllByFavoredCustomers(Set.of(customer)).stream()
                    .filter(map -> map.getTags().containsAll(tagList))
                    .skip(page * size)
                    .limit(size)
                    .map(map -> modelMapper.map(map, MapDto.class))
                    .peek(map -> map.setIsFavorite(true))
                    .collect(Collectors.toSet());
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("User is not a customer");
        }

    }

    public Set<TagDto> getTags() {
        return tagRepository.findAll().stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toSet());
    }

    public Set<MapDto> getBundledMaps(int mapId, long page, int size) {
        if (page < 0 || size < 0)
            throw new IllegalArgumentException("Invalid page or size");
        Set<Bundle> bundles = mapRepository.findById(mapId).orElseThrow(() -> new IllegalArgumentException("Map not found")).getBundles();

        if (bundles.isEmpty()) {
            return Collections.emptySet();
        }

        return bundles.stream()
                .flatMap(bundle -> bundle.getMaps().stream())
                .distinct()
                .filter(map -> map.getId() != mapId)
                .skip(page * size)
                .limit(size)
                .map(map -> modelMapper.map(map, MapDto.class))
                .collect(Collectors.toSet());
    }
}
