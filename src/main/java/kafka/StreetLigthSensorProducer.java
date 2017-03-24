package kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class StreetLigthSensorProducer {
      
	private static Producer<String, String> producer;
    public final static String brokerList = "localhost:9092,localhost:9093";
    private static final String TOPIC = "StreetLigthSensorData";
      
    public void initialize() {
    	Properties props = new Properties();
        props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }
       
    public void publish(String key, String message) {
    	KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, key, message);
        producer.send(data);
    }
   
    public void closeProducer() {
        producer.close();
    }
   
}