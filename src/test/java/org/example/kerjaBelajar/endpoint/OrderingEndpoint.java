package org.example.kerjaBelajar.endpoint;

import java.util.List;
import java.util.Objects;

public class OrderingEndpoint {
	/**
	public IResultDTO<List<ResponseMenuSpecialCategoryDTO>> updateOrderSpecialCategory(Long id, Integer order, OAuth2Authentication authentication, HttpServletRequest request) throws EndPointException {
		try {

			List<MenuSpecialCategory> response = menuSpecialCategoryService.groupOrder(id, order);
			if (Objects.nonNull(response)){
				return APIResponseBuilder.ok(specialCategoryMapper.mapEntitiesIntoDTOs(response));
			} else {
				return APIResponseBuilder.noContent(null);
			}
		} catch (ServiceException e){
			return APIResponseBuilder.internalServerError(null, e, e.getMessage(), request);
		} catch (HttpClientErrorException e ){
			return APIResponseBuilder.badRequest(null, e, e.getMessage(), request);
		}
	 */

	// condition
	/**
	public IResultDTO<ResponseSpecialCategoryDto> addSpecialCategories(RequestMenuSpecialCategoryDto category, OAuth2Authentication authentication, HttpServletRequest request) {
		if (category.getCode().contains(" "))
			return APIResponseBuilder.badRequest(null, new ServiceException("There is something wrong in validating data."), " Code cannot using space character." , request);
		try {
			ResponseSpecialCategoryDto response = addSpecialCategoryMapper.convert(menuSpecialCategoryService.addMenuCategory(category, (User) authentication.getPrincipal()));
			if (Objects.nonNull(response)) {
				return APIResponseBuilder.ok(response);
			} else
				return APIResponseBuilder.badRequest(null,
						new ServiceException("There is something wrong in validating data that makes data returns null"),
						"Please recheck again and make sure that the input data is valid",
						request);
		} catch (ServiceException | DAOException e) {
			return APIResponseBuilder.internalServerError(null, e, "Failed to add special categories",
					request
			);
		}
	}
	 */

}
