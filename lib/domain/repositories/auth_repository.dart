import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';

abstract class AuthRepository {
  Future<Either<String, UserEntity>> login(String email, String password);
  Future<String?> getSavedToken();
  Future<Either<Exception, UserEntity>> updateBiodata(UserEntity data);
  Future<Either<String, UserEntity?>> getSavedUser();
  Future<Either<String, void>> logout();
}
