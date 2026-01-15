// fleet_image_remote_datasource.dart
import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/data/models/maintenance_fleet_response.dart';

abstract class MaintenanceRemoteDatasource {
  Future<MaintenanceFleetResponse> getMaintenances(String token);
}

class MaintenanceRemoteDatasourceImpl implements MaintenanceRemoteDatasource {
  final Dio dio;

  MaintenanceRemoteDatasourceImpl({required this.dio});

  @override
  Future<MaintenanceFleetResponse> getMaintenances(String token) async {
    try {
      final response = await dio.post(
        ApiEndpoints.maintenances,
        options: Options(headers: {'Authorization': token}),
      );

      return MaintenanceFleetResponse.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to fetch fleet images: $e');
    }
  }
}
