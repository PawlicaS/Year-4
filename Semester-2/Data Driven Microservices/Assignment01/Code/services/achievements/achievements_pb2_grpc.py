# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

import achievements_pb2 as achievements__pb2


class AchievementsStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.UpdateAchievements = channel.unary_unary(
                '/achievements.Achievements/UpdateAchievements',
                request_serializer=achievements__pb2.UpdateAchievementRequest.SerializeToString,
                response_deserializer=achievements__pb2.UpdateAchievementResponse.FromString,
                )


class AchievementsServicer(object):
    """Missing associated documentation comment in .proto file."""

    def UpdateAchievements(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_AchievementsServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'UpdateAchievements': grpc.unary_unary_rpc_method_handler(
                    servicer.UpdateAchievements,
                    request_deserializer=achievements__pb2.UpdateAchievementRequest.FromString,
                    response_serializer=achievements__pb2.UpdateAchievementResponse.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'achievements.Achievements', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class Achievements(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def UpdateAchievements(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/achievements.Achievements/UpdateAchievements',
            achievements__pb2.UpdateAchievementRequest.SerializeToString,
            achievements__pb2.UpdateAchievementResponse.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)