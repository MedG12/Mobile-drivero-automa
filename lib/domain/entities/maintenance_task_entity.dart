class MaintenanceTaskEntity {
  final int id; // misal id dari server
  final int vehicleId;
  final String title;
  final String maintenanceType;
  final String location;
  final String description;
  final String feeString;
  final String dateTimes;
  final String enumStatus;

  MaintenanceTaskEntity({
    required this.id,
    required this.vehicleId,
    required this.title,
    required this.maintenanceType,
    required this.location,
    required this.description,
    required this.feeString,
    required this.dateTimes,
    required this.enumStatus,
  });
}
