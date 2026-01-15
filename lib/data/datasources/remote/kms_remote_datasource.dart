import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/data/models/kms_driver_model_respone.dart';


abstract class KmsDriverRemoteDatasource {
  Future<KmsDriverResponseModel> getKmsDriver(int vehicleId, String token);
}
class KmsDriverRemoteDatasourceImpl implements KmsDriverRemoteDatasource {
  final Dio dio;

  KmsDriverRemoteDatasourceImpl({required this.dio});

  @override
  Future<KmsDriverResponseModel> getKmsDriver(int vehicleId, String token) async {
    try {
      final response = await dio.get(
        "${ApiEndpoints.kmsDriver}/$vehicleId",
        options: Options(
          headers: {
            "Authorization": token,  // atau "Bearer $token"
          },
        ),
      );

      return KmsDriverResponseModel.fromJson(response.data);
    } catch (e) {
      throw Exception("Failed to fetch KMS Driver: $e");
    }
  }
}
