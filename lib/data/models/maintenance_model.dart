import 'package:drivero_automa/domain/entities/maintenance_entity.dart';

class MaintenanceModel extends MaintenanceEntity {
  const MaintenanceModel({
    required super.id,
    required super.vehicleId,
    required super.title,
    required super.maintenanceType,
    required super.location,
    required super.description,
    super.feeString,
    super.enumStatus,
    super.dateTimes,
    required super.companyId,
    required super.statusId,
    required super.createdOn,
    required super.modifiedOn,
    required super.brand,
    required super.licensePlat,
  });

  factory MaintenanceModel.fromJson(Map<String, dynamic> json) {
    return MaintenanceModel(
      id: json['id'],
      vehicleId: json['vehicle_id'],
      title: json['title'],
      maintenanceType: json['maintenance_type'],
      location: json['location'],
      description: json['description'],
      feeString: json['fee_string'],
      enumStatus: json['enum_status'],
      dateTimes: json['date_times'] != null
          ? DateTime.parse(json['date_times'])
          : null,
      companyId: json['company_id'],
      statusId: json['status_id'],
      createdOn: DateTime.parse(json['created_on']),
      modifiedOn: DateTime.parse(json['modified_on']),
      brand: json['brand'],
      licensePlat: json['license_plat'],
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
      'enum_status': enumStatus,
      'date_times': dateTimes?.toIso8601String(),
      'company_id': companyId,
      'status_id': statusId,
      'created_on': createdOn.toIso8601String(),
      'modified_on': modifiedOn.toIso8601String(),
      'brand': brand,
      'license_plat': licensePlat,
    };
  }
}
