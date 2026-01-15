import 'package:drivero_automa/domain/entities/edit_user_biodata_etity.dart';
import 'package:drivero_automa/domain/entities/edit_user_password_etity.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:equatable/equatable.dart';

abstract class ProfileEvent extends Equatable {
  const ProfileEvent();

  @override
  List<Object?> get props => [];
}

// Ambil detail profile
class GetProfileEvent extends ProfileEvent {}

// Update biodata
class UpdateBiodataEvent extends ProfileEvent {
  final UserEntity editUser;

  const UpdateBiodataEvent(this.editUser);

  @override
  List<Object?> get props => [editUser];
}

// Change password
class ChangePasswordEvent extends ProfileEvent {
  final EditUserPasswordEntity editPassword;

  const ChangePasswordEvent(this.editPassword);

  @override
  List<Object?> get props => [editPassword];
}
