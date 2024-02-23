part of '../microatmsdk.dart';

class _LogView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        Scrollbar(
          child: TextField(
            readOnly: true,
            scrollController: _MicroController.scollController,
            controller: _MicroController.logController,
            minLines: 3,
            maxLines: 8,
            textAlignVertical: TextAlignVertical.center,
            style: const TextStyle(
                fontSize: 10, letterSpacing: 2, height: 2, color: Colors.green),
            decoration: const InputDecoration(
                filled: true,
                fillColor: Colors.black,
                contentPadding: EdgeInsets.all(20),
                border: OutlineInputBorder(),
                label: Card(
                    color: Colors.deepOrange,
                    child: Padding(
                      padding:
                          EdgeInsets.symmetric(horizontal: 20, vertical: 6),
                      child: Text("Log",
                          style: TextStyle(
                              color: Colors.white,
                              fontWeight: FontWeight.bold,
                              fontSize: 20)),
                    )),
                floatingLabelBehavior: FloatingLabelBehavior.always),
          ),
        ),
        OutlinedButton(
            onPressed: () {
              _MicroController.logController.clear();
            },
            child: const Text("clear log"))
      ],
    );
  }
}
