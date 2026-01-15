import 'package:drivero_automa/data/models/driver_fleet_model.dart';

class DriverFleetResponse {
  final int statusCode;
  final String status;
  final List<DriverFleetModel> data;

  DriverFleetResponse({
    required this.statusCode,
    required this.status,
    required this.data,
  });

  factory DriverFleetResponse.fromJson(Map<String, dynamic> json) {
    final List<dynamic> list = json['result'] ?? [];

    return DriverFleetResponse(
      statusCode: json['statusCode'] ?? 0,
      status: json['status'] ?? "",
      data: list.map((e) => DriverFleetModel.fromJson(e)).toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'statusCode': statusCode,
      'status': status,
      'result': data.map((e) => e.toJson()).toList(),
    };
  }
}
