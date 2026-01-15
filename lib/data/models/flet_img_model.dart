import 'package:drivero_automa/domain/entities/fleet_image_entity.dart';

class FleetImageModel extends FleetImageEntity {
  FleetImageModel({
    required int id,
    required int idFleet,
    required int typeImage,
    required String nameTypeImage,
    required String link,
    required String desc,
    required DateTime createdOn,
  }) : super(
          id: id,
          idFleet: idFleet,
          typeImage: typeImage,
          nameTypeImage: nameTypeImage,
          link: link,
          desc: desc,
          createdOn: createdOn,
        );

  factory FleetImageModel.fromJson(Map<String, dynamic> json) {
    return FleetImageModel(
      id: json['id'],
      idFleet: json['id_fleet'],
      typeImage: json['type_image'],
      nameTypeImage: json['name_type_image'],
      link: json['link'],
      desc: json['desc'] ?? '',
      createdOn: DateTime.parse(json['created_on']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'id_fleet': idFleet,
      'type_image': typeImage,
      'name_type_image': nameTypeImage,
      'link': link,
      'desc': desc,
      'created_on': createdOn.toIso8601String(),
    };
  }

  FleetImageEntity toEntity() {
    return FleetImageEntity(
      id: id,
      idFleet: idFleet,
      typeImage: typeImage,
      nameTypeImage: nameTypeImage,
      link: link,
      desc: desc,
      createdOn: createdOn,
    );
  }
}
