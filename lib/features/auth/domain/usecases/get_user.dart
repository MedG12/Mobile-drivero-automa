import 'package:dartz/dartz.dart';
import 'package:drivero_automa/features/auth/domain/entities/user_entity.dart';
import 'package:drivero_automa/features/auth/domain/repositories/auth_repository.dart';

class GetUser {
  final AuthRepository repository;
  GetUser(this.repository);

  Future<Either<String, UserEntity?>> call() async =>
      await repository.getUser();
}
