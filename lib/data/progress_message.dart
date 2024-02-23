part of '../microatmsdk.dart';

class ProgressMessage {
  late String message;
  late bool hasProgress;

  ProgressMessage({
    this.message = "",
    this.hasProgress = false,
  });
  ProgressMessage.fromJson(Map<String, dynamic> json) {
    message = json['message'];
    hasProgress = json['hasProgress'];
  }
  Map<String, dynamic> toJson() {
    final data = <String, dynamic>{};
    data['message'] = message;
    data['hasProgress'] = hasProgress;
    return data;
  }
}
