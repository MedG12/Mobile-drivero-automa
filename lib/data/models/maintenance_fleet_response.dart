import 'package:drivero_automa/data/models/maintenance_model.dart';

class MaintenanceFleetResponse {
  final int statusCode;
  final String status;
  final List<MaintenanceModel> data;

  MaintenanceFleetResponse({
    required this.statusCode,
    required this.status,
    required this.data,
  });

  factory MaintenanceFleetResponse.fromJson(Map<String, dynamic> json) {
    return MaintenanceFleetResponse(
      statusCode: json['statusCode'],
      status: json['status'],
      data: (json['result'] as List)
          .map((item) => MaintenanceModel.fromJson(item))
          .toList(),
    );
  }
}
