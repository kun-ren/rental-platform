package edu.qust.recommender.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecs implements Serializable {

    private int productId;

    private List<Recommendation> recs;
}
