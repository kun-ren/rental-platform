package edu.qust.recommender.dataLoader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Rating data set
 * User ID, product ID, rating, and timestamp
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
