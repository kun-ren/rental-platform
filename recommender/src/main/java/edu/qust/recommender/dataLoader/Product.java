package edu.qust.recommender.dataLoader;


import lombok.*;

import java.io.Serializable;

/**
 * Product数据集
 * 商品ID  商品名称   商品分类ID  亚马逊ID   商品图片URL  商品分类   商品用户生成标签
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
