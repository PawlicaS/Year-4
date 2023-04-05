import random
from concurrent import futures
import grpc
import pika
from tinydb import TinyDB
import purchase_auth_pb2
import purchase_auth_pb2_grpc
import game_download_pb2
import game_download_pb2_grpc


class Purchase(purchase_auth_pb2_grpc.Purchase):
    def AuthorisePurchase(self, request, context):
        db = TinyDB('db.json')
        x = random.randint(0, 101)  # Unable to implement actual card authorisation now

        if x > 10:
            purchase_id = len(db) + 1
            db.insert({'id': purchase_id, 'charged': request.charge_amount})

            with grpc.insecure_channel('localhost:50053') as channel:
                game_download_key = '1'
                stub = game_download_pb2_grpc.GameDownloadStub(channel)
                response = stub.DownloadLink(game_download_pb2.LinkRequest(download_key=game_download_key))
                print(response)

            if "Game could not be found." in str(response):
                return purchase_auth_pb2.PurchaseResponse(message="Purchase failed, couldn't find game.")

            connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
            channel = connection.channel()
            channel.queue_declare(queue='update_achievement')
            channel.basic_publish(exchange='',
                                  routing_key='update_achievement',
                                  body=b'new_player')
            print(" [x] Sent 'update_achievement!'")
            connection.close()

            print("Authorised Purchase: ", purchase_id)

            return purchase_auth_pb2.PurchaseResponse(message="Purchase successful.")

        else:
            return purchase_auth_pb2.PurchaseResponse(
                message="Payment authorisation failed, please try again later.")


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    purchase_auth_pb2_grpc.add_PurchaseServicer_to_server(Purchase(), server)
    server.add_insecure_port('[::]:50052')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
