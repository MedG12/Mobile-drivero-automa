import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:drivero_automa/domain/repositories/auth_repository.dart';

class UpdateBiodata {
  final AuthRepository repository;

  UpdateBiodata(this.repository);

  Future<Either<Exception, UserEntity>> call(UserEntity data) {
    return repository.updateBiodata(data);
  }
}
