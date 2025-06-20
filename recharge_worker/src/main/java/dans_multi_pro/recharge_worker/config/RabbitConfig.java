package dans_multi_pro.recharge_worker.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dans_multi_pro.recharge_worker.constant.RabbitMQConstants.QUEUE_RUN_RECHARGE_REQUEST_THIRD_PARTY_RECHARGE_SERVICE;

@Configuration
public class RabbitConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public Queue rechargeRequestQueue() {
        return new Queue(QUEUE_RUN_RECHARGE_REQUEST_THIRD_PARTY_RECHARGE_SERVICE, true);
    }
}
