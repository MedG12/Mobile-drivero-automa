import 'dart:math';

import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_state.dart';
import 'package:drivero_automa/features/home/presentation/bloc/home/home_bloc.dart';
import 'package:drivero_automa/features/home/presentation/widgets/vehicle_list.dart';
import 'package:drivero_automa/features/home/presentation/widgets/upcoming_maintenance.dart';
import 'package:drivero_automa/gen/fonts.gen.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_svg/svg.dart';

final List<Map<String, String>> maintenanceSchedules = [
  {
    'date': '16 Jun 2025 - 17:45',
    'title': 'Maintenance for Aether 450X',
    'service': 'Service for Change of Tires',
    'plate': 'AD1234 BCF',
  },
  {
    'date': '28 Jul 2025 - 09:00',
    'title': 'Annual Service for Honda Vario',
    'service': 'Oil Change and Brake Check',
    'plate': 'F 5678 XYZ',
  },
  {
    'date': '15 Aug 2025 - 11:30',
    'title': 'Inspection for Yamaha NMAX',
    'service': 'General Inspection & Tune-up',
    'plate': 'B 9101 ABC',
  },
];

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    context.read<HomeBloc>();
    return BackgroundPageWidget(
      child: Container(
        padding: EdgeInsets.fromLTRB(
          AppSetting.setWidth(24),
          AppSetting.setHeight(50),
          AppSetting.setWidth(24),
          AppSetting.setHeight(10),
        ),
        child: Stack(
          children: [
            // Konten utama
            SingleChildScrollView(
              child: BlocBuilder<AuthBloc, AuthState>(
                builder: (context, state) {
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      if (state is Authenticated)
                        Text(
                          "Hello, ${state.user.name}",
                          style: TextStyle(
                            fontSize: AppSetting.setFontSize(18),
                            color: MyTheme.color.white,
                            fontFamily: FontFamily.inter,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      if (state is AuthLoading)
                        const Center(child: CircularProgressIndicator()),
                      SizedBox(height: AppSetting.setHeight(7)),

                      Text(
                        "LET’S TAKE A LOOK FOR YOUR CAR!",
                        style: TextStyle(
                          fontSize: AppSetting.setFontSize(10),
                          color: MyTheme.color.white,
                          fontFamily: FontFamily.inter,
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                      SizedBox(height: AppSetting.setHeight(20)),

                      Text(
                        "Vehicle List",
                        style: TextStyle(
                          fontSize: AppSetting.setFontSize(18),
                          color: MyTheme.color.white,
                          fontFamily: FontFamily.inter,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      SizedBox(height: AppSetting.setHeight(20)),

                      // contoh list kendaraan
                      const VehicleCard(),
                      const VehicleCard(),
                      const VehicleCard(),
                      SizedBox(height: AppSetting.setHeight(20)),

                      // Upcoming Maintenance
                      Container(
                        padding: EdgeInsets.symmetric(
                          vertical: AppSetting.setHeight(2), // top-bottom
                          horizontal: AppSetting.setWidth(13), // left-right
                        ),

                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            // Title
                            Text(
                              "Upcoming Maintenance",
                              style: TextStyle(
                                fontSize: AppSetting.setFontSize(18),
                                color: MyTheme.color.white,
                                fontFamily: FontFamily.inter,
                                fontWeight: FontWeight.w600,
                              ),
                            ),

                            // Button kanan
                            Container(
                              padding: EdgeInsets.symmetric(
                                vertical: AppSetting.setHeight(
                                  4,
                                ), // tinggi tipis
                                horizontal: AppSetting.setWidth(
                                  12,
                                ), // kiri kanan kecil
                              ),
                              decoration: BoxDecoration(
                                color: MyTheme.color.white,
                                borderRadius: BorderRadius.circular(
                                  100,
                                ), // rounded pill
                              ),
                              child: GestureDetector(
                                onTap: () {
                                  // Aksi button
                                },
                                child: Text(
                                  "See All",
                                  style: TextStyle(
                                    fontSize: AppSetting.setFontSize(14),
                                    color: MyTheme.color.primary,
                                    fontFamily: FontFamily.inter,
                                    fontWeight: FontWeight.w700,
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(height: AppSetting.setHeight(12)),

                      SingleChildScrollView(
                        scrollDirection: Axis.horizontal,
                        child: Row(
                          children: maintenanceSchedules.map((schedule) {
                            return Padding(
                              padding: EdgeInsets.only(
                                right: AppSetting.setWidth(12),
                              ),
                              child: SizedBox(
                                width: AppSetting.setWidth(
                                  250,
                                ), // biar ukuran card konsisten
                                child: UpcomingMaintenanceCard(
                                  date: schedule['date']!,
                                  title: schedule['title']!,
                                  service: schedule['service']!,
                                  plate: schedule['plate']!,
                                ),
                              ),
                            );
                          }).toList(),
                        ),
                      ),
                    ],
                  );
                },
              ),
            ),

            // Floating Button
            Positioned(
              right: AppSetting.setWidth(10),
              bottom: AppSetting.setHeight(13),
              child: GestureDetector(
                onTap: () {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Tombol Info Ditekan!')),
                  );
                },
                child: Container(
                  width: AppSetting.setWidth(66),
                  height: AppSetting.setWidth(66),
                  padding: EdgeInsets.all(AppSetting.setWidth(18)),
                  decoration: BoxDecoration(
                    color: MyTheme.color.primary,
                    shape: BoxShape.circle,
                  ),
                  child: SvgPicture.asset(
                    'assets/icons/perintah.svg',
                    width: AppSetting.setWidth(10),
                    height: AppSetting.setHeight(10),
                    colorFilter: ColorFilter.mode(
                      MyTheme.color.white,
                      BlendMode.srcIn,
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
