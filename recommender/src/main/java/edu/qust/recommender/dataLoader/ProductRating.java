package edu.qust.recommender.dataLoader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Rating 数据集
 * 用户ID   商品ID    评分    时间戳
 * userId: Int, productId: Int, score: Double, timestamp: Int
 */

@Data
@AllArgsConstructor
public class ProductRating implements Serializable {

    private int userId;

    private int productId;

    private float score;

    private int timestamp;
}
