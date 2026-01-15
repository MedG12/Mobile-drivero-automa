import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/repositories/flet_img_repository.dart';
import '../entities/fleet_image_entity.dart';
class GetFleetImagesByFleetIds {
  final FleetImageRepository repository;

  GetFleetImagesByFleetIds(this.repository);

  Future<List<FleetImageEntity>> call({required int fleetId}) async {
    return await repository.getFleetImages(fleetId: fleetId);
  }
}