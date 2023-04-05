from concurrent import futures
import grpc
from tinydb import TinyDB, Query
import game_download_pb2
import game_download_pb2_grpc


class GameDownload(game_download_pb2_grpc.GameDownload):
    def DownloadLink(self, request, context):
        db = TinyDB('db.json')
        game = Query()
        download_key = int(request.download_key)

        if db.search(game.key == download_key):
            result = db.get(game.key == download_key)
            game_name = result.get('name')
            print("Available for download: ", game_name)

            return game_download_pb2.LinkResponse(message=f"{game_name} available for download.")

        else:
            return game_download_pb2.LinkResponse(message="Game could not be found.")


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    game_download_pb2_grpc.add_GameDownloadServicer_to_server(GameDownload(), server)
    server.add_insecure_port('[::]:50053')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
