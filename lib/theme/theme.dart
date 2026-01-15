import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/color_theme.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

TextStyle defaultTextStyle(BuildContext context) {
  return GoogleFonts.inter();
}

const double defaultPadding = MyTheme.defaultPadding;

class MyTheme {
  static ThemeData theme = AppTheme.lightTheme;

  static const double defaultPadding = 16;

  static PColor color = PColor();
}
