package org.example.kerjaBelajar.redis;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
public class ServiceRedis {
    @Override
    public List<ResponseSearchHistoryDTO> inputSearchHistory(
            Long userId, String keyword,
            ESearchActivityType activityType
    ) throws ServiceException {
        String cleanKeyword = keyword.trim().toLowerCase();
        SearchHistory existingSearch;
        if (!cleanKeyword.isEmpty()) {
            try {
                switch (activityType) {
                    case SEARCH_ITEM:
                        existingSearch = searchActivityDAO.findByUserIdAndKeyword(userId, cleanKeyword);
                        break;
                    case SEARCH_OUTLET:
                        existingSearch = searchActivityDAO.findByUserIdAndKeywordOutlet(userId, cleanKeyword);
                        break;
                    default:
                        existingSearch = null;
                }

                if (existingSearch != null) {
                    searchActivityDAO.delete(existingSearch);
                }
                SearchHistory history = new SearchHistory();
                history.setUserId(userId);
                history.setKeyword(cleanKeyword);
                history.setActivityType(activityType);
                history.setCreatedBy(userId);
                history.setCreatedOn(Date.from(Instant.now()));
                searchActivityDAO.save(history);

            } catch (DAOException e) {
                throw new ServiceException("Failed check existing history");
            }
        }

        return this.getRecentHistory(userId, activityType);

    }

    @Override
    public List<ResponseSearchHistoryDTO> deleteSearchHistory(
            List<Long> id, Long userId,
            ESearchActivityType activityType
    ) throws
            ServiceException {

        try {
            List<SearchHistory> histories = searchActivityDAO.findAllByHistoryIdIn(id);
            if (!histories.isEmpty()) {
                searchActivityDAO.deleteAll(histories);
                log.info("Success Delete Search History");
            }

        } catch (DAOException e) {
            throw new ServiceException("Failed get search data");
        }
        return this.getRecentHistory(userId, activityType);
    }

    @Override
    public List<ResponseSearchHistoryDTO> getRecentHistory(Long userId, ESearchActivityType activityType) throws
            ServiceException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SearchHistory> pageHistory;

        List<ResponseSearchHistoryDTO> listResponse;
        try {
            switch (activityType) {
                case SEARCH_ITEM:
                    pageHistory = searchActivityDAO.findAllByUserId(userId, pageable);
                    break;
                case SEARCH_OUTLET:
                    pageHistory = searchActivityDAO.findAllOutletByUserId(userId, pageable);
                    break;
                default:
                    pageHistory = Page.empty();
            }
            listResponse = pageHistory.stream().map(searchHistoryMapper::convert).collect(Collectors.toList());
        } catch (DAOException e) {
            throw new ServiceException("Failed get recent history");
        }
        return listResponse;
    }

    @Override
    public List<ResponseSearchHistoryDTO> clearAllHistory(Long userId, ESearchActivityType activityType) throws
            ServiceException {

        try {
            if (activityType == ESearchActivityType.SEARCH_ITEM) {
                searchActivityDAO.deleteAllSearchItemByUserId(userId);
            } else if (activityType == ESearchActivityType.SEARCH_OUTLET) {
                searchActivityDAO.deleteAllSearchOutletByUserId(userId);
            }
        } catch (DAOException e) {
            throw new ServiceException("user id not found, failed to clear search history");
        }
        return this.getRecentHistory(userId, activityType);
    }

    @Override
    public ResponseTotalCountSpecialCategoryDTO totalCountSpecialCategory(RequestTotalCountSpecialCategoryDTO request)
            throws ServiceException {
        try {
            StatisticSpecialCategory category =
                    redisDAOSpecialCategory.findByLinkIdAndLinkType(request.getId(), request.getType());

            if (Objects.isNull(category)) {
                StatisticSpecialCategory statistic = addTotalCountMapper.convert(request);
                assert statistic != null;
                statistic.setLastUpdate(new Date());
                statistic = redisDAOSpecialCategory.save(statistic);
                return responseTotalCountSpecialCategoryMapper.convert(statistic);
            } else {
                category.setCount(category.getCount() + 1);
                category.setLastUpdate(new Date());
                category = redisDAOSpecialCategory.save(category);
                return responseTotalCountSpecialCategoryMapper.convert(category);
            }
        } catch (DAOException e) {
            log.info("something wrong, check parameter", e);
        }
        return null;
    }

    @Scheduled(initialDelay = 2000L, fixedDelayString = "${sync.scheduler-interval}")
    void executeScheduler() {
        try {
            linkStatusScheduler.syncLinkStatus();
        } catch (Exception e) {
            log.warn("Sync didn't stop normally. Detail: {}", e.getMessage());
        }
    }


    @Override
    public List<StatisticSpecialCategory> getTotalCountSpecialCategories() throws ServiceException {
        Spliterator<StatisticSpecialCategory> listRedis = redisDAOSpecialCategory.findAll().spliterator();
        if (Objects.nonNull(listRedis)) {
            // TODO: Use a config for this
            return StreamSupport.stream(listRedis, false)
                    .filter(x -> x.getLinkId() != 10)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
*/
