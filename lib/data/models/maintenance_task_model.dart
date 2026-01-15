// data/models/maintenance_task_model.dart
import '../../domain/entities/maintenance_task_entity.dart';

class MaintenanceTaskModel extends MaintenanceTaskEntity {
  MaintenanceTaskModel({
    required int id,
    required int vehicleId,
    required String title,
    required String maintenanceType,
    required String location,
    required String description,
    required String feeString,
    required String dateTimes,
    required String enumStatus,
  }) : super(
          id: id,
          vehicleId: vehicleId,
          title: title,
          maintenanceType: maintenanceType,
          location: location,
          description: description,
          feeString: feeString,
          dateTimes: dateTimes,
          enumStatus: enumStatus,
        );

  factory MaintenanceTaskModel.fromJson(Map<String, dynamic> json) {
    return MaintenanceTaskModel(
      id: json['id'] ?? 0,
      vehicleId: json['vehicle_id'],
      title: json['title'],
      maintenanceType: json['maintenance_type'],
      location: json['location'],
      description: json['description'],
      feeString: json['fee_string'],
      dateTimes: json['date_times'],
      enumStatus: json['enum_status'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'vehicle_id': vehicleId,
      'title': title,
      'maintenance_type': maintenanceType,
      'location': location,
      'description': description,
      'fee_string': feeString,
      'date_times': dateTimes,
      'enum_status': enumStatus,
    };
  }
}
