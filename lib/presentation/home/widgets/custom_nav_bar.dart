import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar_v2/persistent_bottom_nav_bar_v2.dart';

class CustomNavBar extends StatelessWidget {
  const CustomNavBar({
    required this.navBarConfig,
    this.navBarDecoration = const NavBarDecoration(),
    this.itemAnimationProperties = const ItemAnimation(),
    this.itemPadding = const EdgeInsets.all(5),
    this.height,
    super.key,
  });

  final NavBarConfig navBarConfig;
  final NavBarDecoration navBarDecoration;
  final EdgeInsets itemPadding;
  final double? height;

  /// This controls the animation properties of the items of the NavBar.
  final ItemAnimation itemAnimationProperties;

  Size getTextSize(ItemConfig item) => (TextPainter(
    text: TextSpan(text: item.title, style: item.textStyle),
    maxLines: 1,
    textDirection: TextDirection.ltr,
  )..layout()).size;

  Widget _buildItem(ItemConfig item, bool isSelected) {
    return AnimatedContainer(
      // dynamic width depending on the text size and icon size
      /// 32 Icon Size
      // width: isSelected
      //     ? getTextSize(item).width + AppSetting.setWidth(61)
      //     : AppSetting.setWidth(60),

      /// 24 Icon size
      width: isSelected
          ? getTextSize(item).width + AppSetting.setWidth(51)
          : AppSetting.setWidth(51),
      duration: itemAnimationProperties.duration,
      curve: itemAnimationProperties.curve,
      padding: itemPadding,
      decoration: BoxDecoration(
        gradient: isSelected
            ? MyTheme.color.darkLightBlueGradientLeftToRight
            : null,
        borderRadius: const BorderRadius.all(Radius.circular(100)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          isSelected ? item.icon : item.inactiveIcon,
          if (item.title != null && isSelected)
            Flexible(
              child: Padding(
                padding: const EdgeInsets.only(left: 8),
                child: FittedBox(
                  child: Text(
                    item.title!,
                    style: item.textStyle.apply(
                      color: isSelected
                          ? item.activeForegroundColor
                          : item.inactiveForegroundColor,
                    ),
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) => DecoratedNavBar(
    decoration: navBarDecoration,
    height: height,
    child: Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: navBarConfig.items.map((item) {
        final int index = navBarConfig.items.indexOf(item);
        return GestureDetector(
          onTap: () {
            navBarConfig.onItemSelected(index);
          },
          child: _buildItem(item, navBarConfig.selectedIndex == index),
        );
      }).toList(),
    ),
  );
}
