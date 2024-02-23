// ignore: file_names
part of '../microatmsdk.dart';

class EmvResult {
  late String communication;
  late String cardType;
  late String pan;
  late String t2d;
  late String icCardData;
  late String expDate;

  EmvResult({
    this.communication = "",
    this.cardType = "",
    this.pan = "",
    this.t2d = "",
    this.icCardData = "",
    this.expDate = "",
  });
  EmvResult.fromJson(Map<String, dynamic> json) {
    communication = json['communication'];
    cardType = json['cardType'];
    pan = json['pan'];
    t2d = json['t2d'];
    icCardData = json['icCardData'];
    expDate = json['expDate'];
  }
  Map<String, dynamic> toJson() {
    final data = <String, dynamic>{};
    data['communication'] = communication;
    data['cardType'] = cardType;
    data['pan'] = pan;
    data['t2d'] = t2d;
    data['icCardData'] = icCardData;
    data['expDate'] = expDate;
    return data;
  }
}
