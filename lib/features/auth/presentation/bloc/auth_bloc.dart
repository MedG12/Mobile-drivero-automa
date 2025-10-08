import 'package:bloc/bloc.dart';
import 'package:dartz/dartz.dart';
import 'package:drivero_automa/features/auth/domain/entities/user_entity.dart';
import 'package:drivero_automa/features/auth/domain/usecases/get_user.dart';
import 'package:drivero_automa/features/auth/domain/usecases/login_user.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_event.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_state.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final LoginUser loginUser;
  final GetUser getUser;

  AuthBloc(this.loginUser, this.getUser) : super(AuthInitial()) {
    on<InitialEvent>((event, emit) async {
      emit(AuthLoading());
      final Either<String, UserEntity?> result = await getUser.call();
      result.fold(
        (error) => emit(AuthFailure(error)),
        (user) =>
            user == null ? emit(AuthInitial()) : emit(Authenticated(user)),
      );
    });
    on<LoginEvent>((event, emit) async {
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
  }
}
