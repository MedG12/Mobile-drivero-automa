import 'package:flutter/material.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/config/app_config.dart';

class CameraFooter extends StatelessWidget {
  final VoidCallback onCapture;

  const CameraFooter({
    super.key,
    required this.onCapture,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      // Ubah opacity jadi 0.4 agar transparan dan tidak menutupi kamera sepenuhnya
      color: MyTheme.color.black.withOpacity(0.4), 
      padding: EdgeInsets.symmetric(
        vertical: AppSetting.setHeight(20), 
        horizontal: AppSetting.setWidth(16)
      ),
      child: Center(
        child: IconButton(
          icon: Icon(
            Icons.camera_alt,
            size: AppSetting.setFontSize(36),
            color: Colors.white,
          ),
          style: IconButton.styleFrom(
            backgroundColor: MyTheme.color.secondary, // Warna tombol sesuai tema Anda
            shape: const CircleBorder(),
            padding: EdgeInsets.all(AppSetting.setWidth(18)), // Padding tombol
            side: BorderSide(
              color: MyTheme.color.white, 
              width: 2
            ), // Tambahan border putih biar terlihat jelas
          ),
          onPressed: onCapture,
        ),
      ),
    );
  }
}