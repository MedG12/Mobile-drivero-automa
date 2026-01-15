import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/maintenance_task_entity.dart';
import 'package:drivero_automa/domain/repositories/maintenance_task_repository.dart';

class AddMaintenanceTaskUseCase {
  final MaintenanceTaskRepository repository;

  AddMaintenanceTaskUseCase(this.repository);

  Future<Either<Exception, MaintenanceTaskEntity>> call({
    required int vehicleId,
    required String title,
    required String maintenanceType,
    required String location,
    required String description,
    required String feeString,
    required String dateTimes,
    required String enumStatus,
  }) async {
    try {
      final result = await repository.addMaintenanceTask(
        vehicleId: vehicleId,
        title: title,
        maintenanceType: maintenanceType,
        location: location,
        description: description,
        feeString: feeString,
        dateTimes: dateTimes,
        enumStatus: enumStatus,
      );
      return Right(result);
    } catch (e) {
      return Left(Exception(e.toString()));
    }
  }
}
