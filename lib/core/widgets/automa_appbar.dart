import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';

class AutomaAppBar extends StatelessWidget implements PreferredSizeWidget {
  const AutomaAppBar({
    super.key,
    required this.scaffoldKey,
    this.title,
    this.leading,
    this.actions,
    this.backgroundColor,
    this.elevation = 0,
    this.centerTitle = true,
    this.automaticallyImplyLeading = true,
  });

  final Widget? title;
  final Widget? leading;
  final List<Widget>? actions;
  final Color? backgroundColor;
  final double elevation;
  final bool centerTitle;
  final bool automaticallyImplyLeading;

  /// Used to open the drawer progammatically
  final GlobalKey<ScaffoldState> scaffoldKey;

  @override
  Widget build(BuildContext context) {
    return AppBar(
      leading: Padding(
        padding: EdgeInsets.only(
          left: AppSetting.setWidth(21),
          top: 8.0,
          bottom: 8.0,
        ),
        child: IconButton(
          onPressed: () => scaffoldKey.currentState?.openDrawer(),
          icon: Icon(Icons.menu, size: 24, color: MyTheme.color.primary),
        ),
      ),
      // leadingWidth: 100,
      backgroundColor: MyTheme.color.white,
      centerTitle: true,
      shadowColor: Colors.transparent,
      title: Image.asset(
        Assets.images.logo.path,
        height: AppSetting.setHeight(42),
      ),
      actionsPadding: EdgeInsets.all(32),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.only(
          bottomLeft: Radius.circular(40),
          bottomRight: Radius.circular(40),
        ),
      ),
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
