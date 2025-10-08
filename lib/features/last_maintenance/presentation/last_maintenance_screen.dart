import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/features/last_maintenance/presentation/last_maintenance_screen_wrapper.dart';
// pastikan path sesuai
import 'package:drivero_automa/features/last_maintenance/presentation/widgets/CardLastMaintenance.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:drivero_automa/config/app_config.dart';

class LastMaintenanceScreen extends StatefulWidget {
  const LastMaintenanceScreen({super.key});

  @override
  State<LastMaintenanceScreen> createState() => _LastMaintenanceScreenState();
}

class _LastMaintenanceScreenState extends State<LastMaintenanceScreen> {
  @override
  Widget build(BuildContext context) {
    return last_maintenance_screen_wrapper(
      child: SingleChildScrollView(
        padding: EdgeInsets.all(AppSetting.setWidth(16)),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(height: AppSetting.setHeight(20)),
            Text(
              "Next Maintenance Schedule",
              style: TextStyle(
                fontFamily: "Inter",
                fontWeight: FontWeight.w700,
                fontSize: AppSetting.setFontSize(18),
                height: AppSetting.setHeight(20) / AppSetting.setFontSize(18),
                letterSpacing: AppSetting.setWidth(-0.24),
                color: MyTheme.color.white,
              ),
            ),

            // Title
            SizedBox(height: AppSetting.setHeight(12)),

            CardLastMaintenance(
              vehicleName: 'Vehicle B',
              plateNumber: 'AD1234 BCF',
              title: 'Maintenance for Vehicle B',
              description:
                  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
              dateTime: '12 December 2024 - 17:45',
              serviceDetail: 'Service for Brake Pads',
              location: 'Location B',
              price: 'IDR XXX,XXX',
              onViewInvoice: () {
                debugPrint('View Invoice pressed');
              },
            ),
            CardLastMaintenance(
              vehicleName: 'Vehicle B',
              plateNumber: 'AD1234 BCF',
              title: 'Maintenance for Vehicle B',
              description:
                  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
              dateTime: '12 December 2024 - 17:45',
              serviceDetail: 'Service for Brake Pads',
              location: 'Location B',
              price: 'IDR XXX,XXX',
              onViewInvoice: () {
                debugPrint('View Invoice pressed');
              },
            ),
            CardLastMaintenance(
              vehicleName: 'Vehicle B',
              plateNumber: 'AD1234 BCF',
              title: 'Maintenance for Vehicle B',
              description:
                  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
              dateTime: '12 December 2024 - 17:45',
              serviceDetail: 'Service for Brake Pads',
              location: 'Location B',
              price: 'IDR XXX,XXX',
              onViewInvoice: () {
                debugPrint('View Invoice pressed');
              },
            ),
            CardLastMaintenance(
              vehicleName: 'Vehicle B',
              plateNumber: 'AD1234 BCF',
              title: 'Maintenance for Vehicle B',
              description:
                  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
              dateTime: '12 December 2024 - 17:45',
              serviceDetail: 'Service for Brake Pads',
              location: 'Location B',
              price: 'IDR XXX,XXX',
              onViewInvoice: () {
                debugPrint('View Invoice pressed');
              },
            ),
            CardLastMaintenance(
              vehicleName: 'Vehicle B',
              plateNumber: 'AD1234 BCF',
              title: 'Maintenance for Vehicle B',
              description:
                  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
              dateTime: '12 December 2024 - 17:45',
              serviceDetail: 'Service for Brake Pads',
              location: 'Location B',
              price: 'IDR XXX,XXX',
              onViewInvoice: () {
                debugPrint('View Invoice pressed');
              },
            ),
            // Bisa tambahkan CardLastMaintenance lainnya jika perlu
          ],
        ),
      ),
    );
  }
}
