package com.javainuse.fanoutexchange.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQFanoutExchangeListnerLongLife implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("Long live sConsuming Message - " + new String(message.getBody()));
	}

}