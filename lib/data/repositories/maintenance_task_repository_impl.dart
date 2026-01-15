import 'package:dartz/dartz.dart';
import '../../domain/entities/maintenance_task_entity.dart';
import '../../domain/repositories/maintenance_task_repository.dart';
import '../datasources/remote/maintenance_task_remote_data_source.dart';
import '../datasources/local/auth_local_datasource.dart';
import '../models/maintenance_task_model.dart';

class MaintenanceTaskRepositoryImpl implements MaintenanceTaskRepository {
  final MaintenanceTaskRemoteDataSource remoteDataSource;
  final AuthLocalDataSource authLocalDataSource;

  MaintenanceTaskRepositoryImpl({
    required this.remoteDataSource,
    required this.authLocalDataSource,
  });

  @override
  @override
Future<MaintenanceTaskEntity> addMaintenanceTask({
  required int vehicleId,
  required String title,
  required String maintenanceType,
  required String location,
  required String description,
  required String feeString,
  required String dateTimes,
  required String enumStatus,
}) async {
  final token = await authLocalDataSource.getToken();
  if (token == null) throw Exception("Token not found");

  final response = await remoteDataSource.addMaintenanceTask(
    token: token,
    vehicleId: vehicleId,
    title: title,
    maintenanceType: maintenanceType,
    location: location,
    description: description,
    feeString: feeString,
    dateTimes: dateTimes,
    enumStatus: enumStatus,
  );

  return MaintenanceTaskModel.fromJson(response.result.toJson());
}

}
