# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: purchase_auth.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x13purchase_auth.proto\x12\npurchasing\">\n\x0fPurchaseRequest\x12\x14\n\x0c\x63\x61rd_details\x18\x01 \x01(\t\x12\x15\n\rcharge_amount\x18\x02 \x01(\t\"#\n\x10PurchaseResponse\x12\x0f\n\x07message\x18\x01 \x01(\t2\\\n\x08Purchase\x12P\n\x11\x41uthorisePurchase\x12\x1b.purchasing.PurchaseRequest\x1a\x1c.purchasing.PurchaseResponse\"\x00\x62\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'purchase_auth_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  _PURCHASEREQUEST._serialized_start=35
  _PURCHASEREQUEST._serialized_end=97
  _PURCHASERESPONSE._serialized_start=99
  _PURCHASERESPONSE._serialized_end=134
  _PURCHASE._serialized_start=136
  _PURCHASE._serialized_end=228
# @@protoc_insertion_point(module_scope)