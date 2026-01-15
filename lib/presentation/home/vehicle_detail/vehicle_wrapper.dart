import 'package:drivero_automa/core/widgets/automa_appbar.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class VehicleWrapper extends StatelessWidget {
   VehicleWrapper({super.key, required this.child});
  final Widget child;

  final GlobalKey<ScaffoldState> _key =  GlobalKey();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _key,
      appBar: AutomaAppBar(
        scaffoldKey: _key,
       // ← tambahkan parameter agar judul kosong
      ),
      backgroundColor: MyTheme.color.darkBlue,
      drawer: Drawer(
        child: ListView(children: const [DrawerHeader(child: Text("Menu"))]),
      ),
      body: child,
    );
  }
}
