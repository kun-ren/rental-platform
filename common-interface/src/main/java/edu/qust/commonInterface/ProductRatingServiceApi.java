package edu.qust.commonInterface;

import edu.qust.commonInterface.param.ProductRatingParam;

import java.util.List;

public interface ProductRatingServiceApi {

    public boolean insertByBatch(List<ProductRatingParam> productRatingParams);

    public List<ProductRatingParam> list();

    public ProductRatingParam getRating(int userId, int productId);
}
