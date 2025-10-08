import 'package:dartz/dartz.dart';
import 'package:drivero_automa/features/auth/domain/entities/user_entity.dart';

abstract class AuthRepository {
  Future<Either<String, UserEntity>> login(String email, String password);
  Future<Either<String, UserEntity?>> getUser();
}
