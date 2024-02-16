package org.example.kafkacode;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerRunnable implements Runnable {

    final Properties properties = new Properties();
    final KafkaProducer<String, String> kafkaProducer;

    final String[] shapes = {"circle", "square", "rectangle", "triangle", "hexagon", "pentagon"};

    final String TOPIC = "shapes";

    private int idx = 0, cnt = 0;


    public ProducerRunnable(String serverLocation) {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverLocation);
//        properties.setProperty(ProducerConfig.)
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        kafkaProducer = new KafkaProducer<>(properties);
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    @Override
    public void run() {

        while(cnt++ < 100) {
            try {

                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, shapes[idx++ % shapes.length]);

                kafkaProducer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        System.out.println("producer:" + getThreadName() +
                                ",topic:" + recordMetadata.topic() +
                                ",partition:" + recordMetadata.partition() +
                                ",offset:" + recordMetadata.offset() +
                                ",timestamp:" + recordMetadata.timestamp() +
                                ",serializedKeySize:" + recordMetadata.serializedKeySize() +
                                ",serializedValueSize:" + recordMetadata.serializedKeySize()
                        );
                    }
                });

                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
