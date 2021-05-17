# producer.py
# This script will publish MQ message to my_exchange MQ exchange

import pika

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', 5672, '/', pika.PlainCredentials('guest', 'guest')))
channel = connection.channel()

channel.basic_publish(exchange='javainuse.fanoutexchange', routing_key='', body='Test!12345')

connection.close()