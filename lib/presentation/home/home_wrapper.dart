import 'package:drivero_automa/core/injector/service_locator.dart';
import 'package:drivero_automa/core/widgets/automa_appbar.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_state.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_bloc.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_event.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:drivero_automa/core/widgets/automa_appbar.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class HomeWrapper extends StatelessWidget {
  final Widget child;

  const HomeWrapper({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    final GlobalKey<ScaffoldState> _key = GlobalKey();

    return Scaffold(
      key: _key,
      appBar: AutomaAppBar(
        scaffoldKey: _key,
      ),
      backgroundColor: MyTheme.color.darkBlue,
      drawer: Drawer(
        child: ListView(
          children: const [
            DrawerHeader(child: Text("Menu")),
          ],
        ),
      ),
      body: child,
    );
  }
}
