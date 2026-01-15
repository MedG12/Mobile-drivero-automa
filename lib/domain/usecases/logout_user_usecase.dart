import 'package:drivero_automa/domain/repositories/auth_repository.dart';

class LogoutUserUsecase {
  AuthRepository repository;

  LogoutUserUsecase(this.repository);
  Future<void> call() async {
    await repository.logout();
  }
}
