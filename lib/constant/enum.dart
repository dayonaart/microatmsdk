part of '../microatmsdk.dart';

///*
/// CHECK_PERMISSION,
/// SCAN_DEVICES,
/// CONNECTING,
/// PAIRING,
/// START_EMV,
/// INJECT_MASTER_KEY,
/// INJECT_WORK_KEY,
/// ENCRYPT_DATA,
/// DECRYPT_DATA,
///*
enum _MethodName {
  CHECK_PERMISSION,
  SCAN_DEVICES,
  CANCEL_SCANNING,
  CONNECTING,
  PAIRING,
  START_EMV,
  INJECT_MASTER_KEY,
  INJECT_WORK_KEY,
  ENCRYPT_DATA,
  DECRYPT_DATA,
}

enum _EventName {
  SCAN_DEVICES_LISTENER,
  PROGRESS_MESSAGE_LISTENER,
}
