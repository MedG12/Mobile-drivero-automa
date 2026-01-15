import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

/// Text Field umum
class CustomTextField extends StatelessWidget {
  final String? label;
  final TextEditingController? controller;
  final Widget? icon;
  final bool readOnly;
  final String? initialValue;
  final Future<void> Function()? onTap; // ✅ tambahan onTap

  const CustomTextField({
    super.key,
    this.label,
    this.controller,
    this.icon,
    this.readOnly = false,
    this.initialValue,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final Color bgColor = readOnly ? Colors.grey.shade200 : Colors.white;
    final Color textColor = readOnly
        ? Colors.grey.shade600
        : MyTheme.color.primary;

    // Jika readOnly dan tidak ada controller → tampilkan container tappable
    if (readOnly && controller == null) {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (label != null)
            Text(
              label!,
              style: AppTextTheme.headline5.copyWith(color: Colors.white),
            ),
          SizedBox(height: AppSetting.setHeight(8)),
          GestureDetector(
            onTap: onTap,
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
              decoration: BoxDecoration(
                color: bgColor,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Text(
                      initialValue ?? '-',
                      style: AppTextTheme.bodyText1.copyWith(color: textColor),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                  if (icon != null) ...[
                    const SizedBox(width: 8),
                    IconTheme(
                      data: IconThemeData(color: Colors.grey.shade500),
                      child: icon!,
                    ),
                  ],
                ],
              ),
            ),
          ),
        ],
      );
    }

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
          readOnly: readOnly,
          style: AppTextTheme.bodyText1.copyWith(color: MyTheme.color.primary),
          controller: controller,
          onTap: onTap,
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
