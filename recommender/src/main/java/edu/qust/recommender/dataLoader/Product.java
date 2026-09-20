package edu.qust.recommender.dataLoader;


import lombok.*;

import java.io.Serializable;

/**
 * Product data set
 * Product ID, product name, category IDs, Amazon ID, image URL, categories, and user-generated tags
 *
 *
 */
@Data
@AllArgsConstructor
public class Product implements Serializable {

    private int productId;
    //private int id;

    private String name;

    private String imageUrl;

    private String categories;

    private String tags;

}
