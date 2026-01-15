import 'package:dartz/dartz.dart';
import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/driver_fleet_remote_datasource.dart';
import 'package:drivero_automa/data/models/driver_fleet_model.dart';
import 'package:drivero_automa/domain/repositories/driver_fleet_repository.dart';


class DriverFleetRepositoryImpl implements DriverFleetRepository {
  final DriverFleetRemoteDataSource remoteDataSource;
  final AuthLocalDataSource authLocalDataSource;

  DriverFleetRepositoryImpl({
    required this.remoteDataSource,
    required this.authLocalDataSource,
  });

  @override
  Future<Either<String, List<DriverFleetModel>>> getDriverFleets(
    int userId,
  ) async {
    try {
      final token = await authLocalDataSource.getToken();
      if (token == null || token.isEmpty) {
        return left("Token tidak ditemukan");
      }

      final data = await remoteDataSource.getDriverFleets(
        userId: userId,
        token: token,
      );

      print("data.first.lastMaintenance: ${data.first.lastMaintenance}");

      return right(data);
    } catch (e) {
      return left(e.toString());
    }
  }

  @override
  Future<Either<String, DriverFleetModel>> getDriverFleetDetail(
    int vehicleId,
  ) async {
    try {
      final token = await authLocalDataSource.getToken();
      if (token == null || token.isEmpty) {
        return left("Token tidak ditemukan");
      }

      final data = await remoteDataSource.getDriverFleetDetail(
        vehicleId: vehicleId,
        token: token,
      );
      return right(data);
    } catch (e) {
      return left(e.toString());
    }
  }
}
