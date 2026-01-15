import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
import 'package:drivero_automa/presentation/home/vehicle_detail/widgets/detail_information.dart';
import 'package:drivero_automa/presentation/home/vehicle_detail/widgets/recent_maintenanace.dart';
import 'package:drivero_automa/presentation/home/vehicle_detail/widgets/upcaming_maintenance.dart';

import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';

import '../bloc/vehicle_detail/vehicle_detail_bloc.dart';
import '../bloc/vehicle_detail/vehicle_detail_state.dart';

// 🔹 Dummy Vehicle (Buat fallback gambar aja)
class Vehicle {
  final String imageUrl;
  Vehicle({required this.imageUrl});
}

final Vehicle dummyVehicle = Vehicle(
  imageUrl: "https://icons.iconarchive.com/icons/praveen/minimal-outline/512/gallery-icon.png",
);

class VehicleDetailScreen extends StatelessWidget {
  final DriverFleetEntity fleet;
  const VehicleDetailScreen({super.key, required this.fleet});

  // 🛠️ Helper function biar format tanggal gak bikin crash
  String _safeDate(DateTime? date) {
    if (date == null) return "-";
    return DateFormat('d MMM yyyy - HH:mm').format(date);
  }

  @override
  Widget build(BuildContext context) {
    return BackgroundPageWidget(
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: AppSetting.setWidth(24)),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(height: AppSetting.setHeight(20)),

              /// 🔹 HEADER
              Padding(
                padding: EdgeInsets.only(
                  top: AppSetting.setHeight(12),
                  left: AppSetting.setWidth(12),
                  right: AppSetting.setWidth(12),
                ),
                child: Row(
                  children: [
                    GestureDetector(
                      onTap: () => Navigator.pop(context),
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          horizontal: AppSetting.setWidth(14),
                          vertical: AppSetting.setHeight(6),
                        ),
                        decoration: BoxDecoration(
                          color: MyTheme.color.white,
                          borderRadius: BorderRadius.circular(30),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.15),
                              blurRadius: 4,
                              offset: const Offset(0, 2),
                            ),
                          ],
                        ),
                        child: Icon(
                          Icons.arrow_back,
                          color: MyTheme.color.primary,
                          size: AppSetting.setFontSize(18),
                        ),
                      ),
                    ),
                    Expanded(
                      child: Center(
                        child: Text(
                          "Vehicle Detail",
                          style: TextStyle(
                            color: MyTheme.color.white,
                            fontSize: AppSetting.setFontSize(18),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(width: 40),
                  ],
                ),
              ),

              SizedBox(height: AppSetting.setHeight(40)),

              /// 🔥 BAGIAN GAMBAR & INFO KENDARAAN
              BlocBuilder<VehicleDetailBloc, VehicleDetailState>(
                builder: (context, state) {
                  // ✅ AMAN: Pakai Getter dari State, kalau null pake dummy
                  final displayImage = state.safeImageUrl ?? dummyVehicle.imageUrl;
                  
                  // ✅ AMAN: Pakai Getter safeKms
                  final displayKms = "${state.safeKms} KMS"; 

                  return VehicleDetailCard(
                    brand: fleet.brand ?? "-", // Handle String null
                    regNumber: fleet.licensePlat ?? "-",
                    imageUrl: displayImage,
                    kms: displayKms, 
                    
                    // 🚨 PERBAIKAN FATAL: Jangan pakai tanda seru (!)
                    insuredUntil: _safeDate(fleet.insuredUntil), 
                    
                    totalSpend: state.totalCompletedFee.toString(),
                    
                    // 🚨 PERBAIKAN FATAL:
                    purchasedOn: _safeDate(fleet.purchasedOn), 
                  );
                },
              ),

              SizedBox(height: AppSetting.setHeight(40)),
              
              /// 🔥 BAGIAN RECENT MAINTENANCE
              BlocBuilder<VehicleDetailBloc, VehicleDetailState>(
                builder: (context, state) {
                  // ✅ Clean Code: Kalau gak ada data, sembunyikan section ini
                  if (!state.hasRecentMaintenance) {
                    return const SizedBox.shrink(); // Widget kosong (invisible)
                  }
                  
                  // Kalau sampai sini, berarti recentMaintenance TIDAK NULL
                  // Kita bisa pakai Text Header + Card nya
                  final recent = state.recentMaintenance!;

                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Padding(
                        padding: EdgeInsets.only(left: AppSetting.setWidth(4)),
                        child: Text(
                          'Recent Maintenance',
                          style: TextStyle(
                            color: MyTheme.color.white,
                            fontWeight: FontWeight.bold,
                            fontSize: AppSetting.setFontSize(18),
                          ),
                        ),
                      ),
                      SizedBox(height: AppSetting.setHeight(16)),
                      RecentMaintenanceCard(
                        title: recent.title ?? "-",
                        description: recent.description ?? "-",
                        dateTime: _safeDate(recent.modifiedOn), // Pakai helper biar aman
                        location: recent.location ?? "-",
                        price: recent.feeString ?? "-",
                        onTapInvoice: () => print("Invoice clicked"),
                      ),
                      SizedBox(height: AppSetting.setHeight(16)), // Spacer bawah
                    ],
                  );
                },
              ),

              /// 🔥 BAGIAN UPCOMING MAINTENANCE
              BlocBuilder<VehicleDetailBloc, VehicleDetailState>(
                builder: (context, state) {
                  // ✅ Clean Code: Cek pake getter
                  if (!state.hasUpcomingMaintenance) {
                    return const SizedBox.shrink();
                  }

                  final upcoming = state.upcomingMaintenance!;

                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Padding(
                        padding: EdgeInsets.only(left: AppSetting.setWidth(4)),
                        child: Text(
                          'Upcoming Maintenance',
                          style: TextStyle(
                            color: MyTheme.color.white,
                            fontWeight: FontWeight.bold,
                            fontSize: AppSetting.setFontSize(18),
                          ),
                        ),
                      ),
                      SizedBox(height: AppSetting.setHeight(16)),
                      UpcomingMaintenance(
                        title: upcoming.title ?? "-",
                        dateTime: _safeDate(upcoming.createdOn),
                        location: upcoming.location ?? "-",
                        onTapReminder: () => print("Reminder clicked"),
                      ),
                      SizedBox(height: AppSetting.setHeight(20)),
                    ],
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}