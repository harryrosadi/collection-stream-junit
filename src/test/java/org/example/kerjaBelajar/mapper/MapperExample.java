package org.example.kerjaBelajar.mapper;
/**

 public class MapperExample extends ADATAMapper<MenuSpecialCategory, ResponseSpecialCategoryDto> {

	 @Override
		public ResponseSpecialCategoryDto convert(MenuSpecialCategory category){
			return ResponseSpecialCategoryDto.builder()
					.code(category.getCode())
					.mainImageUrl(category.getMainImageUrl())
					.favoriteImageUrl(category.getFavoriteImageUrl())
					.groupId(category.getCategoryGroup().getId())
					.name(category.getName())
					.target(category.getTarget())
					.remarks(category.getRemarks())
					.order(category.getOrder())
					.targetPageId(category.getTargetPageId()).build();
		}
}
 */
