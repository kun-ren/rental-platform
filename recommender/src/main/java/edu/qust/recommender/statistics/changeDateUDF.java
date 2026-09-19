package edu.qust.recommender.statistics;


import org.apache.spark.sql.api.java.UDF1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class changeDateUDF implements UDF1<Integer,String> {

    @Override
    public String call(Integer timestamp) throws Exception {
        long timestampMill = timestamp * 1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String yyyyMM = simpleDateFormat.format(new Date(String.valueOf(timestampMill)));
        return yyyyMM;
    }
}
