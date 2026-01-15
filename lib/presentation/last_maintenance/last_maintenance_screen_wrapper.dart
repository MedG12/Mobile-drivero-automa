import 'package:drivero_automa/core/widgets/automa_appbar.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class last_maintenance_screen_wrapper extends StatelessWidget {
  last_maintenance_screen_wrapper({super.key, required this.child});
  final Widget child;

  final GlobalKey<ScaffoldState> _key = GlobalKey(); // Create a key

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _key,
      appBar: AutomaAppBar(scaffoldKey: _key),
      drawer: Drawer(
        child: ListView(
          children: const [DrawerHeader(child: Text("Menu"))],
        ),
      ),
      body: Container(
        decoration: BoxDecoration(
          gradient: MyTheme.color.darkLightDarkBlueGradientTopToBottom,
        ),
        child: child,
      ),
    );
  }
}
