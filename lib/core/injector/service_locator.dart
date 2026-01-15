import 'package:dio/dio.dart';
import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/auth_remote_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/kms_remote_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/maintenance_remote_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/maintenance_task_remote_data_source.dart';
import 'package:drivero_automa/data/datasources/remote/profile_biodata_remote_datasouce.dart';
import 'package:drivero_automa/data/datasources/remote/profile_password_remote_dataset.dart';
import 'package:drivero_automa/data/repositories/auth_repository_impl.dart';
import 'package:drivero_automa/data/repositories/kms_repository_impl.dart';
import 'package:drivero_automa/data/repositories/maintenance_repository_impl.dart';
import 'package:drivero_automa/data/repositories/maintenance_task_repository_impl.dart';
import 'package:drivero_automa/data/repositories/profile_password_repository_impl.dart';
import 'package:drivero_automa/domain/repositories/auth_repository.dart';
import 'package:drivero_automa/domain/repositories/kms_driver_repository.dart';
import 'package:drivero_automa/domain/repositories/maintenance_repository.dart';
import 'package:drivero_automa/domain/repositories/maintenance_task_repository.dart';
import 'package:drivero_automa/domain/repositories/profile_password_repository.dart';
import 'package:drivero_automa/domain/usecases/add_maintenance_task_use_case.dart';
import 'package:drivero_automa/domain/usecases/change_password_usecase.dart';
import 'package:drivero_automa/domain/usecases/fleet_img_by_id.dart';
import 'package:drivero_automa/domain/usecases/get_fleets_usecase.dart';
import 'package:drivero_automa/domain/usecases/get_kms_driver_usecase.dart';
import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'package:drivero_automa/domain/usecases/get_user_usecase.dart';
import 'package:drivero_automa/domain/usecases/login_user.dart';
import 'package:drivero_automa/domain/usecases/logout_user_usecase.dart';
import 'package:drivero_automa/domain/usecases/update_user_biodata_usecase.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/data/datasources/local/driver_fleet_local_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/driver_fleet_remote_datasource.dart';
import 'package:drivero_automa/data/datasources/local/FleetImageLocalData.dart';
import 'package:drivero_automa/data/datasources/remote/FleetImageRemoteDataSourceImpl.dart';
import 'package:drivero_automa/data/repositories/driver_fleet_repository_impl.dart';
import 'package:drivero_automa/data/repositories/fleet_img_repository_impl.dart';
import 'package:drivero_automa/domain/repositories/driver_fleet_repository.dart';
import 'package:drivero_automa/domain/repositories/flet_img_repository.dart';
import 'package:drivero_automa/domain/usecases/FleetImg.dart';
import 'package:drivero_automa/presentation/home/bloc/home/home_bloc.dart';
import 'package:drivero_automa/presentation/home/bloc/vehicle_detail/vehicle_detail_bloc.dart';

import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_bloc.dart';
import 'package:drivero_automa/presentation/next_schedule/bloc/next_schedule_bloc.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_bloc.dart';
import 'package:drivero_automa/presentation/schedule_history/bloc/task_detail_bloc.dart';

import 'package:get_it/get_it.dart';
import 'package:shared_preferences/shared_preferences.dart';

final GetIt sl = GetIt.instance;

Future<void> initServiceLocator() async {
  // ============================================================
  // 🔹 EXTERNAL DEPENDENCIES
  // ============================================================
  final prefs = await SharedPreferences.getInstance();
  sl.registerLazySingleton(() => prefs);

  final dio = Dio(
    BaseOptions(
      baseUrl: 'https://api.automa.id/api',
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
      headers: {'Content-Type': 'application/json'},
    ),
  );
  sl.registerLazySingleton(() => dio);

  // ============================================================
  // 🔹 DATASOURCES
  // ============================================================
  // --- Auth ---
  sl.registerLazySingleton<AuthLocalDataSource>(
    () => AuthLocalDataSourceImpl(prefs: sl()),
  );
  sl.registerLazySingleton<AuthRemoteDataSource>(
    () => AuthRemoteDataSourceImpl(sl()),
  );

  // --- Driver Fleet ---
  sl.registerLazySingleton<DriverFleetLocalDataSource>(
    () => DriverFleetLocalDataSourceImpl(prefs: sl()),
  );
  sl.registerLazySingleton<DriverFleetRemoteDataSource>(
    () => DriverFleetRemoteDataSourceImpl(sl()),
  );

  // --- Fleet Image ---
  sl.registerLazySingleton<FleetImageLocalDataSource>(
    () => FleetImageLocalDataSourceImpl(prefs: sl()),
  );
  sl.registerLazySingleton<FleetImageRemoteDataSource>(
    () => FleetImageRemoteDataSourceImpl(dio: sl<Dio>()),
  );

  //--- Profile Datasources ---
  sl.registerLazySingleton<ProfileBiodataRemoteDatasource>(
    () => ProfileBiodataRemoteDatasourceImpl(dio: sl<Dio>()),
  );

  /// datasource
  sl.registerLazySingleton<PasswordRemoteDatasource>(
    () => PasswordRemoteDatasourceImpl(dio: sl<Dio>()),
  );
  //password
  sl.registerLazySingleton<MaintenanceRemoteDatasource>(
    () => MaintenanceRemoteDatasourceImpl(dio: sl<Dio>()),
  );

  // add task maintenance
  sl.registerLazySingleton<MaintenanceTaskRemoteDataSource>(
    () => MaintenanceTaskRemoteDataSourceImpl(dio: sl()),
  );

  //kms
  sl.registerLazySingleton<KmsDriverRemoteDatasource>(
    () => KmsDriverRemoteDatasourceImpl(dio: sl()),
  );

  // ============================================================
  // 🔹 REPOSITORIES
  // ============================================================
  // --- Auth ---
  sl.registerLazySingleton<AuthRepository>(
    () => AuthRepositoryImpl(sl(), sl()),
  );

  // --- Driver Fleet ---
  sl.registerLazySingleton<DriverFleetRepository>(
    () => DriverFleetRepositoryImpl(
      authLocalDataSource: sl(),
      remoteDataSource: sl(),
    ),
  );

  // --- Fleet Image ---
  sl.registerLazySingleton<FleetImageRepository>(
    () => FleetImageRepositoryImpl(
      remoteDataSource: sl<FleetImageRemoteDataSource>(),
      localDataSource: sl<FleetImageLocalDataSource>(),
      authLocalDataSource: sl<AuthLocalDataSource>(),
    ),
  );

  // password
  sl.registerLazySingleton<PasswordRepository>(
    () => PasswordRepositoryImpl(
      // Sekarang GetIt dapat menemukan kedua dependensi ini
      remoteDatasource: sl<PasswordRemoteDatasource>(),
      authLocalDatasource: sl<AuthLocalDataSource>(),
    ),
  );
  //biodata

  sl.registerLazySingleton<MaintenanceRepository>(
    () => MaintenanceRepositoryImpl(
      authLocalDataSource: sl<AuthLocalDataSource>(),
      maintenanceRemoteDataSource: sl<MaintenanceRemoteDatasource>(),
    ),
  );
  // task maintenance
  sl.registerLazySingleton<MaintenanceTaskRepository>(
    () => MaintenanceTaskRepositoryImpl(
      remoteDataSource: sl(), // inject remote datasource
      authLocalDataSource: sl(), // inject auth local datasource
    ),
  );
  sl.registerLazySingleton(
    () => GetFleetImagesByFleetIds(sl<FleetImageRepository>()),
  );
  //kms
  sl.registerLazySingleton<KmsDriverRepository>(
    () => KmsDriverRepositoryImpl(
      authLocalDataSource: sl(),
      remoteDatasource: sl(), // ✔ BENAR
    ),
  );

  // ============================================================
  // 🔹 USECASES
  // ============================================================
  // --- Auth ---
  sl.registerLazySingleton(() => LoginUser(sl()));
  sl.registerLazySingleton(() => LogoutUserUsecase(sl()));
  sl.registerLazySingleton(() => GetMaintenancesUsecase(sl()));

  // --- Driver Fleet ---
  sl.registerLazySingleton(() => GetDriverFleets(sl()));

  // --- Fleet Image ---
  sl.registerLazySingleton<GetFleetImages>(
    () => GetFleetImages(sl<FleetImageRepository>()),
  );

  // --- Profile ---
  sl.registerLazySingleton(() => GetUserUsecase(sl()));

  // --- Profile ---
  sl.registerLazySingleton(() => UpdateBiodata(sl<AuthRepository>()));
  sl.registerLazySingleton(
    () => ChangePasswordUsecase(sl<PasswordRepository>()),
  );
  //task maintenance
  sl.registerLazySingleton<AddMaintenanceTaskUseCase>(
    () => AddMaintenanceTaskUseCase(sl()),
  );
  //kms
  sl.registerLazySingleton(() => GetKmsDriverUsecase(sl()));

  // ============================================================
  // 🔹 BLOCS
  // ============================================================
  // --- Auth ---
  sl.registerFactory(() => AuthBloc(sl(), sl(), sl()));

  // --- Home ---
  sl.registerFactory<HomeBloc>(
    () => HomeBloc(
      getDriverFleet: sl<GetDriverFleets>(),
      getFleetImages: sl<GetFleetImages>(),
      getMaintenancesUsecase: sl<GetMaintenancesUsecase>(),
    ),
  );
  // schedule
  sl.registerFactory(
    () => EventBloc(
      getMaintenancesUsecase: sl<GetMaintenancesUsecase>(),
      getDriverFleets: sl<GetDriverFleets>(),
      addMaintenanceTaskUseCase: sl(),
    ),
  );

  // --- Vehicle Detail ---

  sl.registerFactory(
    () => VehicleDetailBloc(
      getFleetImagesByFleetIds: sl<GetFleetImagesByFleetIds>(),
      getMaintenances: sl<GetMaintenancesUsecase>(),
      getKmsDriver: sl<GetKmsDriverUsecase>(),
    ),
  );

  // --- Profile ---
  sl.registerFactory(
    () => ProfileBloc(
      getUserUsecase: sl<GetUserUsecase>(),
      updateBiodataUsecase: sl<UpdateBiodata>(),
      changePasswordUsecase: sl<ChangePasswordUsecase>(),
    ),
  );

  //--- Last Maintenance ---
  sl.registerFactory(() => LastMaintenanceBloc(getMaintenances: sl()));

  //--- Next Schedule ---

  sl.registerFactory(() => NextScheduleBloc(getMaintenances: sl()));
}
