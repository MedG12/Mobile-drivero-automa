import 'package:drivero_automa/features/auth/domain/entities/user_entity.dart';

sealed class AuthState {}

class AuthInitial extends AuthState {}

class Authenticated extends AuthState {
  final UserEntity user;
  Authenticated(this.user);
}

class AuthLoading extends AuthState {}

class AuthFailure extends AuthState {
  final String message;
  AuthFailure(this.message);
}
