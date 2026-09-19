package edu.qust.commonInterface.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class ProductRatingParam implements Serializable {

    private int userId;

    private int productId;

    private float score;

    private Timestamp time;
}
