from concurrent import futures
import grpc
from tinydb import TinyDB, Query
import registration_pb2
import registration_pb2_grpc


class Register(registration_pb2_grpc.Register):
    def RegisterUser(self, request, context):
        db = TinyDB('db.json')
        user = Query()

        if not db.search(user.email == request.email):
            db.insert({'email': request.email, 'password': request.password})
            print("Registered Account: ", request.email)

            return registration_pb2.RegisterUserResponse(message="Account successfully registered.")

        else:
            print("Failed To Register Account: ", request.email)
            return registration_pb2.RegisterUserResponse(message="Account with email already exists.")


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    registration_pb2_grpc.add_RegisterServicer_to_server(Register(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
