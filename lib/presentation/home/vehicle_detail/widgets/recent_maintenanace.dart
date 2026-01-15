import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/theme/theme.dart';

class RecentMaintenanceCard extends StatelessWidget {
  final String title;
  final String description;
  final String dateTime;
  final String location;
  final String price;
  final VoidCallback? onTapInvoice; // Callback untuk aksi tombol

  const RecentMaintenanceCard({
    super.key,
    required this.title,
    required this.description,
    required this.dateTime,
    required this.location,
    required this.price,
    this.onTapInvoice,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      // Margin bottom agar jika ada banyak list, ada jarak antar kartu
      margin: EdgeInsets.only(bottom: AppSetting.setHeight(16)),
      padding: EdgeInsets.all(AppSetting.setWidth(24)),
      decoration: BoxDecoration(
        color: MyTheme.color.white, // Ganti cardBackgroundColor
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
          SizedBox(height: AppSetting.setHeight(12)),

          // 2. Deskripsi
          Text(
            description,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(13),
              color: MyTheme.color.secondary,
              height: 1.4,
            ),
            maxLines: 3, // Membatasi baris agar tidak terlalu panjang
            overflow: TextOverflow.ellipsis,
          ),
          SizedBox(height: AppSetting.setHeight(20)),

          // 3. Informasi Detail
          _buildInfoRow(Icons.access_time_filled, dateTime),
          SizedBox(height: AppSetting.setHeight(8)),
          _buildInfoRow(Icons.location_on, location),
          SizedBox(height: AppSetting.setHeight(8)),
          _buildInfoRow(Icons.monetization_on, price),

          SizedBox(height: AppSetting.setHeight(24)),

          // 4. Tombol View Invoice
          SizedBox(
            width: double.infinity,
            height: AppSetting.setHeight(48),
            child: ElevatedButton(
              onPressed: onTapInvoice,
              style: ElevatedButton.styleFrom(
                backgroundColor: MyTheme.color.primary,
                foregroundColor: MyTheme.color.white,
                elevation: 0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(AppSetting.setWidth(24)),
                ),
              ),
              child: Text(
                "View Invoice",
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

  // Helper Widget untuk baris Icon + Teks
  Widget _buildInfoRow(IconData icon, String text) {
    return Row(
      children: [
        Icon(
          icon,
          size: AppSetting.setWidth(18),
          color: MyTheme.color.primary,
        ),
        SizedBox(width: AppSetting.setWidth(10)),
        Expanded(
          // Expanded agar teks panjang tidak error overflow
          child: Text(
            text,
            style: TextStyle(
              fontSize: AppSetting.setFontSize(13),
              fontWeight: FontWeight.w600,
              color: MyTheme.color.primary,
            ),
          ),
        ),
      ],
    );
  }
}
