import 'package:drivero_automa/data/datasources/local/auth_local_datasource.dart';
import 'package:drivero_automa/data/datasources/remote/maintenance_remote_datasource.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:drivero_automa/domain/repositories/maintenance_repository.dart';

class MaintenanceRepositoryImpl implements MaintenanceRepository {
  final AuthLocalDataSource authLocalDataSource;
  final MaintenanceRemoteDatasource maintenanceRemoteDataSource;

  MaintenanceRepositoryImpl({
    required this.authLocalDataSource,
    required this.maintenanceRemoteDataSource,
  });
  @override
  Future<List<MaintenanceEntity>> getMaintenances() async {
    final token = await authLocalDataSource.getToken();
    final response = await maintenanceRemoteDataSource.getMaintenances(token!);
    return response.data;
  }
}
