part of '../microatmsdk.dart';

class BluetoothClient {
  late String name;
  late String macAddress;
  late bool bonded;

  BluetoothClient({
    this.name = "",
    this.macAddress = "",
    this.bonded = false,
  });
  BluetoothClient.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    macAddress = json['macAddress'];
    bonded = json['bonded'];
  }
  Map<String, dynamic> toJson() {
    final data = <String, dynamic>{};
    data['name'] = name;
    data['macAddress'] = macAddress;
    data['bonded'] = bonded;
    return data;
  }
}
