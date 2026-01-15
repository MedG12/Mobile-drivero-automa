import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

class SosReasonSection extends StatelessWidget {
  final int selectedIndex;
  final ValueChanged<int> onSelected;

  const SosReasonSection({
    super.key,
    required this.selectedIndex,
    required this.onSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        children: [
          Text(
            "WHAT HAPPENED?",
            style: TextStyle(
              color: MyTheme.color.white,
              fontSize: AppSetting.setFontSize(16),
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(16)),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              _buildReasonButton(
                iconPath: Assets.icons.accident,
                label: "Accident",
                index: 0,
              ),
              _buildReasonButton(
                iconPath: Assets.icons.heartAttack,
                label: "Heart Attack",
                index: 1,
              ),
              _buildReasonButton(
                iconPath: Assets.icons.fatigue,
                label: "Fatigue",
                index: 2,
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildReasonButton({
    required String iconPath,
    required String label,
    required int index,
  }) {
    final bool isSelected = selectedIndex == index;
    final Color backgroundColor =
        isSelected ? MyTheme.color.white : MyTheme.color.lightBlue;
    final Color foregroundColor =
        isSelected ? MyTheme.color.primary : MyTheme.color.white;

    return GestureDetector(
      onTap: () => onSelected(index),
      child: Container(
        width: AppSetting.setWidth(100),
        height: AppSetting.setHeight(100),
        decoration: BoxDecoration(
          color: backgroundColor,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SvgPicture.asset(
              iconPath,
              width: AppSetting.setWidth(40),
              height: AppSetting.setHeight(40),
              colorFilter: ColorFilter.mode(foregroundColor, BlendMode.srcIn),
            ),
            SizedBox(height: AppSetting.setHeight(8)),
            Text(
              label,
              style: TextStyle(
                color: foregroundColor,
                fontWeight: FontWeight.w600,
                fontSize: AppSetting.setFontSize(12),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
