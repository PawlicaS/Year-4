import random
from concurrent import futures
import grpc
from tinydb import TinyDB, Query
import purchase_auth_pb2
import purchase_auth_pb2_grpc


class Purchase(purchase_auth_pb2_grpc.Purchase):
    def AuthorisePurchase(self, request, context):
        db = TinyDB('db.json')
        x = random.randint(0, 101)  # Unable to implement actual card authorisation now

        if x > 10:
            purchase_id = len(db) + 1
            db.insert({'id': purchase_id, 'charged': request.charge_amount})
            print("Authorised Purchase: ", purchase_id)

            return purchase_auth_pb2.RegisterUserResponse(message="Purchase successful.")

        else:
            return purchase_auth_pb2.RegisterUserResponse(
                message="Payment authorisation failed, please try again later.")


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    purchase_auth_pb2_grpc.add_PurchaseServicer_to_server(Purchase(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
