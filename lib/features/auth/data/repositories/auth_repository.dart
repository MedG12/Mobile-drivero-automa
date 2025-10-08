import 'package:dartz/dartz.dart';
import 'package:drivero_automa/features/auth/data/datasources/auth_local_datasource.dart';
import 'package:drivero_automa/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:drivero_automa/features/auth/domain/entities/user_entity.dart';
import 'package:drivero_automa/features/auth/domain/repositories/auth_repository.dart';

class AuthRepositoryImpl implements AuthRepository {
  final AuthRemoteDataSource remoteDataSource;
  final AuthLocalDataSource localDataSource;

  AuthRepositoryImpl({
    required this.remoteDataSource,
    required this.localDataSource,
  });

  @override
  Future<Either<String, UserEntity>> login(
    String email,
    String password,
  ) async {
    try {
      final response = await remoteDataSource.login(email, password);
      final user = response.user;
      await localDataSource.saveUser(user);
      await localDataSource.saveToken(response.token);
      return Right(user);
    } catch (e) {
      return Left(e.toString());
    }
  }

  @override
  Future<Either<String, UserEntity?>> getUser() async {
    try {
      final user = await localDataSource.getUser();
      print(user);
      return Right(user);
    } catch (e) {
      return Left(e.toString());
    }
  }
}
