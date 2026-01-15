import 'package:drivero_automa/domain/entities/user_entity.dart';

sealed class AuthEvent {}

class LoginEvent extends AuthEvent {
  final String email;
  final String password;
  LoginEvent(this.email, this.password);
}

class InitialEvent extends AuthEvent {}

class CheckLoginStatusEvent extends AuthEvent {}

class LogoutEvent extends AuthEvent {}

class AppStartedEvent extends AuthEvent {}

class UpdateAuthUserEvent extends AuthEvent {
  final UserEntity user;
  UpdateAuthUserEvent(this.user);
}
