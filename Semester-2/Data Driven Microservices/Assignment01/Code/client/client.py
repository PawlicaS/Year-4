import grpc
import bcrypt
import registration_pb2
import registration_pb2_grpc
import purchase_auth_pb2
import purchase_auth_pb2_grpc


def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = registration_pb2_grpc.RegisterStub(channel)

        password = bcrypt.hashpw('123'.encode(), bcrypt.gensalt()).decode()
        response = stub.RegisterUser(
            registration_pb2.RegisterUserRequest(email='testemail1@mail.com', password=password))
        print(response.message)

        password = bcrypt.hashpw('1234'.encode(), bcrypt.gensalt()).decode()
        response = stub.RegisterUser(
            registration_pb2.RegisterUserRequest(email='testemail2@mail.com', password=password))
        print(response.message)

        password = bcrypt.hashpw('12345'.encode(), bcrypt.gensalt()).decode()
        response = stub.RegisterUser(
            registration_pb2.RegisterUserRequest(email='testemail3@mail.com', password=password))
        print(response.message)

        password = bcrypt.hashpw('123456'.encode(), bcrypt.gensalt()).decode()
        response = stub.RegisterUser(
            registration_pb2.RegisterUserRequest(email='testemail4@mail.com', password=password))
        print(response.message)

    with grpc.insecure_channel('localhost:50052') as channel:
        stub = purchase_auth_pb2_grpc.PurchaseStub(channel)
        response = stub.AuthorisePurchase(
            purchase_auth_pb2.PurchaseRequest(card_details='justatest', charge_amount='justatest'))
        print(response.message)
        stub = purchase_auth_pb2_grpc.PurchaseStub(channel)
        response = stub.AuthorisePurchase(
            purchase_auth_pb2.PurchaseRequest(card_details='justatest', charge_amount='justatest'))
        print(response.message)


if __name__ == '__main__':
    run()
