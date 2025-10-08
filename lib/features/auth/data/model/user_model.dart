import 'package:drivero_automa/features/auth/domain/entities/user_entity.dart';

class UserModel extends UserEntity {
  UserModel({
    required String id,
    required String name,
    required String email,
    required String role,
  }) : super(id: id, name: name, email: email, role: role);

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      id: json['user_id'].toString(),
      name: json['name'],
      email: json['email'],
      role: json['roles'],
    );
  }

  Map<String, dynamic> toJson() => {
    'user_id': id,
    'name': name,
    'email': email,
    'roles': role,
  };
}
