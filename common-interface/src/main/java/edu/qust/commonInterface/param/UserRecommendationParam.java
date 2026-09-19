package edu.qust.commonInterface.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserRecommendationParam implements Serializable {

    private int productId;

    private double score;
}

