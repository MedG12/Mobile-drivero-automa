import 'package:dio/dio.dart';
import 'package:drivero_automa/features/auth/data/datasources/auth_local_datasource.dart';
import 'package:drivero_automa/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:drivero_automa/features/auth/data/repositories/auth_repository.dart';
import 'package:drivero_automa/features/auth/domain/repositories/auth_repository.dart';
import 'package:drivero_automa/features/auth/domain/usecases/get_user.dart';
import 'package:drivero_automa/features/auth/domain/usecases/login_user.dart';
import 'package:drivero_automa/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:drivero_automa/features/home/presentation/bloc/home/home_bloc.dart';
import 'package:drivero_automa/features/home/presentation/bloc/vehicle_detail/vehicle_detail_bloc.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_bloc.dart';
import 'package:get_it/get_it.dart';
import 'package:shared_preferences/shared_preferences.dart';

final GetIt sl = GetIt.instance;

Future<void> initServiceLocator() async {
  // Shared Preferences
  final prefs = await SharedPreferences.getInstance();
  sl.registerLazySingleton(() => prefs);

  // Dio client
  final dio = Dio(
    BaseOptions(
      baseUrl: 'https://api.automa.id/api',
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
      headers: {'Content-Type': 'application/json'},
    ),
  );
  sl.registerLazySingleton(() => dio);

  // Data Sources
  sl.registerLazySingleton<AuthLocalDataSource>(
    () => AuthLocalDataSourceImpl(prefs: sl()),
  );
  sl.registerLazySingleton<AuthRemoteDataSource>(
    () => AuthRemoteDataSourceImpl(dio: sl()),
  );
  //repositories
  sl.registerLazySingleton<AuthRepository>(
    () => AuthRepositoryImpl(localDataSource: sl(), remoteDataSource: sl()),
  );

  // UseCases
  sl.registerLazySingleton(() => LoginUser(sl()));
  sl.registerLazySingleton(() => GetUser(sl()));

  // BLoCs
  sl.registerFactory(() => AuthBloc(sl(), sl()));
  sl.registerFactory(() => HomeBloc());
  sl.registerFactory(() => VehicleDetailBloc());
  sl.registerFactory(() => EventBloc());
}
