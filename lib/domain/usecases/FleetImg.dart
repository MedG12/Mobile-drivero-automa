import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';
import 'package:drivero_automa/domain/repositories/flet_img_repository.dart';

class GetFleetImages {
  final FleetImageRepository repository;

  GetFleetImages(this.repository);

  Future<List<FleetImageEntity>> call() async {
    return await repository.getFleetImages(); // fleetId = null
  }
}
