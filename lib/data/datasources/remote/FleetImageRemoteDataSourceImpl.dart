import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/data/models/flet_img_respon.dart';

abstract class FleetImageRemoteDataSource {
  Future<FleetImageResponse> fetchFleetImages({
    int? fleetId,
    required String token,
  });
}

class FleetImageRemoteDataSourceImpl implements FleetImageRemoteDataSource {
  final Dio dio;

  FleetImageRemoteDataSourceImpl({required this.dio});

  @override
  Future<FleetImageResponse> fetchFleetImages({
    int? fleetId,
    required String token,
  }) async {
    try {
      final data = fleetId != null
          ? {'fleet_id': fleetId} // kirim 1 ID
          : null; // null → ambil semua

      final response = await dio.post(
        ApiEndpoints.fleetImages,
        data: data,
        options: Options(
          headers: {'Authorization': token},
        ),
      );

      return FleetImageResponse.fromJson(response.data);
    } catch (e) {
      throw Exception('Failed to fetch fleet images: $e');
    }
  }
}
