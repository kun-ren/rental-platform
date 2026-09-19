package edu.qust.commonInterface;

import edu.qust.commonInterface.param.UserRecsParam;

import java.util.List;

public interface UserRecsServiceApi {

    public List<UserRecsParam> list();

    public boolean save(List<UserRecsParam> userRecsParams);
}
