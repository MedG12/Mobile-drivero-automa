import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar_v2/persistent_bottom_nav_bar_v2.dart';
import 'package:drivero_automa/config/app_config.dart'; // pastiin path sesuai

class VehicleCard extends StatefulWidget {
  const VehicleCard({super.key});

  @override
  State<VehicleCard> createState() => _VehicleCardState();
}

class _VehicleCardState extends State<VehicleCard> {
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(
        vertical: AppSetting.setHeight(10),
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(AppSetting.setWidth(24)),
        child: Material(
          color: MyTheme.color.white,
          child: InkWell(
            onTap: () {
              // Aksi ketika card ditekan
              // Contoh: Navigator.push(context, MaterialPageRoute(builder: (_) => SomePage()));
            },
            child: SizedBox(
              height: AppSetting.setHeight(140),
              child: Row(
                children: [
                  _buildImageSection(),
                  _buildDetailsSection(),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  // Bagian gambar
  Widget _buildImageSection() {
    return Expanded(
      flex: 2,
      child: Stack(
        fit: StackFit.expand,
        children: [
          Image.asset(
            Assets.images.mobilTest.path,
            fit: BoxFit.cover,
          ),
          Container(color: MyTheme.color.customBlue.withOpacity(0.50)),
        ],
      ),
    );
  }

  // Bagian detail
  Widget _buildDetailsSection() {
    return Expanded(
      flex: 3,
      child: Padding(
        padding: EdgeInsets.symmetric(
          horizontal: AppSetting.setWidth(16),
          vertical: AppSetting.setHeight(12),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // Chip model mobil
            Container(
              padding: EdgeInsets.symmetric(
                horizontal: AppSetting.setWidth(12),
                vertical: AppSetting.setHeight(6),
              ),
              decoration: BoxDecoration(
                color: MyTheme.color.primary,
                borderRadius: BorderRadius.circular(AppSetting.setWidth(20)),
              ),
              child: Text(
                'Tesla Model X',
                style: TextStyle(
                  fontFamily: FontFamily.inter,
                  fontWeight: FontWeight.w700,
                  color: MyTheme.color.white,
                  fontSize: AppSetting.setFontSize(12),
                ),
              ),
            ),
            SizedBox(height: AppSetting.setHeight(8)),

            // Nomor Polisi + Icon
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'AD1234 BCF',
                  style: TextStyle(
                    color: MyTheme.color.primary,
                    fontFamily: FontFamily.inter,
                    fontWeight: FontWeight.w900,
                    fontSize: AppSetting.setFontSize(16),
                  ),
                ),
                Icon(
                  Icons.chevron_right,
                  color: MyTheme.color.primary,
                  size: AppSetting.setWidth(28),
                ),
              ],
            ),
            SizedBox(height: AppSetting.setHeight(4)),

            // Info Maintenance
            Text(
              'Recent Maintenance on\n15 December 2024',
              style: TextStyle(
                color: MyTheme.color.secondary,
                fontSize: AppSetting.setFontSize(10),
                fontFamily: FontFamily.inter,
                fontWeight: FontWeight.w400,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
