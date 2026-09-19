package edu.qust.recommender;

import edu.qust.recommender.dataLoader.DataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.xml.crypto.Data;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})

public class recommenderApplication {
    public static void main(String[] args){
        SpringApplication.run(recommenderApplication.class,args);
    }
}
