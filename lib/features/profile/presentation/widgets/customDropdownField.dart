import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class CustomDropdown extends StatelessWidget {
  final String? label;
  final List<String> items;
  final String value;
  final Function(String?) onChanged;
  final IconData? defaultIcon;
  final IconData Function(String value)? iconBuilder;
  // ⬅️ fungsi custom untuk menentukan ikon per item

  const CustomDropdown({
    super.key,
    this.label,
    required this.items,
    required this.value,
    required this.onChanged,
    this.defaultIcon,
    this.iconBuilder,
  });

  @override
  Widget build(BuildContext context) {
    IconData suffixIcon = defaultIcon ?? Icons.arrow_drop_down;

    if (iconBuilder != null) {
      suffixIcon = iconBuilder!(value);
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (label != null)
          Text(
            label!,
            style: AppTextTheme.headline5.copyWith(color: Colors.white),
          ),
        SizedBox(height: AppSetting.setHeight(8)),
        DropdownButtonFormField<String>(
          style: AppTextTheme.bodyText1.copyWith(color: MyTheme.color.primary),
          value: value,
          items: items
              .map((e) => DropdownMenuItem(value: e, child: Text(e)))
              .toList(),
          onChanged: onChanged,
          icon: Icon(suffixIcon, color: Colors.blue),
          decoration: InputDecoration(
            isDense: true,
            contentPadding: const EdgeInsets.symmetric(
              horizontal: 16,
              vertical: 10,
            ),
            filled: true,
            fillColor: Colors.white,
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
