package org.example.kerjaBelajar.redis;
/**
import com.peentar.kf.oa.util.core.exception.DAOException;
import com.telkomsigma.kf.oa.shared.data.model.mobile.useractivity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface ISearchActivityDAO extends JpaRepository<SearchHistory, Long> {

    @Query("select sh from SearchHistory sh where sh.userId = :userId and sh.activityType = 'SEARCH_ITEM' order by sh" +
            ".modifiedOn desc")
    Page<SearchHistory> findAllByUserId(@Param("userId") Long userId, Pageable pageable) throws DAOException;

    @Query("select sh from SearchHistory sh where sh.userId = :userId and sh.activityType = 'SEARCH_OUTLET' order by " +
            "sh.modifiedOn desc")
    Page<SearchHistory> findAllOutletByUserId(@Param("userId") Long userId, Pageable pageable) throws DAOException;

    @Query("select sh from SearchHistory sh where sh.id in :ids")
    List<SearchHistory> findAllByHistoryIdIn(List<Long> ids) throws DAOException;

    @Query("select sh from SearchHistory sh where sh.userId = :userId and sh.keyword= :keyword and sh.activityType = " +
            "'SEARCH_ITEM' ")
    SearchHistory findByUserIdAndKeyword(@Param("userId") Long Userid, @Param("keyword") String keyword) throws
            DAOException;

    @Transactional
    @Modifying
    @Query("update SearchHistory sh set sh.rowStatus = 0 where sh.userId = :userId and sh.activityType = " +
            "'SEARCH_OUTLET' ")
    void deleteAllSearchOutletByUserId(@Param("userId") Long userId) throws DAOException;

    @Transactional
    @Modifying
    @Query("update SearchHistory sh set sh.rowStatus = 0 where sh.userId = :userId and sh.activityType = " +
            "'SEARCH_ITEM' ")
    void deleteAllSearchItemByUserId(@Param("userId") Long userId) throws DAOException;

    @Query("select sh from SearchHistory sh where sh.userId = :userId and sh.keyword= :keyword and sh.activityType = " +
            "'SEARCH_OUTLET' ")
    SearchHistory findByUserIdAndKeywordOutlet(@Param("userId") Long Userid, @Param("keyword") String keyword) throws
            DAOException;


}
*/