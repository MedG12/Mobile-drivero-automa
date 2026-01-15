import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/theme.dart';

class LFramePainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = MyTheme.color.primary
      ..strokeWidth = 4
      ..style = PaintingStyle.stroke;

    final double cornerLength = 30.0;

    // Garis pojok kiri atas
    canvas.drawLine(Offset(0, 0), Offset(cornerLength, 0), paint);
    canvas.drawLine(Offset(0, 0), Offset(0, cornerLength), paint);

    // Garis pojok kanan atas
    canvas.drawLine(Offset(size.width, 0),
        Offset(size.width - cornerLength, 0), paint);
    canvas.drawLine(Offset(size.width, 0),
        Offset(size.width, cornerLength), paint);

    // Garis pojok kiri bawah
    canvas.drawLine(Offset(0, size.height),
        Offset(0, size.height - cornerLength), paint);
    canvas.drawLine(Offset(0, size.height),
        Offset(cornerLength, size.height), paint);

    // Garis pojok kanan bawah
    canvas.drawLine(Offset(size.width, size.height),
        Offset(size.width - cornerLength, size.height), paint);
    canvas.drawLine(Offset(size.width, size.height),
        Offset(size.width, size.height - cornerLength), paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) => false;
}
