// ignore_for_file: no_leading_underscores_for_local_identifiers, non_constant_identifier_names

part of '../microatmsdk.dart';

class Scanning {
  static Future<MethodResult?> show(BuildContext context) async {
    var _show = await showDialog<MethodResult>(
        context: context,
        builder: (context) {
          return const _ScanDialog();
        });
    _MicroController.updateLog("${_show?.toJson()}");
    return _show ?? await Future.value(null);
  }
}

class _ScanDialog extends StatefulWidget {
  const _ScanDialog();

  @override
  State<_ScanDialog> createState() => _ScanDialogState();
}

class _ScanDialogState extends State<_ScanDialog> with _ScanDialogController {
  @override
  void initState() {
    microSdk.messageProgressListener().listen((event) {
      if (!mounted) return;
      setState(() {
        progressMessage = event;
      });
    });
    microSdk.scanDevicesListener().listen((event) {
      if (!mounted) return;
      setState(() {
        client = event;
      });
    });

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        return Future.value(false);
      },
      child: AlertDialog(
        contentPadding: const EdgeInsets.all(0),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
        title: Title(),
        content: Content(),
        actions: [
          OutlinedButton(
              onPressed: () async {
                await microSdk.cancelScanning().then((value) {
                  Navigator.pop(context, value);
                });
              },
              child: const Text("cancel"))
        ],
      ),
    );
  }

  SizedBox Content() {
    return SizedBox(
      height: contentHeigth,
      child: Scrollbar(
        trackVisibility: true,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: SingleChildScrollView(
            child: Column(
              children: List.generate(client?.length ?? 0, (i) {
                var device = client![i]!;
                return OutlinedButton(
                    onPressed: onPress(device, context),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(device.name),
                        Icon(
                          Icons.circle,
                          color:
                              device.bonded ? Colors.green : Colors.deepOrange,
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

  Widget Title() {
    return Column(
      children: [
        Row(
          children: [
            Image.asset(
              "assets/image/logo_bni.png",
              package: "microatmsdk",
              scale: 15,
            ),
            const SizedBox(width: 10),
            Flexible(
              child: Text(
                "${progressMessage?.message}",
                style:
                    const TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
              ),
            ),
          ],
        ),
        Builder(builder: (context) {
          if (progressMessage?.hasProgress ?? false) {
            return Column(
              children: const [
                SizedBox(height: 10),
                LinearProgressIndicator(
                    color: Color.fromARGB(255, 247, 116, 45)),
                SizedBox(height: 10),
              ],
            );
          } else {
            return const Divider(thickness: 2);
          }
        })
      ],
    );
  }
}
