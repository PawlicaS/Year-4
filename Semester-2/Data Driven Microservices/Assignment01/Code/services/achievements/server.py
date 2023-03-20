from concurrent import futures
import grpc
from tinydb import TinyDB, Query
import achievements_pb2
import achievements_pb2_grpc


class Achievements(achievements_pb2_grpc.Achievements):
    def UpdateAchievements(self, request, context):
        db = TinyDB('db.json')
        achievement = Query()

        if db.search(achievement.id == request.achievement_id):
            result = db.get(achievement.id == request.achievement_id)
            if request.completed:
                completed = result.get('completed') + 1
                db.update({request.achievement_id: completed})
            else:
                total = result.get('total') + 1
                db.update({request.achievement_id: total})

            print("Updated: ", request.achievement_id)

            return achievements_pb2.UpdateAchievementResponse(message="Achievement updated.")

        else:
            return achievements_pb2.UpdateAchievementResponse(message="Achievement failed to update.")


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    achievements_pb2_grpc.add_AchievementsServicer_to_server(Register(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
