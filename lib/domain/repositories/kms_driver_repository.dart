import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/kms_driver_entity.dart';

abstract class KmsDriverRepository {
  Future<Either<String, KmsDriverEntity>> getKmsDriver(int vehicleId);
}
