// File: next_maintenance_card.dart

import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:flutter_svg/svg.dart';

class CompletedMaintenanceCard extends StatelessWidget {
  final String date;
  final String title;
  final String service;
  final String plate;
  final VoidCallback? onTap;
  final VoidCallback? onDetailTap;

  const CompletedMaintenanceCard({
    super.key,
    required this.date,
    required this.title,
    required this.service,
    required this.plate,
    this.onTap,
    this.onDetailTap,
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
                  color: MyTheme.color.black.withOpacity(0.05),
                  blurRadius: 8,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: Stack(
              children: [
                // Konten utama
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Row ikon + tanggal
                    Row(
                      children: [
                        SvgPicture.asset(
                          Assets.icons.cardJam,
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

                    // Service description
                    Text(
                      service,
                      style: TextStyle(
                        fontFamily: FontFamily.inter,
                        color: MyTheme.color.secondary,
                        fontSize: AppSetting.setFontSize(12),
                        fontWeight: FontWeight.w400,
                      ),
                    ),
                    SizedBox(height: AppSetting.setHeight(12)),

                    // Plat + Tombol Show Detail di row bawah
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Row(
                          children: [
                            SvgPicture.asset(
                              Assets.icons.carr,
                              width: AppSetting.setWidth(16),
                              height: AppSetting.setWidth(16),
                              color: MyTheme.color.secondary,
                            ),
                            SizedBox(width: AppSetting.setWidth(6)),
                            Text(
                              plate,
                              style: TextStyle(
                                fontFamily: FontFamily.inter,
                                color: MyTheme.color.secondary.withOpacity(0.8),
                                fontSize: AppSetting.setFontSize(12),
                                fontWeight: FontWeight.w900, // bold
                                letterSpacing: 0, // hilangkan jarak huruf
                              ),
                            ),
                          ],
                        ),
                          GestureDetector(
                          onTap: onDetailTap,
                          child: Container(
                            padding: EdgeInsets.symmetric(
                              vertical: AppSetting.setHeight(4),
                              horizontal: AppSetting.setWidth(12),
                            ),
                            decoration: BoxDecoration(
                              color: MyTheme.color.white,
                              borderRadius: BorderRadius.circular(100),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.05),
                                  blurRadius: 4,
                                  offset: const Offset(0, 2),
                                ),
                              ],
                            ),
                            child: Text(
                              "Show InVoice",
                              style: TextStyle(
                                fontSize: AppSetting.setFontSize(12),
                                color: MyTheme.color.primary,
                                fontFamily: FontFamily.inter,
                                fontWeight: FontWeight.w700,
                              ),
                            ),
                          ),
                        ),
                        GestureDetector(
                          onTap: onDetailTap,
                          child: Container(
                            padding: EdgeInsets.symmetric(
                              vertical: AppSetting.setHeight(4),
                              horizontal: AppSetting.setWidth(12),
                            ),
                            decoration: BoxDecoration(
                              color: MyTheme.color.primary,
                              borderRadius: BorderRadius.circular(100),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.05),
                                  blurRadius: 4,
                                  offset: const Offset(0, 2),
                                ),
                              ],
                            ),
                            child: Text(
                              "Show Detail",
                              style: TextStyle(
                                fontSize: AppSetting.setFontSize(12),
                                color: MyTheme.color.white,
                                fontFamily: FontFamily.inter,
                                fontWeight: FontWeight.w700,
                              ),
                            ),
                          ),
                        ),
                       
                      ],
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
