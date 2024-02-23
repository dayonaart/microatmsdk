part of '../microatmsdk.dart';

class MethodResult {
  late String message;
  dynamic data;

  MethodResult({
    this.message = "",
    this.data,
  });
  MethodResult.fromJson(Map<String, dynamic> json) {
    message = json['message'];
    data = json['data'];
  }
  Map<String, dynamic> toJson() {
    final data = <String, dynamic>{};
    data['message'] = message;
    data['data'] = this.data;
    return data;
  }
}
