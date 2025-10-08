import 'package:equatable/equatable.dart';

class BaseScreenItem extends Equatable {
  final String title;
  final String iconPath;

  const BaseScreenItem({required this.title, required this.iconPath});

  @override
  List<Object?> get props => [title, iconPath];
}
