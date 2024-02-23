// ignore_for_file: non_constant_identifier_names

part of '../microatmsdk.dart';

class _OptionViewController {
  Image IconBtn(int i) {
    if (i == 0) {
      return Image.asset(
        "assets/image/ic_tabungan.png",
        package: "microatmsdk",
        filterQuality: FilterQuality.high,
        scale: 4,
      );
    } else {
      return Image.asset(
        "assets/image/ic_giro.png",
        package: "microatmsdk",
        filterQuality: FilterQuality.high,
        scale: 3,
      );
    }
  }

  Text TitleBtn(int i) {
    if (i == 0) {
      return const Text(
        "Tabungan",
        style: TextStyle(color: Colors.black, fontWeight: FontWeight.w500),
      );
    } else {
      return const Text(
        "Giro",
        style: TextStyle(color: Colors.black, fontWeight: FontWeight.w500),
      );
    }
  }

  void Function() onPress(int i, BuildContext context) {
    return () async {
      if (i == 0) {
        _Util.gotoReplacePage(context, _EmvView());
      } else {
        print("GIRO");
      }
    };
  }
}
