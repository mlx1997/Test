package com.mr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.ArrayList;

@SpringBootApplication
@EnableDiscoveryClient
public class UploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class);
    }

//    public static void main(String[] args) {
//        ArrayList list=new ArrayList(-1);
//    }
}
