// ignore_for_file: no_leading_underscores_for_local_identifiers, constant_identifier_names

part of 'microatmsdk.dart';

class _MicroController extends _MicroImpl {
  static TextEditingController logController = TextEditingController();
  static ScrollController scollController = ScrollController();

  static String date() {
    final date = DateTime.now();
    return "${date.hour}:${date.minute}:${date.second}";
  }

  static void updateLog(String? res) {
    logController.text += "• ${date()}\t:\t$res\n";
    scollController.animateTo(scollController.position.maxScrollExtent + 100,
        duration: const Duration(milliseconds: 200), curve: Curves.ease);
  }

  static const methodChannel = MethodChannel('microatmsdk');

  static EventChannel scanEvent =
      EventChannel(_EventName.SCAN_DEVICES_LISTENER.name);

  static EventChannel messageEvent =
      EventChannel(_EventName.PROGRESS_MESSAGE_LISTENER.name);

  Future<MethodResult?> get _checkPermission async {
    var res = await methodChannel
        .invokeMethod<String?>(_MethodName.CHECK_PERMISSION.name);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("checkPermission : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> get _scanDevices async {
    var res = await methodChannel
        .invokeMethod<String?>(_MethodName.SCAN_DEVICES.name);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("scanDevices : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> get _cancelScanning async {
    var res = await methodChannel
        .invokeMethod<String?>(_MethodName.CANCEL_SCANNING.name);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("cancelScanning : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> _pairing(String macAddress) async {
    var res = await methodChannel.invokeMethod<String?>(
        _MethodName.PAIRING.name, macAddress);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("pairing : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> _connecting(String macAddress) async {
    var res = await methodChannel.invokeMethod<String?>(
        _MethodName.CONNECTING.name, macAddress);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("connecting : ${result.toJson()}");
    return result;
  }

  Future<EmvResult?> _startEmv() async {
    var res =
        await methodChannel.invokeMethod<String?>(_MethodName.START_EMV.name);
    if (res == null) return null;
    var result = EmvResult.fromJson(jsonDecode(res));
    updateLog("startEmv : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> get _injectMasterKey async {
    var res = await methodChannel
        .invokeMethod<String?>(_MethodName.INJECT_MASTER_KEY.name);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("injectMasterKey : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> get _injectWorkKey async {
    var res = await methodChannel
        .invokeMethod<String?>(_MethodName.INJECT_WORK_KEY.name);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("injectWorkKey : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> _encryptData(String data) async {
    var res = await methodChannel.invokeMethod<String?>(
        _MethodName.ENCRYPT_DATA.name, data);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("encryptData : ${result.toJson()}");
    return result;
  }

  Future<MethodResult?> _decryptData(String data) async {
    var res = await methodChannel.invokeMethod<String?>(
        _MethodName.DECRYPT_DATA.name, data);
    if (res == null) return null;
    var result = MethodResult.fromJson(jsonDecode(res));
    updateLog("decryptData : ${result.toJson()}");
    return result;
  }

  @override
  Future<MethodResult?> checkPermission() {
    return _checkPermission;
  }

  @override
  Future<MethodResult?> scanDevices(BuildContext context) async {
    var _checkPermission = await checkPermission();
    if (!_checkPermission?.data) return _checkPermission;
    var scan = await _scanDevices;
    if (scan?.data) {
      var dialog = await Future.delayed(const Duration(milliseconds: 300))
          .then((value) => Scanning.show(context));
      return dialog;
    } else {
      return scan;
    }
  }

  @override
  Future<MethodResult?> cancelScanning() {
    return _cancelScanning;
  }

  @override
  Future<MethodResult?> pairing(String macAddress) {
    return _pairing(macAddress);
  }

  @override
  Future<MethodResult?> connecting(String macAddress) {
    return _connecting(macAddress);
  }

  @override
  Future<EmvResult?> startEmv() {
    return _startEmv();
  }

  @override
  Future<MethodResult?> injectMasterKey() {
    return _injectMasterKey;
  }

  @override
  Future<MethodResult?> injectWorkKey() {
    return _injectWorkKey;
  }

  @override
  Future<MethodResult?> encryptData(String data) {
    return _encryptData(data);
  }

  @override
  Future<MethodResult?> decryptData(String data) {
    return _decryptData(data);
  }

  @override
  Stream<List<BluetoothClient?>> scanDevicesListener() {
    return scanEvent.receiveBroadcastStream().map((event) {
      var res = jsonDecode(event) as List;
      return res.map((e) => BluetoothClient.fromJson(e)).toList();
    });
  }

  @override
  Stream<ProgressMessage?> messageProgressListener() {
    return messageEvent.receiveBroadcastStream().map((event) {
      var res = jsonDecode(event);
      return ProgressMessage.fromJson(res);
    });
  }
}
