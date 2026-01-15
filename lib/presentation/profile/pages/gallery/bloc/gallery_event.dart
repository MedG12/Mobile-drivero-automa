import 'package:equatable/equatable.dart';

abstract class GalleryEvent extends Equatable {
  const GalleryEvent();

  @override
  List<Object> get props => [];
}

// Event untuk memanggil/merefresh data foto
class LoadGalleryImages extends GalleryEvent {}