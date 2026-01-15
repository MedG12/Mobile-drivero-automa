import 'package:drivero_automa/core/widgets/automa_appbar.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class ScheduleHistoryWrapper extends StatelessWidget {
  ScheduleHistoryWrapper({super.key, required this.child});
  final Widget child;

  final GlobalKey<ScaffoldState> _key = GlobalKey();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _key,
      appBar: AutomaAppBar(scaffoldKey: _key),
      backgroundColor: MyTheme.color.darkBlue,
      
      // 🔥🔥🔥 TAMBAHKAN INI DI SINI JUGA 🔥🔥🔥
      // Agar background di belakang modal TIDAK ikut naik/gepeng saat keyboard muncul
      resizeToAvoidBottomInset: false, 

      drawer: Drawer(
        child: ListView(children: const [DrawerHeader(child: Text("Menu"))]),
      ),
      body: child,
    );
  }
}