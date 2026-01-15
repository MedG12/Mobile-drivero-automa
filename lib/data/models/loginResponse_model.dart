import 'package:drivero_automa/data/models/user_model.dart';

class LoginResponseModel {
  final String status;
  final UserModel user;

  LoginResponseModel({required this.status, required this.user});

  factory LoginResponseModel.fromJson(Map<String, dynamic> json) {
    final result = json['result'] ?? {};

    return LoginResponseModel(
      status: json['status'] ?? 'unknown',
      user: UserModel.fromJson(result),
    );
  }

  Map<String, dynamic> toJson() {
    return {'status': status, 'result': user.toJson()};
  }
}
