import 'package:drivero_automa/domain/usecases/get_user_usecase.dart';
import 'package:drivero_automa/domain/usecases/update_user_biodata_usecase.dart';
import 'package:drivero_automa/domain/usecases/change_password_usecase.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_event.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_state.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class ProfileBloc extends Bloc<ProfileEvent, ProfileState> {
  final GetUserUsecase getUserUsecase;
  final UpdateBiodata updateBiodataUsecase;
  final ChangePasswordUsecase changePasswordUsecase;

  ProfileBloc({
    required this.getUserUsecase,
    required this.updateBiodataUsecase,
    required this.changePasswordUsecase,
  }) : super(ProfileInitial()) {
    on<GetProfileEvent>((event, emit) async {
      emit(ProfileLoading());
      final result = await getUserUsecase();
      result.fold(
        (error) => emit(ProfileError(error.toString())),
        (user) => emit(ProfileLoaded(user!)),
      );
    });

    on<UpdateBiodataEvent>((event, emit) async {
      emit(ProfileLoading());
      final updateResult = await updateBiodataUsecase(event.editUser);

      await updateResult.fold(
        (failure) async => emit(UpdateBiodataError(failure.toString())),
        (success) async {
          emit(ProfileLoaded(success));
        },
      );
    });

    on<ChangePasswordEvent>((event, emit) async {
      emit(ProfileLoading());
      final result = await changePasswordUsecase(event.editPassword);
      result.fold(
        (error) => emit(ChangePasswordError(error.toString())),
        (_) => emit(const ChangePasswordSuccess()),
      );
    });
  }
}
