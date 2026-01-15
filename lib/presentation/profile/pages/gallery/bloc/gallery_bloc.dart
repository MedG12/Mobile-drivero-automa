import 'dart:io';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:path_provider/path_provider.dart';
import 'gallery_event.dart';
import 'gallery_state.dart';

class GalleryBloc extends Bloc<GalleryEvent, GalleryState> {
  GalleryBloc() : super(GalleryInitial()) {
    on<LoadGalleryImages>(_onLoadImages);
  }

  Future<void> _onLoadImages(
      LoadGalleryImages event, Emitter<GalleryState> emit) async {
    emit(GalleryLoading());
    try {
      final dir = await getApplicationDocumentsDirectory();
      
      // Ambil file, filter hanya .jpg/.jpeg
      final List<FileSystemEntity> files = dir.listSync().where((e) {
        return e is File && 
               (e.path.toLowerCase().endsWith('.jpg') || 
                e.path.toLowerCase().endsWith('.jpeg'));
      }).toList();

      // Urutkan: Terbaru paling atas
      files.sort((a, b) => b.statSync().modified.compareTo(a.statSync().modified));

      emit(GalleryLoaded(files));
    } catch (e) {
      emit(GalleryError("Gagal memuat galeri: $e"));
    }
  }
}