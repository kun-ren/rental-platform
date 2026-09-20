package edu.qust.commonInterface;

import edu.qust.common.base.Response;
import edu.qust.commonInterface.dto.CategoryApiDTO;
import edu.qust.commonInterface.dto.CategorySelectApiDTO;
import edu.qust.commonInterface.param.CategoryApiParam;

import java.util.List;

/**
 * CategoryService Api
 *
 */
public interface CategoryServiceApi {
	/**
	 * Get all category information
	 *
	 * @return CategoryApiDTO list
	 */
	List<CategoryApiDTO> listCategoryApiDTO();

	/**
	 * Add Category
	 *
	 * @param categoryApiParam categoryApiParam
	 * @return common.base.Response
	 */
	Response add(CategoryApiParam categoryApiParam);

	/**
	 * Update a category
	 *
	 * @param categoryApiParam categoryApiParam
	 * @return common.base.Response
	 */
	Response modify(CategoryApiParam categoryApiParam);

	/**
	 * delete category
	 *
	 * @param id category id
	 * @return Response
	 */
	Response delete(Integer id);

	List<CategorySelectApiDTO> listCategorySelectApiDTO();
}
