package org.example.kerjaBelajar.endpoint;

public class DeleteEndpoint {
	/**
	 * 	public IResultDTO<Boolean> deleteCategories(List<Long> id, OAuth2Authentication auth2Authentication, HttpServletRequest httpServletRequest) {
	 * 		try {
	 * 			Boolean response = menuSpecialCategoryService.deleteCategories(id, (User) auth2Authentication.getPrincipal());
	 * 			if (response.equals(Boolean.TRUE)) {
	 * 				return APIResponseBuilder.ok(true);
	 *                        } else {
	 * 				return APIResponseBuilder.noContent(false);
	 *            }* 		} catch (ServiceException e) {
	 * 			return APIResponseBuilder.internalServerError(false, e, e.getMessage(), httpServletRequest);* 		} catch (HttpClientErrorException e) {
	 * 			return APIResponseBuilder.badRequest(false, e, e.getMessage(), httpServletRequest);* 		}
	 * 	}
	 */
}
