import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class SosContentCard extends StatelessWidget {
  final String countdownText;
  final VoidCallback onCancel;

  const SosContentCard({
    super.key,
    required this.countdownText,
    required this.onCancel,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(
        horizontal: AppSetting.setWidth(24),
        vertical: AppSetting.setHeight(32),
      ),
      decoration: BoxDecoration(
        color: MyTheme.color.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        children: [
          Text(
            "Hi, Beji Driver",
            style: TextStyle(
              fontSize: AppSetting.setFontSize(16),
              fontWeight: FontWeight.bold,
              color: MyTheme.color.black,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(4)),
          Text(
            "You're about to call emergency",
            style: TextStyle(
              fontSize: AppSetting.setFontSize(14),
              color: MyTheme.color.black,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(16)),
          Text(
            "This call will be started automatically in...",
            style: TextStyle(
              fontSize: AppSetting.setFontSize(12),
              fontWeight: FontWeight.bold,
              color: MyTheme.color.primary,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(24)),
          Text(
            countdownText,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(64),
              fontWeight: FontWeight.bold,
              color: MyTheme.color.primary,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(24)),
          Text(
            "Your Location Coordinates:",
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(12),
              color: MyTheme.color.black.withOpacity(0.6),
            ),
          ),
          SizedBox(height: AppSetting.setHeight(4)),
          Text(
            "VVJ8+2FG, RT.12/RW.15, Tanjung Priok, North Jakarta City, Jakarta 14310",
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(14),
              fontWeight: FontWeight.w600,
              color: MyTheme.color.black,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(32)),
          SizedBox(
            width: double.infinity,
            child: ElevatedButton(
              onPressed: onCancel,
              
              style:ElevatedButton.styleFrom(
                                    backgroundColor: MyTheme.color.primary,
                                    foregroundColor: MyTheme.color.white,
                padding: EdgeInsets.symmetric(
                  vertical: AppSetting.setHeight(16),
                ),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
              ),
              child: Text(
                "Cancel",
                style: TextStyle(
                  fontSize: AppSetting.setFontSize(16),
                  fontWeight: FontWeight.bold,
                  color: MyTheme.color.white,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
