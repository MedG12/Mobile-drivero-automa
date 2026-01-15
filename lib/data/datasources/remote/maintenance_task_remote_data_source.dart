// data/datasources/maintenance_task_remote_datasource.dart
import 'package:dio/dio.dart';
import 'package:drivero_automa/data/models/maintenance_task_respon_model.dart';

import 'package:drivero_automa/core/constants/api_endpoints.dart';

abstract class MaintenanceTaskRemoteDataSource {
  Future<MaintenanceTaskResponseModel> addMaintenanceTask({
    required String token,
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

class MaintenanceTaskRemoteDataSourceImpl implements MaintenanceTaskRemoteDataSource {
  final Dio dio;

  MaintenanceTaskRemoteDataSourceImpl({required this.dio});

  @override
  Future<MaintenanceTaskResponseModel> addMaintenanceTask({
    required String token,
    required int vehicleId,
    required String title,
    required String maintenanceType,
    required String location,
    required String description,
    required String feeString,
    required String dateTimes,
    required String enumStatus,
  }) async {
    try {
      final response = await dio.post(
        ApiEndpoints.maintenanceAdd, // misal: 'https://api.example.com/maintenance-tasks'
        data: {
          'vehicle_id': vehicleId,
          'title': title,
          'maintenance_type': maintenanceType,
          'location': location,
          'description': description,
          'fee_string': feeString,
          'date_times': dateTimes,
          'enum_status': enumStatus,
        },
        options: Options(
          headers: {
            'Authorization': token,
            'Content-Type': 'application/json',
          },
        ),
      );

      return MaintenanceTaskResponseModel.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to add maintenance task: $e');
    }
  }
}
