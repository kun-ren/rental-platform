package edu.qust.commonInterface.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProductRecsParam implements Serializable {

    private int productId;

    private List<ProductRecommendationParam> recs;
}
