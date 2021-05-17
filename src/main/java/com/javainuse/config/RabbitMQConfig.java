package com.javainuse.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.javainuse.directexchange.service.RabbitMQDirectExchangeListner;
import com.javainuse.fanoutexchange.service.RabbitMQFanoutExchangeListnerLongLife;
import com.javainuse.fanoutexchange.service.RabbitMQFanoutExchangeListnerShortLife;

@Configuration
public class RabbitMQConfig {

	@Value("${javainuse.rabbitmq.longlivequeue}")
	String longLiveQueueName;
	
	@Value("${javainuse.rabbitmq.shortlivequeue}")
	String shortLiveQueueName;

	@Value("${javainuse.rabbitmq.fanoutexchange}")
	String fanoutExchange;

	
	@Value("${javainuse.rabbitmq.directexchangequeue}")
	String directQueueName;

	@Value("${javainuse.rabbitmq.directexchange}")
	String directExchange;

	@Value("${javainuse.rabbitmq.routingkey}")
	private String routingkey;
	
	// DirectExchange
	@Bean
	Queue directQueue() {
		return new Queue(directQueueName, true);
	}

	@Bean
	DirectExchange DirectExchange() {
		return new DirectExchange(directExchange);
	}

	@Bean
	Binding binding(Queue directQueue, DirectExchange directExchange) {
		return BindingBuilder.bind(directQueue).to(directExchange).with(routingkey);
	}
	
	@Bean
	MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueues(directQueue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQDirectExchangeListner());
		return simpleMessageListenerContainer;

	}
	
	//FanoutExchange
	@Bean
	Queue longLiveQueue() {
		return new Queue(longLiveQueueName, true);
	}
	
	@Bean
	Queue shortLiveQueue() {
		return QueueBuilder.durable(shortLiveQueueName).autoDelete().withArgument("x-message-ttl", 5*1000).build();
//		return new Queue(shortLiveQueueName, true);
	}

	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(fanoutExchange);
	}

	@Bean
	Binding fanoutBindingForLongLive(Queue longLiveQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(longLiveQueue).to(fanoutExchange);
	}

	@Bean
	Binding fanoutBindingForShortLice(Queue shortLiveQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(shortLiveQueue).to(fanoutExchange);
	}
	
	@Bean
	MessageListenerContainer messageListenerContainerForLongLive(ConnectionFactory connectionFactory ) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueues(longLiveQueue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQFanoutExchangeListnerLongLife());
		return simpleMessageListenerContainer;

	}
	
	@Bean
	MessageListenerContainer messageListenerContainerShortLive(ConnectionFactory connectionFactory ) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueues(shortLiveQueue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQFanoutExchangeListnerShortLife());
		return simpleMessageListenerContainer;

	}
//--------------------------------------------------------------------------------------------------------------
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}


	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}
