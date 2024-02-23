part of '../microatmsdk.dart';

class _ScanDialogController {
  final microSdk = Microatmsdk();
  List<BluetoothClient?>? client;
  ProgressMessage? progressMessage;
  double get contentHeigth {
    return (client != null && client!.length > 4) ? 250 : 150;
  }

  void Function() onPress(BluetoothClient device, BuildContext context) {
    return () async {
      if ((progressMessage?.hasProgress ?? false) &&
          progressMessage?.message != "Searching") {
        print("Please wait...");
        return;
      }
      if (device.bonded) {
        await microSdk.connecting(device.macAddress).then((value) {
          if (value?.data != null) {
            Navigator.pop(context, value);
            _Util.gotoPage(context, const _OptionsView());
            return;
          }
        });
      } else {
        var pair = await microSdk.pairing(device.macAddress);
        print(pair?.toJson());
      }
    };
  }

  SizedBox DummyContent() {
    return SizedBox(
      height: 250,
      child: Scrollbar(
        trackVisibility: true,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: SingleChildScrollView(
            child: Column(
              children: List.generate(100, (i) {
                return OutlinedButton(
                    onPressed: () async {},
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text("device.name $i"),
                        const Icon(
                          Icons.circle,
                          color: Colors.green,
                          size: 16,
                        )
                      ],
                    ));
              }),
            ),
          ),
        ),
      ),
    );
  }
}
