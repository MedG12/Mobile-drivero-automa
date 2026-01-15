import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/data/models/driver_fleet_model.dart';
import 'package:drivero_automa/data/models/driver_fleet_response.dart';

abstract class DriverFleetRemoteDataSource {
  Future<List<DriverFleetModel>> getDriverFleets({
    required int userId,
    required String token,
  });

  Future<DriverFleetModel> getDriverFleetDetail({
    required int vehicleId,
    required String token,
  });
}

class DriverFleetRemoteDataSourceImpl implements DriverFleetRemoteDataSource {
  final Dio dio;

  DriverFleetRemoteDataSourceImpl(this.dio);

  @override
  Future<List<DriverFleetModel>> getDriverFleets({
    required int userId,
    required String token,
  }) async {
    if (token.isEmpty) {
      throw Exception("Token is empty");
    }

    try {
      final response = await dio.post(
        ApiEndpoints.driverFleetList,
        data: {"user_id": userId},
        options: Options(
          headers: {"Authorization": token, "Content-Type": "application/json"},
        ),
      );

      if (response.statusCode == 200) {
        final fleetResponse = DriverFleetResponse.fromJson(response.data);
        return fleetResponse.data;
      }

      if (response.statusCode == 401) {
        throw Exception("Unauthorized");
      }

      throw Exception("Failed (status: ${response.statusCode})");
    } on DioException catch (e) {
      throw Exception("Dio error: ${e.message}");
    } catch (e) {
      throw Exception("Unknown error: $e");
    }
  }

  @override
  Future<DriverFleetModel> getDriverFleetDetail({
    required int vehicleId,
    required String token,
  }) async {
    if (token.isEmpty) {
      throw Exception("Token is empty");
    }

    try {
      final response = await dio.post(
        "${ApiEndpoints.driverFleetDetail}/$vehicleId",
        options: Options(
          headers: {"Authorization": token, "Content-Type": "application/json"},
        ),
      );

      if (response.statusCode == 200) {
        final fleetDetail = DriverFleetModel.fromJson(response.data['data']);
        return fleetDetail;
      }

      if (response.statusCode == 401) {
        throw Exception("Unauthorized");
      }

      throw Exception("Failed (status: ${response.statusCode})");
    } on DioException catch (e) {
      throw Exception("Dio error: ${e.message}");
    } catch (e) {
      throw Exception("Unknown error: $e");
    }
  }
}
