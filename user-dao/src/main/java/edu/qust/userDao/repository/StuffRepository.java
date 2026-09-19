package edu.qust.userDao.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import edu.qust.userDao.entity.es.StuffDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * stuff repository(search es)
 *
 */
@Repository
public interface StuffRepository extends ElasticsearchRepository<StuffDocument, Integer> {
	/**
	 * find by description and categoryId
	 *
	 * @param description 物品描述
	 * @param categoryId  类别ID
	 * @return StuffDocument list
	 */
	List<StuffDocument> findByDescriptionAndCategoryId(String description, Integer categoryId);

	/**
	 * find by name and CategoryId
	 *
	 * @param name       物品名称
	 * @param categoryId 类别ID
	 * @return StuffDocument list
	 */
	List<StuffDocument> findByNameAndCategoryId(String name, Integer categoryId);

	/**
	 * find by name and description and categoryId
	 *
	 * @param name        物品名称
	 * @param description 物品描述
	 * @param categoryId  类别ID
	 * @return StuffDocument list
	 */
	List<StuffDocument> findByNameAndDescriptionAndCategoryId(String name, String description, Integer categoryId);
}
