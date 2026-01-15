// import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
// import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
// import 'package:drivero_automa/domain/entities/maintenance_entity.dart';

// // vehicle_detail_state.dart
// import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
// import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
// import 'package:drivero_automa/domain/entities/maintenance_entity.dart';

// class VehicleDetailState {
//   final bool isDriverFleetLoading;
//   final bool isKMSDriverLoading;
//   final bool isMaintenancesLoading;
//   final bool isFleetImagesLoading;
//   final DriverFleetEntity? fleet;
//   final String? KMSDriver;
//   final List<FleetImageEntity> fleetImages;
//   final String? error;

//   final MaintenanceEntity? recentMaintenance;
//   final MaintenanceEntity? upcomingMaintenance;

//   // 🔹 total fee dari semua completed maintenance untuk vehicle ini
//   final int totalCompletedFee;

//   const VehicleDetailState({
//     this.isDriverFleetLoading = false,
//     this.isKMSDriverLoading = false,
//     this.isMaintenancesLoading = false,
//     this.isFleetImagesLoading = false,
//     this.fleet,
//     this.KMSDriver,
//     this.fleetImages = const [],
//     this.recentMaintenance,
//     this.upcomingMaintenance,
//     this.totalCompletedFee = 0,
//     this.error,
//   });

//   VehicleDetailState copyWith({
//     bool? isDriverFleetLoading,
//     bool? isKMSDriverLoading,
//     bool? isMaintenancesLoading,
//     bool? isFleetImagesLoading,
//     DriverFleetEntity? fleet,
//     String? KMSDriver,
//     List<FleetImageEntity>? fleetImages,
//     MaintenanceEntity? recentMaintenance,
//     MaintenanceEntity? upcomingMaintenance,
//     int? totalCompletedFee,
//     String? error,
//   }) {
//     return VehicleDetailState(
//       isDriverFleetLoading: isDriverFleetLoading ?? this.isDriverFleetLoading,
//       isKMSDriverLoading: isKMSDriverLoading ?? this.isKMSDriverLoading,
//       isMaintenancesLoading: isMaintenancesLoading ?? this.isMaintenancesLoading,
//       isFleetImagesLoading: isFleetImagesLoading ?? this.isFleetImagesLoading,
//       fleet: fleet ?? this.fleet,
//       KMSDriver: KMSDriver ?? this.KMSDriver,
//       fleetImages: fleetImages ?? this.fleetImages,
//       recentMaintenance: recentMaintenance ?? this.recentMaintenance,
//       upcomingMaintenance: upcomingMaintenance ?? this.upcomingMaintenance,
//       totalCompletedFee: totalCompletedFee ?? this.totalCompletedFee,
//       error: error ?? this.error,
//     );
//   }
// }
import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:equatable/equatable.dart'; // 1. Wajib tambahkan ini

class VehicleDetailState extends Equatable { // 2. Extends Equatable
  final bool isDriverFleetLoading;
  final bool isKmsLoading;
  final bool isMaintenancesLoading;
  final bool isFleetImagesLoading;

  final DriverFleetEntity? fleet;
  final int? vehicleKms;
  final List<FleetImageEntity> fleetImages;
  final MaintenanceEntity? recentMaintenance;
  final MaintenanceEntity? upcomingMaintenance;
  final int totalCompletedFee;
  final String? error;

  const VehicleDetailState({
    this.isDriverFleetLoading = false,
    this.isKmsLoading = false,
    this.isMaintenancesLoading = false,
    this.isFleetImagesLoading = false,
    this.fleet,
    this.vehicleKms,
    this.fleetImages = const [],
    this.recentMaintenance,
    this.upcomingMaintenance,
    this.totalCompletedFee = 0,
    this.error,
  });

  /// -------------------------------------------------------------------------
  /// 🔹 LOGIC HELPER (GETTERS) - BIAR UI GAK MIKIR
  /// -------------------------------------------------------------------------

  // Logic 1: Ambil gambar pertama dengan aman. Kalau kosong, return null.
  String? get safeImageUrl {
    if (fleetImages.isNotEmpty) {
      return fleetImages.first.link;
    }
    return null;
  }

  // Logic 2: Cek apakah ada Recent Maintenance
  bool get hasRecentMaintenance => recentMaintenance != null;

  // Logic 3: Cek apakah ada Upcoming Maintenance
  bool get hasUpcomingMaintenance => upcomingMaintenance != null;

  // Logic 4: Format KMS biar aman (misal return 0 kalau null)
  int get safeKms => vehicleKms ?? 0;

  /// -------------------------------------------------------------------------
  /// 🔹 COPY WITH YANG AMAN UNTUK NULLABLE
  /// -------------------------------------------------------------------------
  VehicleDetailState copyWith({
    bool? isDriverFleetLoading,
    bool? isKmsLoading,
    bool? isMaintenancesLoading,
    bool? isFleetImagesLoading,
    DriverFleetEntity? fleet,
    int? vehicleKms,
    List<FleetImageEntity>? fleetImages,
    // Gunakan trik khusus kalau mau set null, tapi biar simpel kita pakai cara standar dulu.
    // Catatan: Jika ingin me-reset value jadi null, sebaiknya emit state baru atau handle khusus.
    MaintenanceEntity? recentMaintenance, 
    MaintenanceEntity? upcomingMaintenance,
    int? totalCompletedFee,
    String? error,
  }) {
    return VehicleDetailState(
      isDriverFleetLoading: isDriverFleetLoading ?? this.isDriverFleetLoading,
      isKmsLoading: isKmsLoading ?? this.isKmsLoading,
      isMaintenancesLoading: isMaintenancesLoading ?? this.isMaintenancesLoading,
      isFleetImagesLoading: isFleetImagesLoading ?? this.isFleetImagesLoading,
      fleet: fleet ?? this.fleet,
      vehicleKms: vehicleKms ?? this.vehicleKms,
      fleetImages: fleetImages ?? this.fleetImages,
      
      // ⚠️ PERHATIAN: Logic '??' ini akan mempertahankan value lama jika inputnya null.
      // Kalau di BLoC kamu selalu mengirim data baru (overwrite), ini aman.
      recentMaintenance: recentMaintenance ?? this.recentMaintenance,
      upcomingMaintenance: upcomingMaintenance ?? this.upcomingMaintenance,
      
      totalCompletedFee: totalCompletedFee ?? this.totalCompletedFee,
      error: error ?? this.error,
    );
  }

  // 3. Wajib Override Props untuk Equatable
  @override
  List<Object?> get props => [
        isDriverFleetLoading,
        isKmsLoading,
        isMaintenancesLoading,
        isFleetImagesLoading,
        fleet,
        vehicleKms,
        fleetImages,
        recentMaintenance,
        upcomingMaintenance,
        totalCompletedFee,
        error,
      ];
}