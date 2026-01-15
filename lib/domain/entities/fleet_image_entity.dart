class FleetImageEntity {
  final int id;
  final int idFleet;
  final int typeImage;
  final String nameTypeImage;
  final String link;
  final String desc;
  final DateTime createdOn;

  FleetImageEntity({
    required this.id,
    required this.idFleet,
    required this.typeImage,
    required this.nameTypeImage,
    required this.link,
    required this.desc,
    required this.createdOn,
  });
}
