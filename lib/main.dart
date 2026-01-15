import 'package:drivero_automa/app.dart';
import 'package:drivero_automa/bootstrap.dart';
import 'package:drivero_automa/core/injector/service_locator.dart';
import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await initializeDateFormatting("id", null);
  await initServiceLocator();
  bootstrap(() => const App());
}
