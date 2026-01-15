import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:drivero_automa/domain/repositories/auth_repository.dart';

class GetUserUsecase {
  final AuthRepository repository;
  GetUserUsecase(this.repository);

  Future<Either<String, UserEntity?>> call() async {
    return await repository.getSavedUser();
  }
}
