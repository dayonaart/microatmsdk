part of 'microatmsdk.dart';

abstract class _MicroImpl extends PlatformInterface {
  _MicroImpl() : super(token: _token);
  static final Object _token = Object();

  static final _MicroImpl _instance = _MicroController();

  static _MicroImpl get instance => _instance;

  Future<MethodResult?> checkPermission() {
    throw UnimplementedError("checkPermission not implemented");
  }

  Future<MethodResult?> scanDevices(BuildContext context) {
    throw UnimplementedError("scanDevices not implemented");
  }

  Future<MethodResult?> cancelScanning() {
    throw UnimplementedError("cancelScanning not implemented");
  }

  Future<MethodResult?> pairing(String macAddress) {
    throw UnimplementedError("pairing not implemented");
  }

  Future<MethodResult?> connecting(String macAddress) {
    throw UnimplementedError("connecting not implemented");
  }

  Future<EmvResult?> startEmv() {
    throw UnimplementedError("startEmv not implemented");
  }

  Future<MethodResult?> injectMasterKey() {
    throw UnimplementedError("injectMasterKey not implemented");
  }

  Future<MethodResult?> injectWorkKey() {
    throw UnimplementedError("injectWorkKey not implemented");
  }

  Future<MethodResult?> encryptData(String data) {
    throw UnimplementedError("encryptData not implemented");
  }

  Future<MethodResult?> decryptData(String data) {
    throw UnimplementedError("decryptData not implemented");
  }

  Stream<List<BluetoothClient?>> scanDevicesListener() {
    throw UnimplementedError("scanDevicesListener not implemented");
  }

  Stream<ProgressMessage?> messageProgressListener() {
    throw UnimplementedError("messageProgressListener not implemented");
  }
}
