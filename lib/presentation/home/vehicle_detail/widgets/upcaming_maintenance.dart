import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/theme.dart';

class UpcomingMaintenance extends StatelessWidget {
  final String title;
  final String dateTime;
  final String location;
  final VoidCallback? onTapReminder;

  const UpcomingMaintenance({
    super.key,
    required this.title,
    required this.dateTime,
    required this.location,
    this.onTapReminder,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(bottom: AppSetting.setHeight(16)),
      padding: EdgeInsets.all(AppSetting.setWidth(24)),
      decoration: BoxDecoration(
        color: MyTheme.color.white,
        borderRadius: BorderRadius.circular(AppSetting.setWidth(30)),
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
        children: [
          // 1. Judul
          Text(
            title,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(18),
              fontWeight: FontWeight.bold,
              color: MyTheme.color.primary,
            ),
          ),
          SizedBox(height: AppSetting.setHeight(20)),

          // 2. Informasi Waktu & Lokasi
          _buildInfoRow(Icons.access_time_filled, dateTime),
          SizedBox(height: AppSetting.setHeight(12)),
          _buildInfoRow(Icons.location_on, location),

          SizedBox(height: AppSetting.setHeight(24)),

          // 3. Tombol Set Reminder
          SizedBox(
            width: double.infinity,
            height: AppSetting.setHeight(48),
            child: ElevatedButton(
              onPressed: onTapReminder ?? () {},
              style: ElevatedButton.styleFrom(
                backgroundColor: MyTheme.color.primary,
                foregroundColor: MyTheme.color.white,
                elevation: 0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(AppSetting.setWidth(24)),
                ),
              ),
              child: Text(
                "Set Reminder",
                style: TextStyle(
                  fontSize: AppSetting.setFontSize(15),
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildInfoRow(IconData icon, String text) {
    return Row(
      children: [
        Icon(
          icon,
          size: AppSetting.setWidth(20),
          color: MyTheme.color.primary,
        ),
        SizedBox(width: AppSetting.setWidth(12)),
        Expanded(
          child: Text(
            text,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(14),
              fontWeight: FontWeight.w500,
              color: MyTheme.color.primary,
            ),
          ),
        ),
      ],
    );
  }
}
