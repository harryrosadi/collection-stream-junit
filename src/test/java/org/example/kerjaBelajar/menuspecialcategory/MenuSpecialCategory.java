package org.example.kerjaBelajar.menuspecialcategory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class MenuSpecialCategory {
/**
    @Override
    public List<MenuSpecialCategory> getByGroupId(List<Long> groupId) throws ServiceException {
        List<MenuSpecialCategory> menuSpecialCategories = new ArrayList<>();
        try {
            if (!groupId.isEmpty()) {
                menuSpecialCategories = menuSpecialCategoryDAO.findAllByCategoryGroupIdIn(groupId);
            }
            if (menuSpecialCategories.isEmpty()) {
                throw new ServiceException("Menu Special Category not found with group id " + groupId);
            }
        } catch (DAOException e) {
            log.error("Failed to retrieve list groupId %s", e);
        }
        return menuSpecialCategories;
    }

    @Override
    public List<MenuSpecialCategory> getByGroupCode(User user, List<String> groupCodes, String activeStatus, Boolean showGeneratedIcons, boolean showMap) throws ServiceException {
        List<MenuSpecialCategory> menuSpecialCategories;
        List<MenuSpecialCategory> listMenu = new ArrayList<>();
        String code2 = "";

        List<String> datas = Stream.of(String.valueOf(categoryLimit).split("\\|"))
                .map(String::trim)
                .collect(Collectors.toList());

        try {
            if (!CollectionUtils.sizeIsEmpty(groupCodes)) {
                if ("All".equalsIgnoreCase(activeStatus)) {
                    menuSpecialCategories = menuSpecialCategoryDAO.findAllByCategoryGroupCodeInAll(groupCodes);
                    listMenu.addAll(menuSpecialCategories);
                } else {
                    menuSpecialCategories = menuSpecialCategoryDAO.findAllByCategoryGroupCodeInOrderByOrder(groupCodes);
                    listMenu.addAll(menuSpecialCategories);
                }
                for (String codes : groupCodes) {
                    if (showGeneratedIcons) {
                        if (ECategoryType.MENU_HIGHLIGHT.toString().equalsIgnoreCase(codes)) {
                            ResultDTO<List<ResponseTotalCountSpecialCategoryDto>> responseRedisData = masterUserClient.getTotalCountSpecialCategory();
                            List<ResponseTotalCountSpecialCategoryDto> redisDataResult = responseRedisData.getResult();
                            redisDataResult.sort(Comparator.comparing(ResponseTotalCountSpecialCategoryDto::getCount).reversed());

                            List<Long> listId = redisDataResult.stream().map(ResponseTotalCountSpecialCategoryDto::getLinkId).collect(Collectors.toList());

                            Pageable pageable = PageRequest.of(0, 2000);
                            List<MenuSpecialCategory> specialCategoriesByIdList = menuSpecialCategoryDAO.findAllByIdIn(listId, pageable);

                            specialCategoriesByIdList.forEach(redisResult -> listMenu.add(redisMapper.convert(redisResult)));
                        }
                        if (String.valueOf(ECategoryType.PHARMACY_HIGHLIGHT).equalsIgnoreCase(codes)) {
                            code2 = codes;
                            ResultDTO<List<ResponseSubCategoryDTO>> allCategory3 = masterItemAPIClient.findAllCategory3();
                            List<ResponseSubCategoryDTO> resultCategory3 = allCategory3.getResult();
                            listMenu.addAll(category3Mapper.mapEntitiesIntoDTOs(resultCategory3));
                        }
                        if (datas.get(0).equals(codes) && menuSpecialCategories.size() < parseInt(String.valueOf(datas.get(1)))) {
                            menuSpecialCategories = menuSpecialCategoryDAO.findAllByCategoryGroupCodeInOrderByOrder(Collections.singletonList("LAB_ITEM_GROUP"));
                            listMenu.addAll(menuSpecialCategories);
                        }
                    }
                }
                if (!showMap || String.valueOf(ECategoryType.PHARMACY_HIGHLIGHT).equalsIgnoreCase(code2))
                    return listMenu;

                List<String> targetIdCodes = listMenu.stream()
                        .filter(x -> x.getTargetPageId().getIsMapped())
                        .map(x -> x.getTargetPageId().getCode()
                        ).collect(Collectors.toList());

                if (!targetIdCodes.isEmpty()) {
                    Map<String, TargetPageMap> targetPageMaps = targetPageMapDao.findByTargetPageIn(targetIdCodes)
                            .stream().collect(Collectors.toMap(TargetPageMap::getTargetPage, x -> x));

                    if (!targetPageMaps.isEmpty()) {
                        Map<String, MenuSpecialCategoryTargetPage> menuSpecialCategoryTargetPages = menuSpecialCategoryTargetPageDao
                                .findByCodeIn(
                                        targetPageMaps.values()
                                                .stream().map(TargetPageMap::getActualTargetPage)
                                                .collect(Collectors.toList()))
                                .stream().collect(Collectors.toMap(MenuSpecialCategoryTargetPage::getCode, x -> x));

                        listMenu.forEach(x -> {
                            if (x.getTargetPageId().getIsMapped()) {
                                TargetPageMap targetPageMap = targetPageMaps.get(x.getTargetPageId().getCode());
                                if (Objects.nonNull(targetPageMap)) {
                                    x.setTarget(targetPageMap.getTarget());
                                    x.setCode(targetPageMap.getActualTargetPage());
                                    MenuSpecialCategoryTargetPage menuSpecialCategoryTargetPage =
                                            menuSpecialCategoryTargetPages.get(targetPageMap.getActualTargetPage());
                                    if (Objects.nonNull(menuSpecialCategoryTargetPage)) {
                                        x.setTargetPageId(menuSpecialCategoryTargetPage);
                                    }
                                }
                            }
                        });
                    }
                }
            } else {
                throw new ServiceException("Menu Special Category not found with groupCodes " + groupCodes);
            }

        } catch (DAOException e) {
            log.error("Failed to retrieve list groupCodes %s", e);
        }

        if (listMenu.stream().filter(
                l -> Objects.equals(l.getCategoryGroup().getCode(), ECategoryType.LAB_HIGHLIGHT.toString())
        ).count() < Integer.parseInt(datas.get(1).replace(']', ' ').trim())
                && groupCodes.contains(ECategoryType.LAB_HIGHLIGHT.toString())) {
            listMenu.addAll(menuSpecialCategoryDAO.findAll(searchByCode("LAB_ITEM_GROUP"))
                    .stream().sorted(Comparator.comparingInt(MenuSpecialCategory::getOrder)).collect(Collectors.toList()));
        }

        return listMenu;
    }

    private Specification<MenuSpecialCategory> searchByCode(String itemGroupCode) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("categoryGroup").get("code"), itemGroupCode));
    }

    @Transactional
    @Override
    public MenuSpecialCategory updateSpecialCategory(RequestUpdateSpecialCategoryDTO request, User user)
            throws ServiceException, DAOException {

        MenuSpecialCategory category = menuSpecialCategoryDAO.findById(request.getId())
                .stream().findAny().orElseThrow(() -> new ServiceException(
                        "menu special category with id " + request.getId()));

        MenuSpecialCategoryGroup categoryGroup = categoryGroupDAO.findByCode(request.getGroupCode());
        MenuSpecialCategory newCategory;
        if (categoryGroup == null || !categoryGroup.getCode().equals(request.getGroupCode())) {

            categoryGroup = categoryGroupMapper.convert(request);
            if (categoryGroup != null) {
                categoryGroup.setCreatedBy(user.getId());
                categoryGroup.setCreatedOn(new Date());
                MenuSpecialCategoryGroup saveCategoryGroup = categoryGroupDAO.save(categoryGroup);
                newCategory = categoryMapper.convert(request);
                if (newCategory != null) {
                    newCategory.setCategoryGroup(saveCategoryGroup);
                    newCategory.setCreatedBy(user.getId());
                    newCategory.setCreatedOn(new Date());
                    newCategory.setOrder(category.getOrder());
                    menuSpecialCategoryDAO.save(newCategory);
                    log.info("you just added new menu special category group and new menu special category");
                }
                return category;
            }
        } else if (Objects.equals(request.getId(), category.getId())) {
            newCategory = categoryMapper.convert(request);
            assert newCategory != null;
            newCategory.setCreatedOn(category.getCreatedOn());
            newCategory.setOrder(category.getOrder());
            newCategory.setCode(category.getCode());
            newCategory.setCreatedBy(category.getCreatedBy());
            newCategory.setModifiedBy(user.getId());
            newCategory.setCategoryGroup(categoryGroup);
            menuSpecialCategoryDAO.save(newCategory);
            log.info("you just updated menu special category id {}.", category.getId());

        }
        return category;
    }

    @Transactional
    @Override
    public MenuSpecialCategory addMenuCategory(RequestMenuSpecialCategoryDto request, User user) throws DAOException, ServiceException {

        MenuSpecialCategoryGroup categoryGroup = categoryGroupDAO.findByCode(request.getGroupCode());

        List<MenuSpecialCategory> menuSpecialCategories;

        menuSpecialCategories = menuSpecialCategoryDAO.findAllByCategoryGroupCodeInAll(Collections.singletonList(request.getGroupCode()));

        List<String> datas = Stream.of(String.valueOf(categoryLimit).split("\\|"))
                .map(String::trim)
                .collect(Collectors.toList());

        if (menuSpecialCategories.size() >= parseInt(datas.get(1)) && request.getGroupCode().equals(datas.get(0))) {
            throw new ServiceException("special category group code "+ datas.get(0) +" is limited "+ datas.get(1));
        }

        MenuSpecialCategory category = null;
        if (categoryGroup == null || !categoryGroup.getCode().equals(request.getGroupCode())) {
            categoryGroup = addCategoryGroupMapper.convert(request);
            if (categoryGroup != null) {
                categoryGroup.setCreatedBy(user.getId());
                MenuSpecialCategoryGroup saveCategoryGroup = categoryGroupDAO.save(categoryGroup);
                category = addCategoryMapper.convert(request);
                if (category != null) {
                    category.setCreatedBy(user.getId());
                    category.setCategoryGroup(saveCategoryGroup);
                    Integer maxOrder;
                    maxOrder = menuSpecialCategoryDAO.findMaxOrder(category.getCategoryGroup());
                    if (maxOrder == null) {
                        category.setOrder(1);
                    } else if (category.getOrder() == null && category.getOrder() > maxOrder + 1) {
                        category.setOrder(maxOrder + 1);
                    } else if (category.getOrder() == null && category.getOrder() < 1) {
                        category.setOrder(1);
                    }
                    menuSpecialCategoryDAO.save(category);
                    try {
                        menuSpecialCategoryDAO.updateOrderInc(category.getOrder(), category.getCategoryGroup(), category.getId());
                    } catch (DAOException e) {
                        throw new ServiceException("Banner update order failed. " + e.getMessage());
                    }
                }
            }
            log.info("insert new menu special category group and add new to menu special category");

        } else if (request.getGroupCode().equals(categoryGroup.getCode())) {
            category = addCategoryMapper.convert(request);
            assert category != null;
            category.setCreatedBy(user.getId());
            category.setCategoryGroup(categoryGroup);
            Integer maxOrder;
            maxOrder = menuSpecialCategoryDAO.findMaxOrder(category.getCategoryGroup());
            if (maxOrder == null) {
                category.setOrder(1);
            } else if (category.getOrder() > maxOrder + 1) {
                category.setOrder(maxOrder + 1);
            } else if (category.getOrder() < 1) {
                category.setOrder(1);
            }
            menuSpecialCategoryDAO.save(category);
            try {
                menuSpecialCategoryDAO.updateOrderInc(category.getOrder(), category.getCategoryGroup(), category.getId());
            } catch (DAOException e) {
                throw new ServiceException("Banner update order failed. " + e.getMessage());
            }
            log.info("special category group code is already exist, add new one to menu special category");
        }
        return category;
    }

    @Transactional
    @Override
    public List<MenuSpecialCategory> groupOrder(Long id, Integer newOrder) throws ServiceException {
        MenuSpecialCategory category = menuSpecialCategoryDAO.findById(id)
                .stream().findAny().orElseThrow(() -> new ServiceException("menu special category with id " + id));

        if (Objects.nonNull(category)) {
            try {
                Integer existingOrder = menuSpecialCategoryDAO.findMaxOrder(category.getCategoryGroup());
                if (newOrder < 1) {
                    newOrder = 1;
                } else if (newOrder > existingOrder) {
                    newOrder = existingOrder;
                }
                if (category.getOrder() > newOrder) {
                    menuSpecialCategoryDAO.updateOrderIncBetween(category.getOrder(), category.getCategoryGroup(), id, newOrder);
                } else if (category.getOrder() < newOrder) {
                    menuSpecialCategoryDAO.updateOrderDecBetween(category.getOrder(), category.getCategoryGroup(), id, newOrder);
                }
                category.setOrder(newOrder);
                menuSpecialCategoryDAO.save(category);
                log.info("Success Update Order");
            } catch (DAOException e) {
                throw new ServiceException("Update order banner failed. " + e.getMessage());
            }
        } else {
            log.warn("Menu Special Category not found with id {}", id);
            throw new ServiceException("Menu Special Category not found with id " + id);
        }
        return menuSpecialCategoryDAO.findAll();
    }

    @Transactional
    @Override
    public Boolean deleteCategories(List<Long> id, User user) throws ServiceException {
        try {
            List<MenuSpecialCategory> categories = menuSpecialCategoryDAO.findAllByIdIn(id);
            if (!categories.isEmpty()) {
                for (MenuSpecialCategory category : categories) {
                    menuSpecialCategoryDAO.updateOrderDec(category.getOrder(), category.getCategoryGroup(), category.getId());
                    menuSpecialCategoryDAO.deleteAll(categories);
                }
                return true;
            } else {
                return false;
            }
        } catch (DAOException e) {
            throw new ServiceException("Failed get data");
        }
    }

    private final IMenuSpecialCategoryDAO menuSpecialCategoryDAO;
    private final IMenuSpecialCategoryAllDAO menuSpecialCategoryAllDAO;
    private final IMenuSpecialCategoryGroupDAO categoryGroupDAO;
    private final MenuSpecialCategoryUpdateMapper categoryMapper;
    private final MenuSpecialCategoryMapper addCategoryMapper;
    private final MenuSpecialCategoryGroupMapper categoryGroupMapper;
    private final AddMenuSpecialCategoryGroupMapper addCategoryGroupMapper;
    private final IMasterUserClient masterUserClient;
    private final RedisMenuSpecialCategoryMapper redisMapper;
    private final IMasterItemAPIClient masterItemAPIClient;
    private final MenuCategory3Mapper category3Mapper;
    private final UploadImage uploadImage;
    private final CdnImage cdnImage;
    private final ITargetPageMapDao targetPageMapDao;
    private final IMenuSpecialCategoryTargetPageDao menuSpecialCategoryTargetPageDao;


    @Value("${config.profile.file-extension:jpg, jpeg, png, gif}")
    private List<String> allowedFileExtensions;

    @Value("${config.profile.picture-size:1000000}")
    private long maxFileSize;

    @Value("${config.picture.allowed-dimensions-ratio:148,343.0,1;9,17.0,1;1,1.0,0.1}")
    private String allowedDimensionsRatio;

    @Value("${config.category-limit:LAB_HIGHLIGHT|5}")
    private List<String> categoryLimit;


    @Override
    public List<MenuSpecialCategory> getAllSpecialCategory(User user, String activeStatus, boolean showMap) {
        List<MenuSpecialCategory> menuSpecialCategories = new ArrayList<>();
        try {
            if ("All".equalsIgnoreCase(activeStatus)) {
                menuSpecialCategories = menuSpecialCategoryDAO.findAll();
            } else {
                menuSpecialCategories = menuSpecialCategoryDAO.findAllByRowStatusOrderByOrder();
            }

            if (!showMap) return menuSpecialCategories;

            List<String> targetIdCodes = menuSpecialCategories.stream()
                    .filter(x -> x.getTargetPageId().getIsMapped())
                    .map(x -> x.getTargetPageId().getCode()
                    ).collect(Collectors.toList());

            if (!targetIdCodes.isEmpty()) {
                Map<String, TargetPageMap> targetPageMaps = targetPageMapDao.findByTargetPageIn(targetIdCodes)
                        .stream().collect(Collectors.toMap(TargetPageMap::getTargetPage, x -> x));

                if (!targetPageMaps.isEmpty()) {
                    Map<String, MenuSpecialCategoryTargetPage> menuSpecialCategoryTargetPages = menuSpecialCategoryTargetPageDao
                            .findByCodeIn(
                                    targetPageMaps.values()
                                            .stream().map(TargetPageMap::getActualTargetPage)
                                            .collect(Collectors.toList()))
                            .stream().collect(Collectors.toMap(MenuSpecialCategoryTargetPage::getCode, x -> x));

                    menuSpecialCategories.forEach(x -> {
                        if (x.getTargetPageId().getIsMapped()) {
                            TargetPageMap targetPageMap = targetPageMaps.get(x.getTargetPageId().getCode());
                            if (Objects.nonNull(targetPageMap)) {
                                x.setTarget(targetPageMap.getTarget());
                                x.setCode(targetPageMap.getActualTargetPage());
                                MenuSpecialCategoryTargetPage menuSpecialCategoryTargetPage =
                                        menuSpecialCategoryTargetPages.get(targetPageMap.getActualTargetPage());
                                if (Objects.nonNull(menuSpecialCategoryTargetPage)) {
                                    x.setTargetPageId(menuSpecialCategoryTargetPage);
                                }
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error("Failed to retrieve list groupId", e);
        }
        return menuSpecialCategories;
    }

    @Transactional
    @Override
    public boolean undeleteCategories(List<Long> ids, User user) throws ServiceException {
        try {
            List<MenuSpecialCategoryAll> categories = menuSpecialCategoryAllDAO.findAllByIdDeleted(ids);
            if (!categories.isEmpty()) {
                for (MenuSpecialCategoryAll category : categories) {
                    menuSpecialCategoryAllDAO.undeleteCategory(category.getId());
                    menuSpecialCategoryAllDAO.updateOrderInc(category.getOrder(), category.getCategoryGroup(), category.getId());
                }
                return true;
            } else {
                return false;
            }
        } catch (DAOException e) {
            throw new ServiceException("Failed to get data");
        }
    }

    @Transactional
    @Override
    public ResponseAddImageDto addImageSpecialCategory(MultipartFile mainImage, MultipartFile favoriteImage, Long id) throws ServiceException, IOException {

        ResponseAddImageDto result = new ResponseAddImageDto();
        if (Objects.nonNull(mainImage)) {
            if (mainImage.getSize() > maxFileSize) {
                log.warn("File size exceed maximum file size. File size : " + maxFileSize);
                throw new ServiceException("File size can't exceed " + maxFileSize);
            }

            if (!imageValidated(mainImage))
                throw new ServiceException("Inappropriate Image Width and Height");

            uploadImage.getFileExtension(mainImage.getOriginalFilename() == null ? mainImage.getName() : mainImage.getOriginalFilename(), allowedFileExtensions);
        }

        if (Objects.nonNull(favoriteImage)) {
            if (favoriteImage.getSize() > maxFileSize) {
                log.warn("File size exceed maximum file size. File size : " + maxFileSize);
                throw new ServiceException("File size can't exceed " + maxFileSize);
            }
            uploadImage.getFileExtension(favoriteImage.getOriginalFilename() == null ? favoriteImage.getName() : favoriteImage.getOriginalFilename(), allowedFileExtensions);
        }

        MenuSpecialCategory menuSpecialCategory;
        Optional<MenuSpecialCategory> optionalImage = menuSpecialCategoryDAO.findById(id);

        if (optionalImage.isPresent()) {
            menuSpecialCategory = optionalImage.get();
        } else {
            log.warn("Banner not found with id {}", id);
            throw new ServiceException("Banner not found with id " + id);
        }

        String path = "";
        try {
            if (Objects.nonNull(mainImage)) {
                cdnImage.getUrl(menuSpecialCategory.getMainImageUrl(), CdnImage.Size.SQUARE_SMALL);
                path = "images/specialCategory/mainImage/" + menuSpecialCategory.getTargetPageId().getId() + menuSpecialCategory.getTargetPageId().getTargetType() + "/" + System.currentTimeMillis() + "-" + mainImage.getOriginalFilename();
                uploadImage.saveImage(mainImage.getBytes(), path);
                menuSpecialCategory.setMainImageUrl(path);
                result.setMainImageUrl(cdnImage.getUrl(path));
            }
            if (Objects.nonNull(favoriteImage)) {
                cdnImage.getUrl(menuSpecialCategory.getFavoriteImageUrl(), CdnImage.Size.SQUARE_SMALL);
                path = "images/specialCategory/favoriteImage/" + menuSpecialCategory.getTargetPageId().getId() + menuSpecialCategory.getTargetPageId().getTargetType() + "/" + System.currentTimeMillis() + "-" + favoriteImage.getOriginalFilename();
                uploadImage.saveImage(favoriteImage.getBytes(), path);
                menuSpecialCategory.setFavoriteImageUrl(path);
                result.setFavoriteImageUrl(cdnImage.getUrl(path));
            }
        } catch (IOException e) {
            throw new ServiceException("SpecialCategory upload failed. " + e.getMessage());
        }
        menuSpecialCategoryDAO.save(menuSpecialCategory);
        return result;

    }

    private boolean imageValidated(MultipartFile multipartFile) throws IOException {
        BufferedImage image = ImageIO.read(
                new ByteArrayInputStream(multipartFile.getBytes())
        );

        return Arrays.stream(allowedDimensionsRatio.split(";")).anyMatch(
                pic -> {
                    String result = String.format("%.1f", ((double) image.getWidth() / image.getHeight()) *
                            Double.parseDouble(pic.split(",")[0]));
                    String parameter = String.format("%.1f", Double.parseDouble(pic.split(",")[1]));

                    return Double.parseDouble(result) >= Double.parseDouble(parameter) &&
                            Double.parseDouble(result) < Double.parseDouble(parameter) +
                                    Double.parseDouble(pic.split(",")[2]);
                }
        );
    }
*/
}
