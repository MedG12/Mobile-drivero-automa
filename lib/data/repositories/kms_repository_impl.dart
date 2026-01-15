import 'package:dartz/dartz.dart';
import 'package:drivero_automa/data/datasources/remote/kms_remote_datasource.dart';
import 'package:drivero_automa/domain/entities/kms_driver_entity.dart';
import 'package:drivero_automa/domain/repositories/kms_driver_repository.dart';
import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
class KmsDriverRepositoryImpl implements KmsDriverRepository {
  final AuthLocalDataSource authLocalDataSource;
  final KmsDriverRemoteDatasource remoteDatasource;

  KmsDriverRepositoryImpl({
    required this.authLocalDataSource,
    required this.remoteDatasource,
  });

  @override
  Future<Either<String, KmsDriverEntity>> getKmsDriver(int vehicleId) async {
    try {
      final token = await authLocalDataSource.getToken();

      final model = await remoteDatasource.getKmsDriver(vehicleId, token!);

      return Right(model.toEntity());
    } catch (e) {
      return Left(e.toString());
    }
  }
}
