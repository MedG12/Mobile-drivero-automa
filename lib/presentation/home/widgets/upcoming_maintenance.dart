// File: upcoming_maintenance_card.dart

import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:flutter_svg/svg.dart'; // pastiin path sesuai

class UpcomingMaintenanceCard extends StatelessWidget {
  final String date;
  final String title;
  final String service;
  final String plate;
  final VoidCallback? onTap;

  const UpcomingMaintenanceCard({
    super.key,
    required this.date,
    required this.title,
    required this.service,
    required this.plate,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(AppSetting.setWidth(16)),
      child: Material(
        color: MyTheme.color.white,
        child: InkWell(
          onTap: onTap,
          child: Container(
            padding: EdgeInsets.all(AppSetting.setWidth(16)),
            decoration: BoxDecoration(
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.05),
                  blurRadius: 8,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                // Baris untuk ikon dan tanggal
                Row(
                  children: [
                    SvgPicture.asset(
                      Assets.icons.cardJam, // otomatis dari assets.gen.dart
                      width: AppSetting.setWidth(18),
                      height: AppSetting.setWidth(18),
                      color: MyTheme.color.secondary,
                    ),
                    SizedBox(width: AppSetting.setWidth(8)),
                    Text(
                      date,
                      style: TextStyle(
                        fontFamily: FontFamily.inter,
                        color: MyTheme.color.secondary,
                        fontSize: AppSetting.setFontSize(12),
                      ),
                    ),
                  ],
                ),

                SizedBox(height: AppSetting.setHeight(8)),

                // Judul maintenance
                Text(
                  title,
                  style: TextStyle(
                    fontFamily: FontFamily.inter,
                    color: MyTheme.color.primary,
                    fontSize: AppSetting.setFontSize(16),
                    fontWeight: FontWeight.w700,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),

                SizedBox(height: AppSetting.setHeight(4)),

                // Deskripsi service
                Text(
                  service,
                  style: TextStyle(
                    fontFamily: FontFamily.inter,
                    color: MyTheme.color.secondary,
                    fontSize: AppSetting.setFontSize(12),
                    fontWeight: FontWeight.w400,
                  ),
                ),

                SizedBox(height: AppSetting.setHeight(4)),

                // Nomor plat kendaraan
                Text(
                  plate,
                  style: TextStyle(
                    fontFamily: FontFamily.inter,
                    color: MyTheme.color.secondary.withOpacity(0.8),
                    fontSize: AppSetting.setFontSize(12),
                    letterSpacing: AppSetting.setWidth(1.2),
                    fontWeight: FontWeight.w900,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
