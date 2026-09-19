package edu.qust.commonInterface;

import edu.qust.commonInterface.param.StuffApiParam;

import java.util.List;

public interface StuffServiceApi {

    public boolean saveBatch(List<StuffApiParam> stuffs);
}
