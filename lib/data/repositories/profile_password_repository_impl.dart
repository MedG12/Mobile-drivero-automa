// Repository Implementation
import 'package:drivero_automa/data/datasources/remote/profile_password_remote_dataset.dart';
import 'package:drivero_automa/domain/repositories/profile_password_repository.dart';

import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/edit_user_password_etity.dart';

import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';

class PasswordRepositoryImpl implements PasswordRepository {
  final PasswordRemoteDatasource remoteDatasource;
  final AuthLocalDataSource authLocalDatasource;

  PasswordRepositoryImpl({
    required this.remoteDatasource,
    required this.authLocalDatasource,
  });

  @override
  Future<Either<Exception, bool>> updatePassword(EditUserPasswordEntity data) async {
    try {
      final token = await authLocalDatasource.getToken();
      if (token == null) return Left(Exception("Token not found"));

      await remoteDatasource.updatePassword(data, token);

      return const Right(true);
    } catch (e) {
      return Left(Exception(e.toString()));
    }
  }
}
