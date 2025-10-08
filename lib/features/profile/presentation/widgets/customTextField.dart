import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

/// Text Field umum
class CustomTextField extends StatelessWidget {
  final String? label;
  final TextEditingController controller;
  final Widget? icon;
  final bool readOnly;

  const CustomTextField({
    super.key,
    this.label,
    required this.controller,
    this.icon,
    this.readOnly = false,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        if (label != null)
          Align(
            alignment: Alignment.centerLeft,
            child: Text(
              label!,
              style: AppTextTheme.headline5.copyWith(color: Colors.white),
            ),
          ),
        SizedBox(height: AppSetting.setHeight(8)),
        TextField(
          style: AppTextTheme.bodyText1.copyWith(color: MyTheme.color.primary),
          controller: controller,
          decoration: InputDecoration(
            isDense: true,
            contentPadding: const EdgeInsets.symmetric(
              horizontal: 16,
              vertical: 10,
            ),
            filled: true,
            fillColor: Colors.white,
            suffixIcon: icon,
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide.none,
            ),
          ),
        ),
      ],
    );
  }
}
