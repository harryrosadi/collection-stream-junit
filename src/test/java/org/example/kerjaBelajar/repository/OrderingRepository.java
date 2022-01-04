package org.example.kerjaBelajar.repository;

import java.util.List;

public class OrderingRepository {
	/**
	@Query("from MenuSpecialCategory msc join msc.categoryGroup mscg where mscg.rowStatus = 1 and msc.rowStatus = 1 and msc.categoryGroup.id in (:ids)")
	List<MenuSpecialCategory> findAllByCategoryGroupIdIn(@Param("ids") List<Long> groupId) throws DAOException;

	@Query("from MenuSpecialCategory msc join msc.categoryGroup mscg where mscg.rowStatus = 1 and msc.rowStatus = 1")
	List<MenuSpecialCategory> findAllByRowStatus() throws DAOException;

	@Query("select max(a.order) from #{#entityName} a where a.categoryGroup = ?1")
	Integer findMaxOrder(MenuSpecialCategoryGroup categoryGroupId) throws DAOException;

	@Modifying
	@Query("update #{#entityName} a set a.order =a.order+1 where a.order < ?1 and a.order >= ?4 and a.categoryGroup = ?2 " + "and id <> ?3")
	void updateOrderIncBetween(Integer oldOrder, MenuSpecialCategoryGroup categoryGroup, Long id, Integer newOrder) throws DAOException;

	@Modifying
	@Query("update #{#entityName} a set a.order =a.order-1 where a.order > ?1 and a.order <= ?4 and a.categoryGroup = ?2 " + "and id <> ?3")
	void updateOrderDecBetween(Integer oldOrder, MenuSpecialCategoryGroup categoryGroup, Long id, Integer newOrder) throws DAOException;
	*/
}
