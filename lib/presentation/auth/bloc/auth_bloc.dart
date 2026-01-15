import 'package:bloc/bloc.dart';
import 'package:dartz/dartz.dart';
import 'package:drivero_automa/core/injector/service_locator.dart';
import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:drivero_automa/domain/usecases/get_user_usecase.dart';
import 'package:drivero_automa/domain/usecases/login_user.dart';
import 'package:drivero_automa/domain/usecases/logout_user_usecase.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_event.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_state.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final LoginUser loginUser;

  final LogoutUserUsecase logoutUser;
  final GetUserUsecase getUser;

  AuthBloc(this.loginUser, this.getUser, this.logoutUser)
    : super(AuthInitial()) {
    on<UpdateAuthUserEvent>((event, emit) {
      emit(Authenticated(event.user));
    });

    on<AppStartedEvent>((event, emit) async {
      emit(AuthLoading());
      final Either<String, UserEntity?> result = await getUser.call();
      result.fold(
        (error) => emit(AuthFailure(error)),
        (user) =>
            user == null ? emit(AuthInitial()) : emit(Authenticated(user)),
      );
    });
 on<LoginEvent>((event, emit) async {
  // ✅ VALIDASI FORM DI SINI
  if (event.email.isEmpty || event.password.isEmpty) {
    emit(AuthFailure('Username dan password wajib diisi'));
    return;
  }

  emit(AuthLoading());

  final Either<String, UserEntity> result = await loginUser(
    event.email,
    event.password,
  );

  result.fold(
    (error) => emit(AuthFailure(error)),
    (user) => emit(Authenticated(user)),
  );
});


    on<CheckLoginStatusEvent>((event, emit) async {
      emit(AuthLoading());
      final Either<String, UserEntity?> result = await getUser.call();
      result.fold(
        (error) => emit(AuthFailure(error)),
        (user) =>
            user == null ? emit(AuthInitial()) : emit(Authenticated(user)),
      );
    });

    on<LogoutEvent>((event, emit) async {
      emit(AuthLoading());
      await logoutUser.call();
      emit(AuthInitial());
    });
  }
}
