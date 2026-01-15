import 'package:dartz/dartz.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:drivero_automa/domain/repositories/maintenance_repository.dart';

class GetMaintenancesUsecase {
  final MaintenanceRepository repository;

  GetMaintenancesUsecase(this.repository);

  Future<Either<String, List<MaintenanceEntity>>> call() async {
    try {
      // Ambil semua maintenance dari API
      final maintenances = await repository.getMaintenances();

      return Right(maintenances);
    } catch (e) {
      return Left(e.toString());
    }
  }
}
