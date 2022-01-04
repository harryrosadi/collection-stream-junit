package org.example.kerjaBelajar.repository;

public interface UpdateRepository {
/**
	@Query("from MenuSpecialCategory msc join msc.categoryGroup mscg where mscg.rowStatus = 1 and msc.rowStatus = 1 and msc.categoryGroup.id in (:ids)")
	List<MenuSpecialCategory> findAllByCategoryGroupIdIn(@Param("ids") List<Long> groupId) throws DAOException;


	@Query("from MenuSpecialCategory msc join msc.categoryGroup mscg where mscg.rowStatus = 1 and msc.rowStatus = 1")
	List<MenuSpecialCategory> findAllByRowStatus() throws DAOException;


	 @Query("select max(a.order) from #{#entityName} a where a.categoryGroup = ?1")
	 Integer findMaxOrder(MenuSpecialCategoryGroup categoryGroup) throws DAOException;

	 @Modifying
	 @Query("update #{#entityName} a set a.order=a.order+1 where a.order >= ?1 and a.categoryGroup = ?2 and id <> ?3")
	 void updateOrderInc(Integer order, MenuSpecialCategoryGroup categoryGroup, Long id) throws DAOException;

 */
}
