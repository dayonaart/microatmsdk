// ignore_for_file: non_constant_identifier_names

library micro_atm_sdk;

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

part 'micro_controller.dart';
part 'micro_impl.dart';
part 'data/method_result.dart';
part 'data/bluetooth_client.dart';
part 'data/progress_message.dart';
part 'views/scan_dialog.dart';
part 'views/option_view.dart';
part 'views/emv_view.dart';
part 'views/final_view.dart';
part 'views/sdk_log.dart';
part 'controller/scan_dialog_controller.dart';
part 'controller/option_view_controller.dart';
part 'controller/emv_view_controller.dart';
part 'constant/util.dart';
part 'constant/enum.dart';
part 'data/emv_result.dart';

class Microatmsdk {
  Widget SdkLog() {
    return _LogView();
  }

  Future<MethodResult?> scanDevices(BuildContext context) {
    return _MicroImpl.instance.scanDevices(context);
  }

  Future<MethodResult?> cancelScanning() {
    return _MicroImpl.instance.cancelScanning();
  }

  Future<MethodResult?> checkPermission() {
    return _MicroImpl.instance.checkPermission();
  }

  Stream<List<BluetoothClient?>> scanDevicesListener() {
    return _MicroImpl.instance.scanDevicesListener();
  }

  Future<MethodResult?> pairing(String macAddress) {
    return _MicroImpl.instance.pairing(macAddress);
  }

  Future<MethodResult?> connecting(String macAddress) {
    return _MicroImpl.instance.connecting(macAddress);
  }

  Future<MethodResult?> injectMasterKey() {
    return _MicroImpl.instance.injectMasterKey();
  }

  Future<MethodResult?> injectWorkKey() {
    return _MicroImpl.instance.injectWorkKey();
  }

  Future<MethodResult?> encryptData(String data) {
    return _MicroImpl.instance.encryptData(data);
  }

  Future<MethodResult?> decryptData(String data) {
    return _MicroImpl.instance.decryptData(data);
  }

  Future<EmvResult?> startEmv() {
    return _MicroImpl.instance.startEmv();
  }

  Stream<ProgressMessage?> messageProgressListener() {
    return _MicroImpl.instance.messageProgressListener();
  }
}
