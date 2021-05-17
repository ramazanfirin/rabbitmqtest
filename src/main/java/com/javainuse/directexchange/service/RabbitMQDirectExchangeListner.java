package com.javainuse.directexchange.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQDirectExchangeListner implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("Direct Exchange Consuming Message - " + new String(message.getBody()));
	}

}