import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';

abstract class FleetImageRepository {
  Future<List<FleetImageEntity>> getFleetImages({int? fleetId});
}
