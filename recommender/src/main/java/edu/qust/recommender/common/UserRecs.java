package edu.qust.recommender.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecs implements Serializable {

    private int userId;

    private List<Recommendation> recs;
}
