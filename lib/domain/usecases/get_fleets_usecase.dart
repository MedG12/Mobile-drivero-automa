import 'package:dartz/dartz.dart';
import 'package:drivero_automa/data/models/driver_fleet_model.dart';
import 'package:drivero_automa/domain/repositories/driver_fleet_repository.dart';

class GetDriverFleets {
  final DriverFleetRepository repository;

  GetDriverFleets(this.repository);

  Future<Either<String, List<DriverFleetModel>>> call(int userId) async {
    return await repository.getDriverFleets(userId);
  }
}
