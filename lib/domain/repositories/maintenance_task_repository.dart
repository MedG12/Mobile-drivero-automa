import 'package:drivero_automa/domain/entities/maintenance_task_entity.dart';

abstract class MaintenanceTaskRepository {
  /// Menambahkan maintenance task baru
  /// Mengembalikan entity MaintenanceTaskEntity
  Future<MaintenanceTaskEntity> addMaintenanceTask({
    required int vehicleId,
    required String title,
    required String maintenanceType,
    required String location,
    required String description,
    required String feeString,
    required String dateTimes,
    required String enumStatus,
  });
}
