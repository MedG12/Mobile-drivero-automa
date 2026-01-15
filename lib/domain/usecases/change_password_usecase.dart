import 'package:dartz/dartz.dart';

import 'package:drivero_automa/domain/entities/edit_user_password_etity.dart';
import 'package:drivero_automa/domain/repositories/profile_password_repository.dart';

class ChangePasswordUsecase {
  final PasswordRepository repository;

  ChangePasswordUsecase(this.repository);

  Future<Either<Exception, bool>> call(EditUserPasswordEntity data) async {
    return await repository.updatePassword(data);
  }
}
