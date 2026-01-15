import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class BackgroundPageWidget extends StatelessWidget {
  const BackgroundPageWidget({super.key, required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        gradient: MyTheme.color.darkLightDarkBlueGradientTopToBottom,
      ),
      child: child,
    );
  }
}
