import 'dart:convert';
import 'package:drivero_automa/data/models/user_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

abstract class AuthLocalDataSource {
  Future<void> saveToken(String token);
  Future<String?> getToken();
  Future<void> clearToken();

  Future<void> saveUser(UserModel user);
  Future<UserModel?> getUser();
  Future<void> clearUser();

  Future<void> clearAll();
}

class AuthLocalDataSourceImpl implements AuthLocalDataSource {
  final SharedPreferences prefs;

  AuthLocalDataSourceImpl({required this.prefs});

  static const String keyToken = 'auth_token';
  static const String keyUser = 'user_data';

  @override
  Future<void> saveToken(String token) async {
    await prefs.setString(keyToken, token);
  }

  @override
  Future<String?> getToken() async {
    return prefs.getString(keyToken);
  }

  @override
  Future<void> clearToken() async {
    await prefs.remove(keyToken);
  }

  @override
  Future<void> saveUser(UserModel user) async {
    final jsonString = jsonEncode(user.toJson());
    await prefs.setString(keyUser, jsonString);
  }

  @override
  Future<UserModel?> getUser() async {
    final jsonString = prefs.getString(keyUser);
    if (jsonString == null) return null;
    try {
      final Map<String, dynamic> jsonMap = jsonDecode(jsonString);
      return UserModel.fromJson(jsonMap);
    } catch (_) {
      return null;
    }
  }

  @override
  Future<void> clearUser() async {
    await prefs.remove(keyUser);
  }

  @override
  Future<void> clearAll() async {
    await prefs.clear();
  }
}
