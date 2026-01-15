import 'package:dio/dio.dart';
import 'package:drivero_automa/core/constants/api_endpoints.dart';
import 'package:drivero_automa/data/models/driver_model.dart';
import 'package:drivero_automa/data/models/edit_user_biodata_model_respon.dart';
import 'package:drivero_automa/data/models/loginResponse_model.dart';
import 'package:drivero_automa/data/models/user_model.dart';
import 'package:drivero_automa/core/errors/exceptions.dart';
import 'package:intl/intl.dart';

abstract class AuthRemoteDataSource {
  Future<LoginResponseModel> login(String email, String password);
  Future<DriverModel?> getDriverByUserId(int userId, String token);
  Future<UserModel?> updateUserBiodata(
    String name,
    DateTime birthDate,
    int gender,
    String token,
  );
}

class AuthRemoteDataSourceImpl implements AuthRemoteDataSource {
  final Dio dio;

  AuthRemoteDataSourceImpl(this.dio);
@override
Future<LoginResponseModel> login(String email, String password) async {
  try {
    final response = await dio.post(
      ApiEndpoints.login,
      data: {
        'email': email,
        'password': password,
        "app": "mobile:drivero-personal",
      },
    );

    return LoginResponseModel.fromJson(response.data);
  } on DioException catch (e) {
    if (e.response?.statusCode == 401) {
      throw const InvalidCredentialException();
    }

    if (e.response?.statusCode != null &&
        e.response!.statusCode! >= 500) {
      throw const ServerException();
    }

    throw const NetworkException();
  }
}


  @override
  Future<DriverModel?> getDriverByUserId(int userId, String token) async {
    final response = await dio.post(
      ApiEndpoints.driver,
      queryParameters: {'filter[user_id]': userId},
      options: Options(headers: {'Authorization': '$token'}),
    );

    if (response.statusCode == 200) {
      final dataList = response.data['result']['data'] as List;
      if (dataList.isNotEmpty) {
        return DriverModel.fromJson(dataList.first);
      }
      return null;
    } else {
      throw Exception('Failed to fetch driver');
    }
  }

  @override
  Future<UserModel?> updateUserBiodata(
    String name,
    DateTime birthDate,
    int gender,
    String token,
  ) async {
    try {
      final response = await dio.post(
        ApiEndpoints.updateUserBiodata,
        data: {
          "name": name,
          "birth_date": DateFormat('yyyy-MM-dd HH:mm:ss').format(birthDate),
          "gender": gender,
        },
        options: Options(
          headers: {"Authorization": token, "Content-Type": "application/json"},
        ),
      );

      if (response.statusCode == 200) {
        final result = EditUserBiodataResponse.fromJson(response.data);
        print("Biodata updated successfully: ${result.result}");
        return result.result;
      }

      return null;
    } catch (e) {
      throw Exception("Failed to update biodata: $e");
    }
  }
}
