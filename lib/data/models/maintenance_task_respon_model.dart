// data/models/maintenance_task_response_model.dart
import 'maintenance_task_model.dart';

class MaintenanceTaskResponseModel {
  final int statusCode;
  final String status;
  final MaintenanceTaskModel result;

  MaintenanceTaskResponseModel({
    required this.statusCode,
    required this.status,
    required this.result,
  });

  factory MaintenanceTaskResponseModel.fromJson(Map<String, dynamic> json) {
    return MaintenanceTaskResponseModel(
      statusCode: json['statusCode'] ?? 0,
      status: json['status'] ?? '',
      result: MaintenanceTaskModel.fromJson(json['result']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'statusCode': statusCode,
      'status': status,
      'result': result.toJson(),
    };
  }
}
