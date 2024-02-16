package org.example.kafkacode;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {

        String serverLocation = "localhost:9092";

        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, serverLocation);

        String TOPIC = "shapes_new";
        int partitionCount = 4;
        short replicationFactor = 3;


        try (Admin admin = Admin.create(properties)) {

            CreateTopicsResult result = admin.createTopics(Collections.singleton(new NewTopic(TOPIC, partitionCount, replicationFactor)));

            result.values().get(TOPIC).get();

            QuorumInfo quorumInfo = admin.describeMetadataQuorum().quorumInfo().get();

            System.out.println(quorumInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }



        CountDownLatch latch = new CountDownLatch(1);

//        final String serverLocation = "localhost:9092";
//
//
//        int producerCount = 3;
//        int consumerCount = 1;
//
//
//        List<Thread> producers = new ArrayList<>();
//
//        for(int i=0; i<producerCount; i++) {
//            Thread t = new Thread(new ProducerRunnable(serverLocation), "producer_thread_" + i);
//            producers.add(t);
//            t.start();
//        }
//
//        List<Thread> consumers = new ArrayList<>();
//
//        for(int i=0; i<consumerCount; i++) {
//            Thread t = new Thread(new ConsumerRunnable(serverLocation), "consumer_thread_" + i);
//            consumers.add(t);
//            t.start();
//        }


        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}