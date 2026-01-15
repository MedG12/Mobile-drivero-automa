class DriverEntity {
  final int driverId;
  final int userId;
  final String? name;
  final String? telp;
  final String? ktp;
  final DateTime? birthDate;
  final int? gender;

  const DriverEntity({
    required this.driverId,
    required this.userId,
    this.name,
    this.telp,
    this.ktp,
    this.birthDate,
    this.gender,
  });
}
