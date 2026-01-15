class MaintenanceEntity {
  final int id;
  final int vehicleId;
  final String title;
  final String maintenanceType;
  final String location;
  final String description;
  final String? feeString;
  final String? enumStatus;
  final DateTime? dateTimes;
  final int companyId;
  final int statusId;
  final DateTime createdOn;
  final DateTime modifiedOn;
  final String licensePlat;
  final String brand;

  const MaintenanceEntity({
    required this.id,
    required this.vehicleId,
    required this.title,
    required this.maintenanceType,
    required this.location,
    required this.description,
    this.feeString,
    this.enumStatus,
    this.dateTimes,
    required this.companyId,
    required this.statusId,
    required this.createdOn,
    required this.modifiedOn,
    required this.licensePlat,
    required this.brand,
  });
}
