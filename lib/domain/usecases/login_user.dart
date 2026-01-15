import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:drivero_automa/domain/repositories/auth_repository.dart';

class LoginUser {
  final AuthRepository repository;

  LoginUser(this.repository);

  Future<Either<String, UserEntity>> call(String email, String password) async {
    return await repository.login(email, password);
  }
}
