// ignore_for_file: non_constant_identifier_names

part of '../microatmsdk.dart';

class _OptionsView extends StatelessWidget with _OptionViewController {
  const _OptionsView();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        leading: Container(),
        centerTitle: true,
        title: const Text(
          "Pilih Jenis Tabungan",
          style: TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.grey[200],
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: Column(
          children: List.generate(2, (i) => OptionButton(i, context)),
        ),
      ),
    );
  }

  MaterialButton OptionButton(int i, BuildContext context) {
    return MaterialButton(
      padding: const EdgeInsets.all(0),
      onPressed: onPress(i, context),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          IconBtn(i),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    TitleBtn(i),
                    const IconButton(
                        alignment: Alignment.bottomCenter,
                        padding: EdgeInsets.all(0),
                        onPressed: null,
                        iconSize: 20,
                        icon: Icon(
                          Icons.arrow_forward_ios_outlined,
                          color: Colors.lightBlue,
                        ))
                  ],
                ),
                const Divider(thickness: 2, height: 30)
              ],
            ),
          )
        ],
      ),
    );
  }
}
