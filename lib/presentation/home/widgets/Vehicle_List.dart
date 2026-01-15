import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/routes/app_router.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

class VehicleCard extends StatelessWidget {
  final DriverFleetEntity fleet;
  final String imageUrl;

  const VehicleCard({super.key, required this.fleet, required this.imageUrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: AppSetting.setHeight(10)),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(AppSetting.setWidth(24)),
        child: Material(
          color: MyTheme.color.white,
          child: InkWell(
            onTap: () {
              context.goNamed(AppRouter.vehicleDetail, extra: fleet);
            },
            child: SizedBox(
              height: AppSetting.setHeight(140),
              child: Row(
                children: [
                  _buildImageSection(),
                  _buildDetailsSection(), // [PERBAIKAN] Kode dipecah agar rapi
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  // Bagian gambar (tidak banyak berubah)
  Widget _buildImageSection() {
    return Expanded(
      flex: 2,
      child: Stack(
        fit: StackFit.expand,
        children: [
          Image.network(
            imageUrl,
            fit: BoxFit.cover,
            // Widget penanganan jika gambar gagal dimuat
            errorBuilder: (context, error, stackTrace) => Container(
              color: Colors.grey[200],
              child: Icon(
                Icons.directions_car,
                color: Colors.grey[400],
                size: 40,
              ),
            ),
          ),
          Container(color: MyTheme.color.customBlue.withOpacity(0.50)),
        ],
      ),
    );
  }

  // [PERBAIKAN] Bagian detail yang didesain ulang sepenuhnya
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
                fleet.brand, // Menggunakan data dinamis
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
                  fleet.licensePlat, // Menggunakan data dinamis
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

            // Tampilkan info maintenance HANYA jika datanya ada
            if (fleet.lastMaintenance != null) ...[
              SizedBox(height: AppSetting.setHeight(4)),
              Text(
                "Recent maintenance on ${DateFormat('dd MMMM yyyy').format(fleet.lastMaintenance!)}",
                style: TextStyle(
                  color: MyTheme.color.secondary,
                  fontSize: AppSetting.setFontSize(11),
                  fontFamily: FontFamily.inter,
                  fontWeight: FontWeight.w400,
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
