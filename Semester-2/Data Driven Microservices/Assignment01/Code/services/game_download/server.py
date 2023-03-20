from concurrent import futures
import grpc
from tinydb import TinyDB, Query
import game_download_pb2
import game_download_pb2_grpc


class GameDownload(game_download_pb2_grpc.Game_Download):
    def GetDownloadLink(self, request, context):
        db = TinyDB('db.json')
        game = Query()

        if db.search(game.key == request.download_key):
            result = db.get(game.key == request.download_key)
            game_name = result.get('name')
            print("Downloading: ", request.download_key)

            return game_download_pb2.LinkResponse(message=f"{game_name} available for download.")

        else:
            return game_download_pb2.LinkResponse(message="Game could not be found.")


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    game_download_pb2_grpc.add_Game_DownloadServicer_to_server(GameDownload(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
