import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/domain/entities/edit_user_biodata_etity.dart';

abstract class ProfileBiodataRemoteDatasource {
  Future<bool> updateBiodata(EditUserBiodataEntity data, String token);
}

class ProfileBiodataRemoteDatasourceImpl
    implements ProfileBiodataRemoteDatasource {
  final Dio dio;

  ProfileBiodataRemoteDatasourceImpl({required this.dio});

  @override
  Future<bool> updateBiodata(EditUserBiodataEntity data, String token) async {
    try {
      final response = await dio.post(
         ApiEndpoints.updateUserBiodata,
        data: {
          "name": data.name,
          "birth_date": data.birth_date,
          "gender": data.gender,
        },
        options: Options(
          headers: {
            "Authorization": token,
            "Content-Type": "application/json",
          },
        ),
      );

      if (response.statusCode == 200) {
        return true;
      }

      return false;
    } catch (e) {
      throw Exception("Failed to update biodata: $e");
    }
  }
}
