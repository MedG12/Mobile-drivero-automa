import 'package:drivero_automa/features/auth/data/model/token_data.dart';
import 'package:drivero_automa/features/auth/data/model/user_model.dart';

class LoginResponseModel {
  final UserModel user;
  final TokenData token;

  LoginResponseModel({required this.user, required this.token});

  factory LoginResponseModel.fromJson(Map<String, dynamic> json) {
    final result = json['result'] ?? {};
    return LoginResponseModel(
      user: UserModel.fromJson(result),
      token: TokenData.fromJson({
        'token': result['token'],
        'expires': result['expires'],
      }),
    );
  }

  Map<String, dynamic> toJson() => {'user': user.toJson(), 'token': token};
}
