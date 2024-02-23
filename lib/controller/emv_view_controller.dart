part of '../microatmsdk.dart';

class _EmvViewController {
  final microAtm = Microatmsdk();

  void onEmvDone(BuildContext context) {
    _Util.gotoReplacePage(context, const _FinalView());
  }
}
