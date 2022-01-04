package org.example.kerjaBelajar.impl;


public class OrderingDataImpl {
	/**
	 public List<MenuSpecialCategory> getAllSpecialCategory(User user) {
		List<MenuSpecialCategory> menuSpecialCategories = new ArrayList<>();
		try {
			menuSpecialCategories = menuSpecialCategoryDAO.findAllByRowStatus();
		} catch (Exception e) {
			log.error("Failed to retrieve list groupId", e);
		}
		return menuSpecialCategories;
	}

	public List<MenuSpecialCategory> getByGroupId(User user, List<Long> groupId) throws ServiceException {
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
	 */
}
