part of '../microatmsdk.dart';

class _EmvView extends StatefulWidget {
  @override
  State<_EmvView> createState() => _EmvViewState();
}

class _EmvViewState extends State<_EmvView> with _EmvViewController {
  ProgressMessage? progressMessage;
  @override
  void initState() {
    microAtm.startEmv().then((value) => onEmvDone(context));
    microAtm.messageProgressListener().listen((event) {
      if (!mounted) return;
      setState(() {
        progressMessage = event;
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
        child: Scaffold(
            appBar: AppBar(
              elevation: 0,
              leading: Container(),
              centerTitle: true,
              title: const Text(
                "Pilih Jenis Tabungan",
                style:
                    TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
              ),
              backgroundColor: Colors.grey[200],
            ),
            body: SizedBox(
              height: MediaQuery.of(context).size.height,
              width: MediaQuery.of(context).size.width,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Image.asset("assets/image/insert_card.gif",
                      filterQuality: FilterQuality.high,
                      package: "microatmsdk"),
                  Text(
                    "${progressMessage?.message}",
                    style: const TextStyle(fontSize: 18),
                  )
                ],
              ),
            )),
        onWillPop: () async {
          return Future.value(true);
        });
  }
}
