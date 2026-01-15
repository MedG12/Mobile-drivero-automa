import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:equatable/equatable.dart';

abstract class ProfileState extends Equatable {
  const ProfileState();

  @override
  List<Object?> get props => [];
}

// state awal
class ProfileInitial extends ProfileState {}

// state loading
class ProfileLoading extends ProfileState {}

// state ketika detail profile berhasil di-load
class ProfileLoaded extends ProfileState {
  final UserEntity user;

  const ProfileLoaded(this.user);

  @override
  List<Object?> get props => [user];
}

// state error saat fetch profile
class ProfileError extends ProfileState {
  final String message;

  const ProfileError(this.message);

  @override
  List<Object?> get props => [message];
}

// state sukses update biodata dengan membawa data user terbaru
class UpdateBiodataSuccess extends ProfileState {
  final UserEntity user; // ✅ user baru

  const UpdateBiodataSuccess(this.user);

  @override
  List<Object?> get props => [user];
}

// state error update biodata
class UpdateBiodataError extends ProfileState {
  final String message;

  const UpdateBiodataError(this.message);

  @override
  List<Object?> get props => [message];
}

// state sukses change password
class ChangePasswordSuccess extends ProfileState {
  const ChangePasswordSuccess();
}

// state error change password
class ChangePasswordError extends ProfileState {
  final String message;

  const ChangePasswordError(this.message);

  @override
  List<Object?> get props => [message];
}
