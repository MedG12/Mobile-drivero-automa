
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';

abstract class MaintenanceRepository {
  Future<List<MaintenanceEntity>> getMaintenances();
  // Future<List<int>> getMaintenance({required int fleetIds});
}
