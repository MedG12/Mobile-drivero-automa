import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/theme.dart';

class VehicleDetailCard extends StatelessWidget {
  final String brand;
  final String regNumber;
  final String imageUrl;
  final String kms;
  final String insuredUntil;
  final String totalSpend;
  final String purchasedOn;

  const VehicleDetailCard({
    super.key,
    required this.brand,
    required this.regNumber,
    required this.imageUrl,
    required this.kms,
    required this.insuredUntil,
    required this.totalSpend,
    required this.purchasedOn,
  });

  @override
  Widget build(BuildContext context) {
    int gridCount = AppSetting.isTablet(context) ? 4 : 2;

    return Card(
      color: MyTheme.color.greyEF,
      elevation: 4,
      shadowColor: Colors.black.withOpacity(0.1),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(AppSetting.setWidth(24)),
      ),
      clipBehavior: Clip.antiAlias,
      child: Padding(
        padding: EdgeInsets.all(AppSetting.setWidth(16)),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            // IMAGE MOBIL
            AspectRatio(
              aspectRatio: 16 / 9,
              child: LayoutBuilder(
                builder: (context, constraints) {
                  final borderRadius = BorderRadius.circular(
                    constraints.maxWidth * 0.06,
                  );
                  return ClipRRect(
                    borderRadius: borderRadius,
                    child: Stack(
                      fit: StackFit.expand,
                      children: [
                        Image.network(
                          imageUrl,
                          fit: BoxFit.cover,
                          loadingBuilder: (context, child, progress) {
                            if (progress == null) return child;
                            return Container(
                              color: MyTheme.color.grey,
                              child: const Center(
                                child: CircularProgressIndicator(),
                              ),
                            );
                          },
                          errorBuilder: (context, error, stackTrace) {
                            return Container(
                              color: MyTheme.color.grey,
                              child: const Icon(
                                Icons.image_not_supported,
                                size: 60,
                                color: Colors.grey,
                              ),
                            );
                          },
                        ),
                        Container(
                          decoration: BoxDecoration(
                            color: MyTheme.color.customBlue.withOpacity(0.5),
                            borderRadius: borderRadius,
                          ),
                        ),
                      ],
                    ),
                  );
                },
              ),
            ),
            Space.h(16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Container(
                  padding: EdgeInsets.symmetric(
                    horizontal: AppSetting.setWidth(39),
                    vertical: AppSetting.setHeight(3),
                  ),
                  decoration: BoxDecoration(
                    color: MyTheme.color.primary,
                    borderRadius: BorderRadius.circular(
                      AppSetting.setWidth(20),
                    ),
                  ),
                  child: Text(
                    brand,
                    style: TextStyle(
                      fontFamily: FontFamily.inter,
                      fontWeight: FontWeight.w700,
                      color: MyTheme.color.white,
                      fontSize: AppSetting.setFontSize(18),
                    ),
                  ),
                ),
                Text(
                  regNumber,
                  style: TextStyle(
                    color: MyTheme.color.primary,
                    fontFamily: FontFamily.inter,
                    fontWeight: FontWeight.w900,
                    fontSize: AppSetting.setFontSize(16),
                  ),
                ),
              ],
            ),
            Space.h(16),
            GridView.count(
              crossAxisCount: gridCount,
              crossAxisSpacing: AppSetting.setWidth(12),
              mainAxisSpacing: AppSetting.setHeight(12),
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              children: [
                _InfoCard(
                  title: "KMS Driver",
                  subtitle: kms,
                  svgPath: Assets.icons.kms,
                  iconColor: MyTheme.color.primary,
                ),
                _InfoCard(
                  title: "Insured Until",
                  subtitle: insuredUntil,
                  svgPath: Assets.icons.insurance,
                  iconColor: MyTheme.color.primary,
                ),
                _InfoCard(
                  title: "Total Spend",
                  subtitle: totalSpend,
                  svgPath: Assets.icons.dollar,
                  iconColor: MyTheme.color.primary,
                ),
                _InfoCard(
                  title: "Purchased On",
                  subtitle: purchasedOn,
                  svgPath: Assets.icons.cardJam,
                  iconColor: MyTheme.color.primary,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class _InfoCard extends StatelessWidget {
  final String title;
  final String subtitle;
  final String svgPath;
  final Color iconColor;

  const _InfoCard({
    required this.title,
    required this.subtitle,
    required this.svgPath,
    this.iconColor = Colors.black,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: AppSetting.setWidth(180),
      height: AppSetting.setHeight(100),
      decoration: BoxDecoration(
        color: MyTheme.color.white,
        borderRadius: BorderRadius.circular(AppSetting.setWidth(18)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 8,
            spreadRadius: 2,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Padding(
        padding: EdgeInsets.all(AppSetting.setWidth(16)),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SvgPicture.asset(
              svgPath,
              width: AppSetting.setWidth(24),
              height: AppSetting.setHeight(24),
              color: iconColor,
            ),
            Space.h(12),
            Text(
              title,
              style: TextStyle(
                fontSize: AppSetting.setFontSize(16),
                fontWeight: FontWeight.w700,
                color: MyTheme.color.primary,
              ),
            ),
            Space.h(4),
            Text(
              subtitle,
              style: TextStyle(
                fontSize: AppSetting.setFontSize(13),
                fontWeight: FontWeight.w700,
                color: MyTheme.color.primary,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
