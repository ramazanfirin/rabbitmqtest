# consumer.py
# Consume RabbitMQ queue

import pika
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', 5672, '/', pika.PlainCredentials("guest", "guest")))
channel = connection.channel()

#channel.queue_declare(queue='hello', durable=True)
#channel.exchange_declare(exchange='logs', exchange_type='fanout')
#result = channel.queue_declare(queue='')
#result = channel.queue_declare(queue='', exclusive=True
#channel.queue_bind(exchange='logs', queue=result.method.queue)
#channel.exchange_declare(exchange='logs', exchange_type='fanout')

result = channel.queue_declare(queue='', exclusive=True)
queue_name = result.method.queue
channel.queue_bind(exchange='javainuse.fanoutexchange', queue=queue_name)

def callback(ch, method, properties, body):
    print(f'{body} is received')
    
channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)
channel.start_consuming()