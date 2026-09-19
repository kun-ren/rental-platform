package edu.qust.commonInterface;

import edu.qust.commonInterface.param.ProductRecsParam;

import java.util.List;

public interface ProductRecsServiceApi {


    public List<ProductRecsParam> list();

    public boolean save(List<ProductRecsParam> productRecsParams);
}
