import 'package:drivero_automa/data/models/driver_fleet_model.dart';
import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';
import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';

class HomeState {
  final bool isDriverFleetLoading;
  final bool isFleetImagesLoading;
  final bool isMaintenancesLoading;
  final List<DriverFleetEntity> fleets;
  final List<FleetImageEntity> images;
  final List<MaintenanceEntity> maintenances;
  final String? error;

  const HomeState({
    this.isMaintenancesLoading = false,
    this.isDriverFleetLoading = false,
    this.isFleetImagesLoading = false,
    this.fleets = const [],
    this.images = const [],
    this.maintenances = const [],
    this.error,
  });

  HomeState copyWith({
    bool? isDriverFleetLoading,
    bool? isFleetImagesLoading,
    bool? isMaintenancesLoading,
    List<DriverFleetEntity>? fleets,
    List<FleetImageEntity>? images,
    List<MaintenanceEntity>? maintenances,
    String? error,
  }) {
    return HomeState(
      isDriverFleetLoading: isDriverFleetLoading ?? this.isDriverFleetLoading,
      isFleetImagesLoading: isFleetImagesLoading ?? this.isFleetImagesLoading,
      isMaintenancesLoading:
          isMaintenancesLoading ?? this.isMaintenancesLoading,
      fleets: fleets ?? this.fleets,
      images: images ?? this.images,
      maintenances: maintenances ?? this.maintenances,
      error: error ?? this.error, // ← penting!
    );
  }
}
