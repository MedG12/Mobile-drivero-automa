import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/kms_driver_entity.dart';
import 'package:drivero_automa/domain/repositories/kms_driver_repository.dart';

class GetKmsDriverUsecase {
  final KmsDriverRepository repository;

  GetKmsDriverUsecase(this.repository);

  Future<Either<String, KmsDriverEntity>> call(int vehicleId) async {
    return await repository.getKmsDriver(vehicleId);
  }
}
