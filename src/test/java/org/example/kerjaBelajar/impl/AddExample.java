package org.example.kerjaBelajar.impl;

public class AddExample {

	/**
	 * public MenuSpecialCategory addMenuCategory(RequestMenuSpecialCategoryDto request, User user) throws DAOException, ServiceException {
	 *
	 * 		MenuSpecialCategoryGroup categoryGroup = categoryGroupDAO.findByCode(request.getCode());
	 * 		MenuSpecialCategory category = null;
	 * 		if (categoryGroup == null || !categoryGroup.getCode().equals(request.getCode())) {
	 * 			categoryGroup = categoryGroupMapperMapper.convert(request);
	 * 			if (categoryGroup != null) {
	 * 				categoryGroup.setCreatedBy(user.getId());
	 * 				MenuSpecialCategoryGroup saveCategoryGroup = categoryGroupDAO.save(categoryGroup);
	 * 				category = categoryMapper.convert(request);
	 * 				if (category != null) {
	 * 					category.setCreatedBy(user.getId());
	 * 					category.setCategoryGroup(saveCategoryGroup);
	 * 					Integer maxOrder;
	 * 					maxOrder = menuSpecialCategoryDAO.findMaxOrder(category.getCategoryGroup());
	 * 					if (category.getOrder() == null && category.getOrder() > maxOrder + 1){
	 * 						category.setOrder(maxOrder + 1);
	 *                                        } else if (category.getOrder() == null && category.getOrder() < 1){
	 * 						category.setOrder(1);
	 *                    }
	 * 					menuSpecialCategoryDAO.save(category);
	 * 					try {
	 * 						menuSpecialCategoryDAO.updateOrderInc(category.getOrder(), category.getCategoryGroup(), category.getId());
	 *                    }catch (DAOException e){
	 * 						throw new ServiceException("Banner update order failed. " + e.getMessage());
	 *                    }* 				}
	 * 			}
	 * 			log.info("insert new menu special category group and add new to menu special category");*
	 * 		} else if (request.getCode().equals(categoryGroup.getCode())) {
	 * 			category = categoryMapper.convert(request);
	 * 			assert category != null;
	 * 			category.setCreatedBy(user.getId());
	 * 			category.setCategoryGroup(categoryGroup);
	 * 			Integer maxOrder;
	 * 			maxOrder = menuSpecialCategoryDAO.findMaxOrder(category.getCategoryGroup());
	 * 			if (category.getOrder() > maxOrder + 1) {
	 * 				category.setOrder(maxOrder + 1);
	 * 			} else if (category.getOrder() < 1) {
	 * 				category.setOrder(1);
	 * 			}
	 * 			menuSpecialCategoryDAO.save(category);
	 * 			try {
	 * 				menuSpecialCategoryDAO.updateOrderInc(category.getOrder(), category.getCategoryGroup(), category.getId());
	 * 			}catch (DAOException e){
	 * 				throw new ServiceException("Banner update order failed. " + e.getMessage());
	 * 			}
	 * 			log.info("special category group code is already exist, add new one to menu special categor        ;
	 * 		}
	 * 		return category;
	 * 	}
	 */
}
