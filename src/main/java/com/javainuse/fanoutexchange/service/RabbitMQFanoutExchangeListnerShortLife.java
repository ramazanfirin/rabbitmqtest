package com.javainuse.fanoutexchange.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQFanoutExchangeListnerShortLife implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("Short live Consuming Message - " + new String(message.getBody()));
	}

}