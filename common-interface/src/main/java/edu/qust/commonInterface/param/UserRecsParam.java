package edu.qust.commonInterface.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserRecsParam implements Serializable {

    private int userId;

    private List<UserRecommendationParam> recs;
}
