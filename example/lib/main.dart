import 'package:flutter/material.dart';
import 'package:microatmsdk/microatmsdk.dart';

void main() {
  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    home: MyApp(),
  ));
}

class MyApp extends StatelessWidget {
  MyApp({super.key});
  final Microatmsdk microatmsdk = Microatmsdk();

  TextEditingController encryptionController = TextEditingController(
      text: List.generate(16, (i) => i).join("").replaceRange(16, null, ""));
  TextEditingController decryptionController = TextEditingController();
  double getWitdh(BuildContext context) {
    return MediaQuery.of(context).size.width;
  }

  double getHeight(BuildContext context) {
    return MediaQuery.of(context).size.height;
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        top: true,
        child: Scaffold(
          appBar: AppBar(
            title: const Text("Micro ATM SDK"),
            elevation: 0,
            backgroundColor: Colors.grey[500],
            centerTitle: true,
          ),
          body: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
              child: Column(
                children: [
                  SizedBox(
                    width: getWitdh(context),
                    child: OutlinedButton(
                        onPressed: () async {
                          var res = await microatmsdk.scanDevices(context);
                          print(res?.toJson());
                        },
                        child: const Text("scan devices")),
                  ),
                  SizedBox(
                    width: getHeight(context),
                    child: OutlinedButton(
                        onPressed: () async {
                          var res = await microatmsdk.checkPermission();
                          print(res?.toJson());
                        },
                        child: const Text("check permission")),
                  ),
                  SizedBox(
                    width: getWitdh(context),
                    child: OutlinedButton(
                        onPressed: () async {
                          var res = await microatmsdk.pairing("");
                          print(res?.toJson());
                        },
                        child: const Text("device pairing")),
                  ),
                  SizedBox(
                    width: getWitdh(context),
                    child: OutlinedButton(
                        onPressed: () async {
                          var res = await microatmsdk.connecting("");
                          print(res?.toJson());
                        },
                        child: const Text("device connecting")),
                  ),
                  SizedBox(
                    width: getWitdh(context),
                    child: OutlinedButton(
                        onPressed: () async {
                          var res = await microatmsdk.startEmv();
                          print(res?.toJson());
                        },
                        child: const Text("start emv")),
                  ),
                  const SizedBox(height: 20),
                  textField("Encryption", true),
                  textField("Decryption", false),
                  const SizedBox(height: 30),
                  microatmsdk.SdkLog()
                ],
              ),
            ),
          ),
        ));
  }

  TextField textField(String label, bool hasEncryption) {
    return TextField(
      buildCounter: counter,
      textInputAction: TextInputAction.done,
      controller: hasEncryption ? encryptionController : decryptionController,
      onSubmitted: (value) async {
        if (hasEncryption) {
          var res = await microatmsdk.encryptData(value);
          print(res?.toJson());
        } else {
          var res = await microatmsdk.decryptData(value);
          print(res?.toJson());
        }
      },
      decoration: InputDecoration(
          border: const OutlineInputBorder(),
          label: Text(label),
          floatingLabelBehavior: FloatingLabelBehavior.always),
    );
  }

  Widget counter(
    BuildContext context, {
    required int currentLength,
    required int? maxLength,
    required bool isFocused,
  }) {
    return Text(
      '$currentLength',
      semanticsLabel: 'character count',
      style: const TextStyle(
          color: Colors.deepOrange, fontWeight: FontWeight.bold, fontSize: 10),
    );
  }
}
