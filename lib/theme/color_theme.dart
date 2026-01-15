import 'package:flutter/material.dart';

class PColor {
  // Singleton instance
  static final PColor _instance = PColor._internal();

  // Factory constructor returns the same instance
  factory PColor() => _instance;

  // Private constructor
  PColor._internal();

  // Define colors with default values
  Color primary = const Color(0xFF013972);
  Color secondary = const Color(0xFF2170BF);
  Color success = const Color(0xFF4BAC87);
  Color info = const Color(0xFF05BFDB);
  Color warning = const Color(0xFFF19616);
  Color danger = const Color(0xFF900B09);
  Color disabled = const Color(0xFF1D1B20).withOpacity(0.12);
  Color white = const Color(0xFFFFFFFF);
  Color grey = const Color(0xFFAEAEB2);
  Color black = const Color(0xFF000000);
  Color greyEF = const Color(0xFFEFEFEF); 
  // Additional gradient colors
  Color darkBlue = const Color(0xFF19497A);
  Color lightBlue = const Color(0xFF2E86E0);
  Color lightDarkBlue = const Color(0xFF35679A);
  Color darkestBlue = const Color(0xFF043260);
  Color grey200 = const Color(0xFFEEEEEE); // sama dengan Colors.grey[200]
  Color customBlue = const Color(0x80043260);
 Color greyF0 = const Color(0xFFF0F0F0);
  // ✨ Warna baru
  Color lightSkyBlue = const Color(0xFFC3DAF2); // Hex #C3DAF2

  // Gradients lama
  LinearGradient get darkLightBlueGradientLeftToRight => LinearGradient(
        colors: [darkBlue, lightBlue],
        begin: Alignment.centerLeft,
        stops: [0.0, 1.0],
      );

  LinearGradient get lightDarkBlueGradientLeftToRight => LinearGradient(
        colors: [lightDarkBlue, darkBlue],
        begin: Alignment.centerLeft,
        stops: [0.0, 1.0],
      );

  LinearGradient get darkLightDarkBlueGradientTopToBottom => LinearGradient(
        colors: [darkBlue, secondary, darkBlue],
        stops: [0.0, 0.445, 1.0],
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
      );

  // ✨ Gradient baru: putih ke lightSkyBlue
  LinearGradient get whiteToLightSkyBlueGradientLeftToRight => LinearGradient(
        colors: [white, lightSkyBlue],
        begin: Alignment.centerLeft,
        end: Alignment.centerRight,
      );

  get gradient => null;
}
