import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class VehicleDetailScreen extends StatelessWidget {
  const VehicleDetailScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return BackgroundPageWidget(
      child: Center(
        child: Text(
          'Vehicle Detail',
          style: TextStyle(color: MyTheme.color.white),
        ),
      ),
    );
  }
}
