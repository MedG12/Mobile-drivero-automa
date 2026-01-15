import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';
import 'package:flutter_svg/svg.dart';

class CardLastMaintenance extends StatelessWidget {
  final String vehicleName;
  final String plateNumber;
  final String title;
  final String description;
  final String dateTime;
  final String serviceDetail;
  final String location;
  final String price;
  final VoidCallback? onViewInvoice;
  final VoidCallback? onShowDetail;

  const CardLastMaintenance({
    super.key,
    required this.vehicleName,
    required this.plateNumber,
    required this.title,
    required this.description,
    required this.dateTime,
    required this.serviceDetail,
    required this.location,
    required this.price,
    this.onViewInvoice,
    this.onShowDetail,
  });

  @override
  Widget build(BuildContext context) {
    final double cardRadius = AppSetting.setWidth(20);

    return Container(
      margin: EdgeInsets.only(
        top: AppSetting.setHeight(16), // hanya atas
        bottom: AppSetting.setHeight(16), // hanya bawah
      ),
      padding: EdgeInsets.all(AppSetting.setWidth(30)),
      decoration: BoxDecoration(
        color: MyTheme.color.white,
        borderRadius: BorderRadius.circular(cardRadius),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Tag & Plate Number
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Container(
                padding: EdgeInsets.symmetric(
                  horizontal: AppSetting.setWidth(12),
                  vertical: AppSetting.setHeight(6),
                ),
                decoration: BoxDecoration(
                  color: MyTheme.color.primary,
                  borderRadius: BorderRadius.circular(cardRadius),
                ),
                child: Text(
                  vehicleName,
                  style: TextStyle(
                    fontSize: AppSetting.setFontSize(15),
                    color: MyTheme.color.white,
                    fontFamily: FontFamily.inter,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ),
              Text(
                plateNumber,
                style: TextStyle(
                  fontSize: AppSetting.setFontSize(15),
                  color: MyTheme.color.primary,
                  fontFamily: FontFamily.inter,
                  fontWeight: FontWeight.w700,
                ),
              ),
            ],
          ),
          SizedBox(height: AppSetting.setHeight(16)),

          // Title
          Text(
            title,
            style: TextStyle(
              color: MyTheme.color.primary,
              fontWeight: FontWeight.bold,
              fontSize: AppSetting.setFontSize(18),
            ),
          ),
          SizedBox(height: AppSetting.setHeight(8)),

          // Description
          Text(
            description,
            style: TextStyle(
              color: MyTheme.color.primary,
              fontSize: AppSetting.setFontSize(14),
            ),
          ),
          SizedBox(height: AppSetting.setHeight(16)),

          // Detail rows
          _buildDetailRowSvg(iconPath: Assets.icons.alarmClock, text: dateTime),
          SizedBox(height: AppSetting.setHeight(18)),

          // Button
          Row(
            children: [
              Expanded(
                child: OutlinedButton(
                  onPressed: onViewInvoice,
                  style: OutlinedButton.styleFrom(
                    backgroundColor: MyTheme.color.white,
                    side: BorderSide(color: MyTheme.color.primary),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(
                        AppSetting.setWidth(50),
                      ),
                    ),
                    padding: EdgeInsets.symmetric(
                      vertical: AppSetting.setHeight(6),
                    ),
                  ),
                  child: Text(
                    'View Invoice',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: AppSetting.setFontSize(16),
                    ),
                  ),
                ),
              ),
              SizedBox(width: AppSetting.setWidth(12)), // spasi antar tombol
              Expanded(
                child: OutlinedButton(
                  onPressed: onShowDetail,
                  style: OutlinedButton.styleFrom(
                    backgroundColor: MyTheme.color.primary,
                    side: BorderSide(color: MyTheme.color.primary),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(
                        AppSetting.setWidth(30),
                      ),
                    ),
                    padding: EdgeInsets.symmetric(
                      vertical: AppSetting.setHeight(6),
                    ),
                  ),
                  child: Text(
                    'Show Detail',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: AppSetting.setFontSize(16),
                      color: MyTheme.color.white,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildDetailRowSvg({required String iconPath, required String text}) {
    return Row(
      children: [
        SvgPicture.asset(
          iconPath,
          width: AppSetting.setWidth(20),
          height: AppSetting.setHeight(20),
        ),
        SizedBox(width: AppSetting.setWidth(12)),
        Expanded(
          child: Text(
            text,
            style: TextStyle(
              color: MyTheme.color.primary,
              fontSize: AppSetting.setFontSize(14),
            ),
          ),
        ),
      ],
    );
  }
}
