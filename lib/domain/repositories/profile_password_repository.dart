import 'package:dartz/dartz.dart';

import 'package:drivero_automa/domain/entities/edit_user_password_etity.dart';

abstract class PasswordRepository {
  Future<Either<Exception, bool>> updatePassword(
    EditUserPasswordEntity data,
  );
}
