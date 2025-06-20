package dans_multi_pro.recharge_service.config;

import dans_multi_pro.recharge_service.constant.RabbitMQConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    public String HOST;

    @Value("${spring.rabbitmq.username}")
    public String USERNAME;

    @Value("${spring.rabbitmq.password}")
    public String PASSWORD;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(HOST);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return connectionFactory;
    }

    @Bean
    public Queue runThirdPartyRechargeQueue() {
        return new Queue(RabbitMQConstants.QUEUE_RUN_RECHARGE_REQUEST_THIRD_PARTY_RECHARGE_SERVICE, true);
    }

    @Bean
    public DirectExchange runRechargeExchange() {
        return new DirectExchange(RabbitMQConstants.EXCHANGE_RUN_RECHARGE_DIRECT_EVENTS);
    }

    @Bean
    public Binding bindRunRecharge(@Qualifier("runThirdPartyRechargeQueue") Queue queue,  @Qualifier("runRechargeExchange") DirectExchange myExchange) {
        return BindingBuilder.bind(queue).to(myExchange).with(RabbitMQConstants.ROUTING_KEY_RUN_RECHARGE_RECHARGE_EXECUTE);
    }
}