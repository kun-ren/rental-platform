package edu.qust.recommender.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation implements Serializable {

    private int productId;

    private double score;

}
