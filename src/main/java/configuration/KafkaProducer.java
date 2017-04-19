package configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.kafka.core.KafkaTemplate;


@Component
public class KafkaProducer {

	private static KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
		KafkaProducer.kafkaTemplate = kafkaTemplate;
	}

	public static KafkaTemplate<String, String> getKafkaTemplate() {
		return kafkaTemplate;
	}
	
	public static void send(String topic, String data) {
		KafkaProducer.kafkaTemplate.send(topic, data);
	}

}