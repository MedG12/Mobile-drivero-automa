import 'package:dartz/dartz.dart';
import 'package:drivero_automa/data/models/driver_fleet_model.dart';

abstract class DriverFleetRepository {
  Future<Either<String, List<DriverFleetModel>>> getDriverFleets(int userId);
  Future<Either<String, DriverFleetModel>> getDriverFleetDetail(int vehicleId);
}
