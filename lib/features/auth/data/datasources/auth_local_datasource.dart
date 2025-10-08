import 'dart:convert';

import 'package:drivero_automa/features/auth/data/model/token_data.dart';
import 'package:drivero_automa/features/auth/data/model/user_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

abstract class AuthLocalDataSource {
  Future<void> saveUser(UserModel user);
  Future<UserModel?> getUser();
  Future<void> clearUser();
  Future<void> saveToken(TokenData token);
  Future<TokenData> getToken();
  Future<void> clearToken();
  Future<void> clearAll();
}

class AuthLocalDataSourceImpl implements AuthLocalDataSource {
  final SharedPreferences prefs;

  AuthLocalDataSourceImpl({required this.prefs});

  static const String keyToken = 'auth_token';
  static const String keyExpires = 'auth_expires';
  static const String keyUser = 'auth_user';

  @override
  Future<void> saveUser(UserModel user) async =>
      await prefs.setString(keyUser, jsonEncode(user.toJson()));

  @override
  Future<UserModel?> getUser() async {
    try {
      final userString = prefs.getString(keyUser);

      if (userString == null || userString.isEmpty) {
        return null;
      }
      final userJson = jsonDecode(userString);
      print(userJson.runtimeType);
      return UserModel.fromJson(userJson);
    } catch (e) {
      print('Error parsing user: $e');
      return null;
    }
  }

  @override
  Future<void> clearUser() async => await prefs.remove(keyUser);

  @override
  Future<void> saveToken(TokenData data) async {
    await prefs.setString(keyToken, data.token);
    await prefs.setString(keyExpires, data.expires.toString());
  }

  @override
  Future<TokenData> getToken() async {
    final token = await prefs.getString(keyToken);
    final expires = await prefs.getString(keyExpires);
    return TokenData.fromJson({'token': token, 'expires': expires});
  }

  @override
  Future<void> clearToken() async {
    await prefs.remove(keyToken);
    await prefs.remove(keyExpires);
  }

  @override
  Future<void> clearAll() async => await prefs.clear();
}
