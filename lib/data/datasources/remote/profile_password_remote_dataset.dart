// data/datasources/remote/password_remote_datasource.dart
import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/domain/entities/edit_user_password_etity.dart';


abstract class PasswordRemoteDatasource {
  Future<bool> updatePassword(EditUserPasswordEntity data, String token);
}

class PasswordRemoteDatasourceImpl implements PasswordRemoteDatasource {
  final Dio dio;

  PasswordRemoteDatasourceImpl({required this.dio});

  @override
  Future<bool> updatePassword(EditUserPasswordEntity data, String token) async {
    try {
      final response = await dio.post(
        ApiEndpoints.changePassword,
        data: {
          "old_password": data.oldPassword,
          "new_password": data.newPassword,
          "confirm_password": data.confirmPassword,
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
      throw Exception("Failed to update password: $e");
    }
  }
}
