import pika
from tinydb import TinyDB, Query


def update_achievements():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()
    channel.queue_declare(queue='update_achievement')

    def callback(ch, method, properties, body):
        print(" [x] Received %r" % body)
        db = TinyDB('db.json')
        achievement = Query()
        achievement_id = 1
        if b'new_player' in body:
            result = db.search(achievement.id == achievement_id)
            total = result[0]['total']
            total += 1
            db.update({'total': total}, achievement.id == achievement_id)
        else:
            result = db.search(achievement.id == achievement_id)
            completed = result[0]['completed']
            completed += 1
            db.update({'completed': completed}, achievement.id == achievement_id)

    channel.basic_consume(queue='update_achievement', on_message_callback=callback, auto_ack=True)
    print(' [*] Waiting for messages.')
    channel.start_consuming()


if __name__ == '__main__':
    update_achievements()
