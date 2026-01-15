import 'package:drivero_automa/domain/entities/driver_fleet_entity.dart';

class DriverFleetModel extends DriverFleetEntity {
  final int vehicleId;
  final int userId;
  final String brand;
  final String licensePlate;
  final DateTime? insuredUntil;
  final DateTime? purchasedOn;
  final DateTime? lastMaintenance;

  DriverFleetModel({
    required this.vehicleId,
    required this.userId,
    required this.brand,
    required this.licensePlate,
    required this.insuredUntil,
    required this.purchasedOn,
    this.lastMaintenance,
  }) : super(
         vehicleId: vehicleId,
         userId: userId,
         brand: brand,
         licensePlat: licensePlate,
         insuredUntil: insuredUntil,
         purchasedOn: purchasedOn,
         lastMaintenance: lastMaintenance,
       );

  factory DriverFleetModel.fromJson(Map<String, dynamic> json) {
    final recent = json['recent_maintenance'];

    return DriverFleetModel(
      vehicleId: json['vehicle_id'] ?? 0,
      userId: json['user_id'] ?? 0,
      brand: json['brand'] ?? '',
      licensePlate: json['license_plat'] ?? '',
      insuredUntil: json['insured_until'] != null
          ? DateTime.tryParse(json['insured_until'])
          : null,
      purchasedOn: json['purchased_on'] != null
          ? DateTime.tryParse(json['purchased_on'])
          : null,
      lastMaintenance: (recent != null && recent['date_times'] != null)
          ? DateTime.tryParse(recent['date_times'])
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'vehicle_id': vehicleId,
      'user_id': userId,
      'brand': brand,
      'license_plat': licensePlate,
      'insured_until': insuredUntil?.toIso8601String(),
      'purchased_on': purchasedOn?.toIso8601String(),
    };
  }
}
