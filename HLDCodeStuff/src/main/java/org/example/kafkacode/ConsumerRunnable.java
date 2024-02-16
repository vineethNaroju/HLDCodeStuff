package org.example.kafkacode;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.protocol.types.Field;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ConsumerRunnable implements Runnable {

    final Properties properties = new Properties();
    final String CONSUMER_GROUP_ID = "FREQUENCY";

    final String TOPIC = "shapes";

    final KafkaConsumer<String, String> kafkaConsumer;

    public ConsumerRunnable(String serverLocation) {
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverLocation);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer<>(properties);
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }


    @Override
    public void run() {

        kafkaConsumer.subscribe(Collections.singleton(TOPIC), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                kafkaConsumer.seekToBeginning(collection);
            }
        });

        List<PartitionInfo> infoList = kafkaConsumer.partitionsFor(TOPIC);

        for(PartitionInfo pi : infoList) {
            System.out.println("replicas => ");
            for(Node n : pi.replicas()) {
                System.out.println(n.host() + " : " + n.port());
            }

            System.out.println(pi.inSyncReplicas());
            System.out.println(pi.partition());
            break;
        }

        while(true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));

            for(ConsumerRecord<String, String> record : records) {
                System.out.println("consumer:" + getThreadName() +
                        ",key:" + record.key() +
                        ",value:" + record.value() +
                        ",partition:" + record.partition() +
                        ",offset:" + record.offset() +
                        ",topic:" + record.topic() +
                        ",leaderEpoch:" + record.leaderEpoch().orElse(-1) +
                        ",headers:" + record.headers().toString());
            }
        }
    }
}
