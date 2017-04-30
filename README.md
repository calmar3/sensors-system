## Installazione


Installare `maven` e `java 8`

## Configurazione

Modificare in `src/main/java/resources/application.properties` gli attributi per configurare correttamente il sistema
nella seguente maniera:

---------------------------------------------------------------------------------------------------------------------


###### #MongoDB Properties
spring.data.mongodb.host=localhost

spring.data.mongodb.username=usr

spring.data.mongodb.password=pwd

spring.data.mongodb.port=27017

spring.data.mongodb.database=streetlampsensors

###### #Apache Kafka Properties
spring.kafka.producer.bootstrap-servers=localhost:9092, localhost:9093

###### #Sensors-system Properties
streelamp.thread.sleeptime=10000

lightsensor.thread.sleeptime=100

max.num.lamps=60

lamps.for.thread=10

light.sensor.topic=light_sensor_data

streetlamp.topic=lamp_data



---------------------------------------------------------------------------------------------------------------------

## Avvio

Una volta configurata tramite il file di configurazione l'applicazione può essere eseguita in de differenti modalità:

	- tramite IDE (IntelliJ - Eclipse)

	- creare il JAR del progetto tramite il comando `mvn install` eseguito da terminale nella directory contenente il file `pom.xml` ed ed eseguirlo tramite il comando `java -jar -Dspring.config.location=/path_to_file/application.properties sensors-system.jar`  


